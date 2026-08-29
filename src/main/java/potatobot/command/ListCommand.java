package potatobot.command;

import potatobot.storage.Storage;
import potatobot.task.TaskList;
import potatobot.ui.Ui;

/**
 * Displays every task in the task list.
 */
public class ListCommand extends Command {
    /**
     * Creates a command that displays every task.
     */
    public ListCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage(tasks.printList());
    }
}
