package potatobot.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import potatobot.command.AddCommand;
import potatobot.command.DeleteCommand;
import potatobot.command.ExitCommand;
import potatobot.command.FindCommand;
import potatobot.command.ListCommand;
import potatobot.command.MarkCommand;
import potatobot.command.UnmarkCommand;
import potatobot.exception.PotatoBotException;

public class ParserTest {
    private final Parser parser = new Parser();

    @Test
    public void parse_commandsWithoutArguments_correspondingCommandsReturned()
            throws PotatoBotException {
        assertInstanceOf(ListCommand.class, parser.parse("list"));
        assertInstanceOf(ExitCommand.class, parser.parse("bye"));
    }

    @Test
    public void parse_findCommand_findCommandReturned() throws PotatoBotException {
        assertInstanceOf(FindCommand.class, parser.parse("find book"));
    }

    @Test
    public void parse_findCommandWithoutKeyword_exceptionThrown() {
        assertThrows(PotatoBotException.class, () -> parser.parse("find"));
    }

    @Test
    public void parse_numberedCommands_correspondingCommandsReturned()
            throws PotatoBotException {
        assertInstanceOf(MarkCommand.class, parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, parser.parse("unmark 2"));
        assertInstanceOf(DeleteCommand.class, parser.parse("delete 3"));
    }

    @Test
    public void parse_taskCreationCommands_addCommandsReturned()
            throws PotatoBotException {
        assertInstanceOf(AddCommand.class, parser.parse("add read book"));
        assertInstanceOf(AddCommand.class, parser.parse("todo read book"));
        assertInstanceOf(AddCommand.class,
                parser.parse("deadline submit report /by 2026-08-31"));
        assertInstanceOf(AddCommand.class,
                parser.parse("event conference /from 2026-08-31 /to 2026-09-02"));
    }

    @Test
    public void parse_commandMissingRequiredArgument_exceptionThrown() {
        assertThrows(PotatoBotException.class, () -> parser.parse("add"));
        assertThrows(PotatoBotException.class, () -> parser.parse("todo"));
        assertThrows(PotatoBotException.class, () -> parser.parse("mark"));
        assertThrows(PotatoBotException.class, () -> parser.parse("unmark"));
        assertThrows(PotatoBotException.class, () -> parser.parse("delete"));
        assertThrows(PotatoBotException.class, () -> parser.parse("deadline"));
        assertThrows(PotatoBotException.class, () -> parser.parse("event"));
    }

    @Test
    public void parse_numberedCommandWithNonIntegerArgument_exceptionThrown() {
        PotatoBotException exception = assertThrows(
                PotatoBotException.class, () -> parser.parse("mark first"));

        assertEquals(
                "Do you hear yourself?? Task number must be a whole number.",
                exception.getMessage());
    }

    @Test
    public void parse_argumentGivenToArgumentlessCommand_exceptionThrown() {
        assertThrows(PotatoBotException.class, () -> parser.parse("list now"));
        assertThrows(PotatoBotException.class, () -> parser.parse("bye now"));
    }

    @Test
    public void parse_deadlineMissingByDelimiter_exceptionThrown() {
        assertThrows(PotatoBotException.class, () -> parser.parse("deadline submit report 2026-08-31"));
    }

    @Test
    public void parse_eventMissingDateDelimiter_exceptionThrown() {
        assertThrows(PotatoBotException.class, () -> parser.parse("event conference /to 2026-09-02"));
        assertThrows(PotatoBotException.class, () -> parser.parse("event conference /from 2026-08-31"));
    }

    @Test
    public void parse_invalidTaskDate_exceptionThrown() {
        assertThrows(PotatoBotException.class, () -> parser.parse("deadline submit report /by 2026-13-31"));
        assertThrows(PotatoBotException.class, () -> parser.parse(
                "event conference /from invalid /to 2026-09-02"));
    }
}
