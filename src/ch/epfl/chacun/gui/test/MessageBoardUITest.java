package ch.epfl.chacun.gui.test;

import ch.epfl.chacun.*;
import ch.epfl.chacun.gui.MessageBoardUI;
import ch.epfl.chacun.gui.PlayersUI;
import ch.epfl.chacun.tile.Tiles;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ch.epfl.chacun.PlayerColor.PURPLE;
import static ch.epfl.chacun.PlayerColor.YELLOW;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
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
        ObservableValue<GameState> observableGameState = new SimpleObjectProperty<>(gameState);
        ObservableValue<List<MessageBoard.Message>> observableMessages = observableGameState.map(g -> g.messageBoard().messages());
        ObjectProperty<Set<Integer>> tileIds = new SimpleObjectProperty<>();

        Node messageBoardUI = MessageBoardUI.create(observableMessages, tileIds );
        BorderPane root = new BorderPane(messageBoardUI);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("MessageBoardUI Test");
        primaryStage.show();
    }
}
