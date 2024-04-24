package ch.epfl.chacun.gui;

import ch.epfl.chacun.GameState;
import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMaker;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO DESCRIPTION
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class PlayersUI {
    private PlayersUI() {
    }

    public static Node create(ObservableValue<GameState> observableGameState, TextMaker textMaker) {
        VBox vBox = new VBox();

        Set<PlayerColor> players = PlayerColor.ALL.stream()
                .filter(pc -> textMaker.playerName(pc) != null)
                .collect(Collectors.toSet());

        // Observable current player
        ObservableValue<PlayerColor> observableCurrentPlayer =
                observableGameState.map(GameState::currentPlayer);


        // Observable points map
        ObservableValue<Map<PlayerColor, Integer>> observablePoints =
                observableGameState.map(gs -> gs.messageBoard().points());

        // Construct TextFlow instance for each player
        for (PlayerColor color : players) {
            Circle circle = new Circle();
            circle.setRadius(5);
            circle.setFill(ColorMap.fillColor(color));


            // Observable value containing the text with the points for each player color
            ObservableValue<String> observablePointsText =
                    observablePoints.map(map -> 
                        STR."""
                         \{textMaker.playerName(color)} : \{textMaker.points(map.get(color))}
                        """
                    );

            // JavaFX Node
            Text pointsText = new Text();
            pointsText.textProperty().bind(observablePointsText);

            TextFlow textFlow = new TextFlow(
                    circle,
                    pointsText,
                    /* Huts */
                    new Text("   ") // separator
                    /* Pawns */
            );

            // TODO: generate correct svg paths for huts and pawns
            for (int i = 0; i < Occupant.occupantsCount(Occupant.Kind.HUT); i++) {
                // wtf
            }

        }

        // TODO: return vBox containing everything
        return null;
    }
}
