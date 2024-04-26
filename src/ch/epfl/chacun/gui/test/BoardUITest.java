package ch.epfl.chacun.gui.test;

import ch.epfl.chacun.*;
import ch.epfl.chacun.gui.*;
import ch.epfl.chacun.tile.Tiles;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ch.epfl.chacun.PlayerColor.*;

public final class BoardUITest extends Application {
    public static void main(String[] args) {launch(args);}

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
        Occupant pawnR24 = new Occupant(Occupant.Kind.PAWN, 21);

        var tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        var visibleOccupantsP =
                new SimpleObjectProperty<>(Set.of(pawnR24));
        var highlightedTilesP =
                new SimpleObjectProperty<>(Set.<Integer>of());

        var gameStateO = new SimpleObjectProperty<>(gameState);
        var boardNode = BoardUI
                .create(3,
                        gameStateO,
                        tileToPlaceRotationP,
                        visibleOccupantsP,
                        highlightedTilesP,
                        r -> System.out.println("Rotate: " + r),
                        t -> System.out.println("Place: " + t),
                        o -> System.out.println("Select: " + o));

        PlacedTile t2WestOf56 = new PlacedTile(Tiles.TILES.get(2), YELLOW, Rotation.NONE, new Pos(-1, 0));
        gameStateO.set(gameStateO.get().withStartingTilePlaced());

        var rootNode = new BorderPane(boardNode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
        gameStateO.set(gameStateO.get().withPlacedTile(t2WestOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(pawnR24));
    }
}