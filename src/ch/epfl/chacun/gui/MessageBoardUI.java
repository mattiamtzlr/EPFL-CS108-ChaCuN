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
     * @param observableTileIds
     * @return
     */
    public static Node create(ObservableValue<List<MessageBoard.Message>> observableMessages,
                              ObjectProperty<Set<Integer>> observableTileIds) {
        ScrollPane messageBoardScrollPane = new ScrollPane();
        VBox scrollableMessages = new VBox();
        List<Text> messages = new ArrayList<>();

        messageBoardScrollPane.setId("message-board");
        messageBoardScrollPane.setStyle("message-board.css");
        messageBoardScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageBoardScrollPane.setContent(scrollableMessages);
        scrollableMessages.setAlignment(Pos.TOP_LEFT);

        observableMessages.addListener(
                (o, newValue, oldValue) -> newValue.stream()
                        .filter(m -> !oldValue.contains(m))
                        .map(MessageBoard.Message::text)
                        .map(Text::new)
                        .forEach(messages::add));

        for (Text message : messages) {
            message.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
            scrollableMessages.getChildren().add(message);
        }
        runLater(() -> messageBoardScrollPane.setVvalue(1));



    }
}
