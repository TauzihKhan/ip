package potatobot.backend;

/**
 * Represents the user-visible outcome of executing one command.
 *
 * @param message Response to show to the user.
 * @param isExit  Whether the application should stop accepting commands.
 */
public record CommandResult(String message, boolean isExit) {
    /**
     * Creates a non-exit result with the specified message.
     *
     * @param message Response to show to the user.
     * @return Result that keeps the application running.
     */
    public static CommandResult reply(String message) {
        return new CommandResult(message, false);
    }

    /**
     * Creates an exit result with the specified message.
     *
     * @param message Final response to show to the user.
     * @return Result that tells the application to stop.
     */
    public static CommandResult exit(String message) {
        return new CommandResult(message, true);
    }
}
