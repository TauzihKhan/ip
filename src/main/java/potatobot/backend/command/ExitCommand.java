package potatobot.backend.command;

import java.io.IOException;

import potatobot.backend.CommandResult;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.TaskList;

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
    public CommandResult execute(TaskList tasks, Storage storage) {
        try {
            storage.save(tasks);
            return CommandResult.exit("Tasks saved to " + storage.getDisplayFilePath());
        } catch (IOException exception) {
            return CommandResult.exit("I couldn't save your tasks: " + exception.getMessage());
        }
    }
}
