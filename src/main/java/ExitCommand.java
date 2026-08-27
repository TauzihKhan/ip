import java.io.IOException;

/**
 * Saves the task list and exits PotatoBot.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBlankLine();
        try {
            storage.save(tasks);
            ui.showMessage("Tasks saved to " + storage.getDisplayFilePath());
        } catch (IOException exception) {
            ui.showMessage("I couldn't save your tasks: " + exception.getMessage());
        }
        ui.showFarewell();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
