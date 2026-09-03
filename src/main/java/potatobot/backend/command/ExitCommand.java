package potatobot.backend.command;

import potatobot.backend.CommandResult;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.TaskList;

/**
 * Signals that PotatoBot should finish the current session.
 */
public class ExitCommand extends Command {
    /**
     * Creates a command that exits PotatoBot.
     */
    public ExitCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(TaskList tasks, Storage storage) {
        return CommandResult.exit("Bye. I'm rolling back to the potato patch.");
    }
}
