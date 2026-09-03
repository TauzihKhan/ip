package potatobot.frontend.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Starts PotatoBot's JavaFX frontend.
 */
public class GuiApplication extends Application {
    private static final double MINIMUM_WIDTH = 417;
    private static final double MINIMUM_HEIGHT = 220;

    /**
     * Creates and displays the initial PotatoBot window.
     *
     * @param stage Primary JavaFX stage supplied by the runtime.
     */
    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane(new Label("PotatoBot GUI"));
        Scene scene = new Scene(root, 400, 600);

        stage.setTitle("PotatoBot");
        stage.setMinWidth(MINIMUM_WIDTH);
        stage.setMinHeight(MINIMUM_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
