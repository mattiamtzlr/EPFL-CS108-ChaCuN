package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyTileSideTest {
    TileSide.Forest f1 = new TileSide.Forest(new Zone.Forest(347, Zone.Forest.Kind.PLAIN));
    TileSide.Forest f2 = new TileSide.Forest(new Zone.Forest(812, Zone.Forest.Kind.WITH_MENHIR));
    TileSide.Meadow m1 = new TileSide.Meadow(new Zone.Meadow(465, List.of(new Animal(4650, Animal.Kind.DEER)), Zone.SpecialPower.HUNTING_TRAP));
    TileSide.Meadow m2 = new TileSide.Meadow(new Zone.Meadow(142, List.of(new Animal(1420, Animal.Kind.AUROCHS)), null));
    TileSide.River r1 = new TileSide.River(
            new Zone.Meadow(830, new ArrayList<>(), null),
            new Zone.River(831, 2, null),
            new Zone.Meadow(832, new ArrayList<>(), null)
    );
    TileSide.River r2 = new TileSide.River(
            new Zone.Meadow(682, new ArrayList<>(), null),
            new Zone.River(683, 1, new Zone.Lake(688, 1, null)),
            new Zone.Meadow(684, new ArrayList<>(), null)
    );
    @Test
    void allCompareCorrectly() {
        assertTrue(f1.isSameKindAs(f2));
        assertTrue(m2.isSameKindAs(m1));
        assertTrue(r2.isSameKindAs(r1));

        assertFalse(f1.isSameKindAs(m1));
        assertFalse(m2.isSameKindAs(r1));
        assertFalse(r2.isSameKindAs(f2));
    }

    @Test
    void allReturnCorrectly() {
        // TODOy

    }
}