package potatobot;

import java.io.IOException;

import potatobot.command.Command;
import potatobot.command.CommandResult;
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

    private final TaskList tasks;
    private final Parser parser;
    private final Storage storage;
    private final String startupErrorMessage;

    /**
     * Creates a PotatoBot with its default parser, storage, task list, and UI.
     */
    public PotatoBot() {
        this(new Parser(), new TaskList(), new Storage(
                System.getenv().getOrDefault(SAVE_FILE_ENVIRONMENT_VARIABLE, SAVE_FILE_NAME),
                SAVE_FILE_NAME));
    }

    /**
     * Creates a PotatoBot using the specified application collaborators.
     * This constructor supports deterministic tests and alternative front ends.
     *
     * @param parser  Parser used to interpret user input.
     * @param tasks   Task list managed by the application.
     * @param storage Storage used to load and save tasks.
     */
    public PotatoBot(Parser parser, TaskList tasks, Storage storage) {
        this.parser = parser;
        this.tasks = tasks;
        this.storage = storage;
        this.startupErrorMessage = loadTasks();
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
        try (Ui ui = new Ui()) {
            ui.showGreeting();
            if (startupErrorMessage != null) {
                ui.showMessage(startupErrorMessage);
            }

            boolean isExit = false;
            while (!isExit) {
                String input = ui.readCommand();
                if (input == null) {
                    break;
                }

                CommandResult result = getResponse(input);
                if (result.isExit()) {
                    ui.showBlankLine();
                }
                ui.showMessage(result.message());
                isExit = result.isExit();
            }
            if (isExit) {
                ui.showFarewell();
            }
        }
    }

    /**
     * Processes one line of user input without performing console input or output.
     *
     * @param input User command to process.
     * @return Result containing the response and whether the application should
     *         exit.
     */
    public CommandResult getResponse(String input) {
        try {
            Command command = parser.parse(input);
            return command.execute(tasks, storage);
        } catch (PotatoBotException exception) {
            return CommandResult.reply(exception.getMessage());
        }
    }

    /**
     * Loads the saved task list when the application is created.
     * A missing save file represents a new user with an empty task list.
     *
     * @return A user-visible error message, or {@code null} when loading succeeds.
     */
    private String loadTasks() {
        try {
            tasks.add(storage.load().toArray(Task[]::new));
            return null;
        } catch (IOException | PotatoBotException exception) {
            return "I couldn't load your saved tasks: " + exception.getMessage();
        }
    }

}
