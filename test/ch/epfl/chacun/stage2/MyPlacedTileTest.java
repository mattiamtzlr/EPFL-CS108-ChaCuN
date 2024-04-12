package ch.epfl.chacun.stage2;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MyPlacedTileTest {
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
    PlacedTile startTilePlacedRotNone = new PlacedTile(startTile, null, Rotation.NONE, new Pos(0, 0));
    PlacedTile startTilePlacedRotRight = new PlacedTile(startTile, null, Rotation.RIGHT, new Pos(0, 0));
    PlacedTile startTilePlacedRotHalf = new PlacedTile(startTile, null, Rotation.HALF_TURN, new Pos(0, 0));
    PlacedTile startTilePlacedRotLeft = new PlacedTile(startTile, null, Rotation.LEFT, new Pos(0, 0));

    // Tile 61
    Zone.Meadow m610 = new Zone.Meadow(610, List.of(new Animal(6100, Animal.Kind.MAMMOTH)), null);
    TileSide.Meadow t61N = new TileSide.Meadow(m610);
    TileSide.Meadow t61E = new TileSide.Meadow(m610);
    TileSide.Meadow t61S = new TileSide.Meadow(m610);
    TileSide.Meadow t61W = new TileSide.Meadow(m610);

    Tile t61 = new Tile(61, Tile.Kind.NORMAL, t61N, t61E, t61S, t61W);
    PlacedTile t61Placed = new PlacedTile(t61, PlayerColor.GREEN, Rotation.NONE,
            startTilePlacedRotNone.pos().neighbor(Direction.N), new Occupant(Occupant.Kind.PAWN, m610.id()));

    // Tile 88
    Zone.Meadow m880 = new Zone.Meadow(880, Collections.emptyList(), Zone.SpecialPower.SHAMAN);
    Zone.River r881 = new Zone.River(881, 1, null);
    Zone.Meadow m882 = new Zone.Meadow(882, Collections.emptyList(), null);
    Zone.Forest f883 = new Zone.Forest(883, Zone.Forest.Kind.PLAIN);

    TileSide.River t88N = new TileSide.River(m880, r881, m882);
    TileSide.River t88E = new TileSide.River(m882, r881, m880);
    TileSide.Forest t88S = new TileSide.Forest(f883);
    TileSide.Meadow t88W = new TileSide.Meadow(m880);

    Tile t88 = new Tile(88, Tile.Kind.NORMAL, t88N, t88E, t88S, t88W);
    PlacedTile t88Placed = new PlacedTile(t88, PlayerColor.PURPLE, Rotation.NONE,
            startTilePlacedRotNone.pos().neighbor(Direction.W));

    @Test
    void returnIdAndKindCorrectly() {
        assertEquals(56, startTilePlacedRotNone.id());
        assertEquals(Tile.Kind.START, startTilePlacedRotNone.kind());
    }

    @Test
    void getRotatedSidesCorrectly() {
        assertEquals(startW, startTilePlacedRotNone.side(Direction.W));
        assertEquals(startN, startTilePlacedRotRight.side(Direction.E));
        assertEquals(startS, startTilePlacedRotHalf.side(Direction.N));
        assertEquals(startE, startTilePlacedRotLeft.side(Direction.N));
    }

    @Test
    void getZoneByIdWorks() {
        assertEquals(m560, startTilePlacedRotNone.zoneWithId(560));
        assertEquals(f561, startTilePlacedRotNone.zoneWithId(561));
        assertEquals(m562, startTilePlacedRotNone.zoneWithId(562));
        assertEquals(r563, startTilePlacedRotNone.zoneWithId(563));
        assertEquals(l568, startTilePlacedRotNone.zoneWithId(568));
    }

    @Test
    void getZoneByIdThrowsOnInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> startTilePlacedRotNone.zoneWithId(732));
        assertThrows(IllegalArgumentException.class, () -> startTilePlacedRotNone.zoneWithId(564));
    }

    @Test
    void specialPowerZoneWorks() {
        assertNull(startTilePlacedRotNone.specialPowerZone());
        assertEquals(m880, t88Placed.specialPowerZone());
    }

    @Test
    void getDifferentZonesCorrectly() {
        assertEquals(Set.of(f561), startTilePlacedRotNone.forestZones());
        assertEquals(Set.of(m560, m562), startTilePlacedRotNone.meadowZones());
        assertEquals(Set.of(r563), startTilePlacedRotNone.riverZones());

        assertEquals(Set.of(m610), t61Placed.meadowZones());
        assertEquals(Collections.emptySet(), t61Placed.forestZones());
        assertEquals(Collections.emptySet(), t61Placed.riverZones());
    }

    @Test
    void getOccupantsWorksCorrectly() {
        assertEquals(Collections.emptySet(), startTilePlacedRotNone.potentialOccupants());

        assertEquals(
                Set.of(
                        new Occupant(Occupant.Kind.PAWN, m880.id()),
                        new Occupant(Occupant.Kind.PAWN, r881.id()),
                        new Occupant(Occupant.Kind.HUT, r881.id()),
                        new Occupant(Occupant.Kind.PAWN, m882.id()),
                        new Occupant(Occupant.Kind.PAWN, f883.id())
                ),
                t88Placed.potentialOccupants()
        );

        assertEquals(
                Set.of(
                        new Occupant(Occupant.Kind.PAWN, m610.id())
                ),
                t61Placed.potentialOccupants()
        );
    }

    @Test
    void withOccupantWorksOnEmpty() {
        Occupant toAdd = new Occupant(Occupant.Kind.PAWN, f883.id());
        assertEquals(
                new PlacedTile(t88, PlayerColor.PURPLE, Rotation.NONE,
                        startTilePlacedRotNone.pos().neighbor(Direction.W), toAdd),
                t88Placed.withOccupant(toAdd)
        );
    }

    @Test
    void withOccupantThrowsOnFull() {
        Occupant toAdd = new Occupant(Occupant.Kind.PAWN, m610.id());
        assertThrows(
                IllegalArgumentException.class,
                () -> t61Placed.withOccupant(toAdd)
        );
    }

    @Test
    void withNoOccupantWorksCorrectly() {
        PlacedTile expected = new PlacedTile(t61, PlayerColor.GREEN, Rotation.NONE,
                startTilePlacedRotNone.pos().neighbor(Direction.N));

        assertEquals(expected, t61Placed.withNoOccupant());
        assertEquals(t88Placed, t88Placed.withNoOccupant());
    }

    @Test
    void getZoneIdByOccupantCorrectly() {
        assertEquals(m610.id(), t61Placed.idOfZoneOccupiedBy(Occupant.Kind.PAWN));
        assertEquals(-1, startTilePlacedRotNone.idOfZoneOccupiedBy(Occupant.Kind.HUT));
    }
}
