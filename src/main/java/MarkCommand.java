/**
 * Marks a selected task as completed.
 */
public class MarkCommand extends TaskNumberCommand {
    MarkCommand(int taskNumber) {
        super(taskNumber);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PotatoBotException {
        int taskIndex = getTaskIndex(tasks);
        tasks.markDone(taskIndex);
        ui.showMessage("Task completed: " + tasks.get(taskIndex) + "\nKeep going!");
    }
}
