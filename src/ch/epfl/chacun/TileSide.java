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
     * @param forest
     */
    record Forest(Zone.Forest forest) implements TileSide {
        @Override
        public List<Zone> zones() {
            return List.of(forest);
        }

        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof TileSide.Forest;
        }
    }

    /**
     * Forest record representing the meadow type tile border
     * @param forest
     */
    record Meadow(Zone.Meadow meadow) implements TileSide {
        @Override
        public List<Zone> zones() {
            return List.of(meadow);
        }

        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof TileSide.Meadow;
        }
    }

    /**
     * Forest record representing the river type tile border
     * @param meadow1
     * @param river
     * @param meadow2
     */
    record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) implements TileSide {
        @Override
        public List<Zone> zones() {
            return List.of(meadow1, river, meadow2);
        }

        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof TileSide.River;
        }
    }

}
