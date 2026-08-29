package potatobot.command;

import potatobot.exception.PotatoBotException;
import potatobot.storage.Storage;
import potatobot.task.TaskList;
import potatobot.ui.Ui;

/**
 * Marks a selected task as incomplete.
 */
public class UnmarkCommand extends TaskNumberCommand {
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
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PotatoBotException {
        int taskIndex = getTaskIndex(tasks);
        tasks.markReset(taskIndex);
        ui.showMessage("Task Reset: " + tasks.get(taskIndex) + "\nKeep going!");
    }
}
