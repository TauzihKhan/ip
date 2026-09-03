package potatobot.frontend.gui;

import java.net.URL;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import potatobot.backend.CommandResult;
import potatobot.backend.PotatoBot;

/**
 * Controls the main PotatoBot chat window.
 */
public class MainWindow {
    private static final Duration EXIT_DELAY = Duration.seconds(1);

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

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

        String startupErrorMessage = potatoBot.getStartupErrorMessage();
        if (startupErrorMessage != null) {
            dialogContainer.getChildren().add(
                    DialogBox.getBotDialog(startupErrorMessage, potatoBotImage));
        }
    }

    /**
     * Sends the user's input to PotatoBot and displays both sides of the exchange.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        CommandResult result = potatoBot.respondTo(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBotDialog(result.message(), potatoBotImage));
        userInput.clear();

        if (result.isExit()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            closeAfterDelay();
        } else {
            userInput.requestFocus();
        }
    }

    /**
     * Gives JavaFX time to display the final response before closing the application.
     */
    private static void closeAfterDelay() {
        PauseTransition pause = new PauseTransition(EXIT_DELAY);
        pause.setOnFinished(event -> Platform.exit());
        pause.play();
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
