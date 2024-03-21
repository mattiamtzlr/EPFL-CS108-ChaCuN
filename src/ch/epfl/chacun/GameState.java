package ch.epfl.chacun;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
            (tileToPlace == null || nextAction.equals(Action.PLACE_TILE))
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
}
