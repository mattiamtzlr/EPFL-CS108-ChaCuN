package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import ch.epfl.chacun.gui.ImageLoader;
import ch.epfl.chacun.gui.PlayersUI;
import ch.epfl.chacun.Tiles;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.epfl.chacun.PlayerColor.PURPLE;
import static ch.epfl.chacun.PlayerColor.YELLOW;

public class PlayersUITest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map<PlayerColor, String> playerNames = Map.of(
                PURPLE, "Leo",
                YELLOW, "Mattia"
        );

        List<PlayerColor> playerColors = playerNames.keySet().stream()
                .sorted()
                .toList();

        var tilesByKind = Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));

        TileDecks tileDecks = new TileDecks(
                tilesByKind.get(Tile.Kind.START),
                tilesByKind.get(Tile.Kind.NORMAL),
                tilesByKind.get(Tile.Kind.MENHIR)
        );

        TextMaker textMaker = new TextMakerFr(playerNames);

        GameState gameState = GameState.initial(playerColors, tileDecks, textMaker);
        SimpleObjectProperty<GameState> observableGameState = new SimpleObjectProperty<>(gameState);

        Node playersUI = PlayersUI.create(observableGameState, textMaker);
        BorderPane root = new BorderPane(playersUI);

        observableGameState.set(observableGameState.get().withStartingTilePlaced());

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("PlayersUI Test");
        primaryStage.setMinWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        primaryStage.setMinHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
        primaryStage.show();
    }
}
