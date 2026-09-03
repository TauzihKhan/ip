package potatobot.backend;

import java.io.IOException;

import potatobot.backend.command.Command;
import potatobot.backend.exception.PotatoBotException;
import potatobot.backend.parser.Parser;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.Task;
import potatobot.backend.task.TaskList;

/**
 * Provides PotatoBot's application operations to user interfaces.
 */
public class PotatoBot {
    private static final String SAVE_FILE_NAME = "./data/potatabot.txt";
    private static final String SAVE_FILE_ENVIRONMENT_VARIABLE = "POTATOBOT_SAVE_FILE";

    private final TaskList tasks;
    private final Parser parser;
    private final Storage storage;
    private final String startupErrorMessage;

    /**
     * Creates a PotatoBot with its default parser, storage, and task list.
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
     * Processes one line of user input without performing console input or output.
     *
     * @param input User command to process.
     * @return Result containing the response and whether the application should
     *         exit.
     */
    public CommandResult respondTo(String input) {
        try {
            Command command = parser.parse(input);
            return command.execute(tasks, storage);
        } catch (PotatoBotException exception) {
            return CommandResult.reply(exception.getMessage());
        }
    }

    /**
     * Returns a message describing a startup loading failure, if one occurred.
     *
     * @return Startup error message, or {@code null} when loading succeeded.
     */
    public String getStartupErrorMessage() {
        return startupErrorMessage;
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
