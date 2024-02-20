package ch.epfl.chacun;

import java.util.List;

public enum Rotation {
    NONE,
    RIGHT,
    HALF_TURN,
    LEFT;

    public static final List<Rotation> ALL = List.of(Rotation.values());
    public static final int COUNT = ALL.size();

    public Rotation add(Rotation that) {
        return ALL.get((this.ordinal() + that.ordinal()) % COUNT);
    }

    public Rotation negated() {
        return ALL.get((this.ordinal() + (COUNT / 2)) % COUNT);
    }

    public int QuarterTurnsCW() {
        return this.ordinal();
    }

    public int degreesCW() {
        return this.ordinal() * 90;
    }
}
