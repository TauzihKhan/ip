package potatobot.command;

import potatobot.exception.PotatoBotException;

/**
 * Represents a command supported by PotatoBot.
 */
public enum CommandType {
    ADD("add"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    BYE("bye");

    private final String commandWord;

    /**
     * Creates a command type associated with its user-facing command word.
     *
     * @param commandWord word that identifies the command in user input.
     */
    CommandType(String commandWord) {
        this.commandWord = commandWord;
    }

    /**
     * Converts a command word into its matching command type.
     *
     * @param command command word to convert.
     * @return command type matching the command word.
     * @throws PotatoBotException if the command word is not supported.
     */
    public static CommandType parse(String command) throws PotatoBotException {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.commandWord.equals(command)) {
                return commandType;
            }
        }
        throw new PotatoBotException("Me no gets?");
    }
}
