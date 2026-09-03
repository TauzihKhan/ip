package potatobot.backend.command;

import potatobot.backend.CommandResult;
import potatobot.backend.exception.PotatoBotException;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.TaskList;

/**
 * Marks a selected task as completed.
 */
public class MarkCommand extends TaskNumberCommand {
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
        int taskIndex = getTaskIndex(tasks);
        tasks.markDone(taskIndex);
        return CommandResult.reply("Task completed: " + tasks.get(taskIndex) + "\nKeep going!");
    }
}
