package ch.epfl.chacun.week6;

import ch.epfl.chacun.*;

import static ch.epfl.chacun.PlayerColor.*;
import static ch.epfl.chacun.GameState.Action.*;
import static ch.epfl.cs108.Tiles.TILES;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyGameStateTest {
    static final int PAWN_MAX = Occupant.occupantsCount(Occupant.Kind.PAWN);
    static final int HUT_MAX = Occupant.occupantsCount(Occupant.Kind.HUT);

    static TileDecks standardDecks() {
        List<Tile> startTiles = new ArrayList<>();
        List<Tile> normalTiles = new ArrayList<>();
        List<Tile> menhirTiles = new ArrayList<>();

        for (Tile tile : TILES) {
            switch (tile.kind()) {
                case START -> startTiles.add(tile);
                case NORMAL -> normalTiles.add(tile);
                case MENHIR -> menhirTiles.add(tile);
            }
        }

        return new TileDecks(startTiles, normalTiles, menhirTiles);
    }

    static TextMaker basicTextMaker = new BasicTextMaker();
    static MessageBoard emptyMessageBoard = new MessageBoard(
            basicTextMaker, Collections.emptyList());

    // Test tiles
    PlacedTile t56PRotNone = new PlacedTile(
            TILES.get(56), null, Rotation.NONE, new Pos(0, 0)
    );

    PlacedTile t11PRotNoneWestOf56 = new PlacedTile(
            TILES.get(11), RED, Rotation.NONE, t56PRotNone.pos().neighbor(Direction.W)
    );
    Occupant hutL118 = new Occupant(Occupant.Kind.HUT, 118);

    PlacedTile t23PRotNoneEastOf56 = new PlacedTile(
            TILES.get(23), BLUE, Rotation.NONE, t56PRotNone.pos().neighbor(Direction.E)
    );
    Occupant pawnF233 = new Occupant(Occupant.Kind.PAWN, 233);

    PlacedTile t32PRotNoneNorthOf56 = new PlacedTile(
            TILES.get(32), GREEN, Rotation.NONE, t56PRotNone.pos().neighbor(Direction.N)
    );
    Occupant pawnM321 = new Occupant(Occupant.Kind.PAWN, 321);

    PlacedTile t68PRotNoneSouthOf56 = new PlacedTile(
            TILES.get(68), YELLOW, Rotation.NONE, t56PRotNone.pos().neighbor(Direction.S)
    );
    Occupant hutR682 = new Occupant(Occupant.Kind.HUT, 682);

    Board boardCrossAround56Last68 = Board.EMPTY
            .withNewTile(t56PRotNone)
            .withNewTile(t11PRotNoneWestOf56).withOccupant(hutL118)
            .withNewTile(t23PRotNoneEastOf56).withOccupant(pawnF233)
            .withNewTile(t32PRotNoneNorthOf56).withOccupant(pawnM321)
            .withNewTile(t68PRotNoneSouthOf56).withOccupant(hutR682);

    Board boardCrossAround56Last23 = Board.EMPTY
            .withNewTile(t56PRotNone)
            .withNewTile(t11PRotNoneWestOf56).withOccupant(hutL118)
            .withNewTile(t32PRotNoneNorthOf56).withOccupant(pawnM321)
            .withNewTile(t68PRotNoneSouthOf56).withOccupant(hutR682)
            .withNewTile(t23PRotNoneEastOf56).withOccupant(pawnF233);

    /* TODO:
        edge cases of lastTilePotentialOccupants: not enough occupants etc.
        .
        withStartingTilePlaced()
        withPlacedTile()
        withOccupantRemoved()
        withNewOccupant()
     */

    @Test
    void constructorThrowsCorrectly() {
        // only one player
        assertThrows(IllegalArgumentException.class, () -> {
            new GameState(Collections.singletonList(BLUE), standardDecks(), null,
                    Board.EMPTY, START_GAME, emptyMessageBoard);
        });

        // illegal action - tileToPlace combination
        assertThrows(IllegalArgumentException.class, () -> {
            new GameState(ALL, standardDecks(), null, Board.EMPTY,
                    PLACE_TILE, emptyMessageBoard);
        });

        // tileDecks is null
        assertThrows(NullPointerException.class, () -> {
            new GameState(ALL, null, null, Board.EMPTY, START_GAME,
                    emptyMessageBoard);
        });

        // board is null
        assertThrows(NullPointerException.class, () -> {
            new GameState(ALL, standardDecks(), null, null, START_GAME,
                    emptyMessageBoard);
        });

        // nextAction is null
        assertThrows(NullPointerException.class, () -> {
            new GameState(ALL, standardDecks(), null, Board.EMPTY, null,
                    emptyMessageBoard);
        });

        // messageBoard is null
        assertThrows(NullPointerException.class, () -> {
            new GameState(ALL, standardDecks(), null, Board.EMPTY, START_GAME,
                    null);
        });
    }

    @Test
    void initialGameStateWorks() {
        GameState expected = new GameState(ALL, standardDecks(), null, Board.EMPTY,
                START_GAME, emptyMessageBoard);

        assertEquals(
                expected, GameState.initial(ALL, standardDecks(), basicTextMaker)
        );
    }

    @Test
    void currentPlayerWorksOnTrivialCases() {
        for (int i = 0; i < ALL.size(); i++) {
            List<PlayerColor> players = new ArrayList<>(ALL.size());
            for (int j = 0; j < ALL.size(); j++) {
                players.add(ALL.get((i + j) % ALL.size()));
            }

            GameState state = new GameState(players, standardDecks(),
                    standardDecks().topTile(Tile.Kind.NORMAL), Board.EMPTY, PLACE_TILE,
                    emptyMessageBoard);
            assertEquals(players.getFirst(), state.currentPlayer());
        }
    }

    @Test
    void currentPlayerReturnsNullIfStartOrEnd() {
        GameState state1 = new GameState(ALL, standardDecks(), null, Board.EMPTY,
                START_GAME, emptyMessageBoard);

        GameState state2 = new GameState(ALL, standardDecks(), null, Board.EMPTY,
                END_GAME, emptyMessageBoard);

        assertNull(state1.currentPlayer());
        assertNull(state2.currentPlayer());
    }

    @Test
    void freeOccupantsCountIsMaxOnBoardWithNoOccupants() {
        GameState state = GameState.initial(ALL, standardDecks(), basicTextMaker);

        for (Occupant.Kind kind : Occupant.Kind.values()) {
            for (PlayerColor color : ALL) {
                assertEquals(Occupant.occupantsCount(kind),
                        state.freeOccupantsCount(color, kind));
            }
        }
    }

    @Test
    void freeOccupantsCountWorksOnOccupiedBoard() {
        GameState state = new GameState(ALL, standardDecks(), TILES.get(69), boardCrossAround56Last68,
                PLACE_TILE, emptyMessageBoard);

        List<Integer> expected = List.of(
                PAWN_MAX, HUT_MAX - 1,
                PAWN_MAX - 1, HUT_MAX,
                PAWN_MAX - 1, HUT_MAX,
                PAWN_MAX, HUT_MAX - 1,
                PAWN_MAX, HUT_MAX
        );

        List<Integer> actual = new ArrayList<>();
        for (PlayerColor color : ALL) {
            for (Occupant.Kind kind : Occupant.Kind.values()) {
                actual.add(state.freeOccupantsCount(color, kind));
            }
        }

        assertEquals(expected, actual);
    }

    @Test
    void lastTilePotentialOccupantsThrowsOnEmptyBoard() {
        GameState state = GameState.initial(ALL, standardDecks(), basicTextMaker);
        assertThrows(IllegalArgumentException.class, state::lastTilePotentialOccupants);
    }

    @Test
    void lastTilePotentialOccupantsWorksOnTrivialCases() {
        GameState state1 = new GameState(ALL, standardDecks(), TILES.get(69),
                boardCrossAround56Last68, PLACE_TILE, emptyMessageBoard);

        assertEquals(
                /* last placed tile is 68, with a hut on river 682, thus no pawn and no hut can be
                placed there, current player is red, who has > 1 huts and pawns remaining.
                Also, as the forest area that the forest 680 at the top belongs to is occupied by
                a pawn in forest 233, it is also not valid. */

                Set.of(
                        new Occupant(Occupant.Kind.PAWN, 681),
                        new Occupant(Occupant.Kind.PAWN, 683)
                ),
                state1.lastTilePotentialOccupants()
        );

        GameState state2 = new GameState(ALL,standardDecks(), TILES.get(72),
                boardCrossAround56Last23, PLACE_TILE, emptyMessageBoard);

        assertEquals(
                /* last placed tile is 23, with a pawn in forest 233, thus no pawn can be placed
                there, current player is red who has > 1 huts and pawns remaining. */
                Set.of(
                        new Occupant(Occupant.Kind.PAWN, 230),
                        new Occupant(Occupant.Kind.PAWN, 231),
                        new Occupant(Occupant.Kind.PAWN, 232),
                        new Occupant(Occupant.Kind.HUT, 231)
                ),
                state2.lastTilePotentialOccupants()
        );
    }
}