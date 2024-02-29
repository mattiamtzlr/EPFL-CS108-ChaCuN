package ch.epfl.chacun.week2;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Anthony Tamberg (357610)
 * @author Ali Gorgani (371956)
 */
class TileDecksTest {

    private final Zone.Meadow northZone = new Zone.Meadow(560, new ArrayList<>(), null);
    private final Zone.Forest southEastZone = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
    private final Zone.Meadow westZoneMeadow = new Zone.Meadow(562, new ArrayList<>(), null);
    private final Zone.Lake lakeZone = new Zone.Lake(568, 4, Zone.Lake.SpecialPower.RAFT);
    private final Zone.River westZoneRiver = new Zone.River(563, 3, lakeZone);



    private final TileSide.Meadow n = new TileSide.Meadow(northZone);
    private final TileSide.Forest e = new TileSide.Forest(southEastZone);
    private final TileSide.Forest s = new TileSide.Forest(southEastZone);
    private final TileSide.River w = new TileSide.River(westZoneMeadow, westZoneRiver, northZone);

    private final Tile startingTile = new Tile(56, Tile.Kind.START, n, e, s, w);

    private final TileDecks deckFinal = deckBuilder();

    private final TileSide tileSide0 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
    private final Tile topNormalTile = new Tile(0, Tile.Kind.NORMAL, tileSide0, tileSide0, tileSide0, tileSide0);

    private final TileSide tileSideMenhir = new TileSide.Forest(new Zone.Forest(79 * 10, Zone.Forest.Kind.WITH_MENHIR));
    private final TileSide tileSide79 = new TileSide.Forest(new Zone.Forest(79 * 10, Zone.Forest.Kind.PLAIN));
    private final Tile topMenhirTile = new Tile(79, Tile.Kind.MENHIR, tileSideMenhir, tileSide79, tileSide79, tileSide79);

    private TileDecks deckBuilder() {
        List<Tile> normal = new ArrayList<>();
        List<Tile> menhir = new ArrayList<>();
        for (int i = 0; i < 79; ++i) {
            if (i == 56) continue;
            TileSide tileSide = new TileSide.Forest(new Zone.Forest(i * 10, Zone.Forest.Kind.PLAIN));
            normal.add(new Tile(i, Tile.Kind.NORMAL, tileSide, tileSide, tileSide, tileSide));
        }
        for (int i = 79; i < 95; ++i) {
            TileSide tileSideMenhir = new TileSide.Forest(new Zone.Forest(i * 10, Zone.Forest.Kind.WITH_MENHIR));
            TileSide tileSide = new TileSide.Forest(new Zone.Forest(i * 10, Zone.Forest.Kind.PLAIN));
            menhir.add(new Tile(i, Tile.Kind.MENHIR, tileSideMenhir, tileSide, tileSide, tileSide));
        }
        return new TileDecks(Collections.singletonList(startingTile), normal, menhir);
    }

    @Test
    void deckSizeWorks() {
        assertEquals(1, deckFinal.deckSize(Tile.Kind.START));
        assertEquals(78, deckFinal.deckSize(Tile.Kind.NORMAL));
        assertEquals(16, deckFinal.deckSize(Tile.Kind.MENHIR));
    }

    @Test
    void topTileWorks() {
        assertEquals(startingTile, deckFinal.topTile(Tile.Kind.START));
        assertEquals(topNormalTile, deckFinal.topTile(Tile.Kind.NORMAL));
        assertEquals(topMenhirTile, deckFinal.topTile(Tile.Kind.MENHIR));

        assertNull(new TileDecks(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()).topTile(Tile.Kind.NORMAL));
    }

    @Test
    void withTopTileDrawnWorks() {
        List<Tile> startAfter = new ArrayList<>();
        List<Tile> normalAfter = deckFinal.normalTiles().subList(1, deckFinal.normalTiles().size());
        List<Tile> menhirAfter = deckFinal.menhirTiles().subList(1, deckFinal.menhirTiles().size());
        assertEquals(startAfter, deckFinal.withTopTileDrawn(Tile.Kind.START).startTiles());
        assertEquals(normalAfter, deckFinal.withTopTileDrawn(Tile.Kind.NORMAL).normalTiles());
        assertEquals(menhirAfter, deckFinal.withTopTileDrawn(Tile.Kind.MENHIR).menhirTiles());
        assertEquals(new TileDecks(startAfter,normalAfter,menhirAfter), deckFinal.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL).withTopTileDrawn(Tile.Kind.MENHIR));
    }

    @Test
    void withTopTileDrawnUntilWorks() {
        TileDecks normalDeckAfter = deckFinal.withTopTileDrawnUntil(Tile.Kind.NORMAL, t -> t.zones().size() == 2);
        assertEquals(new ArrayList<>(), normalDeckAfter.normalTiles());

        TileDecks startDeckAfter = deckFinal.withTopTileDrawnUntil(Tile.Kind.START, t -> t.id() == 56);
        assertEquals(Collections.singletonList(startingTile), startDeckAfter.startTiles());
    }

    @Test
    void startTilesWorks() {
        assertEquals(Collections.singletonList(startingTile), deckFinal.startTiles());
    }

    @Test
    void normalTilesWorks() {
        List<Tile> normal = new ArrayList<>();
        for (int i = 0; i < 79; ++i) {
            if (i == 56) continue;
            TileSide tileSide = new TileSide.Forest(new Zone.Forest(i * 10, Zone.Forest.Kind.PLAIN));
            normal.add(new Tile(i, Tile.Kind.NORMAL, tileSide, tileSide, tileSide, tileSide));
        }
        assertEquals(normal, deckFinal.normalTiles());
    }

    @Test
    void menhirTilesWorks() {
        List<Tile> menhir = new ArrayList<>();
        for (int i = 79; i < 95; ++i) {
            TileSide tileSideMenhir = new TileSide.Forest(new Zone.Forest(i * 10, Zone.Forest.Kind.WITH_MENHIR));
            TileSide tileSide = new TileSide.Forest(new Zone.Forest(i * 10, Zone.Forest.Kind.PLAIN));
            menhir.add(new Tile(i, Tile.Kind.MENHIR, tileSideMenhir, tileSide, tileSide, tileSide));
        }
        assertEquals(menhir, deckFinal.menhirTiles());
    }
}