package potatobot.backend.command;

import potatobot.backend.CommandResult;
import potatobot.backend.exception.PotatoBotException;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.Task;
import potatobot.backend.task.TaskList;

/**
 * Deletes a selected task from the task list.
 */
public class DeleteCommand extends TaskNumberCommand {
    /**
     * Creates a command that deletes the task with the specified displayed number.
     *
     * @param taskNumber Displayed number of the task to delete.
     */
    public DeleteCommand(int taskNumber) {
        super(taskNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(TaskList tasks, Storage storage) throws PotatoBotException {
        int taskIndex = getTaskIndex(tasks);
        Task deletedTask = tasks.delete(taskIndex);
        return CommandResult.reply("Task deleted: " + deletedTask + "\nKeep it going!");
    }
}
