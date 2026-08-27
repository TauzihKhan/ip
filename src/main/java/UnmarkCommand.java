/**
 * Marks a selected task as incomplete.
 */
public class UnmarkCommand extends TaskNumberCommand {
    UnmarkCommand(int taskNumber) {
        super(taskNumber);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PotatoBotException {
        int taskIndex = getTaskIndex(tasks);
        tasks.markReset(taskIndex);
        ui.showMessage("Task Reset: " + tasks.get(taskIndex) + "\nKeep going!");
    }
}
