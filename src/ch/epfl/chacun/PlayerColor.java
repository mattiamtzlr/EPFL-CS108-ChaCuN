package ch.epfl.chacun;

import java.util.List;

/**
 * PlayerColor enumerator
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public enum PlayerColor {
    RED, BLUE, GREEN, YELLOW, PURPLE;

    /**
     * List containing all the possible player colors in order
     */
    public static final List<PlayerColor> ALL = List.of(values());
}
