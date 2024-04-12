package ch.epfl.chacun.stage2;

import ch.epfl.chacun.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyTileSideTest {
    Zone.Forest fz1 = new Zone.Forest(347, Zone.Forest.Kind.PLAIN);
    TileSide.Forest f1 = new TileSide.Forest(fz1);

    Zone.Forest fz2 = new Zone.Forest(812, Zone.Forest.Kind.WITH_MENHIR);
    TileSide.Forest f2 = new TileSide.Forest(fz2);

    Zone.Meadow mz1 = new Zone.Meadow(465, List.of(new Animal(4650, Animal.Kind.DEER)), Zone.SpecialPower.HUNTING_TRAP);
    TileSide.Meadow m1 = new TileSide.Meadow(mz1);

    Zone.Meadow mz2 = new Zone.Meadow(142, List.of(new Animal(1420, Animal.Kind.AUROCHS)), null);
    TileSide.Meadow m2 = new TileSide.Meadow(mz2);

    Zone.Meadow mzr1 = new Zone.Meadow(830, new ArrayList<>(), null);
    Zone.River rz1 = new Zone.River(831, 2, null);
    Zone.Meadow mzr2 = new Zone.Meadow(832, new ArrayList<>(), null);
    TileSide.River r1 = new TileSide.River(mzr1, rz1, mzr2);

    Zone.Meadow mzr3 = new Zone.Meadow(682, new ArrayList<>(), null);
    Zone.River rz2 = new Zone.River(683, 1, new Zone.Lake(688, 1, null));
    Zone.Meadow mzr4 = new Zone.Meadow(684, new ArrayList<>(), null);
    TileSide.River r2 = new TileSide.River(mzr3, rz2, mzr4);

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
    void allReturnZonesCorrectly() {
        assertEquals(Collections.singletonList(fz1), f1.zones());
        assertEquals(Collections.singletonList(fz2), f2.zones());
        assertEquals(Collections.singletonList(mz1), m1.zones());
        assertEquals(Collections.singletonList(mz2), m2.zones());
        assertEquals(List.of(mzr1, rz1, mzr2), r1.zones());
        assertEquals(List.of(mzr3, rz2, mzr4), r2.zones());
    }
}