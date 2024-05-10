package ch.epfl.chacun.gui.test;

import ch.epfl.chacun.gui.ActionsUI;
import ch.epfl.chacun.gui.ImageLoader;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class ActionsUITest extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        List<String> actions = new ArrayList<>(List.of("HE", "OO", "23", "F3", "H4"));

        SimpleObjectProperty<List<String>> observableActions = new SimpleObjectProperty<>(actions);

        Consumer<String> actionInput = s -> {
            List<String> currentActions = new ArrayList<>(observableActions.get());
            currentActions.add(s);
            observableActions.set(currentActions);
            System.out.println(observableActions.get());
        };
        Node actionsUI = ActionsUI.create(
                observableActions,
                actionInput
        );
        BorderPane root = new BorderPane(actionsUI);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("ActionsUI Test");
        primaryStage.show();
    }
}
