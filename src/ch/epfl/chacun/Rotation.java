package ch.epfl.chacun;

import java.util.List;

/**
 * Rotation enumerator
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public enum Rotation {
    NONE,
    RIGHT,
    HALF_TURN,
    LEFT;

    public static final List<Rotation> ALL = List.of(Rotation.values());
    public static final int COUNT = ALL.size();

    /**
     * Adds another rotation to the current instance, e.g. adding RIGHT to HALF_TURN returns
     * LEFT.
     * @param that (Rotation) rotation to add
     * @return (Rotation) sum of the rotations
     */
    public Rotation add(Rotation that) {
        return ALL.get((this.ordinal() + that.ordinal()) % COUNT);
    }

    /**
     * Returns the opposite of the instance, meaning the rotation, which when added to the
     * original one yields NONE.
     * @return (Rotation) the opposite of the rotation
     */
    public Rotation negated() {
        return ALL.get((COUNT - this.ordinal()) % COUNT);
    }

    /**
     * Returns the number of clock-wise quarter turns represented by the rotation, e.g. HALF_TURN
     * is 2.
     * @return (int) the number of clock-wise quarter turns
     */
    public int quarterTurnsCW() {
        return this.ordinal();
    }

    /**
     * Returns the number of clock-wise degrees represented by the rotation, e.g. HALF_TURN is
     * 180°.
     * @return the number of degrees.
     */
    public int degreesCW() {
        return this.ordinal() * 90;
    }
}
