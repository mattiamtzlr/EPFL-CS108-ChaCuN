package ch.epfl.chacun.gui;


import ch.epfl.chacun.GameState;
import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Pos;
import ch.epfl.chacun.Rotation;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import java.util.Set;
import java.util.function.Consumer;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class BoardUI {
    private BoardUI() {}

    public Node create(ObservableValue<GameState> observableGameState,
                       ObservableValue<Rotation> observableRotation,
                       ObservableValue<Set<Occupant>> observableOccupants,
                       ObservableValue<Set<Integer>> observableHighlightedTiles,
                       Consumer<Rotation> rotationHandler,
                       Consumer<Pos> positionHandler,
                       Consumer<Occupant> occupantHandler) {
        // Grid Pane

        // Two for loops to create each Group

        //      Group

        //      ImageView for the Face of the tile
        //      ImageView for the cancel markers
        //      SVGPath for occupants
        return null;
    }
}
