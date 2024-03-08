package ch.epfl.chacun.week3;

import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Zone;
import ch.epfl.chacun.ZonePartition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Anthony Tamberg (357610)
 * @author Ali Gorgani (371956)
 */
public class ZonePartitionTest {

    Zone.Meadow m560 = new Zone.Meadow(560, new ArrayList<>(), null);
    Zone.Meadow m561 = new Zone.Meadow(561, new ArrayList<>(), null);
    Zone.Meadow m562 = new Zone.Meadow(562, new ArrayList<>(), null);
    Zone.Meadow m563 = new Zone.Meadow(563, new ArrayList<>(), null);
    Zone.Meadow m564 = new Zone.Meadow(564, new ArrayList<>(), null);
    Zone.Meadow m565 = new Zone.Meadow(565, new ArrayList<>(), null);
    Zone.Meadow m566 = new Zone.Meadow(566, new ArrayList<>(), null);
    Zone.Meadow m567 = new Zone.Meadow(567, new ArrayList<>(), null);
    Area<Zone.Meadow> meadowArea1 = new Area<>(Set.of(m560, m561, m562, m563), new ArrayList<>(), 4);
    Area<Zone.Meadow> meadowArea2 = new Area<>(Set.of(m564, m565, m566, m567), new ArrayList<>(), 4);

    ZonePartition<Zone.Meadow> partition = new ZonePartition<>(Set.of(meadowArea1, meadowArea2));


    @Test
    void areaContainingWorks() {
        assertEquals(meadowArea1, partition.areaContaining(m560));
        assertThrows(IllegalArgumentException.class, () -> {
            partition.areaContaining(new Zone.Meadow(123, new ArrayList<>(), null));
        });
    }

    @Test
    void areasWorks() {
        assertEquals(Set.of(meadowArea1, meadowArea2), partition.areas());
    }

    @Test
    void builderAddSingletonWorks() {
        ZonePartition.Builder<Zone.Forest> partition = new ZonePartition.Builder<>(new ZonePartition<>());
        var f677 = new Zone.Forest(677, Zone.Forest.Kind.PLAIN);
        var f678 = new Zone.Forest(678, Zone.Forest.Kind.PLAIN);
        partition.addSingleton(f677, 4);
        partition.addSingleton(f678, 4);

        var singleton1 = new Area<>(Set.of(f677), new ArrayList<>(), 4);
        var singleton2 = new Area<>(Set.of(f678), new ArrayList<>(), 4);

        ZonePartition<Zone.Forest> builtPartition = partition.build();
        ZonePartition<Zone.Forest> expectedPartition = new ZonePartition<>(Set.of(singleton1,singleton2));
        assertEquals(expectedPartition, builtPartition);
    }

    Zone.Forest f500 = new Zone.Forest(500, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Forest f501 = new Zone.Forest(501, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Forest f502 = new Zone.Forest(502, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Forest f503 = new Zone.Forest(503, Zone.Forest.Kind.WITH_MENHIR);

    Zone.Forest f504 = new Zone.Forest(504, Zone.Forest.Kind.PLAIN);

    Area<Zone.Forest> area1 = new Area<Zone.Forest>(Set.of(f500, f501, f502), List.of(PlayerColor.GREEN,PlayerColor.RED), 2);
    Area<Zone.Forest> area2 = new Area<Zone.Forest>(Collections.singleton(f503), List.of(PlayerColor.GREEN), 2);
    ZonePartition<Zone.Forest> partition1 = new ZonePartition<Zone.Forest>(Set.of(area1,area2));

    Area<Zone.Forest> expectedArea1 = new Area<>(Set.of(f500, f501, f502, f503), List.of(PlayerColor.GREEN, PlayerColor.RED, PlayerColor.GREEN), 2);
    ZonePartition<Zone.Forest> expectedPartition1 = new ZonePartition<Zone.Forest>(Set.of(expectedArea1));

    Area<Zone.Forest> expectedArea2 = new Area<>(Set.of(f500, f501, f502, f503), List.of(PlayerColor.GREEN, PlayerColor.RED, PlayerColor.GREEN), 0);
    ZonePartition<Zone.Forest> expectedPartition2 = new ZonePartition<Zone.Forest>(Collections.singleton(expectedArea2));

    Area<Zone.Forest> expectedArea3 = new Area<>(Set.of(f500, f501, f502, f503), List.of(PlayerColor.GREEN, PlayerColor.GREEN), 0);
    ZonePartition<Zone.Forest> expectedPartition3 = new ZonePartition<Zone.Forest>(Collections.singleton(expectedArea3));

    Area<Zone.Forest> expectedArea4 = new Area<>(Set.of(f500,f501,f502,f503), new ArrayList<>(), 0);
    ZonePartition<Zone.Forest> expectedPartition4 = new ZonePartition<Zone.Forest>(Collections.singleton(expectedArea4));

    Area<Zone.Forest> expectedArea5 = new Area<>(Set.of(f500,f501,f502,f503), List.of(PlayerColor.YELLOW), 0);
    ZonePartition<Zone.Forest> expectedPartition5 = new ZonePartition<Zone.Forest>(Collections.singleton(expectedArea5));

    @Test
    void builderUnionWorks() {
        ZonePartition.Builder<Zone.Forest> builder1 = new ZonePartition.Builder<Zone.Forest>(partition1);

        builder1.union(f502, f503);

        // union 1
        assertEquals(expectedPartition1, builder1.build());

        // union 2
        builder1.union(f501, f503);

        assertEquals(expectedPartition2, builder1.build());
    }

    @Test
    void builderRemoveOccupantWorks() {
        ZonePartition.Builder<Zone.Forest> builder2 = new ZonePartition.Builder<Zone.Forest>(expectedPartition2);

        // remove occupant
        assertThrows(IllegalArgumentException.class, () -> {
            builder2.removeOccupant(f504, PlayerColor.GREEN);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            builder2.removeOccupant(f503, PlayerColor.YELLOW);
        });

        builder2.removeOccupant(f502, PlayerColor.RED);

        assertEquals(expectedPartition3, builder2.build());
    }

    @Test
    void builderRemoveAllOccupantOfWorks() {
        ZonePartition.Builder<Zone.Forest> builder3 = new ZonePartition.Builder<Zone.Forest>(expectedPartition3);

        // removeAllOccupantsOf
        assertThrows(IllegalArgumentException.class, () -> {
            builder3.removeAllOccupantsOf(area2);
        });

        builder3.removeAllOccupantsOf(builder3.build().areaContaining(f500));
        assertEquals(expectedPartition4, builder3.build());
    }

    @Test
    void builderAddInitialOccupantWorks() {
        ZonePartition.Builder<Zone.Forest> builder4 = new ZonePartition.Builder<Zone.Forest>(expectedPartition4);

        // addInitialOccupant
        assertThrows(IllegalArgumentException.class, () -> {
            builder4.addInitialOccupant(f504, PlayerColor.YELLOW);
        });

        builder4.removeAllOccupantsOf(builder4.build().areaContaining(f500));
        builder4.addInitialOccupant(f501, PlayerColor.YELLOW);



        assertEquals(expectedPartition5, builder4.build());
    }
}
