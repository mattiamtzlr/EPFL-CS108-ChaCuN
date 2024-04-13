package ch.epfl.chacun.stage7;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.gui.Icon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IconTest {

    @Test
    void newForDoesNotCauseErrors() {
        // TODO look at this once gui
        for (PlayerColor playerColor : PlayerColor.ALL) {
            for (Occupant.Kind kind : Occupant.Kind.values()) {
                assertDoesNotThrow(() -> Icon.newFor(playerColor, kind));
            }
        }
    }
}