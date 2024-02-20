package ch.epfl.chacun;

public class Preconditions {
    private Preconditions() {}

    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) throw new IllegalArgumentException("lol");
    }
}
