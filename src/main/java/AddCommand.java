/**
 * Adds a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PotatoBotException {
        tasks.add(task);
        ui.showMessage("Added: " + task);
    }
}
