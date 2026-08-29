package potatobot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import potatobot.exception.PotatoBotException;

public class TaskListTest {
    @Test
    public void constructor_newTaskList_emptyStateCreated() {
        TaskList tasks = new TaskList();

        assertTrue(tasks.isEmpty());
        assertEquals(0, tasks.size());
        assertEquals("Nothing to see here...", tasks.printList());
        assertEquals("", tasks.toFileContents());
    }

    @Test
    public void add_belowCapacity_taskStored() throws PotatoBotException {
        TaskList tasks = new TaskList();
        Task task = new Task("read book");

        tasks.add(task);

        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertSame(task, tasks.get(0));
    }

    @Test
    public void add_atCapacity_exceptionThrown() throws PotatoBotException {
        TaskList tasks = new TaskList();
        for (int i = 0; i < TaskList.MAX_SIZE; i++) {
            tasks.add(new Task("task " + i));
        }

        PotatoBotException exception = assertThrows(
                PotatoBotException.class, () -> tasks.add(new Task("overflow")));

        assertEquals(
                "Your potato sack is full! It can only hold 100 items.",
                exception.getMessage());
        assertEquals(TaskList.MAX_SIZE, tasks.size());
    }

    @Test
    public void markDone_existingTask_completionStatusUpdated() throws PotatoBotException {
        TaskList tasks = new TaskList();
        tasks.add(new Task("read book"));

        tasks.markDone(0);

        assertEquals("X", tasks.get(0).getStatusIcon());
    }

    @Test
    public void markReset_completedTask_incompleteStatusRestored() throws PotatoBotException {
        TaskList tasks = new TaskList();
        tasks.add(new Task("read book"));
        tasks.markDone(0);

        tasks.markReset(0);

        assertEquals(" ", tasks.get(0).getStatusIcon());
    }

    @Test
    public void delete_existingTask_taskRemovedAndReturned() throws PotatoBotException {
        TaskList tasks = new TaskList();
        Task firstTask = new Task("first");
        Task secondTask = new Task("second");
        tasks.add(firstTask);
        tasks.add(secondTask);

        Task deletedTask = tasks.delete(0);

        assertSame(firstTask, deletedTask);
        assertEquals(1, tasks.size());
        assertSame(secondTask, tasks.get(0));
    }

    @Test
    public void printList_multipleTasks_numberedStatusesReturned() throws PotatoBotException {
        TaskList tasks = new TaskList();
        tasks.add(new Task("read book"));
        tasks.add(new Todo("write code"));
        tasks.markDone(1);

        assertEquals(
                "Here are the tasks in your list:\n"
                        + "  1.[ ] read book\n"
                        + "  2.[X] write code (Todo)",
                tasks.printList());
    }

    @Test
    public void find_keywordMatchesDescriptions_filteredTasksReturned() throws PotatoBotException {
        TaskList tasks = new TaskList();
        tasks.add(new Task("read book"));
        Task completedTask = new Task("return book");
        tasks.add(completedTask);
        tasks.add(new Task("write code"));
        tasks.markDone(1);

        assertEquals(
                "Here are the tasks in your list:\n"
                        + "  1.[ ] read book\n"
                        + "  2.[X] return book",
                tasks.find("BOOK"));
    }

    @Test
    public void find_keywordHasNoMatches_emptyListMessageReturned() throws PotatoBotException {
        TaskList tasks = new TaskList();
        tasks.add(new Task("read book"));

        assertEquals("Nothing to see here...", tasks.find("movie"));
    }

    @Test
    public void toFileContents_multipleTasks_storageFormatReturned() throws PotatoBotException {
        TaskList tasks = new TaskList();
        tasks.add(new Task("read book"));
        tasks.add(new Todo("write code"));
        tasks.markDone(1);

        assertEquals(
                "[ ] read book" + System.lineSeparator() + "[X] write code (Todo)",
                tasks.toFileContents());
    }
}
