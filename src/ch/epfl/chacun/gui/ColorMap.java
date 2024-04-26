package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import javafx.scene.paint.Color;

/**
 * Class to map from PlayerColor to JavaFx color that can be displayed
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class ColorMap {

    private ColorMap() {}

    /**
     * Static method that maps PlayerColors to the corresponding color for JavaFX
     *
     * @param color the color to map
     * @return the color it maps to
     */
    public static Color fillColor(PlayerColor color) {
        return switch (color) {
            case PlayerColor.RED -> Color.RED;
            case PlayerColor.BLUE -> Color.BLUE;
            case PlayerColor.GREEN -> Color.LIME;
            case PlayerColor.YELLOW -> Color.YELLOW;
            case PlayerColor.PURPLE -> Color.PURPLE;
        };
    }

    /**
     * Static method to get the color of the stroke for a PlayerColor
     *
     * @param color the color get the stroke color for
     * @return the stroke color
     */
    public static Color strokeColor(PlayerColor color) {
        return (color.equals(PlayerColor.GREEN) || color.equals(PlayerColor.YELLOW))
                ? fillColor(color)
                .deriveColor(0, 1, 0.6, 1)
                : Color.WHITE;
    }
}
