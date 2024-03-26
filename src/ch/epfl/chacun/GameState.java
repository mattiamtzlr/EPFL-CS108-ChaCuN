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
        return board.lastPlacedTile().potentialOccupants();
    }

    /* ==========================================================================================
       |                           Helper methods, used for methods below                       |
       ========================================================================================== */

    // TODO: maybe this isn't even used lol
    private List<PlayerColor> shiftPlayers() {
        List<PlayerColor> newList = this.players.subList(1, this.players.size());
        newList.add(this.currentPlayer());
        return newList;
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

    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(this.nextAction.equals(Action.PLACE_TILE));
        Preconditions.checkArgument(Objects.isNull(tile.occupant()));

        Board returnBoard = this.board.withNewTile(tile);

        switch (tile.specialPowerZone()) {
            // calculate deer eaten by tigers if hunting trap tile was placed
            case Zone.Meadow meadow when meadow.specialPower().equals(HUNTING_TRAP) -> {

                Area<Zone.Meadow> adjacentMeadow = this.board.adjacentMeadow(
                        tile.pos(), meadow);


                // get all animals, count them
                Set<Animal> animals = Area.animals(adjacentMeadow, Collections.emptySet());
                Map<Animal.Kind, Integer> animalCounts = new HashMap<>();
                animals.forEach(a -> animalCounts.merge(a.kind(), 1, Integer::sum));

                Set<Animal> cancelledAnimals;
                if (animalCounts.get(Animal.Kind.TIGER) >= animalCounts.get(Animal.Kind.DEER)) {
                    // cancel all deer
                    cancelledAnimals = Set.copyOf(animals).stream()
                            .filter(a -> a.kind().equals(Animal.Kind.DEER))
                            .collect(Collectors.toSet());
                    animalCounts.put(Animal.Kind.DEER, 0);

                } else {
                    // cancel some deer
                    AtomicInteger cancelCount = new AtomicInteger();
                    cancelledAnimals = Set.copyOf(animals).stream()
                            .filter(a -> {
                                if (a.kind().equals(Animal.Kind.DEER) &&
                                        cancelCount.get() < animalCounts.get(Animal.Kind.TIGER)) {

                                    cancelCount.getAndIncrement();
                                    return true;
                                }
                                return false;
                            })
                            .collect(Collectors.toSet());
                    animalCounts.computeIfPresent(Animal.Kind.DEER, (k, v) -> v - cancelCount.get());
                }

                MessageBoard newMessageBoard = this.messageBoard.withScoredHuntingTrap(
                        currentPlayer(),
                        adjacentMeadow/*,
                    cancelledAnimals TODO: uncomment this as soon as new version is known */
                );

                return new GameState(this.players, this.tileDecks, null,
                        returnBoard.withMoreCancelledAnimals(animals), Action.OCCUPY_TILE,
                        newMessageBoard);
            }

            case Zone.Meadow meadow when meadow.specialPower().equals(SHAMAN) -> {
                return new GameState(this.players, this.tileDecks, null,
                        returnBoard, Action.RETAKE_PAWN, this.messageBoard);
            }

            case Zone.Lake lake when lake.specialPower().equals(LOGBOAT) -> {
                MessageBoard newMessageBoard = this.messageBoard.withScoredLogboat(
                        currentPlayer(),
                        this.board.riverSystemArea(lake)
                );

                return new GameState(this.players, this.tileDecks, null,
                        returnBoard, Action.OCCUPY_TILE, newMessageBoard);
            }

            case null, default -> {
                return new GameState(this.players, this.tileDecks, null,
                        returnBoard, Action.OCCUPY_TILE, this.messageBoard);
            }
        }
    }
}
