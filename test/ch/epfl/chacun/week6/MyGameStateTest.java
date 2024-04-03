package ch.epfl.chacun.week6;

import ch.epfl.chacun.*;

import static ch.epfl.chacun.PlayerColor.*;
import static ch.epfl.chacun.GameState.Action.*;
import static ch.epfl.chacun.Occupant.Kind.*;
import static ch.epfl.cs108.Tiles.TILES;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyGameStateTest {
    static final int PAWN_MAX = Occupant.occupantsCount(PAWN);
    static final int HUT_MAX = Occupant.occupantsCount(HUT);

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
    PlacedTile t56RotNone = new PlacedTile(
            TILES.get(56), null, Rotation.NONE, new Pos(0, 0)
    );

    PlacedTile t11RotNoneWestOf56 = new PlacedTile(
            TILES.get(11), RED, Rotation.NONE, t56RotNone.pos().neighbor(Direction.W)
    );
    PlacedTile t11RotNoneWestOf56BLUE = new PlacedTile(
            TILES.get(11), BLUE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.W)
    );
    Occupant hutL118 = new Occupant(HUT, 118);

    PlacedTile t23RotNoneEastOf56 = new PlacedTile(
            TILES.get(23), BLUE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.E)
    );
    PlacedTile t23RotNoneEastOf56RED = new PlacedTile(
            TILES.get(23), RED, Rotation.NONE, t56RotNone.pos().neighbor(Direction.E)
    );
    Occupant pawnF233 = new Occupant(PAWN, 233);
    Occupant hutF231 = new Occupant(HUT, 231);

    PlacedTile t32RotNoneNorthOf56 = new PlacedTile(
            TILES.get(32), GREEN, Rotation.NONE, t56RotNone.pos().neighbor(Direction.N)
    );
    PlacedTile t32RotNoneNorthOf56RED = new PlacedTile(
            TILES.get(32), RED, Rotation.NONE, t56RotNone.pos().neighbor(Direction.N)
    );
    Occupant pawnM321 = new Occupant(PAWN, 321);

    PlacedTile t68RotNoneSouthOf56 = new PlacedTile(
            TILES.get(68), YELLOW, Rotation.NONE, t56RotNone.pos().neighbor(Direction.S)
    );
    PlacedTile t68RotNoneSouthOf56BLUE = new PlacedTile(
            TILES.get(68), BLUE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.S)
    );
    Occupant hutR682 = new Occupant(HUT, 682);

    PlacedTile t16RotNoneNorthEastOf56 = new PlacedTile(
            TILES.get(16), PURPLE, Rotation.NONE, t56RotNone.pos().translated(1, -1)
    );
    PlacedTile t16RotNoneNorthEastOf56RED = new PlacedTile(
            TILES.get(16), RED, Rotation.NONE, t56RotNone.pos().translated(1, -1)
    );
    Occupant pawnM162 = new Occupant(PAWN, 162);

    PlacedTile t74RotNoneNorthWestOf56 = new PlacedTile(
            TILES.get(74), BLUE, Rotation.NONE, t56RotNone.pos().translated(-1, -1)
    );
    PlacedTile t74RotNoneNorthWestOf56RED = new PlacedTile(
            TILES.get(74), RED, Rotation.NONE, t56RotNone.pos().translated(-1, -1)
    );
    Occupant pawnM740 = new Occupant(PAWN, 740);

    PlacedTile t33RotNoneSouthWestOf56 = new PlacedTile(
            TILES.get(33), GREEN, Rotation.NONE, t56RotNone.pos().translated(-1, 1)
    );
    PlacedTile t33RotNoneSouthWestOf56RED = new PlacedTile(
            TILES.get(33), RED, Rotation.NONE, t56RotNone.pos().translated(-1, 1)
    );
    Occupant pawnF330 = new Occupant(PAWN, 330);

    PlacedTile t66RotNoneSouthEastOf56 = new PlacedTile(
            TILES.get(66), RED, Rotation.NONE, t56RotNone.pos().translated(1, 1)
    );
    PlacedTile t66RotNoneSouthEastOf56BLUE = new PlacedTile(
            TILES.get(66), BLUE, Rotation.NONE, t56RotNone.pos().translated(1, 1)
    );
    PlacedTile t82RotNoneEastOf56 = new PlacedTile(
            TILES.get(82), PURPLE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.E)
    );
    Occupant pawnM681 = new Occupant(PAWN, 681);
    Occupant pawnF823 = new Occupant(PAWN, 823);


    PlacedTile t61RotNoneSouthEastOf56 = new PlacedTile(
            TILES.get(61), BLUE, Rotation.NONE, t56RotNone.pos().translated(1,1)
    );
    PlacedTile t61RotNoneNorthOf56 = new PlacedTile(
            TILES.get(61), BLUE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.N)
    );
    Occupant pawnM610 = new Occupant(PAWN, 610);

    PlacedTile t22HalfTurnWestOf56 = new PlacedTile(
            TILES.get(22), RED, Rotation.HALF_TURN, t56RotNone.pos().neighbor(Direction.W)
    );
    PlacedTile t24RotNoneSouthWestOf56 = new PlacedTile(
            TILES.get(24), GREEN, Rotation.NONE, t56RotNone.pos().translated(-1,1)
    );
    Occupant pawnM242 = new Occupant(PAWN, 242);
    PlacedTile t29RotNoneSouthOf24 = new PlacedTile(
            TILES.get(29), BLUE, Rotation.NONE, new Pos(-1,2)
    );
    PlacedTile t19HalfTurn2SouthOf56 = new PlacedTile(
            TILES.get(19), PURPLE, Rotation.HALF_TURN, t56RotNone.pos().translated(0, 2)
    );
    Occupant pawnR191 = new Occupant(PAWN, 191);

    Board boardCrossAround56Last68 = Board.EMPTY
            .withNewTile(t56RotNone)
            .withNewTile(t11RotNoneWestOf56).withOccupant(hutL118)
            .withNewTile(t23RotNoneEastOf56).withOccupant(pawnF233)
            .withNewTile(t32RotNoneNorthOf56).withOccupant(pawnM321)
            .withNewTile(t68RotNoneSouthOf56).withOccupant(hutR682);

    Board boardCrossAround56Last23 = Board.EMPTY
            .withNewTile(t56RotNone)
            .withNewTile(t11RotNoneWestOf56).withOccupant(hutL118)
            .withNewTile(t32RotNoneNorthOf56).withOccupant(pawnM321)
            .withNewTile(t68RotNoneSouthOf56).withOccupant(hutR682)
            .withNewTile(t23RotNoneEastOf56).withOccupant(pawnF233);

    Board boardSquareAround56Last66BLUE3Huts = Board.EMPTY
            .withNewTile(t56RotNone)
            .withNewTile(t11RotNoneWestOf56BLUE).withOccupant(hutL118)
            .withNewTile(t23RotNoneEastOf56).withOccupant(hutF231)
            .withNewTile(t32RotNoneNorthOf56)
            .withNewTile(t68RotNoneSouthOf56BLUE).withOccupant(hutR682)
            .withNewTile(t16RotNoneNorthEastOf56)
            .withNewTile(t74RotNoneNorthWestOf56)
            .withNewTile(t33RotNoneSouthWestOf56)
            .withNewTile(t66RotNoneSouthEastOf56BLUE);

    Board boardSquareAround56Last66RED5Pawns = Board.EMPTY
            .withNewTile(t56RotNone)
            .withNewTile(t11RotNoneWestOf56)
            .withNewTile(t23RotNoneEastOf56RED).withOccupant(pawnF233)
            .withNewTile(t32RotNoneNorthOf56RED).withOccupant(pawnM321)
            .withNewTile(t68RotNoneSouthOf56)
            .withNewTile(t16RotNoneNorthEastOf56RED).withOccupant(pawnM162)
            .withNewTile(t74RotNoneNorthWestOf56RED).withOccupant(pawnM740)
            .withNewTile(t33RotNoneSouthWestOf56RED).withOccupant(pawnF330)
            .withNewTile(t66RotNoneSouthEastOf56);
            

    /* TODO:
        withPlacedTile()            This one is gonna be fun (*ಠ_ಠ;)
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
        GameState state = new GameState(ALL, standardDecks(), TILES.get(69),
                boardCrossAround56Last68, PLACE_TILE, emptyMessageBoard);

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
                        new Occupant(PAWN, 681),
                        new Occupant(PAWN, 683)
                ),
                state1.lastTilePotentialOccupants()
        );

        GameState state2 = new GameState(ALL, standardDecks(), TILES.get(72),
                boardCrossAround56Last23, PLACE_TILE, emptyMessageBoard);

        assertEquals(
                /* last placed tile is 23, with a pawn in forest 233, thus no pawn can be placed
                there, current player is red who has > 1 huts and pawns remaining. */
                Set.of(
                        new Occupant(PAWN, 230),
                        new Occupant(PAWN, 231),
                        new Occupant(PAWN, 232),
                        new Occupant(HUT, 231)
                ),
                state2.lastTilePotentialOccupants()
        );
    }

    @Test
    void lastTilePotentialOccupantsWorksWithNoOccupantsRemaining() {
        GameState state1 = new GameState(List.of(BLUE, GREEN, YELLOW, PURPLE, RED), standardDecks(),
                TILES.get(41), boardSquareAround56Last66BLUE3Huts, PLACE_TILE, emptyMessageBoard);

        assertEquals(
                /* last placed tile is 66, possible pawn in 660, 661, 662,  663 and 664; Hut in 662
                not possible as blue (current player) does not have enough huts. */
                Set.of(
                        new Occupant(PAWN, 660),
                        new Occupant(PAWN, 661),
                        new Occupant(PAWN, 662),
                        new Occupant(PAWN, 663),
                        new Occupant(PAWN, 664)
                ),
                state1.lastTilePotentialOccupants()
        );

        GameState state2 = new GameState(ALL, standardDecks(), TILES.get(47),
                boardSquareAround56Last66RED5Pawns, PLACE_TILE, emptyMessageBoard);

        assertEquals(
                /* last placed tile is 66, no possible pawns as red (current player) has none left;
                however possible hut on river 662. */
                Collections.singleton(
                        new Occupant(HUT, 662)
                ),
                state2.lastTilePotentialOccupants()
        );
    }

    @Test
    void withStartingTilePlacedThrowsOnIllegalNextAction() {
        assertThrows(IllegalArgumentException.class, () -> {
            GameState initial = new GameState(ALL, standardDecks(), TILES.get(44), Board.EMPTY,
                    PLACE_TILE, emptyMessageBoard);
            initial.withStartingTilePlaced();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            GameState.Action[] actions = {RETAKE_PAWN, OCCUPY_TILE, END_GAME};

            for (GameState.Action action : actions) {
                GameState initial = new GameState(ALL, standardDecks(), null, Board.EMPTY,
                        action, emptyMessageBoard);

                initial.withStartingTilePlaced();
            }
        });
    }

    @Test
    void withStartingTilePlacedBehavesCorrectly() {
        GameState initial = GameState.initial(
                List.of(PURPLE, YELLOW, BLUE), standardDecks(), basicTextMaker
        );
        GameState withStartingTile = initial.withStartingTilePlaced();

        assertEquals(PLACE_TILE, withStartingTile.nextAction());
        assertEquals(
                Board.EMPTY.withNewTile(t56RotNone),
                withStartingTile.board()
        );
        assertEquals(
                TILES.getFirst(),
                withStartingTile.tileToPlace()
        );
        assertEquals(
                standardDecks().withTopTileDrawn(Tile.Kind.NORMAL),
                withStartingTile.tileDecks()
        );
        assertEquals(PURPLE, withStartingTile.currentPlayer());
    }

    @Test
    void withPlacedTileThrowsOnIllegalNextAction() {
        assertThrows(IllegalArgumentException.class, () -> {
            GameState.Action[] actions = {START_GAME, RETAKE_PAWN, OCCUPY_TILE, END_GAME};

            for (GameState.Action action : actions) {
                GameState initial = new GameState(ALL, standardDecks(), null, Board.EMPTY,
                        action, emptyMessageBoard);

                initial.withPlacedTile(t68RotNoneSouthOf56);
            }
        });
    }

    @Test
    void withPlacedTileThrowsOnOccupiedTile() {
        PlacedTile occupiedTile = new PlacedTile(
                TILES.get(32), PURPLE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.S),
                new Occupant(PAWN, 230)
        );

        assertThrows(IllegalArgumentException.class, () -> {
            GameState state = GameState.initial(ALL, standardDecks(), basicTextMaker)
                    .withStartingTilePlaced()
                    .withPlacedTile(occupiedTile);
        });
    }

    @Test
    void withPlacedTileEntersMenhirStatesForTrivialCase() {
        GameState state = GameState.initial( List.of(GREEN,PURPLE), standardDecks(), basicTextMaker);
        GameState menhirTriggerState = state.withStartingTilePlaced()
                .withPlacedTile(t82RotNoneEastOf56)
                .withPlacedTile(t68RotNoneSouthOf56)
                .withNewOccupant(pawnM681);

        assertEquals(GameState.Action.PLACE_TILE, menhirTriggerState.nextAction());
    }
    @Test
    void withPlacedTileEntersMenhirStatesForNoOccupyPossibleCase() {

        GameState startingState = GameState.initial(ALL, standardDecks(), basicTextMaker)
                .withStartingTilePlaced();
        GameState menhirTriggerState = startingState
                .withPlacedTile(t22HalfTurnWestOf56)
                .withPlacedTile(t24RotNoneSouthWestOf56).withNewOccupant(pawnM242)
                .withPlacedTile(t29RotNoneSouthOf24).withNewOccupant(null)
                .withPlacedTile(t19HalfTurn2SouthOf56).withNewOccupant(pawnR191)
                .withPlacedTile(t82RotNoneEastOf56).withNewOccupant(pawnF823)
                .withPlacedTile(t61RotNoneSouthEastOf56).withNewOccupant(pawnM610)
                .withPlacedTile(t68RotNoneSouthOf56);

        assertEquals(GameState.Action.PLACE_TILE, menhirTriggerState.nextAction());



    }

    /* TODO There might be a bug that causes the game state to skip the occupy phase for
        the first tile placed after the start tile.
        - See Test below for demonstration
    */
    @Test
    void withPlacedTileTileAfterStartEntersOccupy() {
        GameState state = GameState.initial(ALL, standardDecks(), basicTextMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t61RotNoneNorthOf56);
        assertEquals(OCCUPY_TILE, state.nextAction());

    }
}