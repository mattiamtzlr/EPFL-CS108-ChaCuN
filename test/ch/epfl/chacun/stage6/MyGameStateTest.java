package ch.epfl.chacun.stage6;

import ch.epfl.chacun.*;

import static ch.epfl.chacun.PlayerColor.*;
import static ch.epfl.chacun.GameState.Action.*;
import static ch.epfl.chacun.Occupant.Kind.*;
import static ch.epfl.chacun.tile.Tiles.TILES;

import org.junit.jupiter.api.Test;

import java.util.*;

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

    static TileDecks customDecks(List<Integer> standardTileIds, List<Integer> menhirTileIds) {
        return new TileDecks(
                Collections.singletonList(TILES.get(56)),
                standardTileIds.stream().map(TILES::get).toList(),
                menhirTileIds.stream().map(TILES::get).toList()
        );
    }

    static TextMaker textMaker = new BasicTextMaker();
    static MessageBoard emptyMessageBoard = new MessageBoard(
            textMaker, Collections.emptyList());

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
            TILES.get(61), BLUE, Rotation.NONE, t56RotNone.pos().translated(1, 1)
    );

    PlacedTile t61RotNoneNorthOf56 = new PlacedTile(
            TILES.get(61), BLUE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.N)
    );
    Occupant pawnM610 = new Occupant(PAWN, 610);


    PlacedTile t37RotNoneSouthOf56 = new PlacedTile(
            TILES.get(37), PURPLE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.S)
    );


    PlacedTile t88RotNoneTwiceNorthOf56SHAMAN = new PlacedTile(
            TILES.get(88), GREEN, Rotation.NONE, t56RotNone.pos().translated(0, -2)
    );

    PlacedTile t30RotNoneEastOf56 = new PlacedTile(
            TILES.get(30), RED, Rotation.NONE, t56RotNone.pos().neighbor(Direction.E)
    );
    Occupant pawnM300 = new Occupant(PAWN, 300);

    PlacedTile t15RotNoneWestOf56 = new PlacedTile(
            TILES.get(15), BLUE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.W)
    );
    Occupant hutR151 = new Occupant(HUT, 151);

    PlacedTile t62RotNoneNorthEastOf56 = new PlacedTile(
            TILES.get(62), GREEN, Rotation.NONE, t56RotNone.pos().translated(1, -1)
    );

    PlacedTile t31RotNoneNorthWestOf56 = new PlacedTile(
            TILES.get(31), YELLOW, Rotation.NONE, t56RotNone.pos().translated(-1, -1)
    );
    Occupant pawnM311 = new Occupant(PAWN, 311);

    PlacedTile t61RotNoneNorthOf56PURPLE = new PlacedTile(
            TILES.get(61), PURPLE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.N)
    );

    PlacedTile t91RotNoneSouthWestOf56RAFT = new PlacedTile(
            TILES.get(91), RED, Rotation.NONE, t56RotNone.pos().translated(-1, 1)
    );

    PlacedTile t94RotNoneNorthOf56HUNTING_TRAP = new PlacedTile(
            TILES.get(94), PURPLE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.N)
    );

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
                expected, GameState.initial(ALL, standardDecks(), textMaker)
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
        GameState state = GameState.initial(ALL, standardDecks(), textMaker);

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
        GameState state = GameState.initial(ALL, standardDecks(), textMaker);
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
                        new Occupant(PAWN, 682),
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
            GameState state = new GameState(ALL, standardDecks(), TILES.get(44), Board.EMPTY,
                    PLACE_TILE, emptyMessageBoard);
            state.withStartingTilePlaced();
        });

        GameState.Action[] actions = {RETAKE_PAWN, OCCUPY_TILE, END_GAME};

        for (GameState.Action action : actions) {
            assertThrows(IllegalArgumentException.class, () -> {
                GameState state = new GameState(ALL, standardDecks(), null, Board.EMPTY,
                        action, emptyMessageBoard);

                state.withStartingTilePlaced();
            });
        }
    }

    @Test
    void withStartingTilePlacedBehavesCorrectly() {
        GameState initial = GameState.initial(
                List.of(PURPLE, YELLOW, BLUE), standardDecks(), textMaker
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
                standardDecks()
                        .withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL),
                withStartingTile.tileDecks()
        );
        assertEquals(PURPLE, withStartingTile.currentPlayer());
    }

    @Test
    void withPlacedTileThrowsOnIllegalNextAction() {
        GameState.Action[] actions = {START_GAME, RETAKE_PAWN, OCCUPY_TILE, END_GAME};

        for (GameState.Action action : actions) {
            assertThrows(IllegalArgumentException.class, () -> {
                GameState state = new GameState(ALL, standardDecks(), null, Board.EMPTY,
                        action, emptyMessageBoard);

                state.withPlacedTile(t68RotNoneSouthOf56);
            });
        }
    }

    @Test
    void withPlacedTileThrowsOnOccupiedTile() {
        PlacedTile occupiedTile = new PlacedTile(
                TILES.get(32), PURPLE, Rotation.NONE, t56RotNone.pos().neighbor(Direction.S),
                new Occupant(PAWN, 230)
        );

        assertThrows(IllegalArgumentException.class, () -> {
            GameState state = GameState.initial(ALL, standardDecks(), textMaker);

            state.withStartingTilePlaced()
                    .withPlacedTile(occupiedTile);
        });
    }

    @Test
    void withPlacedTileEntersMenhirStatesForTrivialCase() {
        GameState state = GameState.initial(List.of(GREEN, PURPLE), standardDecks(), textMaker);
        GameState menhirTriggerState = state.withStartingTilePlaced()
                .withPlacedTile(t82RotNoneEastOf56).withNewOccupant(null)
                .withPlacedTile(t68RotNoneSouthOf56).withNewOccupant(pawnM681);

        assertEquals(GameState.Action.PLACE_TILE, menhirTriggerState.nextAction());
    }

    @Test
    void withPlacedTileEntersMenhirStatesForNoOccupyPossibleCase() {
        GameState startingState = GameState.initial(ALL, standardDecks(), textMaker)
                .withStartingTilePlaced();

        GameState menhirTriggerState = startingState
                .withPlacedTile(t82RotNoneEastOf56).withNewOccupant(pawnF823)
                .withPlacedTile(t61RotNoneSouthEastOf56).withNewOccupant(pawnM610)
                .withPlacedTile(t37RotNoneSouthOf56);

        assertEquals(GameState.Action.PLACE_TILE, menhirTriggerState.nextAction());
    }

    /* DONE There might be a bug that causes the game state to skip the occupy phase for
        the first tile placed after the start tile.
        - See Test below for demonstration
    */
    @Test
    void withPlacedTileTileAfterStartEntersOccupy() {
        GameState state = GameState.initial(ALL, standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t61RotNoneNorthOf56);

        assertEquals(OCCUPY_TILE, state.nextAction());
    }

    @Test
    void withPlacedTileShamanTileTriggersRetakePawn() {
        // needs to set next action to RETAKE_PAWN
        GameState state = GameState.initial(List.of(GREEN, PURPLE), standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t32RotNoneNorthOf56).withNewOccupant(pawnM321)
                // this below is only to make green play again
                .withPlacedTile(t16RotNoneNorthEastOf56).withNewOccupant(null)
                .withPlacedTile(t88RotNoneTwiceNorthOf56SHAMAN);

        assertEquals(RETAKE_PAWN, state.nextAction());
    }

    private Pos pos(int x, int y) {
        return new Pos(x, y);
    }

    PlacedTile t78RotNoneSouthOf56 = new PlacedTile(
            TILES.get(78), RED, Rotation.NONE, pos(0, 1)
    );
    PlacedTile t25LeftNorthOf56 = new PlacedTile(
            TILES.get(25), BLUE, Rotation.LEFT, pos(0, -1)
    );
    PlacedTile t68RotNoneNorthOf25 = new PlacedTile(
            TILES.get(68), GREEN, Rotation.NONE, pos(0, -2)
    );
    PlacedTile t16RotNoneWestOf25 = new PlacedTile(
            TILES.get(16), YELLOW, Rotation.NONE, pos(-1, -1)
    );
    PlacedTile t37RotNoneWestOf78 = new PlacedTile(
            TILES.get(37), PURPLE, Rotation.NONE, pos(-1, 1)
    );
    PlacedTile t62RotNoneWestOf37 = new PlacedTile(
            TILES.get(62), RED, Rotation.NONE, pos(-2, 1)
    );
    PlacedTile t61RotNoneNorthOf62 = new PlacedTile(
            TILES.get(61), BLUE, Rotation.NONE, pos(-2, 0)
    );
    PlacedTile t88RotNoneWestOf56 = new PlacedTile(
            TILES.get(88), RED, Rotation.NONE, pos(-1, 0)
    );

    @Test
    void withPlacedTileShamanPassesRetakeAndOccupyIfNeitherIsPossible() {
        Occupant hut682 = new Occupant(HUT, 682);
        Occupant pawn250 = new Occupant(PAWN, 250);
        Occupant pawn161 = new Occupant(PAWN, 161);
        Occupant pawn370 = new Occupant(PAWN, 370);
        Occupant pawn610 = new Occupant(PAWN, 610);


        GameState occupyAndRetakeSkipState = GameState.initial(ALL, standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t78RotNoneSouthOf56).withNewOccupant(null)
                .withPlacedTile(t25LeftNorthOf56).withNewOccupant(pawn250)
                .withPlacedTile(t68RotNoneNorthOf25).withNewOccupant(hut682)
                .withPlacedTile(t16RotNoneWestOf25).withNewOccupant(pawn161)
                .withPlacedTile(t37RotNoneWestOf78).withNewOccupant(pawn370)
                .withPlacedTile(t62RotNoneWestOf37).withNewOccupant(null)
                .withPlacedTile(t61RotNoneNorthOf62).withNewOccupant(pawn610)
                .withPlacedTile(t88RotNoneWestOf56);

        assertEquals(PLACE_TILE, occupyAndRetakeSkipState.nextAction());
    }

    @Test
    void withPlacedTileShamanTilePassesRetakeStepIfNoPawnsAvailable() {
        // "passes" = goes straight to figuring out if occupation is possible, doesn't change board

        GameState state = GameState.initial(List.of(GREEN, PURPLE), standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t32RotNoneNorthOf56).withNewOccupant(null)
                // this below is only to make green play again
                .withPlacedTile(t16RotNoneNorthEastOf56).withNewOccupant(null)
                .withPlacedTile(t88RotNoneTwiceNorthOf56SHAMAN);

        assertEquals(OCCUPY_TILE, state.nextAction());
    }

    PlacedTile t59HalfTurnWestOf68 = new PlacedTile(
            TILES.get(59), RED, Rotation.HALF_TURN, pos(-1, -2)
    );
    PlacedTile t84RotNoneWestOf56 = new PlacedTile(
            TILES.get(84), BLUE, Rotation.NONE, pos(-1, 0)
    );
    PlacedTile t93RightNorthOf84 = new PlacedTile(
            TILES.get(93), PURPLE, Rotation.RIGHT, pos(-1, -1)
    );
    PlacedTile t53RotNoneNorthOf56 = new PlacedTile(
            TILES.get(53), RED, Rotation.NONE, pos(0, -1)
    );

    @Test
    void withPlacedTileLogBoatWorksForTrivialCases() {
        GameState boatState1 = GameState.initial(ALL, standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t25LeftNorthOf56).withNewOccupant(null)
                .withPlacedTile(t68RotNoneNorthOf25).withNewOccupant(null)
                .withPlacedTile(t59HalfTurnWestOf68).withNewOccupant(null)
                .withPlacedTile(t84RotNoneWestOf56).withNewOccupant(null)
                .withPlacedTile(t93RightNorthOf84);
        for (PlayerColor color : ALL) {
            if (color.equals(PURPLE))
                assertEquals(6, boatState1.messageBoard().points().get(PURPLE));
            else
                assertNull(boatState1.messageBoard().points().get(color));
        }

        GameState boatState2 = GameState.initial(List.of(RED, GREEN), standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t53RotNoneNorthOf56).withNewOccupant(null)
                .withPlacedTile(t93RightNorthOf84);

        for (PlayerColor color : ALL) {
            if (color.equals(GREEN))
                assertEquals(2, boatState2.messageBoard().points().get(GREEN));
            else
                assertNull(boatState2.messageBoard().points().get(color));
        }
    }

    @Test
    void withPlacedTileHandlesHuntingTrapCorrectly() {
        GameState state = GameState.initial(ALL, standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t30RotNoneEastOf56).withNewOccupant(null)
                .withPlacedTile(t15RotNoneWestOf56).withNewOccupant(null)
                .withPlacedTile(t62RotNoneNorthEastOf56).withNewOccupant(null)
                .withPlacedTile(t31RotNoneNorthWestOf56).withNewOccupant(null)
                .withPlacedTile(t94RotNoneNorthOf56HUNTING_TRAP); // PURPLE

        /* In this configuration the hunting trap gets put into a meadow area with 3 deer, 1 aurochs
         * and 1 tiger, thus 1 deer is eaten by the tiger and the points are thus calculated with
         * 2 deer, 1 tiger and 1 aurochs.
         *
         * However, because of the error in conception, cancelled animals aren't passed, thus points
         * are calculated with all animals, but they should be removed.
         *
         * TODO: WARNING: this test will most probably fail as soon as the new version of
         *  withScoredHuntingTrap of MessageBoard is known and implemented.
         *  .
         *  When this is the case, this test needs to include an assert to check whether the
         *  amount of cancelled deer are correctly determined. */

        int pointsGained = Points.forMeadow(0, 1, 3);
        MessageBoard.Message expectedMessage = new MessageBoard.Message(
                textMaker.playerScoredHuntingTrap(
                        PURPLE,
                        pointsGained,
                        Map.of(
                                Animal.Kind.MAMMOTH, 0,
                                Animal.Kind.AUROCHS, 1,
                                Animal.Kind.DEER, 3,
                                Animal.Kind.TIGER, 1
                        )),
                pointsGained, Collections.singleton(PURPLE),
                Set.of(56, 30, 15, 62, 31, 94)
        );

        // message needs to be sent
        assertEquals(expectedMessage, state.messageBoard().messages().getLast());

        // all animals of the adjacent meadow (thus all above) have to be cancelled
        assertEquals(
                Set.of(
                        new Animal(15_0_0, Animal.Kind.DEER),
                        new Animal(31_1_0, Animal.Kind.TIGER),
                        new Animal(56_0_0, Animal.Kind.AUROCHS),
                        new Animal(62_0_0, Animal.Kind.DEER),
                        new Animal(30_0_0, Animal.Kind.DEER)
                ),
                state.board().cancelledAnimals()
        );
    }

    @Test
    void withOccupantRemovedThrowsOnIllegalNextAction() {
        assertThrows(IllegalArgumentException.class, () -> {
            GameState state = new GameState(ALL, standardDecks(), TILES.get(45), Board.EMPTY,
                    PLACE_TILE, emptyMessageBoard);

            state.withOccupantRemoved(new Occupant(PAWN, 444));
        });

        GameState.Action[] actions = {START_GAME, OCCUPY_TILE, END_GAME};

        for (GameState.Action action : actions) {
            assertThrows(IllegalArgumentException.class, () -> {
                GameState state = new GameState(ALL, standardDecks(), null, Board.EMPTY,
                        action, emptyMessageBoard);

                state.withOccupantRemoved(new Occupant(PAWN, 444));
            });
        }
    }

    @Test
    void withOccupantRemovedThrowsOnHutAsOccupant() {
        // occupant should either be null or a pawn, not a Hut
        assertThrows(IllegalArgumentException.class, () -> {
            GameState state = new GameState(ALL, standardDecks(), null,
                    boardCrossAround56Last68, RETAKE_PAWN, emptyMessageBoard);

            state.withOccupantRemoved(new Occupant(HUT, 444));
        });
    }

    @Test
    void withOccupantRemovedWorksInTrivialCase() {
        GameState state = GameState.initial(List.of(GREEN, PURPLE), standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t32RotNoneNorthOf56).withNewOccupant(pawnM321) // GREEN
                // this below is only to make green play again
                .withPlacedTile(t16RotNoneNorthEastOf56).withNewOccupant(null) // PURPLE
                .withPlacedTile(t88RotNoneTwiceNorthOf56SHAMAN) // GREEN
                .withOccupantRemoved(pawnM321);

        // needs to remove the occupant, thus leaving the board unoccupied
        assertEquals(Collections.emptySet(), state.board().occupants());

        // needs to now be in OCCUPY_TILE state as shaman tile could be occupied by green
        assertEquals(OCCUPY_TILE, state.nextAction());
    }

    @Test
    void withOccupantRemovedPassesOnNullOccupant() {
        // "passes" = goes straight to figuring out if occupation is possible, doesn't change board

        GameState state = GameState.initial(List.of(GREEN, PURPLE), standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t32RotNoneNorthOf56).withNewOccupant(pawnM321) // GREEN
                // this below is only to make green play again
                .withPlacedTile(t16RotNoneNorthEastOf56).withNewOccupant(null) // PURPLE
                .withPlacedTile(t88RotNoneTwiceNorthOf56SHAMAN) // GREEN
                .withOccupantRemoved(null);

        assertEquals(Set.of(pawnM321), state.board().occupants());
        assertEquals(OCCUPY_TILE, state.nextAction());
    }

    @Test
    void withNewOccupantThrowsOnIllegalNextAction() {
        assertThrows(IllegalArgumentException.class, () -> {
            GameState state = new GameState(ALL, standardDecks(), TILES.get(45), Board.EMPTY,
                    PLACE_TILE, emptyMessageBoard);

            state.withOccupantRemoved(new Occupant(PAWN, 444));
        });

        GameState.Action[] actions = {START_GAME, RETAKE_PAWN, END_GAME};

        for (GameState.Action action : actions) {
            assertThrows(IllegalArgumentException.class, () -> {
                GameState state = new GameState(ALL, standardDecks(), null, Board.EMPTY,
                        action, emptyMessageBoard);

                state.withOccupantRemoved(new Occupant(PAWN, 444));
            });
        }
    }

    @Test
    void withNewOccupantWorksOnTrivialCase() {
        GameState state = GameState.initial(ALL, standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t23RotNoneEastOf56).withNewOccupant(pawnF233); // BLUE

        assertEquals(Collections.singleton(pawnF233), state.board().occupants());
    }

    @Test
    void withNewOccupantPassesOnNullOccupant() {
        // "passes" = goes straight to finishing the turn, doesn't change board
        GameState state = GameState.initial(ALL, standardDecks(), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t23RotNoneEastOf56).withNewOccupant(null);

        assertEquals(Collections.emptySet(), state.board().occupants());
    }

    TileDecks shortGame = customDecks(List.of(30, 15, 62, 31, 61, 91),
            Collections.emptyList());


    @Test
    void endOfGameBehavesCorrectlyOnTrivialState() {
        GameState state = GameState.initial(ALL, shortGame, textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t30RotNoneEastOf56).withNewOccupant(pawnM300) // RED
                .withPlacedTile(t15RotNoneWestOf56).withNewOccupant(hutR151) // BLUE
                .withPlacedTile(t62RotNoneNorthEastOf56) // GREEN
                .withPlacedTile(t31RotNoneNorthWestOf56).withNewOccupant(pawnM311) // YELLOW
                .withPlacedTile(t61RotNoneNorthOf56PURPLE) // PURPLE
                .withPlacedTile(t91RotNoneSouthWestOf56RAFT).withNewOccupant(null); // RED

        /* After red's turn the game should be over. Red and yellow both get points for the animals
         * in a meadow they have majority occupants which contains 3 deer, 1 aurochs, 1 mammoth and
         * 1 tiger. The tiger eats one of the deer thus they should get 1*3 + 1*2 + 2*1 = 7 points.
         *
         * Blue has majority occupants in a river system containing 2 fish, the raft and 2 lakes,
         * thus they should get 2*1 + 2*1 = 4 points.
         *
         * As red and yellow both have 7 points they should win the game. */

        int pointsRedYellow = 7;
        int pointsBlue = 4;
        Set<PlayerColor> winners = Set.of(RED, YELLOW);

        assertEquals(END_GAME, state.nextAction());

        Map<PlayerColor, Integer> expectedPoints = Map.of(
                RED, pointsRedYellow,
                BLUE, pointsBlue,
                YELLOW, pointsRedYellow
        );
        assertEquals(expectedPoints, state.messageBoard().points());

        MessageBoard.Message expectedWinMessage = new MessageBoard.Message(
                textMaker.playersWon(winners, pointsRedYellow),
                0, Collections.emptySet(), Collections.emptySet()
        );
        assertEquals(expectedWinMessage, state.messageBoard().messages().getLast());
    }

    //==============================================================================================
    //                     🔥WILDFIRE🔥
    //==============================================================================================
    /*
        It should be noted that fire only drives smilodons out of the meadow when the final score is
        being tallied, and has no influence on the score for the hunting trap.
    */
    // Testing if it has no effect where it should simply do nothing
    @Test
    void withFinalPointsCountedWildFireIdleCase() {
        List<PlayerColor> players = List.of(BLUE, YELLOW);
        List<Integer> tileIds = List.of(61, 85, 62, 59, 49);


        PlacedTile t85RotNoneNorthEastOf56 = new PlacedTile(
                TILES.get(85), YELLOW, Rotation.NONE, pos(1, -1)
        );
        PlacedTile t62RotNoneNorthEastOf56 = new PlacedTile(
                TILES.get(62), BLUE, Rotation.NONE, pos(-1, -1)
        );
        PlacedTile t59RightWestOf56 = new PlacedTile(
                TILES.get(59), YELLOW, Rotation.RIGHT, pos(-1, 0)
        );
        PlacedTile t49RotNoneSouthWestOf56 = new PlacedTile(
                TILES.get(49), BLUE, Rotation.NONE, pos(-1, 1)
        );
        Occupant pawnM490 = new Occupant(PAWN, 490);

        GameState state = GameState.initial(players,
                        customDecks(tileIds, Collections.emptyList()), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t61RotNoneNorthOf56).withNewOccupant(pawnM610)
                .withPlacedTile(t85RotNoneNorthEastOf56)
                .withPlacedTile(t62RotNoneNorthEastOf56)
                .withPlacedTile(t59RightWestOf56).withNewOccupant(null)
                .withPlacedTile(t49RotNoneSouthWestOf56).withNewOccupant(pawnM490);
        assertEquals(END_GAME, state.nextAction());

        assertEquals(6, state.messageBoard().points().get(BLUE));
        assertNull(state.messageBoard().points().get(YELLOW));

    }

    @Test
    void withFinalPointsCountedWildfireJoinsMeadowsCase() {
        List<PlayerColor> players = List.of(BLUE, YELLOW);
        List<Integer> stdTileIds = List.of(61, 62, 59, 47, 49, 9, 37);
        List<Integer> mnhTileIds = Collections.singletonList(85);


        PlacedTile t62RotNoneNorthEastOf56 = new PlacedTile(
                TILES.get(62), YELLOW, Rotation.NONE, pos(-1, -1)
        );
        PlacedTile t59RightWestOf56 = new PlacedTile(
                TILES.get(59), BLUE, Rotation.RIGHT, pos(-1, 0)
        );
        PlacedTile t47HalfTurnSouthOf56 = new PlacedTile(
                TILES.get(47), YELLOW, Rotation.HALF_TURN, pos(0, 1)
        );

        PlacedTile t49RotNoneSouthWestOf56 = new PlacedTile(
                TILES.get(49), BLUE, Rotation.NONE, pos(-1, 1)
        );
        Occupant pawnM490 = new Occupant(PAWN, 490);
        PlacedTile t09RightSouthEastOf56 = new PlacedTile(
                TILES.get(9), YELLOW, Rotation.RIGHT, pos(1, 1)
        );
        Occupant pawnM094 = new Occupant(PAWN, 94);

        PlacedTile t37LeftEastOf56 = new PlacedTile(
                TILES.get(37), BLUE, Rotation.LEFT, pos(1, 0)
        );
        PlacedTile t85RotNoneNorthEastOf56 = new PlacedTile(
                TILES.get(85), BLUE, Rotation.NONE, pos(1, -1)
        );


        GameState state = GameState.initial(players,
                        customDecks(stdTileIds, mnhTileIds), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t61RotNoneNorthOf56).withNewOccupant(pawnM610)
                .withPlacedTile(t62RotNoneNorthEastOf56)
                .withPlacedTile(t59RightWestOf56).withNewOccupant(null)
                .withPlacedTile(t47HalfTurnSouthOf56).withNewOccupant(null)
                .withPlacedTile(t49RotNoneSouthWestOf56).withNewOccupant(pawnM490)
                .withPlacedTile(t09RightSouthEastOf56).withNewOccupant(pawnM094)
                .withPlacedTile(t37LeftEastOf56).withNewOccupant(null)
                .withPlacedTile(t85RotNoneNorthEastOf56);
        assertEquals(END_GAME, state.nextAction());

        assertEquals(9, state.messageBoard().points().get(BLUE));
        assertNull(state.messageBoard().points().get(YELLOW));

    }

    @Test
    void withFinalPointsCountedRespectsFinalPlacedTile() {
        List<PlayerColor> players = List.of(BLUE, YELLOW);
        TileDecks smallDecks = new TileDecks(Collections.singletonList(TILES.get(56)),
                Collections.singletonList(TILES.get(59)), Collections.emptyList());
        PlacedTile t59RightWestOf56 = new PlacedTile(
                TILES.get(59), BLUE, Rotation.RIGHT, pos(-1, 0)
        );
        Occupant pawnM590 = new Occupant(PAWN, 590);

        GameState state = GameState.initial(players, smallDecks, textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t59RightWestOf56)
                .withNewOccupant(pawnM590);
        assertEquals(2, state.messageBoard().points().get(BLUE));
    }

    @Test
    void withFinalPointsCountedHuntingTrapIsIndependentOfWildFire() {

        List<PlayerColor> players = List.of(BLUE, YELLOW);
        List<Integer> stdTileIds = List.of(61, 62, 59, 47, 49, 9, 42, 36, 37);
        List<Integer> mnhTileIds = List.of(94, 85);


        PlacedTile t62RotNoneNorthEastOf56 = new PlacedTile(
                TILES.get(62), YELLOW, Rotation.NONE, pos(-1, -1)
        );
        PlacedTile t59RightWestOf56 = new PlacedTile(
                TILES.get(59), BLUE, Rotation.RIGHT, pos(-1, 0)
        );
        PlacedTile t47HalfTurnSouthOf56 = new PlacedTile(
                TILES.get(47), YELLOW, Rotation.HALF_TURN, pos(0, 1)
        );

        PlacedTile t49RotNoneSouthWestOf56 = new PlacedTile(
                TILES.get(49), BLUE, Rotation.NONE, pos(-1, 1)
        );
        Occupant pawnM490 = new Occupant(PAWN, 490);
        PlacedTile t09RightSouthEastOf56 = new PlacedTile(
                TILES.get(9), YELLOW, Rotation.RIGHT, pos(1, 1)
        );
        Occupant pawnM094 = new Occupant(PAWN, 94);

        PlacedTile t37LeftEastOf56 = new PlacedTile(
                TILES.get(37), BLUE, Rotation.LEFT, pos(1, 0)
        );
        PlacedTile t85RotNoneNorthEastOf56 = new PlacedTile(
                TILES.get(85), BLUE, Rotation.NONE, pos(1, -1)
        );
        PlacedTile t42RotNoneAtMinus2_0 = new PlacedTile(
                TILES.get(42), BLUE, Rotation.NONE, pos(-2, 0)
        );
        PlacedTile t36RotNoneAtNeg2_Neg1 = new PlacedTile(
                TILES.get(36), YELLOW, Rotation.NONE, pos(-2, -1)
        );
        PlacedTile t94RotNoneAt0_Neg2 = new PlacedTile(
                TILES.get(94), YELLOW, Rotation.NONE, pos(0, -2)
        );
        PlacedTile t46RotNoneAt2_1 = new PlacedTile(
                TILES.get(46), BLUE, Rotation.NONE, pos(2, 1)
        );


        GameState huntingTrapBeforeFire = GameState.initial(players,
                        customDecks(stdTileIds, mnhTileIds), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t61RotNoneNorthOf56).withNewOccupant(pawnM610)
                .withPlacedTile(t62RotNoneNorthEastOf56)
                .withPlacedTile(t59RightWestOf56).withNewOccupant(null)
                .withPlacedTile(t47HalfTurnSouthOf56).withNewOccupant(null)
                .withPlacedTile(t49RotNoneSouthWestOf56).withNewOccupant(pawnM490)
                .withPlacedTile(t09RightSouthEastOf56).withNewOccupant(pawnM094)
                .withPlacedTile(t42RotNoneAtMinus2_0).withNewOccupant(null)
                .withPlacedTile(t36RotNoneAtNeg2_Neg1).withNewOccupant(null)
                .withPlacedTile(t94RotNoneAt0_Neg2).withNewOccupant(null)
                .withPlacedTile(t37LeftEastOf56).withNewOccupant(null)
                .withPlacedTile(t85RotNoneNorthEastOf56);

        List<Integer> stdTileIds2 = List.of(61, 62, 59, 47, 49, 9, 37, 42, 46, 36);
        List<Integer> mnhTileIds2 = List.of(85, 94);
        GameState fireBeforeHuntingTrap = GameState.initial(players,
                        customDecks(stdTileIds2, mnhTileIds2), textMaker)
                .withStartingTilePlaced()
                .withPlacedTile(t61RotNoneNorthOf56).withNewOccupant(pawnM610)
                .withPlacedTile(t62RotNoneNorthEastOf56)
                .withPlacedTile(t59RightWestOf56).withNewOccupant(null)
                .withPlacedTile(t47HalfTurnSouthOf56).withNewOccupant(null)
                .withPlacedTile(t49RotNoneSouthWestOf56).withNewOccupant(pawnM490)
                .withPlacedTile(t09RightSouthEastOf56).withNewOccupant(pawnM094)
                .withPlacedTile(t37LeftEastOf56).withNewOccupant(null)
                .withPlacedTile(t85RotNoneNorthEastOf56)
                .withPlacedTile(t42RotNoneAtMinus2_0).withNewOccupant(null)
                .withPlacedTile(t46RotNoneAt2_1).withNewOccupant(null)
                .withPlacedTile(t36RotNoneAtNeg2_Neg1).withNewOccupant(null)
                .withPlacedTile(t94RotNoneAt0_Neg2).withNewOccupant(null);

        assertEquals(huntingTrapBeforeFire.messageBoard().points(),
                fireBeforeHuntingTrap.messageBoard().points());
    }
}