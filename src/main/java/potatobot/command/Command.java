package potatobot.command;

import potatobot.exception.PotatoBotException;
import potatobot.storage.Storage;
import potatobot.task.TaskList;
import potatobot.ui.Ui;

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
     * @param ui      User interface used to report the result.
     * @param storage Storage used when persistence is required.
     * @throws PotatoBotException If the command cannot be completed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws PotatoBotException;

    /**
     * Indicates whether PotatoBot should stop after this command.
     *
     * @return {@code true} if this command exits PotatoBot.
     */
    public boolean isExit() {
        return false;
    }
}
