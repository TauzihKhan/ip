package potatobot.frontend.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
     * Echoes submitted text until command handling is connected in checkpoint 5.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        Label message = new Label("You: " + input);
        message.setWrapText(true);
        message.getStyleClass().add("user-message");
        dialogContainer.getChildren().add(message);
        userInput.clear();
    }
}
