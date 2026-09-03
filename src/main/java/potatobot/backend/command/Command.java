package potatobot.backend.command;

import potatobot.backend.CommandResult;
import potatobot.backend.exception.PotatoBotException;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.TaskList;

/**
 * Represents an executable user command.
 */
public abstract class Command {
    /**
     * Creates a command.
     */
    protected Command() {
    }

    /**
     * Executes this command using the application's collaborators.
     *
     * @param tasks   Task list to read or update.
     * @param storage Storage used when persistence is required.
     * @return User-visible result of executing the command.
     * @throws PotatoBotException If the command cannot be completed.
     */
    public abstract CommandResult execute(TaskList tasks, Storage storage)
            throws PotatoBotException;
}
