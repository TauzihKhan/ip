package potatobot.command;

import potatobot.exception.PotatoBotException;
import potatobot.storage.Storage;
import potatobot.task.TaskList;
import potatobot.ui.Ui;

/**
 * Marks a selected task as completed.
 */
public class MarkCommand extends TaskNumberCommand {
    /**
     * Creates a command that completes the task with the specified displayed
     * number.
     *
     * @param taskNumber displayed number of the task to complete.
     */
    public MarkCommand(int taskNumber) {
        super(taskNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PotatoBotException {
        int taskIndex = getTaskIndex(tasks);
        tasks.markDone(taskIndex);
        ui.showMessage("Task completed: " + tasks.get(taskIndex) + "\nKeep going!");
    }
}
