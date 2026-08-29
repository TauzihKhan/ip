package potatobot.command;

import java.io.IOException;

import potatobot.storage.Storage;
import potatobot.task.TaskList;
import potatobot.ui.Ui;

/**
 * Saves the task list and exits PotatoBot.
 */
public class ExitCommand extends Command {
    /**
     * Creates a command that saves the task list and exits PotatoBot.
     */
    public ExitCommand() {
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
