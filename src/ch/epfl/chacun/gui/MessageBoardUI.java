package ch.epfl.chacun.gui;


import ch.epfl.chacun.MessageBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Set;

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
        messageBoardRoot.setId("message-board");
        messageBoardRoot.setStyle("message-board.css");
        VBox scrollBox = new VBox();

    }
}
