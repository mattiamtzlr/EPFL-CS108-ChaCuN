package ch.epfl.chacun.week5;

import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import org.junit.jupiter.api.Test;

import java.util.Collections;
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
            startTilePlacedRotNone.pos().neighbor(Direction.N), new Occupant(Occupant.Kind.PAWN, m610.id()));
    PlacedTile t61PlacedWestOft88 = new PlacedTile(t61, RED, Rotation.NONE, new Pos(-2, 0), new Occupant(Occupant.Kind.PAWN, m610.id()));
    Tile[] tiles = {startTile, t88, t61};
    ZonePartitions emptySetup = ZonePartitions.EMPTY;

    @Test
    void boardEmptyContainsNoTiles(){

    }
    @Test
    void boardEmptyContainsNoAnimals(){

    }
    @Test
    void boardEmptyContainsEmptyZonePartitions(){

    }

    // tileAt
    @Test
    void tileAtWorksForTrivialCase() {

    }
    @Test
    void tileAtIsNullForPositionOutsideBoard() {

    }

    // tileWithId
    @Test
    void tileWithIdWorksForTrivialCase() {

    }
    @Test
    void tileWithIdThrowsIfTileNotContained() {

    }

    // cancelledAnimals
    @Test
    void cancelledAnimalsWorksForTrivialCase() {

    }
    @Test
    void cancelledAnimalsMaintainsImmutability() {

    }

    // occupants
    @Test
    void occupantsWorksForTrivialCase() {
        Board board = Board.EMPTY;
        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, m610.id());
        Occupant occupant2 = new Occupant(Occupant.Kind.HUT, r881.id());

        Board actual = board.withNewTile(
            new PlacedTile(t61, BLUE, Rotation.NONE, new Pos(2, 2), occupant1)
        ).withNewTile(
            new PlacedTile(t88, GREEN, Rotation.LEFT, new Pos(-2, 3), occupant2)
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
    void riverAreaWorksForTrivialCase() {
        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest);

        Area<Zone.River> expectedRiverArea1 = new Area<>(Set.of(r881, r563), Collections.emptyList() ,1 );

        assertEquals(expectedRiverArea1, board.riverArea(r563));
        assertEquals(expectedRiverArea1, board.riverArea(r881));
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
        // : returns the meadow adjacent to the given zone,
        // in the form of an area containing only the zones in this meadow

        Board board = Board.EMPTY
                .withNewTile(startTilePlacedRotNone)
                .withNewTile(t88PlacedWest)
                .withNewTile(t61PlacedWestOft88);

        // The adjacent meadow area contains ALL occupants of the original area thus RED for m562
        Area<Zone.Meadow> expectedAdjacentMeadowAreaM562 = new Area<>(Set.of(m880, m562), Collections.singletonList(RED), 0);
        Area<Zone.Meadow> expectedAdjacentMeadowAreaM560 = new Area<>(Set.of(m560, m882), Collections.emptyList(), 0);

        assertEquals(expectedAdjacentMeadowAreaM560,
                board.adjacentMeadow(startTilePlacedRotNone.pos(), m560));
        assertEquals(expectedAdjacentMeadowAreaM562,
                board.adjacentMeadow(startTilePlacedRotNone.pos(), m562));

    }
    @Test
    void adjacentMeadowHandlesOccupantsCorrectly() {
        // : all occupants of the complete meadow

    }

    @Test
    void occupantsCountWorksOnTrivialCase() {
        Board board = Board.EMPTY;
        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, m610.id());
        Occupant occupant2 = new Occupant(Occupant.Kind.HUT, r881.id());
        Occupant occupant3 = new Occupant(Occupant.Kind.HUT, 38);

        Board actual = board.withNewTile(
            new PlacedTile(t61, BLUE, Rotation.NONE, new Pos(2, 2), occupant1)
        ).withNewTile(
            new PlacedTile(t88, GREEN, Rotation.LEFT, new Pos(-2, 3), occupant2)
        ).withNewTile(
            new PlacedTile(Tiles.TILES.get(3), GREEN, Rotation.NONE, new Pos(-2, -1), occupant3)
        );

        assertEquals(1, actual.occupantCount(BLUE, Occupant.Kind.PAWN));
        assertEquals(2, actual.occupantCount(GREEN, Occupant.Kind.HUT));
    }
    
    // TODO Test occupantCount
    // TODO Test insertionPositions
    // TODO Test lastPlacedTile
    // TODO Test forestsClosedByLastTile
    // TODO Test riversClosedByLastTile
    // TODO Test canAddTile
    // TODO Test couldPlaceTile
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

    // TODO Test withOccupant
    // TODO Test withoutOccupant
    // TODO Test withoutGatherersOrFishersIn
    // TODO Test WithMoreCancelledAnimals
    // equals
    @Test
    void equalsWorksForTrivialBoards() {

    }
    @Test
    void equalsWorksForEmptyBoards() {
        Board board = Board.EMPTY;
        Board board1 = board.withNewTile(startTilePlacedRotNone);

        assertTrue(board.equals(Board.EMPTY));
        assertFalse(board1.equals(Board.EMPTY));
    }

    // TODO Test hashCode

    @Test
    void boardParamsAreImmutable(){

    }





}