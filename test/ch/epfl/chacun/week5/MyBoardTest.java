package ch.epfl.chacun.week5;

import ch.epfl.chacun.*;
import static ch.epfl.chacun.tile.Tiles.TILES;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.epfl.chacun.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class MyBoardTest {

    // Start Tile
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
    PlacedTile t88PlacedWest = new PlacedTile(t88, PURPLE, Rotation.NONE,
            startTilePlacedRotNone.pos().neighbor(Direction.W));
    PlacedTile t88PlacedSouth = new PlacedTile(t88, PURPLE, Rotation.HALF_TURN,
            startTilePlacedRotNone.pos().neighbor(Direction.S));

    // Tile 61
    Zone.Meadow m610 = new Zone.Meadow(610, List.of(new Animal(6100, Animal.Kind.MAMMOTH)), null);
    TileSide.Meadow t61N = new TileSide.Meadow(m610);
    TileSide.Meadow t61E = new TileSide.Meadow(m610);
    TileSide.Meadow t61S = new TileSide.Meadow(m610);
    TileSide.Meadow t61W = new TileSide.Meadow(m610);
    Tile t61 = new Tile(61, Tile.Kind.NORMAL, t61N, t61E, t61S, t61W);
    PlacedTile t61Placed = new PlacedTile(t61, PlayerColor.GREEN, Rotation.NONE,
            startTilePlacedRotNone.pos().neighbor(Direction.N));
    PlacedTile t61PlacedWestOft88 = new PlacedTile(t61, RED, Rotation.NONE, new Pos(-2, 0));

    // Tile 82
    Zone.Meadow m820 = new Zone.Meadow(820, Collections.emptyList(), null);
    Zone.River r821  =new Zone.River(821, 0, new Zone.Lake(828, 2, null));
    Zone.Meadow m822 = new Zone.Meadow(822, List.of(new Animal(8220, Animal.Kind.DEER)), null);
    Zone.Forest f823 = new Zone.Forest(823, Zone.Forest.Kind.PLAIN);

    TileSide.River t82N = new TileSide.River(m820, r821, m822);
    TileSide.Meadow t82E = new TileSide.Meadow(m822);
    TileSide.Meadow t82S = new TileSide.Meadow(m882);
    TileSide.Forest t82W = new TileSide.Forest(f823);

    Tile t82 = new Tile(82, Tile.Kind.NORMAL, t82N, t82E, t82S, t82W);

    PlacedTile t82NorthOfT88Rotated = new PlacedTile(t82, GREEN, Rotation.HALF_TURN, new Pos(-1,-1));
    PlacedTile t82SouthOfT88Rotated = new PlacedTile(t82, GREEN, Rotation.RIGHT, new Pos(-1,1));

    Tile[] tiles = {startTile, t88, t61};
    ZonePartitions emptySetup = ZonePartitions.EMPTY;


    @Test
    void boardEmptyContainsNoTiles(){
        Board board = Board.EMPTY;
        for (int i = 0; i < 624; i++) {
            int finalI = i;
            assertThrows(IllegalArgumentException.class, () -> board.tileWithId(finalI));
        }
        for (int i = -12; i < 13; i++) {
            for (int j = -12; j < 13; j++) {
                assertNull(board.tileAt(new Pos(i, j)));
            }
        }
    }

    // tileAt
    @Test
    void tileAtWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1,-1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2,-1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2,0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2,1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1,1)));


        assertEquals(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1,-1)), board.tileAt(new Pos(-1,-1)));
        assertEquals(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2,-1)), board.tileAt(new Pos(-2,-1)));
        assertEquals(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2,0)), board.tileAt(new Pos(-2,0)));
        assertEquals(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2,1)), board.tileAt(new Pos(-2,1)));
        assertEquals(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1,1)), board.tileAt(new Pos(-1,1)));
    }
    @Test
    void tileAtIsNullForPositionOutsideBoard() {
        Board board = Board.EMPTY;
        assertNull(board.tileAt(new Pos(13, 13)));
    }

    // tileWithId
    @Test
    void tileWithIdWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1,-1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2,-1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2,0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2,1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1,1)));


        assertEquals(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1,-1)), board.tileWithId(34));
        assertEquals(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2,-1)), board.tileWithId(31));
        assertEquals(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2,0)), board.tileWithId(35));
        assertEquals(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2,1)), board.tileWithId(33));
        assertEquals(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1,1)), board.tileWithId(36));
    }
    @Test
    void tileWithIdThrowsIfTileNotContained() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1,-1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2,-1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2,0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2,1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1,1)));
        assertThrows(IllegalArgumentException.class, () -> board.tileWithId(20));
    }

    // occupants
    @Test
    void occupantsWorksForTrivialCase() {
        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, m610.id());
        Occupant occupant2 = new Occupant(Occupant.Kind.HUT, r881.id());

        Board actual = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(
            new PlacedTile(t61, BLUE, Rotation.NONE, new Pos(0, -1), occupant1)
        ).withNewTile(
            new PlacedTile(t88, GREEN, Rotation.NONE, new Pos(-1, 0), occupant2)
        );

        assertEquals(Set.of(occupant1, occupant2), actual.occupants());
    }
    @Test
    void occupantsWorksForNoOccupants() {
        Board board = Board.EMPTY;
        assertEquals(Collections.emptySet(), board.occupants());
    }

    // *Area type methods

    @Test
    void forestAreaWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest);

        Area<Zone.Forest> expectedForestArea1 = new Area<>(Set.of(f561), Collections.emptyList(), 2);
        Area<Zone.Forest> expectedForestArea2 = new Area<>(Set.of(f883), Collections.emptyList(), 1);

        assertEquals(expectedForestArea1, board.forestArea(f561));
        assertEquals(expectedForestArea2, board.forestArea(f883));
    }

    @Test
    void forestAreaThrowsOnAreaNotContained(){
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone);
        assertThrows(IllegalArgumentException.class, ()->board.forestArea(f883));
    }

    @Test
    void meadowAreaWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest);

        Area<Zone.Meadow> expectedMeadowArea1 = new Area<>(Set.of(m560, m882), Collections.emptyList(), 2);
        Area<Zone.Meadow> expectedMeadowArea2 = new Area<>(Set.of(m562, m880), Collections.emptyList(), 2);

        assertEquals(expectedMeadowArea1, board.meadowArea(m560));
        assertEquals(expectedMeadowArea1, board.meadowArea(m882));
        assertEquals(expectedMeadowArea2, board.meadowArea(m562));
        assertEquals(expectedMeadowArea2, board.meadowArea(m880));
    }
    @Test
    void meadowAreaThrowsOnAreaNotContained(){
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone);
        assertThrows(IllegalArgumentException.class, ()->board.meadowArea(m610));
    }
    @Test
    void riverAreaWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest);

        Area<Zone.River> expectedRiverArea1 = new Area<>(Set.of(r881, r563), Collections.emptyList() ,1 );

        assertEquals(expectedRiverArea1, board.riverArea(r563));
        assertEquals(expectedRiverArea1, board.riverArea(r881));
    }
    @Test
    void riverAreaThrowsOnAreaNotContained(){
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone);
        assertThrows(IllegalArgumentException.class, ()->board.riverArea(r881));
    }
    @Test
    void riverSystemAreaWorksForTrivialCase() {

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest);

        Area<Zone.Water> expectedRiverSystemArea1 = new Area<>(Set.of(r881, r563, l568), Collections.emptyList() ,1 );

        assertEquals(expectedRiverSystemArea1, board.riverSystemArea(r563));
        assertEquals(expectedRiverSystemArea1, board.riverSystemArea(r881));
        assertEquals(expectedRiverSystemArea1, board.riverSystemArea(l568));
    }
    @Test
    void riverSystemAreaThrowsOnAreaNotContained(){
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone);
        assertThrows(IllegalArgumentException.class, ()->board.riverArea(r881));
    }
    @Test
    void meadowAreasWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest);

        Area<Zone.Meadow> expectedMeadowArea1 = new Area<>(Set.of(m560, m882), Collections.emptyList(), 2);
        Area<Zone.Meadow> expectedMeadowArea2 = new Area<>(Set.of(m562, m880), Collections.emptyList(), 2);

        assertEquals(Set.of(expectedMeadowArea1, expectedMeadowArea2), board.meadowAreas());
    }
    @Test
    void riverSystemAreasWorksForTrivialCase() {

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedSouth);

        Area<Zone.Water> expectedRiverSystemArea1 = new Area<>(Set.of(r563, l568), Collections.emptyList() ,1 );
        Area<Zone.Water> expectedRiverSystemArea2 = new Area<>(Set.of(r881), Collections.emptyList() ,2 );

        assertEquals(Set.of(expectedRiverSystemArea1, expectedRiverSystemArea2), board.riverSystemAreas());
    }
    @Test
    void adjacentMeadowWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88);

        Area<Zone.Meadow> expectedAdjacentMeadowAreaM562 = new Area<>(Set.of(m880, m562), Collections.emptyList(), 0);
        Area<Zone.Meadow> expectedAdjacentMeadowAreaM560 = new Area<>(Set.of(m560, m882), Collections.emptyList(), 0);

        assertEquals(expectedAdjacentMeadowAreaM560,
                board.adjacentMeadow(startTilePlacedRotNone.pos(), m560));
        assertEquals(expectedAdjacentMeadowAreaM562,
                board.adjacentMeadow(startTilePlacedRotNone.pos(), m562));

    }
    @Test
    void adjacentMeadowHandlesOccupantsCorrectly() {

        Occupant purplePawnM880 = new Occupant(Occupant.Kind.PAWN, 880);
        Occupant greenPawnM820 = new Occupant(Occupant.Kind.PAWN, 820);

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88)
                .withNewTile(t82SouthOfT88Rotated)
                .withOccupant(purplePawnM880);

        Area<Zone.Meadow> expectedAdjacentMeadowAreaM562 = new Area<>(Set.of(m880, m562), List.of(PURPLE), 0);
        Area<Zone.Meadow> expectedAdjacentMeadowAreaM560 = new Area<>(Set.of(m560, m882), Collections.emptyList(), 0);

        assertEquals(expectedAdjacentMeadowAreaM560.occupants(),
                board.adjacentMeadow(startTilePlacedRotNone.pos(), m560).occupants());
        assertEquals(expectedAdjacentMeadowAreaM562.occupants(),
                board.adjacentMeadow(startTilePlacedRotNone.pos(), m562).occupants());
    }

    @Test
    void occupantsCountWorksOnTrivialCase() {
        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, m610.id());
        Occupant occupant2 = new Occupant(Occupant.Kind.HUT, r881.id());
        Occupant occupant3 = new Occupant(Occupant.Kind.HUT, 38);

        Board actual = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(
            new PlacedTile(t61, BLUE, Rotation.NONE, new Pos(0, -1), null)
        ).withOccupant(occupant1)
                .withNewTile(
            new PlacedTile(t88, GREEN, Rotation.NONE, new Pos(-1, 0), null)
        ).withOccupant(occupant2)
                .withNewTile(
            new PlacedTile(TILES.get(3), GREEN, Rotation.NONE, new Pos(0, -2), null)
        ).withOccupant(occupant3);

        assertEquals(1, actual.occupantCount(BLUE, Occupant.Kind.PAWN));
        assertEquals(2, actual.occupantCount(GREEN, Occupant.Kind.HUT));
    }
    @Test
    void occupantsCountWorksOnEmptyBoard() {
        Board board = Board.EMPTY;
        for (PlayerColor color : ALL) {
            for (Occupant.Kind kind : Occupant.Kind.values()) {
                assertEquals(0, board.occupantCount(color, kind));
            }
        }
    }

    // insertionPositions

    @Test
    void insertionPositionsWorksForTrivialCase() {
        Occupant purplePawnM880 = new Occupant(Occupant.Kind.PAWN, 880);

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88)
                .withNewTile(t82SouthOfT88Rotated)
                .withOccupant(purplePawnM880);

        Set<Pos> expectedPositions = new HashSet<>();

        expectedPositions.add(new Pos(-2,-1));
        expectedPositions.add(new Pos(-1,-1));
        expectedPositions.add(new Pos(0,-1));
        expectedPositions.add(new Pos(1,0));
        expectedPositions.add(new Pos(0,1));
        expectedPositions.add(new Pos(-1,2));
        expectedPositions.add(new Pos(-2,1));
        expectedPositions.add(new Pos(-3,0));

        assertEquals(expectedPositions, board.insertionPositions());
    }
    @Test
    void insertionPositionsWorksForRingCase() {

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1,-1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2,-1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2,0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2,1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1,1)));



        Set<Pos> expectedPositions = new HashSet<>();

        expectedPositions.add(new Pos(-2,-2));
        expectedPositions.add(new Pos(-1,-2));
        expectedPositions.add(new Pos(0,-2));
        expectedPositions.add(new Pos(1,-1));
        expectedPositions.add(new Pos(1,0));
        expectedPositions.add(new Pos(0,1));
        expectedPositions.add(new Pos(-1,2));
        expectedPositions.add(new Pos(-2,2));
        expectedPositions.add(new Pos(-3,1));
        expectedPositions.add(new Pos(-3,0));
        expectedPositions.add(new Pos(-3,-1));
        expectedPositions.add(new Pos(-1,0));

        assertEquals(expectedPositions, board.insertionPositions());
    }

    // lastPlacedTile
    @Test
    void lastPlacedTileTrivialCase(){

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1,-1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2,-1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2,0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2,1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1,1)));

        assertEquals(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1,1)), board.lastPlacedTile());
    }
    @Test
    void lastPlacedTileEmptyCase(){
        Board board = Board.EMPTY;
        assertNull(board.lastPlacedTile());
    }
    @Test
    void lastPlacedTileStartCase(){
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone);
        assertEquals(startTilePlacedRotNone, board.lastPlacedTile());
    }


    // forestsClosedByLastTile

    @Test
    void forestsClosedByLastTileWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1, -1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2, -1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2, 0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2, 1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1, 1)))
                .withNewTile(new PlacedTile(TILES.get(68), PURPLE, Rotation.LEFT, new Pos(-1, 0)));
        Zone.Forest f351 = new Zone.Forest(351, Zone.Forest.Kind.PLAIN);
        Zone.Forest f680 = new Zone.Forest(680, Zone.Forest.Kind.PLAIN);

        Area<Zone.Forest> forestArea = new Area<>(Set.of(f351, f680), Collections.emptyList(), 0);

        assertEquals(Set.of(forestArea), board.forestsClosedByLastTile());

    }
    @Test
    void riversClosedByLastTileWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1, -1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2, -1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2, 0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2, 1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1, 1)))
                .withNewTile(new PlacedTile(TILES.get(68), PURPLE, Rotation.LEFT, new Pos(-1, 0)));
        Zone.River r682 = new Zone.River(682, 0, null);

        Area<Zone.River> riverArea = new Area<>(Set.of(r682, r563), Collections.emptyList(), 0);

        assertEquals(Set.of(riverArea), board.riversClosedByLastTile());

    }
    // canAddTile
    @Test
    void canAddWorksForTrivialCase() {

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1, -1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2, -1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2, 0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2, 1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1, 1)));

        assertTrue(board.canAddTile(new PlacedTile(TILES.get(68), PURPLE, Rotation.LEFT, new Pos(-1, 0))));
        assertFalse(board.canAddTile(new PlacedTile(TILES.get(68), PURPLE, Rotation.NONE, new Pos(-1, 0))));
    }

    // couldPlaceTile

    @Test
    void couldPlaceTileWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1, -1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2, -1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2, 0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2, 1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1, 1)));
        assertTrue(board.couldPlaceTile(TILES.get(68)));
    }
    @Test
    void couldPlaceTileIsFalseForNoFit() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1, -1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2, -1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2, 0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2, 1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1, 1)));
            assertFalse(board.couldPlaceTile(TILES.get(13)));
    }


    // withNewTile
    @Test
    void withNewTileWorksForStartTile() {
        Board board = Board.EMPTY.withNewTile(startTilePlacedRotNone);
        assertEquals(startTilePlacedRotNone, board.tileAt(new Pos(0,0)));
    }
    @Test
    void withNewTileWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(t88PlacedWest);
        assertEquals(startTilePlacedRotNone, board.tileAt(new Pos(0,0)));
        assertEquals(t61Placed, board.tileAt(new Pos(0,-1)));
        assertEquals(t88PlacedWest, board.tileAt(new Pos(-1,0)));
    }
    @Test
    void withNewTileThrowsOnNotAbleToAdd() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1, -1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2, -1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2, 0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2, 1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1, 1)));

        assertThrows(IllegalArgumentException.class, ()-> board.withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.NONE, new Pos(-1, 1))));

    }

    // withOccupant
    @Test
    void withOccupantWorksForTrivialCase() {
        Occupant redPawnM610 = new Occupant(Occupant.Kind.PAWN, 610);
        Occupant greenPawnF883 = new Occupant(Occupant.Kind.PAWN, 883);

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88)
                .withOccupant(redPawnM610)
                .withOccupant(greenPawnF883);

        assertEquals(Set.of(redPawnM610, greenPawnF883), board.occupants());

    }

    @Test
    void withOccupantThrowsOnAlreadyOccupied() {
        Occupant greenPawnF883 = new Occupant(Occupant.Kind.PAWN, 883);
        Occupant redPawnM880 = new Occupant(Occupant.Kind.PAWN, 880);
        Occupant greenPawnM610 = new Occupant(Occupant.Kind.PAWN, 610);

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88)
                .withOccupant(greenPawnF883);

        assertThrows(IllegalArgumentException.class, () -> board.withOccupant(redPawnM880));
        assertThrows(IllegalArgumentException.class, () -> board.withOccupant(greenPawnF883));
        assertDoesNotThrow(() -> board.withOccupant(greenPawnM610));
    }

    // withoutOccupant

    @Test
    void withoutOccupantWorksForTrivialCase() {

        Occupant redPawnM610 = new Occupant(Occupant.Kind.PAWN, 610);
        Occupant greenPawnF883 = new Occupant(Occupant.Kind.PAWN, 883);

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88)
                .withOccupant(redPawnM610)
                .withOccupant(greenPawnF883)
                .withoutOccupant(greenPawnF883);

        assertEquals(Set.of(redPawnM610), board.occupants());

        board = board.withoutOccupant(redPawnM610);
        assertEquals(Collections.emptySet(), board.occupants());
    }
    @Test
    void withoutOccupantThrowsOnOccupantNotPresent() {
        Occupant redPawnM610 = new Occupant(Occupant.Kind.PAWN, 610);
        Occupant purplePawnR882 = new Occupant(Occupant.Kind.PAWN, 882);
        

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88)
                .withOccupant(redPawnM610);

        assertThrows(IllegalArgumentException.class, () -> board.withoutOccupant(purplePawnR882));
    }
    @Test
    void withoutOccupantThrowsRemoveFromEmpty() {
        Occupant redPawnM610 = new Occupant(Occupant.Kind.PAWN, 610);
        Occupant greenPawnF883 = new Occupant(Occupant.Kind.PAWN, 883);

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88);

        assertThrows(IllegalArgumentException.class, () -> board.withoutOccupant(greenPawnF883));
        assertThrows(IllegalArgumentException.class, () -> board.withoutOccupant(redPawnM610));
    }
    @Test
    void withoutGatherersOrFishersInWorksForTrivialCase() {
        Occupant redPawnM610 = new Occupant(Occupant.Kind.PAWN, 610);
        Occupant purplePawnF883 = new Occupant(Occupant.Kind.PAWN, 883);
        Occupant greenPawnR821 = new Occupant(Occupant.Kind.PAWN, 821);

        Area<Zone.Forest> forestArea = new Area<>(Set.of(f883), List.of(PURPLE), 1);
        Area<Zone.River> riverArea = new Area<>(Set.of(r881, r563, r821), List.of(GREEN), 0);

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88)
                .withNewTile(t82NorthOfT88Rotated)
                .withOccupant(purplePawnF883)
                .withOccupant(redPawnM610)
                .withOccupant(greenPawnR821)
                .withoutGatherersOrFishersIn(Set.of(forestArea), Collections.emptySet());

        assertEquals(Set.of(redPawnM610, greenPawnR821), board.occupants());
        board = board.withoutGatherersOrFishersIn(Collections.emptySet(), Set.of(riverArea));
        assertEquals(Set.of(redPawnM610), board.occupants());

    }
    @Test
    void withoutGatherersOrFishersInWorksForHutAndFisherPresentCase() {

        Occupant redPawnM610 = new Occupant(Occupant.Kind.PAWN, 610);
        Occupant purpleHutR881 = new Occupant(Occupant.Kind.HUT, 881);
        Occupant greenPawnR821 = new Occupant(Occupant.Kind.PAWN, 821);

        Area<Zone.River> riverArea1 = new Area<>(Set.of(r881, r563),Collections.emptyList(), 1);
        Area<Zone.River> riverArea2 = new Area<>(Set.of(r821), List.of(GREEN), 1);

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88)
                .withNewTile(t82SouthOfT88Rotated)
                .withOccupant(purpleHutR881)
                .withOccupant(redPawnM610)
                .withOccupant(greenPawnR821)
                .withoutGatherersOrFishersIn(Collections.emptySet(), Set.of(riverArea1, riverArea2));

        assertEquals(Set.of(redPawnM610, purpleHutR881), board.occupants());
    }
    // WithMoreCancelledAnimals

    @Test
    void withMoreCancelledAnimalsWorksForTrivialCase() {
        Set<Animal> animalSet = Set.of(new Animal(5600, Animal.Kind.AUROCHS),
                new Animal(6100, Animal.Kind.MAMMOTH));
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t61Placed)
                .withNewTile(new PlacedTile(TILES.get(34), GREEN, Rotation.RIGHT, new Pos(-1, -1)))
                .withNewTile(new PlacedTile(TILES.get(31), RED, Rotation.NONE, new Pos(-2, -1)))
                .withNewTile(new PlacedTile(TILES.get(35), BLUE, Rotation.NONE, new Pos(-2, 0)))
                .withNewTile(new PlacedTile(TILES.get(33), PURPLE, Rotation.LEFT, new Pos(-2, 1)))
                .withNewTile(new PlacedTile(TILES.get(36), YELLOW, Rotation.LEFT, new Pos(-1, 1)))
                .withMoreCancelledAnimals(animalSet);

        assertEquals(animalSet, board.cancelledAnimals());

    }

}