package ch.epfl.chacun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {
    public TileDecks {
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }
    public int deckSize(Tile.Kind kind) {
        return switch (kind) {
            case START -> startTiles.size();
            case NORMAL -> normalTiles.size();
            case MENHIR -> menhirTiles.size();
        };

    }

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
    //==================================================================================================================
    // TODO this is hacky at best, test thoroughly :)
    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        TileDecks temp = new TileDecks(startTiles, normalTiles, menhirTiles);
        while (!predicate.test(temp.topTile(kind))) {
            temp = temp.withTopTileDrawn(kind);
        }
        return temp;
    }
    //==================================================================================================================
}
