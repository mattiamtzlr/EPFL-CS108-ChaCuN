package ch.epfl.chacun;

/**
 * Preconditions class for argument checking
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class Preconditions {
    private Preconditions() {}

    /**
     * Used to check if the arguments to a method are correct.
     *
     * @param shouldBeTrue Condition which needs to be true for an argument to be valid e.g.
     *                     (a.length > 0)
     * @throws IllegalArgumentException if the passed condition is false.
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) throw new IllegalArgumentException();
    }
}
