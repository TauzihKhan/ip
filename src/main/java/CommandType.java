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
  EVENT("event");

  private final String commandWord;

  CommandType(String commandWord) {
    this.commandWord = commandWord;
  }

  /**
   * Converts a command word into its matching command type.
   *
   * @throws PotatoBotException if the command word is not supported
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
