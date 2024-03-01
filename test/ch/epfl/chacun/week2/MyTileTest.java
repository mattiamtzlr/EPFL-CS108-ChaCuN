package ch.epfl.chacun.week2;

import ch.epfl.chacun.Animal;
import ch.epfl.chacun.Tile;
import ch.epfl.chacun.TileSide;
import ch.epfl.chacun.Zone;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MyTileTest {
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

    Tile normalTile = new Tile(47, Tile.Kind.NORMAL, m1, r1, f2, f2);

    Zone.Meadow m560 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)), null);
    Zone.Forest f561 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Meadow m562 = new Zone.Meadow(562, Collections.emptyList(), null);
    Zone.Lake l568 = new Zone.Lake(568, 1, null);
    Zone.River r563 = new Zone.River(563, 0, l568);
    TileSide.Meadow startN = new TileSide.Meadow(m560);
    TileSide.Forest startE = new TileSide.Forest(f561);
    TileSide.Forest startS = new TileSide.Forest(f561);
    TileSide.River startW = new TileSide.River(m562, r563, m560);
    Tile startTile = new Tile(56, Tile.Kind.START, startN, startE, startS, startW);

    @Test
    void returnSidesCorrectly() {
        assertEquals(List.of(m1, r1, f2, f2), normalTile.sides());
    }

    @Test
    void startReturnsSideZonesCorrectly() {
        assertEquals(Set.of(m560, f561, m562, r563), startTile.sideZones());
    }

    @Test
    void startRetunsZonesCorrectly() {
        assertEquals(Set.of(m560, f561, m562, r563, l568), startTile.zones());
    }
}
