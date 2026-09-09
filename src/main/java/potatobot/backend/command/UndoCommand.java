package potatobot.backend.command;

import potatobot.backend.CommandResult;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.TaskList;

/**
 * Requests that PotatoBot reverse its most recent state-changing command.
 */
public class UndoCommand extends Command {
    /** Message shown when the current session has no command left to undo. */
    public static final String EMPTY_HISTORY_MESSAGE =
            "PotatoBot can't travel THAT far back in time sorry :(";

    /**
     * Creates a command that requests an undo operation.
     */
    public UndoCommand() {
    }

    /**
     * Returns the empty-history response when no application history is supplied.
     * PotatoBot handles this command directly when session history is available.
     *
     * @param tasks Task list managed by the application.
     * @param storage Storage used by the application.
     * @return Result explaining that no command is available to undo.
     */
    @Override
    public CommandResult execute(TaskList tasks, Storage storage) {
        return CommandResult.reply(EMPTY_HISTORY_MESSAGE);
    }
}
