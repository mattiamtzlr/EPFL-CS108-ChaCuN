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

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that generates the UI for the interface showing the players and their available occupants
 * as well as their points.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class PlayersUI {
    private PlayersUI() {
    }

    /**
     * Creates the players interface using the given game state (observable) and text maker
     * instances.
     *
     * @param observableGameState the observable (!) game state to be used
     * @param textMaker           the text maker to be used
     * @return the root node of the players ui (VBox)
     */
    public static Node create(ObservableValue<GameState> observableGameState, TextMaker textMaker) {
        Set<PlayerColor> players = PlayerColor.ALL.stream()
                .filter(pc -> textMaker.playerName(pc) != null)
                .collect(Collectors.toSet());

        // Observable current player
        ObservableValue<PlayerColor> observableCurrentPlayer =
                observableGameState.map(GameState::currentPlayer);


        // Observable points map
        ObservableValue<Map<PlayerColor, Integer>> observablePoints =
                observableGameState.map(gs -> gs.messageBoard().points());

        // TextFlow array to add to vbox later
        TextFlow[] textFlows = new TextFlow[PlayerColor.ALL.size()];

        // Construct TextFlow instance for each player
        for (PlayerColor color : players) {
            Circle circle = new Circle();
            circle.setRadius(5);
            circle.setFill(ColorMap.fillColor(color));

            // Observable value containing the text with the points for each player color
            ObservableValue<String> observablePointsText =
                    observablePoints.map(map -> {
                        int points = map.getOrDefault(color, 0);

                        return STR."""
                         \{textMaker.playerName(color)} : \{textMaker.points(points)}
                        """;
                    });

            Text pointsText = new Text();
            pointsText.textProperty().bind(observablePointsText);

            TextFlow textFlow = new TextFlow(
                    circle,
                    pointsText
            );

            textFlow.getStyleClass().add("player");

            // generate correct svg paths for huts
            for (int i = 0; i < Occupant.occupantsCount(Occupant.Kind.HUT); i++) {
                Node hutSVG = Icon.newFor(color, Occupant.Kind.HUT);

                // to use in lambda
                int index = i;
                ObservableValue<Double> opacity = observableGameState.map(
                        gs -> index < gs.freeOccupantsCount(color, Occupant.Kind.HUT)
                                ? 1 : 0.1
                );

                hutSVG.opacityProperty().bind(opacity);

                textFlow.getChildren().add(hutSVG);
            }

            textFlow.getChildren().add(new Text("   "));

            // generate correct svg paths for pawns
            for (int i = 0; i < Occupant.occupantsCount(Occupant.Kind.PAWN); i++) {
                Node pawnSVG = Icon.newFor(color, Occupant.Kind.PAWN);

                // to use in lambda
                int index = i;
                ObservableValue<Double> opacity = observableGameState.map(
                        gs -> index < gs.freeOccupantsCount(color, Occupant.Kind.PAWN)
                                ? 1 : 0.1
                );

                pawnSVG.opacityProperty().bind(opacity);

                textFlow.getChildren().add(pawnSVG);
            }

            textFlows[color.ordinal()] = textFlow;
        }

        // update classes on current player
        observableCurrentPlayer.addListener((_, oldColor, newColor) -> {
            if (oldColor != null)
                textFlows[oldColor.ordinal()].getStyleClass().remove("current");
            textFlows[newColor.ordinal()].getStyleClass().add("current");
        });

        // remove null values, as not all players might be present.
        VBox vBox = new VBox(
                Arrays.stream(textFlows)
                        .filter(Objects::nonNull)
                        .toArray(TextFlow[]::new)
        );
        vBox.getStylesheets().add("players.css");
        vBox.setId("players");
        return vBox;
    }
}
