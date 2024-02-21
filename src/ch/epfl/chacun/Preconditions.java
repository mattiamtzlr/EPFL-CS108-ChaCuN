package ch.epfl.chacun;

/**
 * Preconditions class for argument checking
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (???)
 */
public class Preconditions {
    private Preconditions() {}

    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) throw new IllegalArgumentException("lol");
    }
}
