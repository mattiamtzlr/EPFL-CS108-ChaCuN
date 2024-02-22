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

    public static final List<PlayerColor> ALL = List.of(PlayerColor.values());
}
