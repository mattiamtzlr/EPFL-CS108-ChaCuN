package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import javafx.scene.paint.Color;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class ColorMap {

    private ColorMap() {
    }

    public Color fillColor(PlayerColor color) {
        Color returnColor = null;
        return switch (color) {
            case PlayerColor.RED    -> Color.RED;
            case PlayerColor.BLUE   -> Color.BLUE;
            case PlayerColor.GREEN  -> Color.LIME;
            case PlayerColor.YELLOW -> Color.YELLOW;
            case PlayerColor.PURPLE -> Color.PURPLE;
        };
    }
}
