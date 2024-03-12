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
    ZonePartitions initialSetup = ZonePartitions.EMPTY;
    @Test
    void addTileWorksForTrivialCase() {

        ZonePartitions.Builder builder = new ZonePartitions.Builder(initialSetup);
        builder.addTile(startTile);
        ZonePartitions actualPartitions = builder.build();

        Area<Zone.Forest> forestArea = new Area<>(Set.of(f561), Collections.emptyList(), 2);
        Area<Zone.Meadow> meadowArea1 = new Area<>(Set.of(m560), Collections.emptyList(), 2);
        Area<Zone.Meadow> meadowArea2 = new Area<>(Set.of(m562), Collections.emptyList(), 1);
        Area<Zone.River> riverArea = new Area<>(Set.of(r563), Collections.emptyList(), 1);

        Area<Zone.Water> waterArea = new Area<>(Set.of(r563, l568), Collections.emptyList(), 1);

        ZonePartition<Zone.Forest> forestPart = new ZonePartition<>(Set.of(forestArea));
        ZonePartition<Zone.Meadow> meadowPart = new ZonePartition<>(Set.of(meadowArea1, meadowArea2));
        ZonePartition<Zone.River> riverPart = new ZonePartition<>(Set.of(riverArea));
        ZonePartition<Zone.Water> waterPart = new ZonePartition<>(Set.of(waterArea));

        assertEquals(forestPart, actualPartitions.forests());
        assertEquals(meadowPart, actualPartitions.meadows());
        assertEquals(riverPart, actualPartitions.rivers());
        assertEquals(waterPart, actualPartitions.riverSystems());
    }
    @Test
   void connectSidesWorksForTrivialCase() {}
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