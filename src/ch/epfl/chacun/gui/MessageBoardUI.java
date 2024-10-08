package ch.epfl.chacun.gui;


import ch.epfl.chacun.MessageBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * UI Class that manages the display of messages during a game of ChaCuN
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class MessageBoardUI {
    private MessageBoardUI() {}

    /**
     * Method that creates the node where messages are displayed
     *
     * @param observableMessages the messages to display
     * @param relevantTileIds    a set where the ids of tiles that should be highlighted are added
     *                           in the method.
     * @return The node for the JavaFX tree
     */
    public static Node create(ObservableValue<List<MessageBoard.Message>> observableMessages,
                              ObjectProperty<Set<Integer>> relevantTileIds) {

        VBox scrollableMessages = new VBox();
        ScrollPane messageBoardScrollPane = new ScrollPane(scrollableMessages);

        messageBoardScrollPane.setId("message-board");
        messageBoardScrollPane.getStylesheets().add("message-board.css");
        messageBoardScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollableMessages.setAlignment(Pos.TOP_LEFT);

        observableMessages.addListener(
            (_, oldMessageList, newMessageList) -> {

                List<MessageBoard.Message> newMessages = newMessageList.stream()
                    .filter(m -> !oldMessageList.contains(m))
                    .toList();

                for (MessageBoard.Message message : newMessages) {
                    Text newText = new Text(message.text());
                    newText.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
                    newText.setOnMouseEntered(
                        _ -> relevantTileIds.setValue(message.tileIds()));
                    newText.setOnMouseExited(
                        _ -> relevantTileIds.setValue(Collections.emptySet()));

                    scrollableMessages.getChildren().add(newText);

                    messageBoardScrollPane.layout();
                    messageBoardScrollPane.setVvalue(1);
                }
            });


        return messageBoardScrollPane;
    }
}
