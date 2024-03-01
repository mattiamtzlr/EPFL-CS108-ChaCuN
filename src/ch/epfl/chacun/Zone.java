package ch.epfl.chacun;

import java.util.List;

/**
 * Interface for the Zones
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public sealed interface Zone {
    enum SpecialPower {
        SHAMAN, LOGBOAT, HUNTING_TRAP, PIT_TRAP, WILD_FIRE, RAFT
    }

    /**
     * Returns the id of the tile which that zone is defined in. For example if the zone
     * id is 561 (eastern forest on starting tile), then this would return 56.
     * @param zoneId (int) the id of the zone
     * @return (int) the id of the tile containing the zone
     */
    static int tileId(int zoneId) {
        return zoneId / 10;
    }

    /**
     * Returns the local id of the zone, which is its id on that tile. For example if the
     * zone is 561 (eastern forest on starting tile), then this would return 1
     * @param zoneId (int) the id of the zone
     * @return (int) the local id of the zone
     */
    static int localId(int zoneId) {
        return zoneId - (tileId(zoneId) * 10);
    }

    /**
     * Returns the id of the zone
     * @return (int) id of the zone
     */
    int id();

    /**
     * Returns the tile id containing this zone
     * @return (int) tile id of this zone
     */
    default int tileId() {
        return tileId(this.id());
    }

    /**
     * Returns the local of this zone in its tile
     * @return (int) local id of this zone
     */
    default int localId() {
        return localId(this.id());
    }

    /**
     * Returns the special power of this zone if it has one, otherwise it returns null.
     * @return (SpecialPower) by default null
     */
    default SpecialPower specialPower() {
        return null;
    }

    // =================================== | Zones | ====================================

    /**
     * Forest record used to represent a forest zone
     * @param id (int) The global id of the forest
     * @param kind (Zone.Forest.Kind) kind of forest
     */
    record Forest(int id, Kind kind) implements Zone {
        /**
         * Forest record used to represent a forest zone
         * @param id (int) The global id of the forest
         * @param kind (Zone.Forest.Kind) kind of forest
         */
        public enum Kind {
            PLAIN, WITH_MENHIR, WITH_MUSHROOMS
        }
    }

    /**
     * Meadow record used to represent a meadow zone
     * @param id (int) the global id of the meadow
     * @param animals (List<Animal>) list of animals living in that meadow
     * @param specialPower (Zone.SpecialPower) special power of this meadow, may be null
     */
    record Meadow(int id, List<Animal> animals, SpecialPower specialPower) implements Zone {
        /**
         * Meadow record used to represent a meadow zone
         * @param id (int) the global id of the meadow
         * @param animals (List<Animal>) list of animals living in that meadow
         * @param specialPower (Zone.SpecialPower) special power of this meadow, may be null
         */
        public Meadow {
            animals = List.copyOf(animals);
        }
    }

    /**
     * Water interface used to model water-based zones
     */
    sealed interface Water extends Zone {
        /**
         * Returns the number of fish in this water zone
         * @return (int) number of fish
         */
        int fishCount();
    }

    /**
     * Lake record used to represent a lake zone
     *
     * @param id           (int) global id of the lake
     * @param fishCount    (int) number of fish in the lake
     * @param specialPower (Zone.SpecialPower) special power of the lake, may be null
     */
    record Lake(int id, int fishCount, SpecialPower specialPower) implements Water {
        /**
         * Lake record used to represent a lake zone
         *
         * @param id           (int) global id of the lake
         * @param fishCount    (int) number of fish in the lake
         * @param specialPower (Zone.SpecialPower) special power of the lake, may be null
         */
        public Lake {
            Preconditions.checkArgument(fishCount >= 0);
        }
    }

    /**
     * River record used to represent a river zone
     * @param id (int) global id of the river
     * @param fishCount (int) number of fish in the river
     * @param lake (Zone.Lake) lake that this river connects to, may be null, if it doesn't
     *             connect to a river
     */
    record River(int id, int fishCount, Lake lake) implements Zone {
        /**
         * River record used to represent a river zone
         * @param id (int) global id of the river
         * @param fishCount (int) number of fish in the river
         * @param lake (Zone.Lake) lake that this river connects to, may be null, if it doesn't
         *             connect to a river
         */
        public River {
            Preconditions.checkArgument(fishCount >= 0);
        }

        /**
         * Tests whether the river object is connected to a river *on the same tile*.
         * The starting tile river would return true.
         * @return Boolean value whether there exists a lake on the same tile
         */
        public boolean hasLake() {
            return lake != null;
        }
    }
}