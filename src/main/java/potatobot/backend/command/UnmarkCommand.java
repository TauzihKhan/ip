package potatobot.backend.command;

import potatobot.backend.CommandResult;
import potatobot.backend.exception.PotatoBotException;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.TaskList;

/**
 * Marks a selected task as incomplete.
 */
public class UnmarkCommand extends TaskNumberCommand implements UndoableCommand {
    private int unmarkedTaskIndex = -1;

    /**
     * Creates a command that resets the task with the specified displayed number.
     *
     * @param taskNumber Displayed number of the task to reset.
     */
    public UnmarkCommand(int taskNumber) {
        super(taskNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(TaskList tasks, Storage storage) throws PotatoBotException {
        unmarkedTaskIndex = getTaskIndex(tasks);
        tasks.markReset(unmarkedTaskIndex);
        return CommandResult.reply("Task Reset: " + tasks.get(unmarkedTaskIndex) + "\nKeep going!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo(TaskList tasks) {
        assert unmarkedTaskIndex >= 0 : "The unmark command must execute before it is undone";
        tasks.markDone(unmarkedTaskIndex);
    }
}
