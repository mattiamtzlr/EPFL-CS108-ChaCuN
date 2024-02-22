package ch.epfl.chacun;

import java.util.List;

/**
 * Direction Enumerator
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public enum Direction {
    N, E, S, W;
    public static final List<Direction> ALL = List.of(Direction.values());
    public static final int COUNT = ALL.size();

    public Direction rotated(Rotation rotation) {
        return ALL.get((this.ordinal() + rotation.quarterTurnsCW()) % COUNT);
    }

    public Direction opposite() {
        return ALL.get((this.ordinal() + (COUNT / 2)) % COUNT);
    }
}
