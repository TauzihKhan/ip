package potatobot;

import java.io.IOException;
import java.util.List;

import potatobot.command.Command;
import potatobot.exception.PotatoBotException;
import potatobot.parser.Parser;
import potatobot.storage.Storage;
import potatobot.task.Task;
import potatobot.task.TaskList;
import potatobot.ui.Ui;

/**
 * Runs PotatoBot's text-based interaction with the user.
 */
public class PotatoBot {
    private static final String SAVE_FILE_NAME = "./data/potatabot.txt";
    private static final String SAVE_FILE_ENVIRONMENT_VARIABLE = "POTATOBOT_SAVE_FILE";

    private final TaskList tasks = new TaskList();
    private final Parser parser = new Parser();
    private final Storage storage = new Storage(
            System.getenv().getOrDefault(SAVE_FILE_ENVIRONMENT_VARIABLE, SAVE_FILE_NAME),
            SAVE_FILE_NAME);
    private final Ui ui = new Ui();

    /**
     * Creates a PotatoBot with its default parser, storage, task list, and UI.
     */
    public PotatoBot() {
    }

    /**
     * Starts PotatoBot.
     *
     * @param args Command-line arguments; currently unused.
     */
    public static void main(String[] args) {
        new PotatoBot().run();
    }

    /**
     * Runs the command loop until the user exits or standard input ends.
     */
    public void run() {
        ui.showGreeting();
        handleStart();

        boolean isExit = false;
        while (!isExit) {
            String input = ui.readCommand();
            if (input == null) {
                break;
            }

            try {
                Command command = parser.parse(input);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (PotatoBotException exception) {
                ui.showMessage(exception.getMessage());
            }
        }
        ui.close();
    }

    /**
     * Loads the saved task list when the application starts.
     * A missing save file represents a new user with an empty task list.
     */
    private void handleStart() {
        try {
            List<Task> loadedTasks = storage.load();
            for (Task savedTask : loadedTasks) {
                tasks.add(savedTask);
            }
        } catch (IOException | PotatoBotException exception) {
            ui.showMessage("I couldn't load your saved tasks: " + exception.getMessage());
        }
    }

}
