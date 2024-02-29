package ch.epfl.chacun.week2;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Anthony Tamberg (357610)
 * @author Ali Gorgani (371956)
 */
class PlacedTileTest {

    private Zone.Meadow northZone = new Zone.Meadow(560, new ArrayList<>(), null);
    private Zone.Forest southEastZone = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
    private Zone.Meadow westZoneMeadow = new Zone.Meadow(562, new ArrayList<>(), null);
    private Zone.Lake lakeZone = new Zone.Lake(568, 4, Zone.Lake.SpecialPower.RAFT);
    private Zone.River westZoneRiver = new Zone.River(563, 3, lakeZone);

    private TileSide.Meadow n = new TileSide.Meadow(northZone);
    private TileSide.Forest e = new TileSide.Forest(southEastZone);
    private TileSide.Forest s = new TileSide.Forest(southEastZone);
    private TileSide.River w = new TileSide.River(westZoneMeadow, westZoneRiver, northZone);

    private Tile firstTile = new Tile(56, Tile.Kind.START, n, e, s, w);

    private PlacedTile placedFirstTile = new PlacedTile(firstTile, null, Rotation.NONE, Pos.ORIGIN);

    private Occupant occupantPawn = new Occupant(Occupant.Kind.PAWN, 560);
    private PlacedTile placedFirstTileOccupied = new PlacedTile(firstTile, null, Rotation.NONE, Pos.ORIGIN, occupantPawn);


    @Test
    void placedTileConstructorThrowsOnNullTile() {
        assertThrows(IllegalArgumentException.class, () -> {
          new PlacedTile(null, null, Rotation.NONE, Pos.ORIGIN);
        });
    }

    @Test
    void placedTileConstructorThrowsOnNullRotation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PlacedTile(firstTile, null, null, Pos.ORIGIN);
        });
    }

    @Test
    void placedTileConstructorThrowsOnNullPos() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PlacedTile(firstTile, null, Rotation.NONE, null);
        });
    }

    @Test
    void idWorks() {
        assertEquals(56, placedFirstTile.id());
    }

    @Test
    void kindWorks() {
        assertEquals(Tile.Kind.START, placedFirstTile.kind());
    }

    @Test
    void sideWorks() {
        assertEquals(n, placedFirstTile.side(Direction.N));
        assertEquals(e, placedFirstTile.side(Direction.E));
        assertEquals(s, placedFirstTile.side(Direction.S));
        assertEquals(w, placedFirstTile.side(Direction.W));
    }

    @Test
    void zoneWithIdWorks() {
        Set<Zone> zones = new HashSet<>();
        zones.add(northZone); zones.add(southEastZone); zones.add(westZoneMeadow); zones.add(westZoneRiver); zones.add(lakeZone);
        for (Zone zone : zones) {
            assertEquals(zone, placedFirstTile.zoneWithId(zone.id()));
        }
    }

    @Test
    void zoneWithIdThrowsOnUnknownId() {
        assertThrows(IllegalArgumentException.class, () -> {
          placedFirstTile.zoneWithId(573);
        });
    }

    @Test
    void specialPowerZoneWorks() {
        assertEquals(lakeZone, placedFirstTile.specialPowerZone());
    }

    @Test
    void forestZonesWork() {
        assertEquals(Collections.singleton(southEastZone), placedFirstTile.forestZones());
    }

    @Test
    void meadowZonesWork() {
        Set<Zone> zones = new HashSet<>();
        zones.add(westZoneMeadow);
        zones.add(northZone);
        assertEquals(zones, placedFirstTile.meadowZones());
    }

    @Test
    void riverZonesWork() {
        assertEquals(Collections.singleton(westZoneRiver), placedFirstTile.riverZones());
    }

    @Test
    void potentialOccupantsWork() {
        Set<Occupant> expectedOccupants = new HashSet<>();
        expectedOccupants.add(new Occupant(Occupant.Kind.PAWN,560));
        expectedOccupants.add(new Occupant(Occupant.Kind.PAWN,561));
        expectedOccupants.add(new Occupant(Occupant.Kind.PAWN,562));
        expectedOccupants.add(new Occupant(Occupant.Kind.PAWN,563));
        expectedOccupants.add(new Occupant(Occupant.Kind.HUT,568));
        assertEquals(expectedOccupants, placedFirstTile.potentialOccupants());
    }

    @Test
    void withOccupantWorks() {
        assertEquals(placedFirstTileOccupied, placedFirstTile.withOccupant(new Occupant(Occupant.Kind.PAWN, 560)));
    }

    @Test
    void withOccupantThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            placedFirstTileOccupied.withOccupant(new Occupant(Occupant.Kind.HUT, 560));
        });
    }

    @Test
    void withNoOccupantWorks() {
        assertEquals(placedFirstTile, placedFirstTileOccupied.withNoOccupant());
    }

    @Test
    void idOfZoneOccupiedByWorks() {
        assertEquals(occupantPawn.zoneId(), placedFirstTileOccupied.idOfZoneOccupiedBy(Occupant.Kind.PAWN));
        assertEquals(-1, placedFirstTile.idOfZoneOccupiedBy(Occupant.Kind.PAWN));
        assertEquals(-1, placedFirstTileOccupied.idOfZoneOccupiedBy(Occupant.Kind.HUT));
    }

    @Test
    void tile() {
        assertEquals(firstTile, placedFirstTile.tile());
    }

    @Test
    void placer() {
        assertEquals(null, placedFirstTile.placer());
    }

    @Test
    void rotation() {
        assertEquals(Rotation.NONE, placedFirstTile.rotation());
    }

    @Test
    void pos() {
        assertEquals(Pos.ORIGIN, placedFirstTile.pos());
    }

    @Test
    void occupant() {
        assertNull(placedFirstTile.occupant());
        assertEquals(occupantPawn, placedFirstTileOccupied.occupant());
    }
}
