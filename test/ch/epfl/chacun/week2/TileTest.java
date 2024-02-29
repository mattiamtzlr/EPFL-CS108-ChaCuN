package ch.epfl.chacun.week2;

import ch.epfl.chacun.Tile;
import ch.epfl.chacun.TileSide;
import ch.epfl.chacun.Zone;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Anthony Tamberg (357610)
 * @author Ali Gorgani (371956)
 */
class TileTest {
    private Zone.Meadow northZone = new Zone.Meadow(560, new ArrayList<>(), null);
    private Zone.Forest southEastZone = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
    private Zone.Meadow westZoneMeadow = new Zone.Meadow(562, new ArrayList<>(), null);
    private Zone.Lake lakeZone = new Zone.Lake(568, 4, null);
    private Zone.River westZoneRiver = new Zone.River(563, 3, lakeZone);

    private TileSide.Meadow n = new TileSide.Meadow(northZone);
    private TileSide.Forest e = new TileSide.Forest(southEastZone);
    private TileSide.Forest s = new TileSide.Forest(southEastZone);
    private TileSide.River w = new TileSide.River(westZoneMeadow, westZoneRiver, northZone);

    private Tile firstTile = new Tile(56, Tile.Kind.START, n, e, s, w);

    @Test
    void sidesWork() {
        List<TileSide> sides = new ArrayList<>();
        sides.add(n);
        sides.add(e);
        sides.add(s);
        sides.add(w);
        assertEquals(sides, firstTile.sides());
    }

    @Test
    void sideZonesWork() {
        Set<Zone> zones = new HashSet<>();
        zones.add(northZone);
        zones.add(southEastZone);
        zones.add(westZoneMeadow);
        zones.add(westZoneRiver);
        assertEquals(zones, firstTile.sideZones());
    }

    @Test
    void zonesWork() {
        Set<Zone> zones = new HashSet<>();
        zones.add(northZone);
        zones.add(southEastZone);
        zones.add(westZoneMeadow);
        zones.add(westZoneRiver);
        zones.add(lakeZone);
        assertEquals(zones, firstTile.zones());
    }

    @Test
    void id() {
        assertEquals(56, firstTile.id());
    }

    @Test
    void kind() {
        assertEquals(Tile.Kind.START, firstTile.kind());
    }

    @Test
    void n() {
        assertEquals(n, firstTile.n());
    }

    @Test
    void e() {
        assertEquals(n, firstTile.n());
    }

    @Test
    void s() {
        assertEquals(n, firstTile.n());
    }

    @Test
    void w() {
        assertEquals(n, firstTile.n());
    }
}