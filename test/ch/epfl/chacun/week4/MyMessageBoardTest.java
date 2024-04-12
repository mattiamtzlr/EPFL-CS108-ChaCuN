package ch.epfl.chacun.week4;

import ch.epfl.chacun.*;
import ch.epfl.chacun.BasicTextMaker;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyMessageBoardTest {
    TextMaker textMaker = new BasicTextMaker();
    MessageBoard.Message points1 = new MessageBoard.Message(textMaker.points(20), 20,
            Set.of(PlayerColor.RED, PlayerColor.BLUE), Collections.emptySet());
    MessageBoard.Message points2 = new MessageBoard.Message(textMaker.points(30), 30,
            Set.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW),
            Collections.emptySet());
    MessageBoard.Message points3 = new MessageBoard.Message(textMaker.points(10), 10,
            Set.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.PURPLE),
            Collections.emptySet());

    MessageBoard points = new MessageBoard(textMaker, List.of(points1, points2, points3));

    @Test
    void pointsWorksOnTrivialCase() {
        Map<PlayerColor, Integer> expected = new HashMap<>(Map.of(
                PlayerColor.RED, 60,
                PlayerColor.BLUE, 50,
                PlayerColor.GREEN, 30,
                PlayerColor.YELLOW, 40,
                PlayerColor.PURPLE, 10
        ));

        assertEquals(expected, points.points());
    }

    @Test
    void pointsWorksOnEmptyMessageList() {
        assertEquals(new HashMap<PlayerColor, Integer>(),
                new MessageBoard(null, Collections.emptyList()).points());
    }

    @Test
    void pointsWorksOnEmptyMessages() {
        assertEquals(new HashMap<PlayerColor, Integer>(),
                new MessageBoard(null, List.of(
                        new MessageBoard.Message("Message", 0, Collections.emptySet(),
                                Collections.emptySet()),
                        new MessageBoard.Message("Message", 10, Collections.emptySet(),
                                Collections.emptySet())
                )).points());
    }

    /* ==========================================================================================
       |         Methods that only add a message based on a condition are not tested            |
       |                            as that seems kind of unnecessary                           |
       ========================================================================================== */
}