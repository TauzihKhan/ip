package potatobot.backend.command;

import potatobot.backend.exception.PotatoBotException;
import potatobot.backend.task.TaskList;

/**
 * Represents a state-changing command that can reverse its completed action.
 */
public interface UndoableCommand {
    /**
     * Reverses this command by updating the task list directly.
     * This method must only be called after the command executes successfully.
     *
     * @param tasks Task list changed by the original command.
     * @throws PotatoBotException If the original task cannot be restored.
     */
    void undo(TaskList tasks) throws PotatoBotException;
}
