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

    /**
     * List of all possible directions in order
     */
    public static final List<Direction> ALL = List.of(Direction.values());

    /**
     * Number of all possible rotations
     */
    public static final int COUNT = ALL.size();

    /**
     * Method to change a direction by rotation
     *
     * @param rotation rotation to be applied
     * @return new direction with applied rotation
     */
    public Direction rotated(Rotation rotation) {
        return ALL.get((this.ordinal() + rotation.quarterTurnsCW()) % COUNT);
    }

    /**
     * Method to change the direction to opposite
     *
     * @return new direction in opposite direction
     */
    public Direction opposite() {
        return rotated(Rotation.HALF_TURN);
    }
}
