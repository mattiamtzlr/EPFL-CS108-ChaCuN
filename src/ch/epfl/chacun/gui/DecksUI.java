package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import java.util.function.Consumer;

/**
 * TODO DESCRIPTION
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class DecksUI {
    private DecksUI() {
    }

    /**
     * TODO
     * @param observableTileToPlace
     * @param observableNormalTilesLeft
     * @param observableMenhirTilesLeft
     * @param observableAltText
     * @param skipPawnAction
     * @return
     */
    public static Node create(
            ObservableValue<Tile> observableTileToPlace,
            ObservableValue<Integer> observableNormalTilesLeft,
            ObservableValue<Integer> observableMenhirTilesLeft,
            ObservableValue<String> observableAltText,
            Consumer<Occupant> skipPawnAction
    ) {
        // TODO
        return null;
    }
}
