package ch.epfl.chacun.stage7;

import ch.epfl.chacun.PlayerColor;
import javafx.scene.paint.Color;
import ch.epfl.chacun.gui.ColorMap;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ColorMapTest {

    @Test
    void fillColorWorksForEveryCase() {
        assertEquals(Color.RED, ColorMap.fillColor(PlayerColor.RED));
        assertEquals(Color.BLUE, ColorMap.fillColor(PlayerColor.BLUE));
        assertEquals(Color.LIME, ColorMap.fillColor(PlayerColor.GREEN));
        assertEquals(Color.YELLOW, ColorMap.fillColor(PlayerColor.YELLOW));
        assertEquals(Color.PURPLE, ColorMap.fillColor(PlayerColor.PURPLE));
    }

    @Test
    void strokeColorWorksForEveryCase() {
        assertEquals(Color.WHITE, ColorMap.strokeColor(PlayerColor.RED));
        assertEquals(Color.WHITE, ColorMap.strokeColor(PlayerColor.BLUE));
        assertEquals(Color.WHITE, ColorMap.strokeColor(PlayerColor.PURPLE));

        assertEquals(Color.LIME.deriveColor(0,1,0.6,1)
                , ColorMap.strokeColor(PlayerColor.GREEN));
        assertEquals(Color.YELLOW.deriveColor(0,1,0.6,1)
                , ColorMap.strokeColor(PlayerColor.YELLOW));
    }
}