package potatobot.command;

import potatobot.exception.PotatoBotException;
import potatobot.storage.Storage;
import potatobot.task.Task;
import potatobot.task.TaskList;
import potatobot.ui.Ui;

/**
 * Adds a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the specified task.
     *
     * @param task Task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PotatoBotException {
        tasks.add(task);
        ui.showMessage("Added: " + task);
    }
}
