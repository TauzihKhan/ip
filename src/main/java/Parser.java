/**
 * Converts raw user input into commands that PotatoBot can execute.
 */
public class Parser {
    private static final int NO_TASK_NUMBER = 0;
    private static final String INVALID_COMMAND_MESSAGE = "Me no gets?";

    /**
     * Parses one line of user input.
     *
     * @param input user input to parse.
     * @return structured command represented by the input.
     * @throws PotatoBotException if the command or its arguments are invalid.
     */
    public Command parse(String input) throws PotatoBotException {
        String[] inputParts = input.split(" ", 2);
        CommandType commandType = CommandType.parse(inputParts[0]);

        if (inputParts.length == 1) {
            if (commandType == CommandType.LIST || commandType == CommandType.BYE) {
                return new Command(commandType, NO_TASK_NUMBER, null);
            }
            throw new PotatoBotException(INVALID_COMMAND_MESSAGE);
        }

        String argument = inputParts[1];
        return switch (commandType) {
        case MARK, UNMARK, DELETE -> new Command(commandType, parseTaskNumber(argument), null);
        case ADD -> new Command(commandType, NO_TASK_NUMBER, new Task(argument));
        case TODO -> new Command(commandType, NO_TASK_NUMBER, new Todo(argument));
        case DEADLINE -> new Command(commandType, NO_TASK_NUMBER, parseDeadline(argument));
        case EVENT -> new Command(commandType, NO_TASK_NUMBER, parseEvent(argument));
        case LIST, BYE -> throw new PotatoBotException(INVALID_COMMAND_MESSAGE);
        };
    }

    /**
     * Parses a task number used by a mark, unmark, or delete command.
     *
     * @param argument text expected to contain a whole number.
     * @return parsed task number.
     * @throws PotatoBotException if the argument is not a whole number.
     */
    private static int parseTaskNumber(String argument) throws PotatoBotException {
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException exception) {
            throw new PotatoBotException(
                    "Do you hear yourself?? Task number must be a whole number.");
        }
    }

    /**
     * Parses the description and date of a deadline command.
     *
     * @param argument deadline command argument.
     * @return deadline represented by the argument.
     * @throws PotatoBotException if the deadline details are invalid.
     */
    private static Deadline parseDeadline(String argument) throws PotatoBotException {
        String[] deadlineDetails = argument.split(" /by ", 2);
        if (deadlineDetails.length < 2) {
            throw new PotatoBotException(INVALID_COMMAND_MESSAGE);
        }
        return new Deadline(deadlineDetails[0], deadlineDetails[1]);
    }

    /**
     * Parses the description and dates of an event command.
     *
     * @param argument event command argument.
     * @return event represented by the argument.
     * @throws PotatoBotException if the event details are invalid.
     */
    private static Event parseEvent(String argument) throws PotatoBotException {
        String[] eventDetails = argument.split(" /from ", 2);
        if (eventDetails.length < 2) {
            throw new PotatoBotException(INVALID_COMMAND_MESSAGE);
        }

        String[] eventTimes = eventDetails[1].split(" /to ", 2);
        if (eventTimes.length < 2) {
            throw new PotatoBotException(INVALID_COMMAND_MESSAGE);
        }
        return new Event(eventDetails[0], eventTimes[0], eventTimes[1]);
    }
}
