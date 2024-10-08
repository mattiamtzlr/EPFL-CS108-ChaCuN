package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import javafx.scene.Node;
import javafx.scene.shape.SVGPath;

/**
 * Class which generates the icons of the pawns and huts used in the game.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class Icon {
    private static final String PAWN = "M -10 10 H -4 L 0 2 L 6 10 H 12 L 5 0 L 12 -2 L 12 -4 L 6"
        + " -6 L 6 -10 L 0 -10 L -2 -4 L -6 -2 L -8 -10 L -12 -10 L -8 6 Z";
    private static final String HUT = "M -8 10 H 8 V 2 H 12 L 0 -10 L -12 2 H -8 Z";

    private Icon() {}

    /**
     * Method to yield a new JavaFX Node containing the svg for an icon of either a pawn or a hut in
     * a given color.
     *
     * @param color the color of the player of which the occupant should be drawn
     * @param kind  the kind of occupant
     * @return the Node containing that icon
     */
    public static Node newFor(PlayerColor color, Occupant.Kind kind) {
        SVGPath svg = new SVGPath();
        svg.setFill(ColorMap.fillColor(color));
        svg.setStroke(ColorMap.strokeColor(color));
        svg.setContent(kind == Occupant.Kind.PAWN ? PAWN : HUT);
        return svg;
    }
}


