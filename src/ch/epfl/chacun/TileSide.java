package ch.epfl.chacun;

import java.util.List;


/**
 * TileSide interface to handle tile border types
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public sealed interface TileSide {
    List<Zone> zones();

    boolean isSameKindAs(TileSide that);

    /**
     * Forest record representing the forest type tile border
     *
     * @param forest a forest zone
     */
    record Forest(Zone.Forest forest) implements TileSide {
        /**
         * Returns a list of the zone on this side of the tile
         *
         * @return (List) list of zone on this TileSide
         */
        @Override
        public List<Zone> zones() {
            return List.of(forest);
        }

        /**
         * Method to check whether two TileSides are of the same kind
         *
         * @param that (TileSide) Side of a tile we want to compare
         * @return Boolean value if "that" is a forest tile side
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof TileSide.Forest;
        }
    }

    /**
     * Forest record representing the meadow type tile border
     *
     * @param meadow a meadow zone
     */
    record Meadow(Zone.Meadow meadow) implements TileSide {
        /**
         * Returns a list of the zone on this side of the tile
         *
         * @return (List) list of zone on this TileSide
         */
        @Override
        public List<Zone> zones() {
            return List.of(meadow);
        }

        /**
         * Method to check whether two TileSides are of the same kind
         *
         * @param that (TileSide) Side of a tile we want to compare
         * @return Boolean value if "that" is a meadow tile side
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof TileSide.Meadow;
        }
    }

    /**
     * Forest record representing the river type tile border
     *
     * @param meadow1 the first meadow zone (counterclockwise of the river)
     * @param river   the river zone
     * @param meadow2 the second meadow zone (clockwise of the river)
     */
    record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) implements TileSide {
        /**
         * Returns a list of the zones on this side of the tile
         *
         * @return (List) list of zones on this TileSide
         */
        @Override
        public List<Zone> zones() {
            return List.of(meadow1, river, meadow2);
        }

        /**
         * Method to check whether two TileSides are of the same kind
         *
         * @param that (TileSide) Side of a tile we want to compare
         * @return Boolean value if "that" is a river tile side
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof TileSide.River;
        }
    }

}
