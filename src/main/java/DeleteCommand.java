/**
 * Deletes a selected task from the task list.
 */
public class DeleteCommand extends TaskNumberCommand {
    DeleteCommand(int taskNumber) {
        super(taskNumber);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PotatoBotException {
        int taskIndex = getTaskIndex(tasks);
        Task deletedTask = tasks.delete(taskIndex);
        ui.showMessage("Task deleted: " + deletedTask + "\nKeep it going!");
    }
}
