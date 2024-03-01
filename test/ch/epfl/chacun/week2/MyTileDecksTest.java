package ch.epfl.chacun.week2;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class MyTileDecksTest {

    Animal a5601 = new Animal(5601, Animal.Kind.AUROCHS);
    Zone.Meadow m560 = new Zone.Meadow(560, List.of(a5601), null);
    Zone.Forest f561 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);

    Zone.Meadow m562 = new Zone.Meadow(562, List.of(), null);
    Zone.Lake l568 = new Zone.Lake(568, 1, null);
    Zone.River r563 = new Zone.River(563, 0, l568);
    TileSide n = new TileSide.Meadow(m560);
    TileSide e = new TileSide.Forest(f561);
    TileSide s = new TileSide.Forest(f561);
    TileSide w = new TileSide.River(m562, r563, m560);
    Tile startTile = new Tile(56, Tile.Kind.START, n, e, s, w);
    Tile trivialNormalTile = new Tile(670, Tile.Kind.NORMAL, n, n, n, n);
    Tile trivialMenhirTile = new Tile(780, Tile.Kind.MENHIR, e, e, e, e);
    Tile specialMenhirTile = new Tile(660, Tile.Kind.MENHIR, w, e, e, e);
    TileDecks trivialDeck = new TileDecks(List.of(startTile), List.of(trivialNormalTile), List.of(trivialMenhirTile, specialMenhirTile));
    TileDecks emptyDeck = new TileDecks(List.of(), List.of(), List.of());

    @Test
    void deckSizeWorksForTrivialDeck() {
        assertEquals(1, trivialDeck.deckSize(Tile.Kind.START));
        assertEquals(1, trivialDeck.deckSize(Tile.Kind.NORMAL));
        assertEquals(2, trivialDeck.deckSize(Tile.Kind.MENHIR));
        }
    @Test
    void deckSizeWorksForEmptyDeck() {
        assertEquals(0, emptyDeck.deckSize(Tile.Kind.START));
        assertEquals(0, emptyDeck.deckSize(Tile.Kind.NORMAL));
        assertEquals(0, emptyDeck.deckSize(Tile.Kind.MENHIR));
    }

    @Test
    void topTileWorksForTrivialDeck() {
        assertEquals(startTile, trivialDeck.topTile(Tile.Kind.START));
        assertEquals(trivialNormalTile, trivialDeck.topTile(Tile.Kind.NORMAL));
        assertEquals(trivialMenhirTile, trivialDeck.topTile(Tile.Kind.MENHIR));

    }
    @Test
    void topTileWorksForEmptyDeck() {
        assertNull(emptyDeck.topTile(Tile.Kind.MENHIR));
    }

    @Test
    void withTopTileDrawn() {
        TileDecks expectedDeck = new TileDecks(List.of(startTile), List.of(), List.of(trivialMenhirTile, specialMenhirTile));
        assertEquals(expectedDeck, trivialDeck.withTopTileDrawn(Tile.Kind.NORMAL));

    }
    @Test
    void withTopTileDrawnWorksTrivialDeck() {
        TileDecks expectedDeck = new TileDecks(List.of(startTile), List.of(trivialNormalTile), List.of(specialMenhirTile));
        assertEquals(expectedDeck, trivialDeck.withTopTileDrawn(Tile.Kind.MENHIR));

    }
    @Test
    void withTopTileDrawnFailsOnEmptySet() {
        assertThrows(IllegalArgumentException.class, () -> emptyDeck.withTopTileDrawn(Tile.Kind.NORMAL));
    }

    @Test
    void withTopTileDrawnUntilWorksOnTrivialDeck() {
        TileDecks expectedDeck = new TileDecks(List.of(startTile), List.of(trivialNormalTile), List.of(specialMenhirTile));
        Predicate<Tile> predicate = t -> (t.sides().equals(List.of(w, e, e, e)));
        assertEquals(expectedDeck, trivialDeck.withTopTileDrawnUntil(Tile.Kind.MENHIR, predicate));
    }
    // Checking for an edge case where more tiles should be drawn than are on the stack
    @Test
    void withTopTileDrawnUntilWorksWithTrivialPredicate() {
        Predicate<Tile> trivialPredicate = t -> false;
        TileDecks expectedDeck = new TileDecks(List.of(startTile), List.of(trivialNormalTile), List.of());
        assertEquals(expectedDeck, trivialDeck.withTopTileDrawnUntil(Tile.Kind.MENHIR, trivialPredicate));
    }

}