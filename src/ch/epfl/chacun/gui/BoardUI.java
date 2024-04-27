package ch.epfl.chacun.gui;


import ch.epfl.chacun.*;
import static ch.epfl.chacun.Occupant.Kind.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class BoardUI {
    private BoardUI() {
    }

    public static Node create(
            int boardSize,
            ObservableValue<GameState> observableGameState,
            ObservableValue<Rotation> observableRotation,
            ObservableValue<Set<Occupant>> observableOccupants,
            ObservableValue<Set<Integer>> observableHighlightedTiles,
            Consumer<Rotation> rotationHandler,
            Consumer<Pos> positionHandler,
            Consumer<Occupant> occupantHandler) {

        WritableImage emptyTileImage = new WritableImage(1, 1);
        emptyTileImage
                .getPixelWriter()
                .setColor(0, 0, Color.gray(0.98));


        // Grid Pane
        GridPane boardPane = new GridPane();
        boardPane.setId("board-grid");

        // Two for loops to create each Group
        for (int x = -boardSize; x <= boardSize; x++) {
            for (int y = -boardSize; y <= boardSize; y++) {
                //      ImageView for the Face of the tile
                ImageView tileFace = new ImageView();
                tileFace.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileFace.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileFace.imageProperty().setValue(emptyTileImage);
                // Listener pointing to the correct tile on the board
                int finalX = x;
                int finalY = y;
                Group boardSquare = new Group(tileFace);

                observableGameState.map(GameState::board)
                    .addListener((_, _, newBoard) -> {
                        PlacedTile tile = newBoard.tileAt(new Pos(finalX, finalY));
                        if (Objects.nonNull(tile)) {

                            tileFace.imageProperty()
                                    .setValue(ImageLoader.normalForTileId(tile.id()));

                            tile.meadowZones().stream()
                                .map(Zone.Meadow::animals)
                                .flatMap(Collection::stream)
                                .forEach(a -> {
                                    ObservableValue<Boolean> isVisible =
                                        new SimpleObjectProperty<>(newBoard.cancelledAnimals()
                                                .contains(a));

                                    ImageView cancellationMarker = new ImageView();
                                    cancellationMarker.getStyleClass().add("marker");
                                    cancellationMarker.setId(STR."marker_\{a.id()}");
                                    cancellationMarker.visibleProperty().bind(isVisible);

                                    boardSquare.getChildren().add(cancellationMarker);
                                });

                            tile.potentialOccupants()
                                .forEach(
                                    o -> {
                                        Node svgPath = switch (o.kind()) {
                                            case HUT ->
                                                Icon.newFor(tile.placer(), HUT);
                                            case PAWN ->
                                                    Icon.newFor(tile.placer(), PAWN);
                                        };

                                        ObservableValue<Boolean> isVisible =
                                                observableOccupants.map(s -> s.contains(o));
                                        svgPath.visibleProperty().bind(isVisible);
                                        svgPath.setId(o.kind().equals(HUT)
                                                ? STR."hut_\{o.zoneId()}"
                                                : STR."pawn_\{o.zoneId()}"
                                        );
                                         svgPath.rotateProperty()
                                                 .set(tile.rotation().negated().degreesCW());
                                         svgPath.setOnMouseClicked(_ -> occupantHandler.accept(o));

                                        boardSquare.getChildren().add(svgPath);
                                    }
                                );

                        }
                    });

                //      Group
                boardPane.add(boardSquare, x + boardSize, y + boardSize);

                //==================================================================================
                //   TODO actually implement this stuff don't just fuck around
                //
                //                          This is experimental !!!
                //==================================================================================
                Blend borderBlend = new Blend();
                borderBlend.setMode(BlendMode.SRC_OVER);
                ColorInput colorInput = new ColorInput();
                colorInput.paintProperty().bind(observableGameState.map(g ->
                        (g.currentPlayer() != null) ?
                                ColorMap.fillColor(g.currentPlayer()) :
                                Color.WHITE));
                colorInput.setWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                colorInput.setHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);


                // Could not make this work with the next tile, so we got pikachu now
                Image pika = new Image("file:cornucopia/pikachu.png");

                ImageInput nextTileImage = new ImageInput();
                nextTileImage.sourceProperty().bind(observableGameState.map(
                                g -> (g.tileToPlace() != null) ?
                                        pika :
                                        emptyTileImage));


                borderBlend.setTopInput(colorInput);
                borderBlend.setBottomInput(nextTileImage);

                borderBlend.setOpacity(0.5);

                boardSquare.setOnMouseEntered(_ -> {
                    if (observableGameState.getValue().board().insertionPositions()
                            .contains(new Pos(finalX, finalY))){
                        boardSquare.effectProperty().setValue(borderBlend);
                        System.out.println(
                                STR."InsertionPositions did contain: \{finalX}|\{finalY}");
                        }
                    else
                        System.out.println(
                                STR."InsertionPositions did not contain: \{finalX}|\{finalY}");
                });
                boardSquare.setOnMouseExited(_ -> {
                    if (observableGameState.getValue().board().insertionPositions()
                            .contains(new Pos(finalX, finalY)))
                        boardSquare.effectProperty().setValue(null);
                });
                //==================================================================================
            }
        }
        // Scroll Pane
        ScrollPane boardView = new ScrollPane(boardPane);
        boardView.setId("board-scroll-pane");
        boardView.getStylesheets().add("board.css");

        return boardView;
    }
}
