package potatobot.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import potatobot.backend.parser.Parser;
import potatobot.backend.storage.Storage;
import potatobot.backend.task.TaskList;

public class PotatoBotTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void respondTo_addThenList_stateRetainedAcrossCommands() {
        PotatoBot potatoBot = createPotatoBot(temporaryDirectory.resolve("tasks.txt"));

        CommandResult addResult = potatoBot.respondTo("todo read book");
        CommandResult listResult = potatoBot.respondTo("list");

        assertEquals("Added: read book (Todo)", addResult.message());
        assertFalse(addResult.isExit());
        assertTrue(listResult.message().contains("1.[ ] read book (Todo)"));
        assertFalse(listResult.isExit());
    }

    @Test
    public void respondTo_invalidCommand_errorResultReturned() {
        PotatoBot potatoBot = createPotatoBot(temporaryDirectory.resolve("tasks.txt"));

        CommandResult result = potatoBot.respondTo("dance");

        assertEquals("Me no gets?", result.message());
        assertFalse(result.isExit());
    }

    @Test
    public void respondTo_undoWithNoHistory_emptyHistoryMessageReturned() {
        PotatoBot potatoBot = createPotatoBot(temporaryDirectory.resolve("tasks.txt"));

        CommandResult result = potatoBot.respondTo("undo");

        assertEquals("PotatoBot can't travel THAT far back in time sorry :(", result.message());
        assertFalse(result.isExit());
    }

    @Test
    public void respondTo_undoAdd_additionReversed() {
        PotatoBot potatoBot = createPotatoBot(temporaryDirectory.resolve("tasks.txt"));
        potatoBot.respondTo("todo read book");

        CommandResult result = potatoBot.respondTo("undo");

        assertEquals(
                "PototaBot has travelled back in time: todo read book undone",
                result.message());
        assertEquals("Nothing to see here...", potatoBot.respondTo("list").message());
    }

    @Test
    public void respondTo_undoMarkAndUnmark_statusChangesReversedInOrder() {
        PotatoBot potatoBot = createPotatoBot(temporaryDirectory.resolve("tasks.txt"));
        potatoBot.respondTo("todo read book");
        potatoBot.respondTo("mark 1");
        potatoBot.respondTo("unmark 1");

        assertEquals(
                "PototaBot has travelled back in time: unmark 1 undone",
                potatoBot.respondTo("undo").message());
        assertTrue(potatoBot.respondTo("list").message().contains("1.[X] read book (Todo)"));

        assertEquals(
                "PototaBot has travelled back in time: mark 1 undone",
                potatoBot.respondTo("undo").message());
        assertTrue(potatoBot.respondTo("list").message().contains("1.[ ] read book (Todo)"));
    }

    @Test
    public void respondTo_undoDelete_deletedTaskRestoredAtOriginalPosition() {
        PotatoBot potatoBot = createPotatoBot(temporaryDirectory.resolve("tasks.txt"));
        potatoBot.respondTo("add first");
        potatoBot.respondTo("add second");
        potatoBot.respondTo("delete 1");

        CommandResult result = potatoBot.respondTo("undo");

        assertEquals(
                "PototaBot has travelled back in time: delete 1 undone",
                result.message());
        assertEquals(
                "Here are the tasks in your potato sack:\n"
                        + "  1.[ ] first\n"
                        + "  2.[ ] second",
                potatoBot.respondTo("list").message());
    }

    @Test
    public void respondTo_sixAdditionsOnlyFiveMostRecentCommandsCanBeUndone() {
        PotatoBot potatoBot = createPotatoBot(temporaryDirectory.resolve("tasks.txt"));
        for (int taskNumber = 1; taskNumber <= 6; taskNumber++) {
            potatoBot.respondTo("add task " + taskNumber);
        }

        for (int undoCount = 0; undoCount < 5; undoCount++) {
            potatoBot.respondTo("undo");
        }

        assertEquals(
                "PotatoBot can't travel THAT far back in time sorry :(",
                potatoBot.respondTo("undo").message());
        assertEquals(
                "Here are the tasks in your potato sack:\n  1.[ ] task 1",
                potatoBot.respondTo("list").message());
    }

    @Test
    public void respondTo_nonMutatingAndFailedCommands_lastMutationRemainsUndoable() {
        PotatoBot potatoBot = createPotatoBot(temporaryDirectory.resolve("tasks.txt"));
        potatoBot.respondTo("add read book");
        potatoBot.respondTo("list");
        potatoBot.respondTo("delete 2");

        CommandResult result = potatoBot.respondTo("undo");

        assertEquals(
                "PototaBot has travelled back in time: add read book undone",
                result.message());
        assertEquals("Nothing to see here...", potatoBot.respondTo("list").message());
    }

    @Test
    public void constructor_existingSaveFile_tasksAvailableImmediately() throws IOException {
        Path saveFile = temporaryDirectory.resolve("tasks.txt");
        Files.writeString(saveFile, "[X] read book (Todo)");

        PotatoBot potatoBot = createPotatoBot(saveFile);

        assertTrue(potatoBot.respondTo("list").message().contains("1.[X] read book (Todo)"));
    }

    @Test
    public void respondTo_bye_tasksSavedAndExitResultReturned() throws IOException {
        Path saveFile = temporaryDirectory.resolve("nested/tasks.txt");
        PotatoBot potatoBot = createPotatoBot(saveFile);
        potatoBot.respondTo("todo read book");

        CommandResult result = potatoBot.respondTo("bye");

        assertEquals("Bye. I'm rolling back to the potato patch.", result.message());
        assertTrue(result.isExit());
        assertEquals("[ ] read book (Todo)", Files.readString(saveFile));
    }

    @Test
    public void shutdown_withoutBye_tasksSaved() throws IOException {
        Path saveFile = temporaryDirectory.resolve("tasks.txt");
        PotatoBot potatoBot = createPotatoBot(saveFile);
        potatoBot.respondTo("todo read book");

        potatoBot.shutdown();

        assertEquals("[ ] read book (Todo)", Files.readString(saveFile));
    }

    @Test
    public void shutdown_afterBye_tasksSavedOnlyOnce() throws IOException {
        Path saveFile = temporaryDirectory.resolve("tasks.txt");
        PotatoBot potatoBot = createPotatoBot(saveFile);
        potatoBot.respondTo("todo read book");
        potatoBot.respondTo("bye");
        Files.writeString(saveFile, "external change");

        potatoBot.shutdown();

        assertEquals("external change", Files.readString(saveFile));
    }

    @Test
    public void shutdown_firstSaveFails_secondCallRetries() throws IOException {
        Path saveFile = temporaryDirectory.resolve("tasks.txt");
        Files.createDirectory(saveFile);
        PotatoBot potatoBot = createPotatoBot(saveFile);
        potatoBot.respondTo("todo read book");

        potatoBot.shutdown();
        Files.delete(saveFile);
        potatoBot.shutdown();

        assertEquals("[ ] read book (Todo)", Files.readString(saveFile));
    }

    /**
     * Creates a PotatoBot whose persistence is isolated to the current test.
     *
     * @param saveFile Save-file location for the test.
     * @return PotatoBot backed by the specified temporary file.
     */
    private static PotatoBot createPotatoBot(Path saveFile) {
        return new PotatoBot(
                new Parser(),
                new TaskList(),
                new Storage(saveFile.toString()));
    }
}
