package potatobot.backend.parser;

import potatobot.backend.command.AddCommand;
import potatobot.backend.command.Command;
import potatobot.backend.command.CommandType;
import potatobot.backend.command.DeleteCommand;
import potatobot.backend.command.ExitCommand;
import potatobot.backend.command.FindCommand;
import potatobot.backend.command.ListCommand;
import potatobot.backend.command.MarkCommand;
import potatobot.backend.command.UnmarkCommand;
import potatobot.backend.exception.PotatoBotException;
import potatobot.backend.task.Deadline;
import potatobot.backend.task.Event;
import potatobot.backend.task.Task;
import potatobot.backend.task.Todo;

/**
 * Converts raw user input into commands that PotatoBot can execute.
 */
public class Parser {
    private static final String INVALID_COMMAND_MESSAGE = "Me no gets?";

    /**
     * Creates a parser for PotatoBot commands.
     */
    public Parser() {
    }

    /**
     * Parses one line of user input.
     *
     * @param input User input to parse.
     * @return Structured command represented by the input.
     * @throws PotatoBotException If the command or its arguments are invalid.
     */
    public Command parse(String input) throws PotatoBotException {
        String[] inputParts = input.split(" ", 2);
        CommandType commandType = CommandType.parse(inputParts[0]);

        if (inputParts.length == 1) {
            if (commandType == CommandType.LIST) {
                return new ListCommand();
            }
            if (commandType == CommandType.BYE) {
                return new ExitCommand();
            }
            throw new PotatoBotException(INVALID_COMMAND_MESSAGE);
        }

        String argument = inputParts[1];
        return switch (commandType) {
            case MARK -> new MarkCommand(parseTaskNumber(argument));
            case UNMARK -> new UnmarkCommand(parseTaskNumber(argument));
            case DELETE -> new DeleteCommand(parseTaskNumber(argument));
            case ADD -> new AddCommand(new Task(argument));
            case TODO -> new AddCommand(new Todo(argument));
            case DEADLINE -> new AddCommand(parseDeadline(argument));
            case EVENT -> new AddCommand(parseEvent(argument));
            case LIST, BYE -> throw new PotatoBotException(INVALID_COMMAND_MESSAGE);
            case FIND -> new FindCommand(argument);
        };
    }

    /**
     * Parses a task number used by a mark, unmark, or delete command.
     *
     * @param argument Text expected to contain a whole number.
     * @return Parsed task number.
     * @throws PotatoBotException If the argument is not a whole number.
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
     * @param argument Deadline command argument.
     * @return Deadline represented by the argument.
     * @throws PotatoBotException If the deadline details are invalid.
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
     * @param argument Event command argument.
     * @return Event represented by the argument.
     * @throws PotatoBotException If the event details are invalid.
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
