package ch.epfl.chacun.week3;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyZonePartitionTest {
    private Zone.Meadow m121 = new Zone.Meadow(121, List.of(), null);
    private Zone.Meadow m122 = new Zone.Meadow(122, List.of(new Animal(1221, Animal.Kind.MAMMOTH)), null);
    private Zone.Meadow m123 = new Zone.Meadow(123, List.of(new Animal(1231, Animal.Kind.AUROCHS)), Zone.Meadow.SpecialPower.HUNTING_TRAP);
    private Zone.Meadow m124 = new Zone.Meadow(124, List.of(), null);
    private Zone.Forest f567 = new Zone.Forest(567, Zone.Forest.Kind.WITH_MENHIR);
    private Zone.Forest f568 = new Zone.Forest(568, Zone.Forest.Kind.PLAIN);
    private Zone.Forest f569 = new Zone.Forest(569, Zone.Forest.Kind.WITH_MUSHROOMS);
    private Zone.Forest f570 = new Zone.Forest(570, Zone.Forest.Kind.PLAIN);

    @Test
    void zonePartitionIsImmutable(){

        Set<Area<Zone>> sA = new HashSet<>();
        sA.add(new Area<>(Set.of(m123, m121), List.of(), 5));

        ZonePartition<Zone> zP = new ZonePartition<>(sA);

        try {zP.areas().clear();} catch (UnsupportedOperationException e) {/**/}

        assertEquals(sA,zP.areas());
    }

    @Test
    void areaContainingWorksForTrivialCase(){
        Set<Area<Zone.Meadow>> sA = new HashSet<>();

        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m124, m122), List.of(), 5);

        sA.add(a1);
        sA.add(a2);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);

        assertEquals(a1, zP.areaContaining(m123));
        assertEquals(a1, zP.areaContaining(m121));
        assertEquals(a2, zP.areaContaining(m124));
        assertEquals(a2, zP.areaContaining(m122));

        Set<Area<Zone.Forest>> sA2 = new HashSet<>();

        Area<Zone.Forest> a3 = new Area<>(Set.of(f567, f568), List.of(), 5);
        Area<Zone.Forest> a4 = new Area<>(Set.of(f570, f569), List.of(), 5);

        sA2.add(a3);
        sA2.add(a4);

        ZonePartition<Zone.Forest> zP2 = new ZonePartition<>(sA2);

        assertEquals(a3, zP2.areaContaining(f567));
        assertEquals(a3, zP2.areaContaining(f568));
        assertEquals(a4, zP2.areaContaining(f569));
        assertEquals(a4, zP2.areaContaining(f570));
    }

    @Test
    void areaContainingThrowsOnZoneNotContained(){
        Set<Area<Zone.Meadow>> sA = new HashSet<>();
        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m122), List.of(), 5);

        sA.add(a1);
        sA.add(a2);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);
        assertThrows(IllegalArgumentException.class, () -> zP.areaContaining(m124));

        Set<Area<Zone.Forest>> sA2 = new HashSet<>();

        Area<Zone.Forest> a3 = new Area<>(Set.of(f567, f568), List.of(), 5);
        Area<Zone.Forest> a4 = new Area<>(Set.of(f570), List.of(), 5);

        sA2.add(a3);
        sA2.add(a4);

        ZonePartition<Zone.Forest> zP2 = new ZonePartition<>(sA2);
        assertThrows(IllegalArgumentException.class, () -> zP2.areaContaining(f569));

    }

    @Test
    void addSingletonWorksForTrivialCase(){

        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m122), List.of(), 5);
        Set<Area<Zone.Meadow>> sA = Set.of(a1, a2);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Meadow> zPBuilder = new ZonePartition.Builder<>(zP);

        zPBuilder.addSingleton(m124, 2);

        Area<Zone.Meadow> expectedSingleton = new Area<>(Set.of(m124), List.of(), 2);

        Set<Area<Zone.Meadow>> expectedSet = Set.of(a1, a2, expectedSingleton);

        assertEquals(expectedSet, zPBuilder.build().areas());

        Area<Zone.Forest> a3 = new Area<>(Set.of(f567, f568), List.of(), 5);
        Area<Zone.Forest> a4 = new Area<>(Set.of(f569), List.of(), 5);
        Set<Area<Zone.Forest>> sA2 = Set.of(a3, a4);
        ZonePartition<Zone.Forest> zP2 = new ZonePartition<>(sA2);
        ZonePartition.Builder<Zone.Forest> zPBuilder2 = new ZonePartition.Builder<>(zP2);

        zPBuilder2.addSingleton(f570, 2);

        Area<Zone.Forest> expectedSingleton2 = new Area<>(Set.of(f570), List.of(), 2);

        Set<Area<Zone.Forest>> expectedSet2 = Set.of(a3, a4, expectedSingleton2);

        assertEquals(expectedSet2, zPBuilder2.build().areas());

    }

    @Test
    void addInitialOccupantWorksForTrivialCase(){
        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(PlayerColor.PURPLE), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m122), List.of(), 5);
        Set<Area<Zone.Meadow>> sA = Set.of(a1, a2);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Meadow> zPBuilder = new ZonePartition.Builder<>(zP);

        zPBuilder.addInitialOccupant(m122, PlayerColor.RED);

        Area<Zone.Meadow> expectedA2 = new Area<>(Set.of(m122), List.of(PlayerColor.RED), 5);
        Set<Area<Zone.Meadow>> expectedSet = Set.of(a1, expectedA2);

        assertEquals(expectedSet, zPBuilder.build().areas());
    }

    @Test
    void addInitialOccupantThrowsOnZoneNotContained(){
        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(PlayerColor.PURPLE), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m122), List.of(), 5);
        Set<Area<Zone.Meadow>> sA = Set.of(a1, a2);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Meadow> zPBuilder = new ZonePartition.Builder<>(zP);

        assertThrows(IllegalArgumentException.class, ()-> zPBuilder.addInitialOccupant(m124, PlayerColor.RED));

    }

    @Test
    void addInitialOccupantThrowsOnAlreadyOccupied(){
        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(PlayerColor.PURPLE), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m122), List.of(), 5);
        Set<Area<Zone.Meadow>> sA = Set.of(a1, a2);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Meadow> zPBuilder = new ZonePartition.Builder<>(zP);

        assertThrows(IllegalArgumentException.class, ()-> zPBuilder.addInitialOccupant(m123, PlayerColor.RED));
        assertThrows(IllegalArgumentException.class, ()-> zPBuilder.addInitialOccupant(m123, PlayerColor.PURPLE));
    }


    @Test
    void removeOccupantWorksForTrivialCase(){
        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(PlayerColor.PURPLE), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m122), List.of(), 5);
        Set<Area<Zone.Meadow>> sA = Set.of(a1, a2);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Meadow> zPBuilder = new ZonePartition.Builder<>(zP);

        zPBuilder.removeOccupant(m123, PlayerColor.PURPLE);

        Area<Zone.Meadow> expectedA1 = new Area<>(Set.of(m123, m121), List.of(), 5);
        Set<Area<Zone.Meadow>> expectedSet = Set.of(a2, expectedA1);

        assertEquals(expectedSet, zPBuilder.build().areas());
    }
    @Test
    void removeOccupantThrowsOnColorNotContained() {
        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(PlayerColor.PURPLE), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m122), List.of(), 5);
        Set<Area<Zone.Meadow>> sA = Set.of(a1, a2);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Meadow> zPBuilder = new ZonePartition.Builder<>(zP);

        assertThrows(IllegalArgumentException.class, ()-> zPBuilder.removeOccupant(m123, PlayerColor.RED));
        assertThrows(IllegalArgumentException.class, ()-> zPBuilder.removeOccupant(m122, PlayerColor.PURPLE));
    }
    @Test
    void removeOccupantThrowsOnZoneNotContained() {
        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(PlayerColor.PURPLE), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m122), List.of(), 5);
        Set<Area<Zone.Meadow>> sA = Set.of(a1, a2);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Meadow> zPBuilder = new ZonePartition.Builder<>(zP);

        assertThrows(IllegalArgumentException.class, ()-> zPBuilder.removeOccupant(m124, PlayerColor.RED));
    }
    @Test
    void removeAllOccupantsOfWorksForTrivialCase(){
        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(PlayerColor.PURPLE, PlayerColor.GREEN), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m122), List.of(), 5);
        Set<Area<Zone.Meadow>> sA = Set.of(a1, a2);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Meadow> zPBuilder = new ZonePartition.Builder<>(zP);

        zPBuilder.removeAllOccupantsOf(a1);

        Area<Zone.Meadow> expectedA1 = new Area<>(Set.of(m123, m121), List.of(), 5);
        Set<Area<Zone.Meadow>> expectedSet = Set.of(a2, expectedA1);

        assertEquals(expectedSet, zPBuilder.build().areas());
    }

    @Test
    void removeAllOccupantsThrowsOnAreaNotContained(){
        Area<Zone.Meadow> a1 = new Area<>(Set.of(m123, m121), List.of(PlayerColor.PURPLE, PlayerColor.GREEN), 5);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(m122), List.of(PlayerColor.RED), 5);
        Set<Area<Zone.Meadow>> sA = Set.of(a1);

        ZonePartition<Zone.Meadow> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Meadow> zPBuilder = new ZonePartition.Builder<>(zP);

        assertThrows(IllegalArgumentException.class, ()-> zPBuilder.removeAllOccupantsOf(a2));

    }
    @Test
    void unionWorksForTrivialCase(){
        Area<Zone.Forest> a1 = new Area<>(Set.of(f570, f569), List.of(PlayerColor.PURPLE, PlayerColor.GREEN), 5);
        Area<Zone.Forest> a2 = new Area<>(Set.of(f568), List.of(PlayerColor.RED), 5);

        ZonePartition<Zone.Forest> zP = new ZonePartition<>(Set.of(a1, a2));
        ZonePartition.Builder<Zone.Forest> zPBuilder = new ZonePartition.Builder<>(zP);

        zPBuilder.union(f568, f569);


        Area<Zone.Forest> expectedA1 = new Area<>(Set.of(f568, f569, f570), List.of(PlayerColor.PURPLE, PlayerColor.GREEN, PlayerColor.RED), 8);
        Set<Area<Zone.Forest>> expectedSet = Set.of(expectedA1);

        assertEquals(expectedSet, zPBuilder.build().areas());

    }
    @Test
    void unionWorksForTwiceSameArea(){
        Area<Zone.Forest> a1 = new Area<>(Set.of(f570, f569), List.of(PlayerColor.PURPLE, PlayerColor.GREEN), 5);
        Set<Area<Zone.Forest>> sA = Set.of(a1);

        ZonePartition<Zone.Forest> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Forest> zPBuilder = new ZonePartition.Builder<>(zP);

        zPBuilder.union(f570, f569);


        Area<Zone.Forest> expectedA1 = new Area<>(Set.of(f569, f570), List.of(PlayerColor.PURPLE, PlayerColor.GREEN), 3);
        Set<Area<Zone.Forest>> expectedSet = Set.of(expectedA1);

        assertEquals(expectedSet, zPBuilder.build().areas());
    }
    @Test
    void unionThrowsOnZonesNotContained(){
        Area<Zone.Forest> a1 = new Area<>(Set.of(f570, f569), List.of(PlayerColor.PURPLE, PlayerColor.GREEN), 5);
        Area<Zone.Forest> a2 = new Area<>(Set.of(f568), List.of(PlayerColor.RED), 5);
        Set<Area<Zone.Forest>> sA = Set.of(a1, a2);

        ZonePartition<Zone.Forest> zP = new ZonePartition<>(sA);
        ZonePartition.Builder<Zone.Forest> zPBuilder = new ZonePartition.Builder<>(zP);

        assertThrows(IllegalArgumentException.class, ()-> zPBuilder.union(f567, f569));
    }

    @Test
    void buildWorksForTrivialCase(){
        Area<Zone.Forest> a1 = new Area<>(Set.of(f570, f569), List.of(PlayerColor.PURPLE, PlayerColor.GREEN), 5);
        Area<Zone.Forest> a2 = new Area<>(Set.of(f568), List.of(PlayerColor.RED), 5);

        ZonePartition<Zone.Forest> zP = new ZonePartition<>(Set.of(a1, a2));

        ZonePartition.Builder<Zone.Forest> zPBuilder = new ZonePartition.Builder<>(zP);

        assertEquals(zP, zPBuilder.build());


        zPBuilder.addSingleton(f567, 2);

        Area<Zone.Forest> expectedSingleton = new Area<>(Set.of(f567), Collections.emptyList(), 2);

        Set<Area<Zone.Forest>> expectedAreaSet = Set.of(a1, a2, expectedSingleton);

        ZonePartition<Zone.Forest> expectedZonePartition = new ZonePartition<>(expectedAreaSet);

        assertEquals(expectedZonePartition, zPBuilder.build());
    }

}