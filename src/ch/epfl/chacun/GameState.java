package ch.epfl.chacun;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Zone.SpecialPower.*;

/**
 * GameState record, used to represent an entire game.
 *
 * @param players      the list of all the players, in the order that they have to play, thus the
 *                     current player is at the head
 * @param tileDecks    the three decks of cards
 * @param tileToPlace  the eventual (can be null) tile to be placed, either from the normal stack or
 *                     the menhir stack
 * @param board        the board of this game
 * @param nextAction   the next action to be performed
 * @param messageBoard the message board containing all current messages
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record GameState(
        List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace, Board board,
        Action nextAction, MessageBoard messageBoard
) {
    /**
     * Action enumerator containing the possible actions in a game
     */
    public enum Action {
        START_GAME, PLACE_TILE, RETAKE_PAWN, OCCUPY_TILE, END_GAME,
    }

    /**
     * GameState record, used to represent an entire game.
     *
     * @param players      the list of all the players, in the order that they have to play, thus
     *                     the current player is at the head
     * @param tileDecks    the three decks of cards
     * @param tileToPlace  the eventual (can be null) tile to be placed, either from the normal
     *                     stack or the menhir stack
     * @param board        the board of this game
     * @param nextAction   the next action to be performed
     * @param messageBoard the message board containing all current messages
     */
    public GameState {
        Objects.requireNonNull(tileDecks);
        Objects.requireNonNull(board);
        Objects.requireNonNull(nextAction);
        Objects.requireNonNull(messageBoard);
        Preconditions.checkArgument(players.size() >= 2);
        Preconditions.checkArgument(
                (tileToPlace == null ^ nextAction.equals(Action.PLACE_TILE))
        );

        players = List.copyOf(players);
        // TODO: I think this is enough immutability business but (*ಠ_ಠ;)
    }

    /**
     * Facilitates the creation of a new game by returning a new GameState object based on the
     * following:
     *
     * @param players   the list of all the players, in the order that they have to play, thus
     *                  the current player is at the head
     * @param tileDecks the three decks of cards
     * @param textMaker the TextMaker instance used for the message board
     * @return a GameState able to be used for a fresh game of ChaCuN
     */
    public static GameState initial(
            List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker
    ) {
        return new GameState(
                players, tileDecks, null, Board.EMPTY, Action.START_GAME,
                new MessageBoard(textMaker, Collections.emptyList())
        );
    }

    /* ==========================================================================================
       |                Getters, obtain information about the game state                        |
       ========================================================================================== */

    /**
     * Returns the current player, or null if there is none (START_GAME or END_GAME)
     *
     * @return the current player (PlayerColor) or null
     */
    public PlayerColor currentPlayer() {
        if (this.nextAction.equals(Action.START_GAME) || this.nextAction.equals(Action.END_GAME))
            return null;

        return this.players.getFirst();
    }

    /**
     * Returns the amount of free occupants for the given player of the given kind, meaning the
     * number of occupants that are able to be played
     *
     * @param player the player (PlayerColor) to be queried
     * @param kind   the kind of occupant to count
     * @return the number of free occupants
     */
    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind) {
        return Occupant.occupantsCount(kind) - this.board.occupantCount(player, kind);
    }

    /**
     * Returns the potential occupants of the most recently placed tile.
     *
     * @return Set of occupants that could be placed on the most recently placed tile
     */
    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(!this.board.equals(Board.EMPTY));

        // this warning can be ignored, as the board always has a last placed tile if it's not empty
        return this.board.lastPlacedTile().potentialOccupants().stream()
                .filter(o -> freeOccupantsCount(currentPlayer(), o.kind()) > 0)
                .filter(o -> {
                    Zone zone = this.board.lastPlacedTile().zoneWithId(o.zoneId());
                    return switch (zone) {
                        case Zone.Meadow meadow -> !this.board.meadowArea(meadow).isOccupied();
                        case Zone.Forest forest -> !this.board.forestArea(forest).isOccupied();

                        // TODO: this seems to behave correctly but it's weird
                        case Zone.Water water when o.kind().equals(Occupant.Kind.PAWN) ->
                                !this.board.riverArea((Zone.River) water).isOccupied() &&
                                        !this.board.riverSystemArea(water).isOccupied();

                        case Zone.Water water when o.kind().equals(Occupant.Kind.HUT) ->
                                !this.board.riverSystemArea(water).isOccupied();

                        default -> false;
                    };
                }).collect(Collectors.toSet());
    }

    /* ==========================================================================================
       |                           Helper methods, used for methods below                       |
       ========================================================================================== */

    private List<PlayerColor> shiftPlayers() {
        List<PlayerColor> newList = List.copyOf(this.players).subList(1, this.players.size());
        newList.add(this.currentPlayer());
        return newList;
    }

    private GameState withOccupationIfDeOccupationImpossible(
            Board newBoard, MessageBoard newMessageBoard) {

        if (freeOccupantsCount(currentPlayer(), Occupant.Kind.PAWN)
                < Occupant.occupantsCount(Occupant.Kind.PAWN)) {

            return new GameState(this.players, this.tileDecks, null, newBoard,
                    Action.RETAKE_PAWN, newMessageBoard);
        }

        return withTurnFinishedIfOccupationImpossible(newBoard, newMessageBoard);
    }

    private GameState withTurnFinishedIfOccupationImpossible(
            Board newBoard, MessageBoard newMessageBoard) {

        boolean enoughOccupants = false;
        for (Occupant occupant : lastTilePotentialOccupants()) {
            if (freeOccupantsCount(currentPlayer(), occupant.kind()) > 0)
                enoughOccupants = true;
        }

        if (enoughOccupants && !lastTilePotentialOccupants().isEmpty())
            return new GameState(this.players, this.tileDecks, null, newBoard,
                    Action.OCCUPY_TILE, newMessageBoard);

        return withTurnFinished(newBoard, newMessageBoard);
    }

    private GameState withTurnFinished(Board newBoard, MessageBoard messageBoard) {
        boolean menhir = false;
        MessageBoard newMessageBoard = new MessageBoard(
                messageBoard.textMaker(), messageBoard.messages());

        // add points for closed forests and check for menhir forests
        Set<Area<Zone.Forest>> closedForests = newBoard.forestsClosedByLastTile();
        if (!closedForests.isEmpty()) {
            for (Area<Zone.Forest> forest : closedForests) {
                newMessageBoard = newMessageBoard.withScoredForest(forest);
                if (Area.hasMenhir(forest) && !menhir) {
                    newMessageBoard = newMessageBoard.withClosedForestWithMenhir(
                            currentPlayer(), forest);
                    menhir = true;
                }
            }
        }

        // add points for closed rivers
        Set<Area<Zone.River>> closedRivers = newBoard.riversClosedByLastTile();
        if (!closedRivers.isEmpty()) {
            for (Area<Zone.River> river : closedRivers) {
                newMessageBoard = newMessageBoard.withScoredRiver(river);
            }
        }

        // check next tile and handle accordingly
        TileDecks newTileDecks;
        Tile nextTile;
        if (menhir) {
            newTileDecks = this.tileDecks.withTopTileDrawnUntil(
                    Tile.Kind.MENHIR,
                    newBoard::couldPlaceTile
            );

            nextTile = newTileDecks.topTile(Tile.Kind.MENHIR);

            if (nextTile != null)
                return new GameState(this.players, newTileDecks, nextTile, newBoard,
                        Action.PLACE_TILE, newMessageBoard);
        }

        newTileDecks = this.tileDecks.withTopTileDrawnUntil(
                Tile.Kind.NORMAL,
                newBoard::couldPlaceTile
        );

        nextTile = newTileDecks.topTile(Tile.Kind.NORMAL);

        if (nextTile != null) {
            return new GameState(shiftPlayers(), newTileDecks, nextTile, newBoard,
                    Action.PLACE_TILE, newMessageBoard);
        } else {
            return withFinalPointsCounted();
        }
    }

    private GameState withFinalPointsCounted() {

        Board newBoard = this.board;
        MessageBoard newMessageBoard = this.messageBoard;

        // count meadow points
        for (Area<Zone.Meadow> meadow : newBoard.meadowAreas()) {
            Set<Animal> cancelledDeer;

            if (meadow.zoneWithSpecialPower(WILD_FIRE) != null)
                // don't cancel any deer if there is a fire in the area
                cancelledDeer = Collections.emptySet();

            else if (meadow.zoneWithSpecialPower(PIT_TRAP) != null) {
                Zone.Meadow pitTrapZone = (Zone.Meadow) meadow.zoneWithSpecialPower(PIT_TRAP);
                // if the pit trap is in the area, say hi

                Area<Zone.Meadow> adjMeadow = this.board().adjacentMeadow(
                        newBoard.tileWithId(pitTrapZone.tileId()).pos(),
                        pitTrapZone
                );

                Set<Animal> animals = Area.animals(meadow, Collections.emptySet());
                Map<Animal.Kind, Integer> animalCounts = new HashMap<>();
                animals.forEach(a -> animalCounts.merge(a.kind(), 1, Integer::sum));

                AtomicInteger cancelCount = new AtomicInteger();
                // add all deer that are not in the adjacent meadow
                cancelledDeer = Set.copyOf(animals).stream()
                        .filter(a -> {
                            if (a.kind().equals(Animal.Kind.DEER) &&
                                    !Area.animals(adjMeadow, Collections.emptySet()).contains(a) &&
                                    cancelCount.get() < animalCounts.get(Animal.Kind.TIGER)) {

                                cancelCount.getAndIncrement();
                                return true;
                            }
                            return false;
                        })
                        .collect(Collectors.toSet());

                // add more deer until count reached
                if (cancelCount.get() < animalCounts.get(Animal.Kind.TIGER))
                    cancelledDeer.addAll(Set.copyOf(animals).stream()
                            .filter(a -> {
                                if (a.kind().equals(Animal.Kind.DEER) &&
                                        cancelCount.get() < animalCounts.get(Animal.Kind.TIGER)) {

                                    cancelCount.getAndIncrement();
                                    return true;
                                }
                                return false;
                            })
                            .collect(Collectors.toSet())
                    );

                newMessageBoard = newMessageBoard.withScoredPitTrap(adjMeadow, cancelledDeer);

            } else {
                cancelledDeer = cancelledDeerInMeadow(meadow);
            }

            newBoard = newBoard.withMoreCancelledAnimals(cancelledDeer);
            newMessageBoard = newMessageBoard.withScoredMeadow(meadow, cancelledDeer);
        }

        // count points in river systems
        for (Area<Zone.Water> riverSystem : newBoard.riverSystemAreas()) {
            if (riverSystem.zoneWithSpecialPower(RAFT) != null)
                // give additional points if there is the raft
                newMessageBoard = newMessageBoard.withScoredRaft(riverSystem);

            newMessageBoard = newMessageBoard.withScoredRiverSystem(riverSystem);
        }

        // determine winners

        int maxPoints = 0;
        for (Integer points : newMessageBoard.points().values()) {
            maxPoints = Math.max(maxPoints, points);
        }

        Set<PlayerColor> winners = new HashSet<>();
        for (PlayerColor playerColor : PlayerColor.ALL) {
            if (newMessageBoard.points().getOrDefault(playerColor, 0) == maxPoints)
                winners.add(playerColor);
        }

        newMessageBoard = newMessageBoard.withWinners(winners, maxPoints);

        return new GameState(this.players, this.tileDecks, null, newBoard,
                Action.END_GAME, newMessageBoard);
    }

    private static Set<Animal> cancelledDeerInMeadow(Area<Zone.Meadow> meadowArea) {
        // get all animals, count them
        Set<Animal> animals = Area.animals(meadowArea, Collections.emptySet());
        Map<Animal.Kind, Integer> animalCounts = new HashMap<>();
        animals.forEach(a -> animalCounts.merge(a.kind(), 1, Integer::sum));

        Set<Animal> cancelledDeer;
        /*if (animalCounts.get(Animal.Kind.TIGER) >= animalCounts.get(Animal.Kind.DEER)) {
            // cancel all deer ☠
            cancelledDeer = Set.copyOf(animals).stream()
                    .filter(a -> a.kind().equals(Animal.Kind.DEER))
                    .collect(Collectors.toSet());

            // dunno if this is necessary but hey
            animalCounts.put(Animal.Kind.DEER, 0);

        } else {
            // cancel some deer 🪦
            AtomicInteger cancelCount = new AtomicInteger();
            cancelledDeer = Set.copyOf(animals).stream()
                    .filter(a -> {
                        if (a.kind().equals(Animal.Kind.DEER) &&
                                cancelCount.get() < animalCounts.get(Animal.Kind.TIGER)) {

                            cancelCount.getAndIncrement();
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toSet());

            // dunno if this is necessary but hey
            animalCounts.computeIfPresent(Animal.Kind.DEER, (k, v) -> v - cancelCount.get());
        }*/

        // this should actually be enough: cancel as many deer as there are tigers, or all
        // if there are >= tigers than deer ☠ 🪦
        AtomicInteger cancelCount = new AtomicInteger();
        cancelledDeer = Set.copyOf(animals).stream()
                .filter(a -> {
                    if (a.kind().equals(Animal.Kind.DEER) &&
                            cancelCount.get() < animalCounts.get(Animal.Kind.TIGER)) {

                        cancelCount.getAndIncrement();
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toSet());

        // dunno if this is necessary but hey
        animalCounts.computeIfPresent(Animal.Kind.DEER, (k, v) -> v - cancelCount.get());

        return cancelledDeer;
    }

    /* ==========================================================================================
       |                        State machine methods, keep up the control flow                 |
       ========================================================================================== */

    /**
     * Returns the same GameState, but with the starting tile placed in the center of the board
     *
     * @return the GameState with the starting tile
     */
    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(this.nextAction.equals(Action.START_GAME));

        PlacedTile startTile = new PlacedTile(
                tileDecks.topTile(Tile.Kind.START), null, Rotation.NONE, new Pos(0, 0)
        );
        Board newBoard = this.board.withNewTile(startTile);
        Tile nextTile = this.tileDecks.topTile(Tile.Kind.NORMAL);

        return new GameState(this.players, this.tileDecks.withTopTileDrawn(Tile.Kind.NORMAL),
                nextTile, newBoard, Action.PLACE_TILE, this.messageBoard);
    }

    /**
     * Returns the same GameState, but with the given tile added and all the corresponding actions
     * taken care of.
     *
     * @param tile the tile (PlacedTile) to be added to the board
     * @return the new game state
     */
    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(this.nextAction.equals(Action.PLACE_TILE));
        Preconditions.checkArgument(Objects.isNull(tile.occupant()));

        /*
            PSA: the addition of a new tile always works, as the starting tile accepts every single
            zone type, and after the start, tile selection is handled by withTurnFinished.
         */
        Board newBoard = this.board.withNewTile(tile);
        MessageBoard newMessageBoard = this.messageBoard;
        boolean shaman = false;

        switch (tile.specialPowerZone()) {
            // calculate deer eaten by tigers if hunting trap tile was placed
            case Zone.Meadow meadow when meadow.specialPower().equals(HUNTING_TRAP) -> {

                Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(
                        tile.pos(), meadow);

                Set<Animal> cancelledDeer = cancelledDeerInMeadow(adjacentMeadow);

                newMessageBoard = newMessageBoard.withScoredHuntingTrap(
                        currentPlayer(),
                        adjacentMeadow/*,
                    cancelledDeer TODO: uncomment this as soon as new version is known */
                );

                newBoard = newBoard.withMoreCancelledAnimals(
                        Area.animals(adjacentMeadow, Collections.emptySet())
                );
            }

            // add points for logboat
            case Zone.Lake lake when lake.specialPower().equals(LOGBOAT) ->
                    newMessageBoard = newMessageBoard.withScoredLogboat(
                            currentPlayer(),
                            newBoard.riverSystemArea(lake)
                    );

            // set shaman flag
            case Zone.Meadow meadow when meadow.specialPower().equals(SHAMAN) -> shaman = true;

            case null, default -> {
            }
        }
        /* TODO I think the shaman special power should trigger before the occupation step,
                does this work correctly in that context?
                    · Probably would not behave as intended if the shaman is placed before the
                      placer had the chance to place a pawn but still should get
                      the chance to place an occupant during the occupy phase
                      --> RETAKE Impossible does not imply OCCUPY Impossible
                .
                Good thinking, but I was planning to call withTurnFinishedIfOccupationImpossible
                in withTurnFinishedIfDeOccupationImpossible, so it should work out. - Mattia
        */
        if (shaman)
            return withOccupationIfDeOccupationImpossible(newBoard, newMessageBoard);
        else
            return withTurnFinishedIfOccupationImpossible(newBoard, newMessageBoard);
    }

    /**
     * Method that handles the RETAKE_PAWN action that arises after having placed the shaman.
     *
     * @param occupant The occupant the current player chooses to remove from the board, null if
     *                 the player chose not to remove a pawn.
     * @return A new board without the occupant that was passed, next action OCCUPY_TIlE.
     * If either null was passed or the current player does not have any pawns on the board,
     * the same board is kept and only the next action is changed to OCCUPY_TILE.
     * @throws IllegalArgumentException if either the next action is not RETAKE_PAWN or the occupant
     *                                  that is passed is neither null nor a pawn.
     */
    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(this.nextAction.equals(Action.RETAKE_PAWN));
        Preconditions.checkArgument(occupant == null
                || occupant.kind().equals(Occupant.Kind.PAWN));

        if (occupant == null || this.board.occupantCount(this.currentPlayer(), occupant.kind()) < 1)
            return withTurnFinishedIfOccupationImpossible(this.board, this.messageBoard);

        return withTurnFinishedIfOccupationImpossible(
                this.board.withoutOccupant(occupant), this.messageBoard
        );
    }

    /**
     * Method that handles the OCCUPY_TILE step.
     *
     * @param occupant The occupant to be added to the board.
     * @return A new GameState with the additional occupant.
     */
    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction.equals(Action.OCCUPY_TILE));

        if (occupant == null)
            return withTurnFinished(this.board, this.messageBoard);

        Board newBoard = board.withOccupant(occupant);
        return withTurnFinished(newBoard, this.messageBoard);
    }
}
