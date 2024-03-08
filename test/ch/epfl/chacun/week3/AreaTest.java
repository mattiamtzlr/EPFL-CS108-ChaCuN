package ch.epfl.chacun.week3;
import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AreaTest {
    @Test
    void testAreaCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR)),
                List.of(PlayerColor.RED, PlayerColor.BLUE), -4));
    }
    @Test
    void testHasMenhir() {
        var area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR)),
                List.of(PlayerColor.RED, PlayerColor.BLUE), 4);
        assertTrue(Area.hasMenhir(area));
    }
    @Test
    void testIsOccupied() {
        var area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR)),
                List.of(PlayerColor.RED, PlayerColor.BLUE), 4);
        assertTrue(area.isOccupied());
    }
    @Test
    void testMushroomCount() {
        var area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.WITH_MUSHROOMS)),
                List.of(PlayerColor.RED, PlayerColor.BLUE), 4);

        assertEquals(1, Area.mushroomGroupCount(area));
    }
    @Test
    void testAnimal() {
        var meadow = new Zone.Meadow(0, List.of(
                new Animal(1, Animal.Kind.DEER),
                new Animal(2, Animal.Kind.AUROCHS)
        ), null);

        var area = new Area<>(Set.of(meadow), List.of(PlayerColor.RED, PlayerColor.BLUE), 4);

        var cancelledAnimals = Set.of(new Animal(1, Animal.Kind.DEER));

        Set<Animal> remainingAnimals = Area.animals(area, cancelledAnimals);

        assertEquals(Animal.Kind.AUROCHS, remainingAnimals.iterator().next().kind());
    }
    @Test
    void testRiverFishCount() {
        var lake = new Zone.Lake(0, 3, null);

        var river = new Zone.River(10, 2, lake);

        var area = new Area<>(Set.of(river), List.of(PlayerColor.RED, PlayerColor.BLUE), 4);

        assertEquals(5, Area.riverFishCount(area));
    }
    @Test
    void testRiverSystemFishCount() {
        var lake = new Zone.Lake(0, 9, null);

        var river1 = new Zone.River(10, 2, lake);

        var river2 = new Zone.River(20, 4, lake);

        var area = new Area<Zone.Water>(Set.of(river1, lake, river2), List.of(PlayerColor.RED, PlayerColor.BLUE), 4);

        assertEquals(15, Area.riverSystemFishCount(area));

    }
    @Test
    void testLakeCount() {
        var lake = new Zone.Lake(0, 3, null);

        var river = new Zone.River(10, 2, lake);

        var area = new Area<Zone.Water>(Set.of(river, lake), List.of(PlayerColor.RED, PlayerColor.BLUE), 4);

        assertEquals(1, Area.lakeCount(area));
    }
    @Test
    void testIsClosed() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        var forest2 = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(20, Zone.Forest.Kind.PLAIN);
        var forest4 = new Zone.Forest(30, Zone.Forest.Kind.PLAIN);
        var area = new Area<>(Set.of(forest1, forest2, forest3, forest4), List.of(PlayerColor.RED, PlayerColor.BLUE), 0);

        assertTrue(area.isClosed());
    }
    @Test
    void testMajorityOccupants() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        var forest2 = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(20, Zone.Forest.Kind.PLAIN);
        var forest4 = new Zone.Forest(30, Zone.Forest.Kind.PLAIN);
        var area = new Area<>(Set.of(forest1, forest2, forest3, forest4), List.of(PlayerColor.GREEN, PlayerColor.BLUE,
                                                                                  PlayerColor.GREEN, PlayerColor.RED, PlayerColor.RED), 0);
        assertEquals(Set.of(PlayerColor.GREEN, PlayerColor.RED), area.majorityOccupants());
    }
    @Test
    void testConnectTo() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        var forest2 = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(20, Zone.Forest.Kind.PLAIN);
        var forest4 = new Zone.Forest(30, Zone.Forest.Kind.PLAIN);
        var area1 = new Area<>(Set.of(forest1, forest2, forest3, forest4), List.of(PlayerColor.GREEN), 1);
        var forest5 = new Zone.Forest(40, Zone.Forest.Kind.PLAIN);
        var area2 = new Area<>(Set.of(forest5), List.of(PlayerColor.GREEN), 1);
        var finalArea = area2.connectTo(area1);
        assertEquals(0, finalArea.openConnections());
    }
    @Test
    void testWithInitialOccupantFail() {
        var forest = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        var area = new Area<>(Set.of(forest), List.of(PlayerColor.GREEN), 4);
        assertThrows(IllegalArgumentException.class, () -> area.withInitialOccupant(PlayerColor.BLUE));
    }
    @Test
    void testWithInitialOccupant() {
        var forest = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        var area = new Area<>(Set.of(forest), List.of(), 4);
        var newArea = area.withInitialOccupant(PlayerColor.RED);
        assertEquals(PlayerColor.RED.ordinal(), (newArea.occupants().get(0)).ordinal());
    }
    @Test
    void testWithoutOccupants() {
        var forest = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        var area = new Area<>(Set.of(forest), List.of(PlayerColor.RED), 4);
        var newArea = area.withoutOccupants();
        assertTrue(newArea.occupants().isEmpty());
    }
    @Test
    void testTileIds() {
        var forest = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        var meadow = new Zone.Meadow(10, List.of(), null);
        var area = new Area<>(Set.of(forest, meadow), List.of(PlayerColor.GREEN), 6);
        var tileIds = area.tileIds();
        Iterator<Integer> iterator = tileIds.iterator();
        assertTrue(iterator.next() == 0 && iterator.next() == 1);
        assertEquals(Set.of(0,1), tileIds);
    }
    @Test
    void zoneWithSpecialPower() {
        var forest = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        var meadow = new Zone.Meadow(10, List.of() , Zone.Meadow.SpecialPower.SHAMAN);
        var area = new Area<>(Set.of(forest, meadow), List.of(PlayerColor.GREEN), 6);
        assertEquals(Zone.SpecialPower.SHAMAN.ordinal(), (area.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN)).specialPower().ordinal());
    }
}
