package potatobot.backend.command;

import potatobot.backend.CommandResult;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.TaskList;

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
    public CommandResult execute(TaskList tasks, Storage storage) {
        return CommandResult.reply(tasks.printList());
    }
}
