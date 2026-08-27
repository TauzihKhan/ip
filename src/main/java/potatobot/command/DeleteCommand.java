package potatobot.command;

import potatobot.exception.PotatoBotException;
import potatobot.storage.Storage;
import potatobot.task.Task;
import potatobot.task.TaskList;
import potatobot.ui.Ui;

/**
 * Deletes a selected task from the task list.
 */
public class DeleteCommand extends TaskNumberCommand {
    /**
     * Creates a command that deletes the task with the specified displayed number.
     *
     * @param taskNumber displayed number of the task to delete.
     */
    public DeleteCommand(int taskNumber) {
        super(taskNumber);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PotatoBotException {
        int taskIndex = getTaskIndex(tasks);
        Task deletedTask = tasks.delete(taskIndex);
        ui.showMessage("Task deleted: " + deletedTask + "\nKeep it going!");
    }
}
