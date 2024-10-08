package ch.epfl.chacun;

import java.util.*;

/**
 * MessageBoard record, which represents the board of messages containing info about the game and
 * its players displayed on the right of the play area.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record MessageBoard(TextMaker textMaker, List<Message> messages) {
    /**
     * MessageBoard record, which represents the board of messages containing info about the game
     * and its players displayed on the right of the play area.
     *
     * @param textMaker TextMaker instance which allows to generate messages
     * @param messages  list of messages already on the board
     */
    public MessageBoard {
        messages = List.copyOf(messages);
    }

    /**
     * Returns a map of players (by color) and their total number of points gained over all messages
     *
     * @return HashMap of PlayerColor-Integer pairs
     */
    public Map<PlayerColor, Integer> points() {
        Map<PlayerColor, Integer> points = new HashMap<>();

        for (Message message : messages) {
            for (PlayerColor scorer : message.scorers) {
                points.merge(scorer, message.points(), Integer::sum);
            }
        }

        return points;
    }

    private MessageBoard withNewMessage(Message message) {
        List<Message> newMessages = new ArrayList<>(this.messages);
        if (message != null)
            newMessages.add(message);
        return new MessageBoard(this.textMaker, newMessages);
    }

    /**
     * Returns a new message board identical to the current instance, unless the given forest is
     * occupied, in which case the board contains a new message indicating that its majority
     * occupants have won the points associated with the forest having been closed.
     *
     * @param forest the forest area to be evaluated
     * @return the new message board, may contain the new message
     */
    public MessageBoard withScoredForest(Area<Zone.Forest> forest) {
        int tileCount = forest.tileIds().size();
        int mushroomGroupCount = Area.mushroomGroupCount(forest);
        Set<PlayerColor> majorityOccupants = forest.majorityOccupants();

        Message newMessage = null;
        if (forest.isOccupied()) {
            int pointsGained = Points.forClosedForest(tileCount, mushroomGroupCount);

            if (pointsGained > 0)
                newMessage = new Message(
                        this.textMaker.playersScoredForest(
                                majorityOccupants, pointsGained, mushroomGroupCount, tileCount
                        ),
                        pointsGained, majorityOccupants, forest.tileIds()
                );
        }

        return withNewMessage(newMessage);
    }

    /**
     * Returns a new message board identical to the current instance, but with a new message
     * indicating that the given player has the right to play a second round after closing the
     * given forest, because it contains one or more menhirs.
     *
     * @param player the player (PlayerColor) that closed the forest
     * @param forest the forest area to be evaluated
     * @return the new message board, may contain the new message
     */
    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        Message newMessage = null;

        if (forest.isClosed() && Area.hasMenhir(forest)) {
            newMessage = new Message(
                    this.textMaker.playerClosedForestWithMenhir(player),
                    0, Collections.emptySet(), forest.tileIds()
            );
        }

        return withNewMessage(newMessage);
    }

    /**
     * Returns a new message board identical to the current instance, unless the given river is
     * occupied, in which case the board contains a new message indicating that the rivers majority
     * occupants have won the points associated with river being closed.
     *
     * @param river the river area to be evaluated
     * @return the new message board, may contain the new message
     */
    public MessageBoard withScoredRiver(Area<Zone.River> river) {
        int tileCount = river.tileIds().size();
        int fishCount = Area.riverFishCount(river);
        Set<PlayerColor> majorityOccupants = river.majorityOccupants();

        Message newMessage = null;

        if (river.isOccupied()) {
            int pointsGained = Points.forClosedRiver(tileCount, fishCount);

            if (pointsGained > 0)
                newMessage = new Message(
                        this.textMaker.playersScoredRiver(
                                majorityOccupants, pointsGained, fishCount, tileCount
                        ),
                        pointsGained, majorityOccupants, river.tileIds()
                );
        }

        return withNewMessage(newMessage);
    }

    private static Map<Animal.Kind, Integer> animalCounts(Set<Animal> animals) {
        Map<Animal.Kind, Integer> animalCounts = new HashMap<>();
        for (Animal animal : animals) {
            animalCounts.merge(animal.kind(), 1, Integer::sum);
        }
        return animalCounts;
    }

    private static int pointsForMeadow(Map<Animal.Kind, Integer> animalCounts) {
        return Points.forMeadow(
                animalCounts.getOrDefault(Animal.Kind.MAMMOTH, 0),
                animalCounts.getOrDefault(Animal.Kind.AUROCHS, 0),
                animalCounts.getOrDefault(Animal.Kind.DEER, 0)
        );
    }

    /**
     * Returns a new message board identical to the current instance, unless a player has placed the
     * hunting trap and won points, in which case the board contains a new message indicating this.
     *
     * @param scorer         The player (PlayerColor) that scored the points
     * @param adjacentMeadow The meadow area which contains the hunting trap
     * @return the new message board, may contain the new message
     */
    public MessageBoard withScoredHuntingTrap(
        PlayerColor scorer,
        Area<Zone.Meadow> adjacentMeadow,
        Set<Animal> cancelledAnimals
    ) {
        Message newMessage = null;

        Zone.Meadow specialPowerZone = (Zone.Meadow)
                adjacentMeadow.zoneWithSpecialPower(Zone.SpecialPower.HUNTING_TRAP);

        if (specialPowerZone != null) {
            Set<Animal> animals = Area.animals(
                    adjacentMeadow,
                    cancelledAnimals
            );

            Map<Animal.Kind, Integer> animalCounts = animalCounts(animals);
            int pointsGained = pointsForMeadow(animalCounts);

            if (pointsGained > 0)
                newMessage = new Message(
                        this.textMaker.playerScoredHuntingTrap(scorer, pointsGained,
                                animalCounts), pointsGained,
                        Collections.singleton(scorer), adjacentMeadow.tileIds()
                );
        }

        return withNewMessage(newMessage);
    }

    /**
     * Returns a new message board identical to the current instance, but with a new message
     * indicating that the given player has obtained the points corresponding to the placement of
     * the logboat in the given river system.
     *
     * @param scorer      The player (PlayerColor) that placed the logboat
     * @param riverSystem the river system containing the logboat
     * @return the new message board, may contain the new message
     */
    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {
        int lakeCount = Area.lakeCount(riverSystem);
        int pointsGained = Points.forLogboat(lakeCount);

        Message newMessage = new Message(
                this.textMaker.playerScoredLogboat(
                        scorer, pointsGained, lakeCount),
                pointsGained, Collections.singleton(scorer), riverSystem.tileIds()
        );

        return withNewMessage(newMessage);
    }

    /**
     * Returns a new message board identical to the current instance, unless the given meadow is
     * occupied and the points it gives to its majority occupants - calculated without the given
     * canceled animals - are greater than 0, in which case the board contains a new message
     * indicating that these players have won said points.
     *
     * @param meadow           the meadow area of which the points are calculated
     * @param cancelledAnimals the set of animals which should not be considered
     * @return the new message board, may contain the new message
     */
    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Message newMessage = null;

        Set<Animal> animals = Area.animals(meadow, cancelledAnimals);

        Map<Animal.Kind, Integer> animalCounts = animalCounts(animals);
        int pointsGained = pointsForMeadow(animalCounts);

        if (meadow.isOccupied() && pointsGained > 0)
            newMessage = new Message(
                    this.textMaker.playersScoredMeadow(
                            meadow.majorityOccupants(), pointsGained, animalCounts),
                    pointsGained, meadow.majorityOccupants(), meadow.tileIds()
            );

        return withNewMessage(newMessage);
    }

    /**
     * Returns a new message board identical to the current instance, unless the given river system
     * is occupied and the points it gives to its majority occupants are greater than 0, in which
     * case the board contains a new message indicating that these players have won said points.
     *
     * @param riverSystem the river system area of which the points are calculated
     * @return the new message board, may contain the new message
     */
    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {
        Message newMessage = null;

        int fishCount = Area.riverSystemFishCount(riverSystem);
        int pointsGained = Points.forRiverSystem(fishCount);
        Set<PlayerColor> majorityOccupants = riverSystem.majorityOccupants();

        if (riverSystem.isOccupied() && pointsGained > 0) {
            newMessage = new Message(
                    this.textMaker.playersScoredRiverSystem(
                            majorityOccupants, pointsGained, fishCount),
                    pointsGained, majorityOccupants, riverSystem.tileIds()
            );
        }

        return withNewMessage(newMessage);
    }

    /**
     * Returns a new message board identical to the current instance, unless the given meadow, which
     * contains the pit trap, is occupied and the points - calculated without the given canceled
     * animals - it gives to its majority occupants are greater than 0, in which case the board
     * contains a new message indicating that these players have won said points.
     *
     * @param adjacentMeadow   the meadow area containing the pit trap
     * @param cancelledAnimals the set of animals which should not be considered
     * @return the new message board, may contain the new message
     */
    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow,
                                          Set<Animal> cancelledAnimals) {
        Message newMessage = null;

        if (adjacentMeadow.isOccupied()) {
            Set<Animal> animals = Area.animals(
                    adjacentMeadow,
                    Set.copyOf(cancelledAnimals)
            );

            Map<Animal.Kind, Integer> animalCounts = animalCounts(animals);
            int pointsGained = pointsForMeadow(animalCounts);

            if (pointsGained > 0)
                newMessage = new Message(
                        this.textMaker.playersScoredPitTrap(
                                adjacentMeadow.majorityOccupants(), pointsGained,
                                animalCounts), pointsGained,
                        adjacentMeadow.majorityOccupants(), adjacentMeadow.tileIds()
                );
        }

        return withNewMessage(newMessage);
    }

    /**
     * Returns a new message board identical to the current instance, unless the given river system
     * containing the raft is occupied, in which case the table contains a new message indicating
     * that its majority occupants have won the corresponding points.
     *
     * @param riverSystem the river system area containing the raft
     * @return the new message board, may contain the new message
     */
    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {
        Message newMessage = null;

        if (riverSystem.isOccupied()) {

            int lakeCount = Area.lakeCount(riverSystem);
            int pointsGained = Points.forRaft(lakeCount);

            if (pointsGained > 0) {
                newMessage = new Message(
                        this.textMaker.playersScoredRaft(
                                riverSystem.majorityOccupants(), pointsGained, lakeCount),
                        pointsGained, riverSystem.majorityOccupants(), riverSystem.tileIds()
                );
            }
        }

        return withNewMessage(newMessage);
    }

    /**
     * Returns a new message board identical to the current instance, but containing a new message
     * indicating that the given player(s) has/have won the game with the given number of points.
     *
     * @param winners the set of players that won the game
     * @param points  the amount of points with which the winners have won.
     * @return the new message board with the new message
     */
    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {
        Message newMessage = new Message(
                this.textMaker.playersWon(winners, points), 0, Collections.emptySet(),
                Collections.emptySet()
        );

        return withNewMessage(newMessage);
    }

    /**
     * Message record, representing a message shown on the message board
     *
     * @param text    text of the message
     * @param points  number of points associated to the message >= 0
     * @param scorers set of players that got points, can be empty
     * @param tileIds set of tile ids of the tiles which have a connection to this message,
     *                can be empty
     */
    public record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {

        /**
         * Message record, representing a message shown on the message board
         *
         * @param text    text of the message
         * @param points  number of points associated to the message >= 0
         * @param scorers set of players that got points, can be empty
         * @param tileIds set of tile ids of the tiles which have a connection to this message,
         *                can be empty
         * @throws IllegalArgumentException if points is less than zero
         */
        public Message {
            Objects.requireNonNull(text);
            Preconditions.checkArgument(points >= 0);
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }
}
