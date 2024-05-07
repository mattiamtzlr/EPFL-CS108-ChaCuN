package ch.epfl.chacun.gui;

import ch.epfl.chacun.Base32;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class ActionsUI {
    private ActionsUI() {}

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
                        text.add(FMT."%2d\{i}:\{recentActions.get(i)}");
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
                            .filter(s -> actionInput.getText().length() <= 1)
                            .collect(Collectors.joining()));
                    return change;

                }));
        actionInput.setOnAction(a -> {
            if (!actionInput.getText().isEmpty()) {
                actionHandler.accept(String.valueOf(actionInput.getText()));
                actionInput.setText("");
            }
        });

        HBox actionInputBox = new HBox(actionHistory, actionInput);
        actionInputBox.setStyle("actions.css");
        actionInputBox.setId("actions");

        return actionInputBox;

    }

}
