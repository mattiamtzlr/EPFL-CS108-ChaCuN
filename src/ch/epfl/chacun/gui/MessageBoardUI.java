package ch.epfl.chacun.gui;


import ch.epfl.chacun.MessageBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static javafx.application.Platform.runLater;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class MessageBoardUI {
    private MessageBoardUI() {}

    /**
     *
     * @param observableMessages
     * @param tileIdsProperties
     * @return
     */
    public static Node create(ObservableValue<List<MessageBoard.Message>> observableMessages,
                              ObjectProperty<Set<Integer>> tileIdsProperties) {
        ScrollPane messageBoardScrollPane = new ScrollPane();
        VBox scrollableMessages = new VBox();
        List<Text> messages = new ArrayList<>();

        messageBoardScrollPane.setId("message-board");
        messageBoardScrollPane.setStyle("message-board.css");
        messageBoardScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageBoardScrollPane.setContent(scrollableMessages);
        scrollableMessages.setAlignment(Pos.TOP_LEFT);

        observableMessages.addListener(
                (o, oldMessageList, newMessageList) -> {

                    List<MessageBoard.Message> newMessages = newMessageList.stream()
                        .filter(m -> !oldMessageList.contains(m))
                        .toList();

                    for (MessageBoard.Message message : newMessages) {
                        Text newText = new Text(message.text());
                        newText.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
                        newText.setOnMouseEntered(
                                _ -> tileIdsProperties.setValue(message.tileIds()));
                        newText.setOnMouseExited(
                                _ -> tileIdsProperties.setValue(Collections.emptySet()));

                        scrollableMessages.getChildren().add(newText);

                    }
                });


        runLater(() -> messageBoardScrollPane.setVvalue(1));

        return messageBoardScrollPane;
    }
}
