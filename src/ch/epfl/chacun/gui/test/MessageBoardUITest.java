package ch.epfl.chacun.gui.test;

import ch.epfl.chacun.*;
import ch.epfl.chacun.gui.ImageLoader;
import ch.epfl.chacun.gui.MessageBoardUI;
import ch.epfl.chacun.Tiles;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

import static ch.epfl.chacun.PlayerColor.PURPLE;
import static ch.epfl.chacun.PlayerColor.YELLOW;

public class MessageBoardUITest extends Application {
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

        PlacedTile t2WestOf56 = new PlacedTile(Tiles.TILES.get(2), YELLOW, Rotation.NONE, new Pos(-1, 0));
        Occupant pawnR24 = new Occupant(Occupant.Kind.PAWN, 21);


        SimpleObjectProperty<List<MessageBoard.Message>> observableMessages = new SimpleObjectProperty<>(gameState.messageBoard().messages());
        ObjectProperty<Set<Integer>> tileIds = new SimpleObjectProperty<>();

        Node messageBoardUI = MessageBoardUI.create(observableMessages, tileIds);
        BorderPane root = new BorderPane(messageBoardUI);


        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("MessageBoardUI Test");
        primaryStage.setMinWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        primaryStage.setMaxHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
        primaryStage.show();

        List<MessageBoard.Message> newMessageList = gameState
                .withStartingTilePlaced()
                .withPlacedTile(t2WestOf56).withNewOccupant(pawnR24)
                .messageBoard().messages();
        observableMessages.set(newMessageList);


    }
}
