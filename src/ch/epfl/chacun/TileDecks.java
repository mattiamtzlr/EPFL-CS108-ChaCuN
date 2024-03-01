package ch.epfl.chacun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Record to keep track of the three different decks.
 * - startDeck: contains start tile
 * - normalDeck: contains all normal tiles
 * - menhirDeck: contains menhir tiles
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {
    /**
     * Compact constructor to ensure immutability
     * @param startTiles the starting tile
     * @param normalTiles a collection containing all normal tiles
     * @param menhirTiles a collection containing all menhir tiles
     */
    public TileDecks {
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }

    /**
     * Method to calculate the size of a chosen deck
     * @param kind Deck kind
     * @return the number of cards in the chosen deck
     */
    public int deckSize(Tile.Kind kind) {
        return switch (kind) {
            case START -> startTiles.size();
            case NORMAL -> normalTiles.size();
            case MENHIR -> menhirTiles.size();
        };

    }

    /**
     * Method to get the top Tile in a chosen deck
     * @param kind Deck kind
     * @return the Tile on the top of a chosen deck
     */
    public Tile topTile(Tile.Kind kind) {
        try {
            return switch (kind) {
                case START -> startTiles.getFirst();
                case NORMAL -> normalTiles.getFirst();
                case MENHIR -> menhirTiles.getFirst();
            };
        } catch(NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Method to get the deck triplet without one top tile
     * @param kind Deck kind
     * @return a TileDecks instance with one tile missing from a chosen deck
     */
    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        Preconditions.checkArgument(deckSize(kind) != 0);
        return switch(kind) {
            case START -> new TileDecks(
                        startTiles.subList(1, startTiles.size()),
                        normalTiles,
                        menhirTiles);
            case NORMAL -> new TileDecks(
                    startTiles,
                    normalTiles.subList(1, normalTiles.size()),
                    menhirTiles);
            case MENHIR -> new TileDecks(
                    startTiles,
                    normalTiles,
                    menhirTiles.subList(1, menhirTiles.size())
            );
        };
    }

    /**
     * Method that allows to batch remove tiles from the top of a chosen deck
     * @param kind Deck kind
     * @param predicate Condition to be checked on the top tiles of the chosen deck
     * @return a TileDecks instance with every tile on the top of (kind)Tiles that does not pass the
     * predicate test removed
     */
    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        TileDecks temp = new TileDecks(startTiles, normalTiles, menhirTiles);
        while (temp.topTile(kind) != null && !predicate.test(temp.topTile(kind))) {
            temp = temp.withTopTileDrawn(kind);
        }
        return temp;
    }
}
