package ch.epfl.chacun.gui;


import ch.epfl.chacun.MessageBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        ScrollPane messageBoardRoot = new ScrollPane();
        VBox scrollableMessages = new VBox();

        messageBoardRoot.setId("message-board");
        messageBoardRoot.setStyle("message-board.css");
        messageBoardRoot.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageBoardRoot.setContent(scrollableMessages);
        scrollableMessages.setAlignment(Pos.TOP_LEFT);
        observableMessages.addListener(
                (o, oldMessages, newMessages) -> ())



                newMessages.stream()
                        .filter(f -> !oldMessages.contains(f))
                        .map(MessageBoard.Message::text)
                        .map(m -> new Text(m))
                        .forEach(t -> scrollableMessages.getChildren().add(t));

    }
}
