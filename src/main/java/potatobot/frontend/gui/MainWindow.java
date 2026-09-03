package potatobot.frontend.gui;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import potatobot.backend.PotatoBot;

/**
 * Controls the main PotatoBot chat window.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    private PotatoBot potatoBot;

    private final Image userImage = loadImage("/userPic.jpg");
    private final Image potatoBotImage = loadImage("/potatobotPic.jpeg");

    /**
     * Keeps the conversation scrolled to its newest content.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the backend instance shared throughout the GUI session.
     *
     * @param potatoBot PotatoBot backend used by this window.
     */
    public void setPotatoBot(PotatoBot potatoBot) {
        this.potatoBot = potatoBot;
    }

    /**
     * Adds sample user and bot dialogs until command handling is connected in checkpoint 5.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBotDialog("PotatoBot heard: " + input, potatoBotImage));
        userInput.clear();
    }

    /**
     * Loads a required image resource and reports its path clearly if it is absent.
     *
     * @param resourcePath Classpath location of the image.
     * @return Loaded JavaFX image.
     */
    private static Image loadImage(String resourcePath) {
        URL imageUrl = MainWindow.class.getResource(resourcePath);
        if (imageUrl == null) {
            throw new IllegalStateException("Missing image resource: " + resourcePath);
        }
        return new Image(imageUrl.toExternalForm());
    }
}
