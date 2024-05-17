package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import ch.epfl.chacun.TileDecks;
import ch.epfl.chacun.gui.DecksUI;
import ch.epfl.chacun.Tiles;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DecksUITest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var tilesByKind = Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));

        TileDecks tileDecks = new TileDecks(
                tilesByKind.get(Tile.Kind.START),
                tilesByKind.get(Tile.Kind.NORMAL),
                tilesByKind.get(Tile.Kind.MENHIR)
        );

        ObservableValue<Tile> nextTile =
                new SimpleObjectProperty<>(tileDecks.topTile(Tile.Kind.NORMAL));

        ObservableValue<Integer> normalTilesNum =
                new SimpleObjectProperty<>(tileDecks.normalTiles().size());

        ObservableValue<Integer> menhirTilesNum =
                new SimpleObjectProperty<>(tileDecks.menhirTiles().size());

        SimpleObjectProperty<String> altText = new SimpleObjectProperty<>("");

        Consumer<Occupant> consumer = (_ -> {
            System.out.println("Pawn action cancelled.");
            altText.set("");
        });

        Node decksUI = DecksUI.create(nextTile, normalTilesNum, menhirTilesNum, altText, consumer);
        BorderPane root = new BorderPane(decksUI);

        altText.set("Click to cancel pawn action.");

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("PlayersUI Test");
        primaryStage.show();
    }
}
