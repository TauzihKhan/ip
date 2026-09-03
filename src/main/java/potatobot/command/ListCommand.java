package potatobot.command;

import potatobot.storage.Storage;
import potatobot.task.TaskList;

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
