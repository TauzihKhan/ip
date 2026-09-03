package potatobot.frontend.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents one message and its speaker's display picture in the chat.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    /**
     * Creates a dialog box containing the specified message and image.
     *
     * @param text Message displayed in the dialog box.
     * @param image Display picture belonging to the speaker.
     */
    private DialogBox(String text, Image image) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                DialogBox.class.getResource("/view/DialogBox.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the dialog-box layout", exception);
        }

        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /**
     * Creates a dialog aligned for a message sent by the user.
     *
     * @param text User's message.
     * @param image User's display picture.
     * @return Dialog box for the user's message.
     */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /**
     * Creates a dialog aligned for a reply sent by PotatoBot.
     *
     * @param text PotatoBot's reply.
     * @param image PotatoBot's display picture.
     * @return Dialog box for PotatoBot's reply.
     */
    public static DialogBox getBotDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        return dialogBox;
    }

    /**
     * Reverses the child order and alignment to place PotatoBot's image on the left.
     */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("bot-label");
    }
}
