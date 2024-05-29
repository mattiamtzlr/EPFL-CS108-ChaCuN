package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

/**
 * Class that generates the UI for the tile decks showing how many tiles are left and which tile
 * is the next tile to be placed.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class DecksUI {
    private DecksUI() {}

    /**
     * Creates a new UI for the tile decks with the given parameters below.
     *
     * @param observableTileToPlace     observable version of the next tile to place (Tile)
     * @param observableNormalTilesLeft observable integer specifying how many normal tiles are
     *                                  left on the normal tile deck
     * @param observableMenhirTilesLeft observable integer specifying how many normal tiles are
     *                                  left on the normal tile deck
     * @param observableAltText         observable string instance specifying the text that
     *                                  should be shown when no next tile to place is available
     *                                  i.e. during pawn placement or retake.
     *                                  Passing an empty string signals that a tile should be shown,
     *                                  passing a non-empty string signals that that string
     *                                  should be shown
     * @param skipPawnAction            event handler (Consumer) that should be called when the
     *                                  current player doesn't want to place / retake a pawn by
     *                                  clicking on the text specified by observableAltText
     * @return the UI for the tile decks as a node.
     */
    public static Node create(
        ObservableValue<Tile> observableTileToPlace,
        ObservableValue<Integer> observableNormalTilesLeft,
        ObservableValue<Integer> observableMenhirTilesLeft,
        ObservableValue<String> observableAltText,
        Consumer<Occupant> skipPawnAction
    ) {
        // -------------------------------- Tile to place: either the face of a tile or the alt text
        Text altText = new Text();
        altText.textProperty().bind(observableAltText);

        // ensure that the text is not too large
        altText.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE * 0.8);

        ObservableValue<Boolean> altTextShown = observableAltText.map(s -> !s.isEmpty());
        altText.visibleProperty().bind(altTextShown);

        // handle the user clicking on the text to skip the pawn action
        altText.setOnMouseClicked(_ -> skipPawnAction.accept(null));

        ImageView tileFace = new ImageView();
        tileFace.imageProperty()
            .bind(observableTileToPlace.map(ttp -> ImageLoader.largeForTileId(ttp.id())));
        tileFace.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        tileFace.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);

        tileFace.visibleProperty().bind(altTextShown.map(b -> !b));

        StackPane tileToPlace = new StackPane(tileFace, altText);
        tileToPlace.setId("next-tile");

        // ----------------------------------------------------------------------- Normal tiles deck
        Text normalTileNum = new Text();
        normalTileNum.textProperty().bind(observableNormalTilesLeft.map(Object::toString));

        ImageView normalBack = new ImageView();
        normalBack.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
        normalBack.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
        normalBack.setId("NORMAL");

        StackPane normalTiles = new StackPane(normalBack, normalTileNum);

        // ----------------------------------------------------------------------- Menhir tiles deck
        Text menhirTileNum = new Text();
        menhirTileNum.textProperty().bind(observableMenhirTilesLeft.map(Object::toString));

        ImageView menhirBack = new ImageView();
        menhirBack.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
        menhirBack.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
        menhirBack.setId("MENHIR");

        StackPane menhirTiles = new StackPane(menhirBack, menhirTileNum);

        // ---------------------------------------------------------- HBox containing the two stacks
        HBox hBox = new HBox(normalTiles, menhirTiles);
        hBox.setId("decks");

        // ------------------------------------------------------------------ VBox containing the UI
        VBox vBox = new VBox(hBox, tileToPlace);
        vBox.getStylesheets().add("decks.css");

        return vBox;
    }
}
