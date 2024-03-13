package ch.epfl.chacun.week4;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyZonePartitionsTest {

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
    PlacedTile t88Placed = new PlacedTile(t88, PlayerColor.PURPLE, Rotation.NONE,
            startTilePlacedRotNone.pos().neighbor(Direction.W));

    // Tile 61
    Zone.Meadow m610 = new Zone.Meadow(610, List.of(new Animal(6100, Animal.Kind.MAMMOTH)), null);
    TileSide.Meadow t61N = new TileSide.Meadow(m610);
    TileSide.Meadow t61E = new TileSide.Meadow(m610);
    TileSide.Meadow t61S = new TileSide.Meadow(m610);
    TileSide.Meadow t61W = new TileSide.Meadow(m610);
    Tile t61 = new Tile(61, Tile.Kind.NORMAL, t61N, t61E, t61S, t61W);
    PlacedTile t61Placed = new PlacedTile(t61, PlayerColor.GREEN, Rotation.NONE,
            startTilePlacedRotNone.pos().neighbor(Direction.N), new Occupant(Occupant.Kind.PAWN, m610.id()));
    Tile[] tiles = {startTile, t88, t61};
    ZonePartitions initialSetup = ZonePartitions.EMPTY;
    @Test
    void addTileWorksForTrivialCase() {

        // TODO How should this method work, if we call it multiple times ?  ?

        ZonePartitions.Builder builder = new ZonePartitions.Builder(initialSetup);
        builder.addTile(startTile);
        ZonePartitions actualPartitionsStartTile = builder.build();
        ZonePartitions.Builder builder1 = new ZonePartitions.Builder(initialSetup);
        builder1.addTile(t88);
        ZonePartitions actualPartitionsTile88 = builder1.build();
        ZonePartitions.Builder builder2 = new ZonePartitions.Builder(initialSetup);
        builder2.addTile(t61);
        ZonePartitions actualPartitionsTile61 = builder2.build();

        Area<Zone.Forest> forestArea56 = new Area<>(Set.of(f561), Collections.emptyList(), 2);
        Area<Zone.Meadow> meadowArea156 = new Area<>(Set.of(m560), Collections.emptyList(), 2);
        Area<Zone.Meadow> meadowArea256 = new Area<>(Set.of(m562), Collections.emptyList(), 1);
        Area<Zone.River> riverArea56 = new Area<>(Set.of(r563), Collections.emptyList(), 1);

        Area<Zone.Water> waterArea56 = new Area<>(Set.of(r563, l568), Collections.emptyList(), 1);

        ZonePartition<Zone.Forest> expectedForestPart56 = new ZonePartition<>(Set.of(forestArea56));
        ZonePartition<Zone.Meadow> expectedMeadowPart56 = new ZonePartition<>(Set.of(meadowArea156, meadowArea256));
        ZonePartition<Zone.River> expectedRiverPart56 = new ZonePartition<>(Set.of(riverArea56));
        ZonePartition<Zone.Water> expectedWaterPart56 = new ZonePartition<>(Set.of(waterArea56));

        assertEquals(expectedForestPart56, actualPartitionsStartTile.forests());
        assertEquals(expectedMeadowPart56, actualPartitionsStartTile.meadows());
        assertEquals(expectedRiverPart56, actualPartitionsStartTile.rivers());
        assertEquals(expectedWaterPart56, actualPartitionsStartTile.riverSystems());

        Area<Zone.Forest> forestArea88 = new Area<>(Set.of(f883), Collections.emptyList(), 1);
        Area<Zone.Meadow> meadowArea188 = new Area<>(Set.of(m880), Collections.emptyList(), 3);
        Area<Zone.Meadow> meadowArea288 = new Area<>(Set.of(m882), Collections.emptyList(), 2);
        Area<Zone.River> riverArea88 = new Area<>(Set.of(r881), Collections.emptyList(), 2);

        ZonePartition<Zone.Forest> expectedForestPart88 = new ZonePartition<>(Set.of(forestArea88));
        ZonePartition<Zone.Meadow> expectedMeadowPart88 = new ZonePartition<>(Set.of(meadowArea188, meadowArea288));
        ZonePartition<Zone.River> expectedRiverPart88 = new ZonePartition<>(Set.of(riverArea88));


        assertEquals(expectedForestPart88, actualPartitionsTile88.forests());
        assertEquals(expectedMeadowPart88, actualPartitionsTile88.meadows());
        assertEquals(expectedRiverPart88, actualPartitionsTile88.rivers());

        Area<Zone.Meadow> meadowArea61 = new Area<>(Set.of(m610), Collections.emptyList(), 4);

        ZonePartition<Zone.Meadow> expectedMeadowPart61 = new ZonePartition<>(Set.of(meadowArea61));

        assertEquals(expectedMeadowPart61, actualPartitionsTile61.meadows());
    }
    /*
    @Test
    void addTileThrowsWhenTileIsAlreadyPlayed() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(initialSetup);
        builder.addTile(startTile);
        assertThrows(IllegalArgumentException.class, () -> builder.addTile(startTile));
    }
    */
    @Test
   void connectSidesWorksForTrivialCase() {

        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);

        builder.addTile(startTile);
        builder.addTile(t88);

        TileSide eastSide88 = new TileSide.River(m882, r881, m880);
        TileSide westSide56 = new TileSide.River(m562, r563, m560);

        builder.connectSides(eastSide88, westSide56);

        Area<Zone.Forest> forestArea56 = new Area<>(Set.of(f561), Collections.emptyList(), 2);
        Area<Zone.Forest> forestArea88 = new Area<>(Set.of(f883), Collections.emptyList(), 1);
        Area<Zone.Meadow> meadowArea156 = new Area<>(Set.of(m560, m882), Collections.emptyList(), 2);
        Area<Zone.Meadow> meadowArea256 = new Area<>(Set.of(m562, m880), Collections.emptyList(), 2);
        Area<Zone.River> riverArea56 = new Area<>(Set.of(r563, r881), Collections.emptyList(), 1);
        Area<Zone.Water> waterArea56 = new Area<>(Set.of(r881, l568, r563), Collections.emptyList(), 1);

        ZonePartition<Zone.Forest> expectedForestPart = new ZonePartition<>(Set.of(forestArea56, forestArea88));
        ZonePartition<Zone.Meadow> expectedMeadowPart = new ZonePartition<>(Set.of(meadowArea256, meadowArea156));
        ZonePartition<Zone.River> expectedRiverPart = new ZonePartition<>(Set.of(riverArea56));
        ZonePartition<Zone.Water> expectedWaterPart = new ZonePartition<>(Set.of(waterArea56));

        ZonePartitions expectedPartition = new ZonePartitions(expectedForestPart, expectedMeadowPart, expectedRiverPart, expectedWaterPart);

        assertEquals(expectedPartition, builder.build());

        builder.addTile(t61);

        TileSide eastSideT61 = new TileSide.Meadow(m610);
        TileSide westSideT88 = new TileSide.Meadow(m880);

        builder.connectSides(eastSideT61, westSideT88);

        Area<Zone.Meadow> meadowArea618856 = new Area<>(Set.of(m880, m610, m562), Collections.emptyList(), 4);

        ZonePartition<Zone.Meadow> expectedMeadowPartWith3Tiles = new ZonePartition<>(Set.of(meadowArea156, meadowArea618856));


        ZonePartitions expectedPartition3Tiles = new ZonePartitions(expectedForestPart, expectedMeadowPartWith3Tiles, expectedRiverPart, expectedWaterPart);

        assertEquals(expectedPartition3Tiles, builder.build());

    }

    @Test
    void connectSidesThrowsOnMismatch() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);

        builder.addTile(startTile);
        builder.addTile(t88);

        TileSide southSide88 = new TileSide.Forest(f883);
        TileSide northSide56 = new TileSide.Meadow(m560);

        assertThrows(IllegalArgumentException.class, () -> builder.connectSides(northSide56, southSide88));
    }
    @Test
    void connectSidesThrowsOnEmptySide() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);

        builder.addTile(startTile);
        builder.addTile(t88);

        TileSide northSide56 = new TileSide.Meadow(m560);

        assertThrows(IllegalArgumentException.class, () -> builder.connectSides(northSide56, null));
    }
    @Test
    void connectSidesThrowsOnTwiceSameSide() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);

        builder.addTile(startTile);

        TileSide northSide56 = new TileSide.Meadow(m560);

        assertThrows(IllegalArgumentException.class, () -> builder.connectSides(northSide56, northSide56));
    }
    @Test
   void addInitialOccupantWorksForTrivialCase() {}
    @Test
   void removePawnWorksForTrivialCase(){}
    @Test
   void clearGatherersWorksForTrivialCase() {}
    @Test
   void clearFishersWorksForTrivialCase() {}
    @Test
    void buildWorksForTrivialCase() {}
}