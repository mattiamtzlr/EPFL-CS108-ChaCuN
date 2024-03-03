package ch.epfl.chacun.week3;

import ch.epfl.chacun.*;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyAreaTest {
    @Test
    void majorityOccupantsWorks() {
        List<PlayerColor> occupants1 = List.of(
                PlayerColor.RED,
                PlayerColor.GREEN,
                PlayerColor.PURPLE,
                PlayerColor.YELLOW,
                PlayerColor.RED,
                PlayerColor.GREEN,
                PlayerColor.RED
        );

        List<PlayerColor> occupants2 = List.of(
                PlayerColor.RED,
                PlayerColor.GREEN,
                PlayerColor.PURPLE,
                PlayerColor.YELLOW,
                PlayerColor.RED,
                PlayerColor.GREEN,
                PlayerColor.YELLOW
        );

        Area<Zone.Meadow> area1 = new Area<>(Collections.emptySet(), occupants1, 0);
        Area<Zone.Meadow> area2 = new Area<>(Collections.emptySet(), occupants2, 0);

        assertEquals(Set.of(PlayerColor.RED), area1.majorityOccupants());
        assertEquals(
                Set.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.YELLOW),
                area2.majorityOccupants()
        );
    }
}