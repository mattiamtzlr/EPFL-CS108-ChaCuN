package ch.epfl.chacun.stage3;

import ch.epfl.chacun.*;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyAreaTest {
    Zone.Forest fzPlain1 = new Zone.Forest(873, Zone.Forest.Kind.PLAIN);
    Zone.Forest fzPlain2 = new Zone.Forest(291, Zone.Forest.Kind.PLAIN);
    Zone.Forest fzMenhir = new Zone.Forest(564, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Forest fzMushrooms1 = new Zone.Forest(472, Zone.Forest.Kind.WITH_MUSHROOMS);
    Zone.Forest fzMushrooms2 = new Zone.Forest(807, Zone.Forest.Kind.WITH_MUSHROOMS);
    Zone.Forest fzMushrooms3 = new Zone.Forest(581, Zone.Forest.Kind.WITH_MUSHROOMS);

    Area<Zone.Forest> faPlain = new Area<>(
            Set.of(fzPlain1, fzPlain2), List.of(PlayerColor.RED, PlayerColor.GREEN), 3
    );
    Area<Zone.Forest> faMenhirMushrooms = new Area<>(
            Set.of(fzMenhir, fzMushrooms1, fzMushrooms2, fzMushrooms3),
            List.of(PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.PURPLE), 2
    );

    Animal deer = new Animal(7161, Animal.Kind.DEER);
    Animal aurochs = new Animal(7160, Animal.Kind.AUROCHS);
    Zone.Meadow mz1 = new Zone.Meadow(
            716, List.of(aurochs, deer), Zone.SpecialPower.WILD_FIRE
    );
    Animal tiger1 = new Animal(2170, Animal.Kind.TIGER);
    Animal tiger2 = new Animal(2171, Animal.Kind.TIGER);
    Zone.Meadow mz2 = new Zone.Meadow(
            217, List.of(tiger1, tiger2), null
    );
    Area<Zone.Meadow> ma = new Area<>(Set.of(mz1, mz2), Collections.emptyList(), 2);

    Zone.River rz1 = new Zone.River(313, 2, null);
    Zone.River rz2 = new Zone.River(722, 1, null);
    Zone.Lake lz1 = new Zone.Lake(648, 2, Zone.SpecialPower.LOGBOAT);
    Zone.River rz3 = new Zone.River(641, 1, lz1);
    Zone.River rz4 = new Zone.River(767, 0, null);
    Zone.Lake lz2 = new Zone.Lake(138, 1, null);
    Zone.River rz5 = new Zone.River(134, 2, lz2);
    Area<Zone.River> ra = new Area<>(Set.of(rz1, rz2, rz3), Collections.emptyList(), 0);
    Area<Zone.Water> rsa = new Area<>(Set.of(rz1, rz2, rz3, rz4, rz5, lz1, lz2),
            Collections.emptyList(), 0);

    @Test
    void areaConstructionThrowsOnNegativeOpenConnections() {
        assertThrows(IllegalArgumentException.class, () ->
                new Area<>(Collections.emptySet(), Collections.emptyList(), -1));
    }

    @Test
    void hasMenhirWorks() {
        assertTrue(Area.hasMenhir(faMenhirMushrooms));
        assertFalse(Area.hasMenhir(faPlain));
    }

    @Test
    void mushroomGroupCountWorks() {
        assertEquals(0, Area.mushroomGroupCount(faPlain));
        assertEquals(3, Area.mushroomGroupCount(faMenhirMushrooms));
    }

    @Test
    void animalsWorks() {
        assertEquals(Collections.emptySet(),
                Area.animals(new Area<>(Collections.emptySet(), Collections.emptyList(), 0), Collections.emptySet()));

        assertEquals(Set.of(aurochs, tiger1, tiger2), Area.animals(ma, Set.of(deer)));
    }

    @Test
    void riverFishCountWorks() {
        assertEquals(6, Area.riverFishCount(ra));
        assertEquals(0, Area.riverFishCount(
                new Area<>(Set.of(
                        new Zone.River(937, 0, null)
                ), Collections.emptyList(), 0)
        ));
    }

    @Test
    void riverSystemFishCountWorks() {
        assertEquals(9, Area.riverSystemFishCount(rsa));
        assertEquals(0, Area.riverSystemFishCount(
                new Area<>(Set.of(
                        new Zone.Lake(498, 0, null)
                ), Collections.emptyList(), 0)
        ));
    }

    @Test
    void lakeCountWorks() {
        assertEquals(2, Area.lakeCount(rsa));
        assertEquals(0, Area.lakeCount(
                new Area<>(Set.of(
                        new Zone.River(941, 1, null)
                ), Collections.emptyList(), 0)
        ));
    }

    @Test
    void isClosedWorks() {
        assertTrue(ra.isClosed());
        assertTrue(rsa.isClosed());
        assertFalse(ma.isClosed());
        assertFalse(faPlain.isClosed());
        assertFalse(faMenhirMushrooms.isClosed());
    }

    @Test
    void isOccupiedWorks() {
        assertTrue(faPlain.isOccupied());
        assertTrue(faMenhirMushrooms.isOccupied());
        assertFalse(ma.isOccupied());
        assertFalse(ra.isOccupied());
        assertFalse(rsa.isOccupied());
    }

    @Test
    void majorityOccupantsWorks() {
        List<PlayerColor> occupants1 = List.of(
                PlayerColor.RED,
                PlayerColor.GREEN,
                PlayerColor.PURPLE,
                PlayerColor.YELLOW,
                PlayerColor.RED,
                PlayerColor.GREEN,
                PlayerColor.RED
        );

        List<PlayerColor> occupants2 = List.of(
                PlayerColor.RED,
                PlayerColor.GREEN,
                PlayerColor.PURPLE,
                PlayerColor.YELLOW,
                PlayerColor.RED,
                PlayerColor.GREEN,
                PlayerColor.YELLOW
        );

        Area<Zone.Meadow> area1 = new Area<>(Collections.emptySet(), occupants1, 0);
        Area<Zone.Meadow> area2 = new Area<>(Collections.emptySet(), occupants2, 0);

        assertEquals(Set.of(PlayerColor.RED), area1.majorityOccupants());
        assertEquals(
                Set.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.YELLOW),
                area2.majorityOccupants()
        );
        assertEquals(
                Set.of(PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.PURPLE),
                faMenhirMushrooms.majorityOccupants());
    }

    @Test
    void connectToWorksOnDifferentAreas() {
        Area<Zone.Forest> expectedConnection = new Area<>(Set.of(
                fzPlain1, fzPlain2, fzMenhir, fzMushrooms1, fzMushrooms2, fzMushrooms3
        ), PlayerColor.ALL, 3);

        assertEquals(expectedConnection, faPlain.connectTo(faMenhirMushrooms));
    }

    @Test
    void connectToWorksOnSameArea() {
        Area<Zone.Meadow> expectedConnection = new Area<>(Set.of(mz1, mz2),
                Collections.emptyList(), 0);

        assertEquals(expectedConnection, ma.connectTo(ma));
    }

    @Test
    void withInitialOccupantThrowsOnOccupiedArea() {
        assertThrows(IllegalArgumentException.class,
                () -> faPlain.withInitialOccupant(PlayerColor.RED));
    }

    @Test
    void withInitialOccupantWorks() {
        Area<Zone.Meadow> expectedMeadow = new Area<>(Set.of(mz1, mz2),
                List.of(PlayerColor.BLUE), 2);
        Area<Zone.River> expectedRiver = new Area<>(Set.of(rz1, rz2, rz3),
                List.of(PlayerColor.GREEN), 0);

        assertEquals(expectedMeadow,ma.withInitialOccupant(PlayerColor.BLUE));
        assertEquals(expectedRiver, ra.withInitialOccupant(PlayerColor.GREEN));
    }

    @Test
    void withoutOccupantThrowsOnUnoccupiedArea() {
        assertThrows(IllegalArgumentException.class,
                () -> ma.withoutOccupant(PlayerColor.BLUE));
    }

    @Test
    void withoutOccupantWorks() {
        Area<Zone.Forest> expectedFaPlain = new Area<>(
                Set.of(fzPlain1, fzPlain2), List.of(PlayerColor.GREEN), 3
        );
        Area<Zone.Forest> expectedFaMenhirMushrooms = new Area<>(
                Set.of(fzMenhir, fzMushrooms1, fzMushrooms2, fzMushrooms3),
                List.of( PlayerColor.BLUE, PlayerColor.PURPLE), 2
        );

        assertEquals(expectedFaPlain, faPlain.withoutOccupant(PlayerColor.RED));
        assertEquals(expectedFaMenhirMushrooms,faMenhirMushrooms.withoutOccupant(PlayerColor.YELLOW));
    }

    @Test
    void withoutOccupantsWorks() {
        Area<Zone.Forest> expectedFaPlain = new Area<>(
                Set.of(fzPlain1, fzPlain2), Collections.emptyList(), 3
        );
        Area<Zone.Forest> expectedFaMenhirMushrooms = new Area<>(
                Set.of(fzMenhir, fzMushrooms1, fzMushrooms2, fzMushrooms3),
                Collections.emptyList(), 2
        );

        assertEquals(expectedFaPlain, faPlain.withoutOccupants());
        assertEquals(expectedFaMenhirMushrooms, faMenhirMushrooms.withoutOccupants());
    }

    @Test
    void tileIdsWorks() {
        assertEquals(Set.of(56, 47, 80, 58), faMenhirMushrooms.tileIds());
        assertEquals(Set.of(31, 72, 64, 76, 13), rsa.tileIds());
    }

    @Test
    void zoneWithSpecialPowerWorks() {
        assertEquals(lz1, rsa.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT));
        assertEquals(mz1, ma.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE));

        assertNull(faPlain.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT));
        assertNull(rsa.zoneWithSpecialPower(Zone.SpecialPower.RAFT));
    }
}