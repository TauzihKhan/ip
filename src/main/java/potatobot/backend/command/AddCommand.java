package potatobot.backend.command;

import potatobot.backend.CommandResult;
import potatobot.backend.exception.PotatoBotException;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.Task;
import potatobot.backend.task.TaskList;

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
    public CommandResult execute(TaskList tasks, Storage storage) throws PotatoBotException {
        tasks.add(task);
        return CommandResult.reply("Added: " + task);
    }
}
