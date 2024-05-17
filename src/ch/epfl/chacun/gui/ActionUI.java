package ch.epfl.chacun.gui;

import ch.epfl.chacun.Base32;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;

/**
 * This class contains the logic for the UI that handles the user inputting actions that
 * a remote player took.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class ActionUI {
    private ActionUI() {
    }

    /**
     * Create method for the actions user interface. This is where a user will enter the
     * encoded actions for players who are not currently present at the same computer.
     *
     * @param observableActions all actions that were taken during the game, in order.
     * @param actionHandler     the consumer that will be called if an action should be executed.
     * @return a node for the javafx scene.
     */
    public static Node create(
            ObservableValue<List<String>> observableActions,
            Consumer<String> actionHandler) {

        // Text representing the last four actions.
        Text actionHistory = new Text();
        actionHistory.textProperty().bind(
                observableActions.map(oA -> {
                    StringJoiner text = new StringJoiner(", ");
                    int size = oA.size();
                    List<String> recentActions = oA.subList(
                            size - ((size > 3) ? 4 : size), size);

                    for (int i = 0; i < recentActions.size(); i++) {
                        int index = oA.size() - recentActions.size() + i;
                        text.add(FMT."%2d\{index}:\{recentActions.get(i)}");
                    }
                    return text.toString();
                }));

        // TextField for user input
        TextField actionInput = new TextField();
        actionInput.setId("action-field");
        actionInput.setTextFormatter(new TextFormatter<>(
                change -> {
                    change.setText(change.getText().chars()
                            .mapToObj(Character::toString)
                            .map(String::toUpperCase)
                            .filter(Base32::isValid)
                            .filter(_ -> actionInput.getText().length() <= 1)
                            .collect(Collectors.joining()));
                    return change;

                }));
        actionInput.setOnAction(_ -> {
            if (!actionInput.getText().isEmpty()) {
                actionHandler.accept(String.valueOf(actionInput.getText()));
                actionInput.setText("");
            }
        });

        actionHistory.setTextAlignment(TextAlignment.CENTER);
        actionInput.setPrefWidth(60);

        HBox actionInputBox = new HBox(actionHistory, actionInput);

        actionInputBox.setAlignment(Pos.CENTER);

        actionInputBox.getStylesheets().add("actions.css");
        actionInputBox.setId("actions");

        return actionInputBox;

    }

}
