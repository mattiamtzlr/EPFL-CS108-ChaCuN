package ch.epfl.chacun;

import java.util.*;

/**
 * TODO DESCRIPTION and JavaDoc
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
         * @param tileIds set of tile ids of the tiles which have a connection to this message, can be empty
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
}
