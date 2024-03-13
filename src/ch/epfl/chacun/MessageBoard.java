package ch.epfl.chacun;

import java.util.*;

/**
 * TODO DESCRIPTION and JavaDoc everything
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record MessageBoard(TextMaker textMaker, List<Message> messages) {
    public record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {

        /**
         * Message record, representing a message shown on the message board
         *
         * @param text    text of the message
         * @param points  number of points associated to the message >= 0
         * @param scorers set of players that got points, can be empty
         * @param tileIds set of tile ids of the tiles which have a connection to this message,
         *                can be empty
         */
        public Message {
            Objects.requireNonNull(text);
            Preconditions.checkArgument(points >= 0);
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }

    /**
     * Constructs a new message board
     * @param textMaker TextMaker instance which allows to generate messages
     * @param messages list of messages already on the board
     */
    public MessageBoard {
        messages = List.copyOf(messages);
    }

    public Map<PlayerColor, Integer> points() {
        Map<PlayerColor, Integer> points = new HashMap<>();

        for (Message message : messages) {
            for (PlayerColor scorer : message.scorers) {
                points.merge(scorer, message.points(), Integer::sum);
            }
        }

        return points;
    }

    // ==================== TODO: test the absolute living shit out of the methods below

    public MessageBoard withScoredForest(Area<Zone.Forest> forest) {
        List<Message> newMessages = new ArrayList<>(Set.copyOf(this.messages));

        if (forest.isOccupied()) {
            int pointsGained = Points.forClosedForest(forest.tileIds().size(),
                    Area.mushroomGroupCount(forest));

            newMessages.add(new Message(
                    this.textMaker.playersScoredForest(
                            forest.majorityOccupants(), pointsGained,
                            Area.mushroomGroupCount(forest), forest.tileIds().size()
                    ), pointsGained, forest.majorityOccupants(), forest.tileIds())
            );
        }

        return new MessageBoard(this.textMaker, newMessages);
    }

    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        List<Message> newMessages = new ArrayList<>(Set.copyOf(this.messages));

        if (Area.hasMenhir(forest)) {
            newMessages.add(new Message(
                    this.textMaker.playerClosedForestWithMenhir(player),
                    0, Collections.emptySet(), forest.tileIds())
            );
        }

        return new MessageBoard(this.textMaker, newMessages);
    }

    public MessageBoard withScoredRiver(Area<Zone.River> river) {
        List<Message> newMessages = new ArrayList<>(Set.copyOf(this.messages));

        if (river.isOccupied()) {
            int pointsGained = Points.forClosedRiver(river.tileIds().size(),
                    Area.riverFishCount(river));

            newMessages.add(new Message(
                    this.textMaker.playersScoredRiver(
                            river.majorityOccupants(), pointsGained,
                            Area.riverFishCount(river), river.tileIds().size()
                    ), pointsGained, river.majorityOccupants(), river.tileIds())
            );
        }

        return new MessageBoard(this.textMaker, newMessages);
    }

    public MessageBoard withScoredHuntingTrap(PlayerColor scorer,
                                              Area<Zone.Meadow> adjacentMeadow) {
        List<Message> newMessages = new ArrayList<>(Set.copyOf(this.messages));

        // READ CONSEILS DE PROGRAMMATION

        // Rest of code TODO

        return new MessageBoard(this.textMaker, newMessages);
    }

    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {
        List<Message> newMessages = new ArrayList<>(Set.copyOf(this.messages));

        if (riverSystem.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT) != null) {
            int pointsGained = Points.forLogboat(Area.lakeCount(riverSystem));
            newMessages.add(new Message(
                this.textMaker.playerScoredLogboat(scorer, pointsGained, Area.lakeCount(riverSystem)),
                pointsGained, Collections.singleton(scorer), riverSystem.tileIds()
            ));
        }

        return new MessageBoard(this.textMaker, newMessages);
    }

    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        List<Message> newMessages = new ArrayList<>(Set.copyOf(this.messages));

        // READ CONSEILS DE PROGRAMMATION

        // Rest of code TODO

        return new MessageBoard(this.textMaker, newMessages);
    }

    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {
        List<Message> newMessages = new ArrayList<>(Set.copyOf(this.messages));

        int fishCount = Area.riverSystemFishCount(riverSystem);
        int pointsGained = Points.forRiverSystem(fishCount);

        if (riverSystem.isOccupied() && pointsGained > 0)
            newMessages.add(new Message(
                this.textMaker.playersScoredRiverSystem(
                    riverSystem.majorityOccupants(), pointsGained, fishCount),
                pointsGained, riverSystem.majorityOccupants(), riverSystem.tileIds()
            ));

        return new MessageBoard(this.textMaker, newMessages);
    }

    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow,
                                          Set<Animal> cancelledAnimals) {
        List<Message> newMessages = new ArrayList<>(Set.copyOf(this.messages));

        // READ CONSEILS DE PROGRAMMATION

        // Rest of code TODO

        return new MessageBoard(this.textMaker, newMessages);
    }

    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {
        List<Message> newMessages = new ArrayList<>(Set.copyOf(this.messages));

        if (riverSystem.zoneWithSpecialPower(Zone.SpecialPower.RAFT) != null
            && riverSystem.isOccupied()) {

            int lakeCount = Area.lakeCount(riverSystem);
            int pointsGained = Points.forRaft(lakeCount);

            newMessages.add(new Message(
                this.textMaker.playersScoredRaft(riverSystem.majorityOccupants(), pointsGained, lakeCount),
                pointsGained, riverSystem.majorityOccupants(), riverSystem.tileIds()
            ));
        }

        return new MessageBoard(this.textMaker, newMessages);
    }

    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {
        List<Message> newMessages = new ArrayList<>(Set.copyOf(this.messages));

        newMessages.add(new Message(
            this.textMaker.playersWon(winners, points), points, winners, Collections.emptySet()
        ));

        return new MessageBoard(this.textMaker, newMessages);
    }
}
