package potatobot.backend.command;

import potatobot.backend.CommandResult;
import potatobot.backend.exception.PotatoBotException;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.TaskList;

/**
 * Marks a selected task as completed.
 */
public class MarkCommand extends TaskNumberCommand implements UndoableCommand {
    private int markedTaskIndex = -1;

    /**
     * Creates a command that completes the task with the specified displayed
     * number.
     *
     * @param taskNumber Displayed number of the task to complete.
     */
    public MarkCommand(int taskNumber) {
        super(taskNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(TaskList tasks, Storage storage) throws PotatoBotException {
        markedTaskIndex = getTaskIndex(tasks);
        tasks.markDone(markedTaskIndex);
        return CommandResult.reply("Task completed: " + tasks.get(markedTaskIndex) + "\nKeep going!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo(TaskList tasks) {
        assert markedTaskIndex >= 0 : "The mark command must execute before it is undone";
        tasks.markReset(markedTaskIndex);
    }
}
