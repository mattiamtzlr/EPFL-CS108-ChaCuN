package ch.epfl.chacun.gui;


import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.function.Consumer;

import static ch.epfl.chacun.Occupant.Kind.HUT;
import static ch.epfl.chacun.Occupant.Kind.PAWN;

/**
 * Class which handles the generation of the UI for the game area / board.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class BoardUI {
    private BoardUI() {
    }

    private static class CachedImageLoader {
        private final Map<Integer, Image> cache;

        /**
         * Helper class that handles image caching and loading, only uses NORMAL sized images, as
         * only they are used in this UI.
         */
        private CachedImageLoader() {
            this.cache = new HashMap<>();
        }

        /**
         * Returns the image of the tile face for the given tile id, either by getting it from the
         * cache, if available or by getting it from the ImageLoader class.
         *
         * @param tileId the id of the tile to be retrieved
         * @return the Image of the tile face
         */
        private Image get(int tileId) {
            Image temp = cache.get(tileId);
            if (Objects.isNull(temp)) {
                Image toReturn = ImageLoader.normalForTileId(tileId);
                cache.put(tileId, toReturn);
                return toReturn;

            } else {
                return temp;
            }
        }
    }

    private record CellData(Image background, Integer rotation, Color overlay) {}

    private static final CachedImageLoader cachedImageLoader = new CachedImageLoader();

    /**
     * Method which creates and handles the UI for the game area / board based on the given
     * parameters below.
     *
     * @param boardSize                  the reach (distance from the center of the board to the
     *                                   edge of the board, exclusive) of the board
     * @param observableGameState        observable value of the current game state
     * @param observableRotation         observable value of the rotation of the next tile to place
     * @param observableOccupants        observable value of the set of occupants that should be
     *                                   visible
     * @param observableHighlightedTiles observable value of the set of tiles that should be
     *                                   highlighted, e.g. when hovering over a message. If empty,
     *                                   no tiles are highlighted.
     * @param rotationHandler            an event handler of type Consumer<Rotation>, to be
     *                                   called when the current player wants to rotate the
     *                                   current tile to place.
     * @param positionHandler            an event handler of type Consumer<Pos>, to be called
     *                                   when the current player wants to place down the current
     *                                   tile.
     * @param occupantHandler            an event handler of type Consumer<Occupant>, to be
     *                                   called when the current player selects an occupant.
     * @return the UI of the game area / board.
     */
    public static Node create(
            int boardSize,
            ObservableValue<GameState> observableGameState,
            ObservableValue<Rotation> observableRotation,
            ObservableValue<Set<Occupant>> observableOccupants,
            ObservableValue<Set<Integer>> observableHighlightedTiles,
            Consumer<Rotation> rotationHandler,
            Consumer<Pos> positionHandler,
            Consumer<Occupant> occupantHandler) {

        // empty background
        WritableImage emptyTileImage = new WritableImage(1, 1);
        emptyTileImage
                .getPixelWriter()
                .setColor(0, 0, Color.gray(0.98));

        // Grid Pane
        GridPane boardPane = new GridPane();
        boardPane.setId("board-grid");

        // "Fringe" - while a tile is being placed, the set of insertion positions otherwise empty
        ObservableValue<Set<Pos>> fringe = observableGameState.map(gs -> {
            if (gs.nextAction().equals(GameState.Action.PLACE_TILE))
                return gs.board().insertionPositions();

            return Collections.emptySet();
        });

        // Two for loops to create each Group
        for (int x = -boardSize; x <= boardSize; x++) {
            for (int y = -boardSize; y <= boardSize; y++) {

                // ImageView for the Face of the tile
                ObservableValue<ImageView> tileFace = new SimpleObjectProperty<>(new ImageView());

                Pos currentPos = new Pos(x, y);
                Group boardSquare = new Group(tileFace.getValue());

                ObservableValue<PlacedTile> currentTile = observableGameState
                        .map(gs -> gs.board().tileAt(currentPos));

                ObservableValue<PlacedTile> tileToPlace = Bindings.createObjectBinding(
                        () -> observableGameState.getValue().tileToPlace() != null
                                ? new PlacedTile(
                                    observableGameState.getValue().tileToPlace(),
                                    observableGameState.getValue().currentPlayer(),
                                    observableRotation.getValue(),
                                    currentPos
                                )
                                : null,
                        observableGameState,
                        observableRotation
                );

                ObservableValue<Boolean> isHovered = boardSquare.hoverProperty();

                ObservableValue<CellData> cellData = Bindings.createObjectBinding(
                        () -> {
                            Image image = emptyTileImage;
                            if (currentTile.getValue() != null)
                                image = cachedImageLoader.get(currentTile.getValue().id());

                            else if (isHovered.getValue() && tileToPlace.getValue() != null
                                    && fringe.getValue().contains(currentPos))

                                image = cachedImageLoader.get(tileToPlace.getValue().id());


                            int rotation = 0;
                            if (currentTile.getValue() != null)
                                rotation = currentTile.getValue().rotation().degreesCW();

                            else if (tileToPlace.getValue() != null)
                                rotation = tileToPlace.getValue().rotation().degreesCW();


                            Color color = Color.TRANSPARENT;
                            if (fringe.getValue().contains(currentPos)) {
                                if (tileToPlace.getValue() != null
                                        && isHovered.getValue()
                                        && !observableGameState.getValue().board()
                                        .canAddTile(tileToPlace.getValue()))
                                    color = Color.WHITE;


                                else if (!isHovered.getValue())
                                    color = ColorMap.fillColor(observableGameState
                                            .map(GameState::currentPlayer).getValue());

                            } else if (currentTile.getValue() != null
                                    && !observableHighlightedTiles.getValue().isEmpty()
                                    && !observableHighlightedTiles.getValue()
                                    .contains(currentTile.getValue().id()))
                                color = Color.BLACK;

                            return new CellData(image, rotation, color);
                        },
                        currentTile,
                        observableGameState,
                        tileToPlace,
                        isHovered,
                        observableHighlightedTiles,
                        fringe
                 );

                boardSquare.setOnMouseClicked(e -> {
                    if (fringe.getValue().contains(currentPos))
                        switch (e.getButton()) {
                            case PRIMARY -> positionHandler.accept(currentPos);
                            case SECONDARY -> {
                                if (e.isAltDown())
                                    rotationHandler.accept(Rotation.RIGHT);
                                else
                                    rotationHandler.accept(Rotation.LEFT);
                            }
                        }
                });

                // Listener pointing to the correct tile on the board
                currentTile.addListener((_, _, newTile) -> {
                        if (Objects.nonNull(newTile)) {

                            // construct markers for all cancelled animals
                            newTile.meadowZones().stream()
                                    .map(Zone.Meadow::animals)
                                    .flatMap(Collection::stream)
                                    .forEach(a -> {
                                        ObservableValue<Boolean> isVisible =
                                                observableGameState.map(gs -> gs.board()
                                                        .cancelledAnimals().contains(a));

                                        ImageView cancelMarker = new ImageView();
                                        cancelMarker.setFitWidth(ImageLoader.MARKER_FIT_SIZE);
                                        cancelMarker.setFitHeight(ImageLoader.MARKER_FIT_SIZE);
                                        cancelMarker.getStyleClass().add("marker");
                                        cancelMarker.setId(STR."marker_\{a.id()}");
                                        cancelMarker.visibleProperty().bind(isVisible);

                                        boardSquare.getChildren().add(cancelMarker);
                                    });

                            // construct svg's for all visible occupants
                            newTile.potentialOccupants()
                                .forEach(
                                    o -> {
                                        Node svgPath = switch (o.kind()) {
                                            case HUT -> Icon.newFor(newTile.placer(), HUT);
                                            case PAWN -> Icon.newFor(newTile.placer(), PAWN);
                                        };

                                        ObservableValue<Boolean> isVisible =
                                                observableOccupants.map(s -> s.contains(o));
                                        svgPath.visibleProperty().bind(isVisible);
                                        svgPath.setId(o.kind().equals(HUT)
                                                ? STR."hut_\{o.zoneId()}"
                                                : STR."pawn_\{o.zoneId()}"
                                        );
                                        svgPath.rotateProperty()
                                                .set(newTile.rotation().negated().degreesCW());
                                        svgPath.setOnMouseClicked(_ -> occupantHandler.accept(o));

                                        boardSquare.getChildren().add(svgPath);
                                    }
                                );
                        }
                });

                tileFace.getValue().setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileFace.getValue().setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);

                tileFace.getValue().imageProperty().bind(cellData.map(CellData::background));

                ColorInput overlayColor = new ColorInput();
                overlayColor.paintProperty().bind(cellData.map(CellData::overlay));
                overlayColor.setWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                overlayColor.setHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);

                Blend overlay = new Blend(BlendMode.SRC_OVER);
                overlay.setTopInput(overlayColor);
                overlay.setOpacity(0.5);

                boardSquare.setEffect(overlay);
                boardSquare.rotateProperty().bind(cellData.map(CellData::rotation));

                //      Group
                boardPane.add(boardSquare, x + boardSize, y + boardSize);

            }
        }
        // Scroll Pane
        ScrollPane boardView = new ScrollPane(boardPane);
        boardView.setHvalue(0.5);
        boardView.setVvalue(0.5);
        boardView.setId("board-scroll-pane");
        boardView.getStylesheets().add("board.css");

        return boardView;
    }
}
