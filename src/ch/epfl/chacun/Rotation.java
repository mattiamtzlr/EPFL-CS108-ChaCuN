package ch.epfl.chacun;

import java.util.List;

public enum Rotation {
    NONE,
    RIGHT,
    HALF_TURN,
    LEFT;

    public static final List<Rotation> ALL = List.of(Rotation.values());
    public static final int COUNT = ALL.size();

    private static Rotation fromOrdinal(int ordinal) {
        for (Rotation rot : Rotation.values()) {
            if (rot.ordinal() == ordinal) return rot;
        }

        return null;
    }

    public Rotation add(Rotation that) {
        return fromOrdinal((this.ordinal() + that.ordinal()) % COUNT);
    }

    public Rotation negated() {
        return fromOrdinal((this.ordinal() + (COUNT / 2)) % COUNT);
    }

    public int QuarterTurnsCW() {
        return this.ordinal();
    }

    public int degreesCW() {
        return this.ordinal() * 90;
    }
}
