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

        assertEquals("Tasks saved to " + saveFile, result.message());
        assertTrue(result.isExit());
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
