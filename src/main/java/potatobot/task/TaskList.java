package potatobot.task;

import java.util.ArrayList;
import java.util.List;

import potatobot.exception.PotatoBotException;

/**
 * Stores and manages a bounded list of tasks.
 */
public class TaskList {
    /** Maximum number of tasks that the list can store. */
    public static final int MAX_SIZE = 100;

    private final List<Task> tasks = new ArrayList<>(MAX_SIZE);

    /**
     * Creates an empty task list.
     */
    public TaskList() {
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index Zero-based index of the task.
     * @return Task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return Number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Adds an item to the task list.
     *
     * @param item Task to add.
     * @throws PotatoBotException If the task list is already full.
     */
    public void add(Task item) throws PotatoBotException {
        if (tasks.size() >= MAX_SIZE) {
            throw new PotatoBotException("Your potato sack is full! It can only hold 100 items.");
        }

        tasks.add(item);
    }

    /**
     * Returns {@code true} if this task list contains no tasks.
     *
     * @return {@code true} if this list is empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Marks the task at the specified index as completed.
     *
     * @param index Zero-based index of the task to mark.
     */
    public void markDone(int index) {
        tasks.get(index).markDone();
    }

    /**
     * Marks the task at the specified index as incomplete.
     *
     * @param index Zero-based index of the task to reset.
     */
    public void markReset(int index) {
        tasks.get(index).markReset();
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index Zero-based index of the task to remove.
     * @return Removed task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Formats all tasks as a numbered list for display to the user.
     *
     * @return Formatted task list, or an empty-list message if no tasks exist.
     */
    public String printList() {
        if (isEmpty()) {
            return "Nothing to see here...";
        }

        StringBuilder message = new StringBuilder("Here are the tasks in your potato sack:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            message.append("\n  ")
                    .append(i + 1)
                    .append(".[")
                    .append(task.getStatusIcon())
                    .append("] ")
                    .append(task);
        }
        return message.toString();
    }

    /**
     * Formats tasks whose descriptions contain a keyword as a numbered list.
     *
     * @param keyword Keyword to find in task descriptions.
     * @return Formatted matching tasks, or an empty-list message if none match.
     */
    public String find(String keyword) {
        StringBuilder message = new StringBuilder("Here are the tasks in your potato sack:");
        int matchingTaskNumber = 0;
        for (Task task : tasks) {
            if (task.matchesKeyword(keyword)) {
                matchingTaskNumber++;
                message.append("\n  ")
                        .append(matchingTaskNumber)
                        .append(".[")
                        .append(task.getStatusIcon())
                        .append("] ")
                        .append(task);
            }
        }
        return matchingTaskNumber == 0 ? "Nothing to see here..." : message.toString();
    }

    /**
     * Converts the task list to the text stored in the save file.
     * Each task occupies one line and includes its completion status.
     *
     * @return Task list in its storage format.
     */
    public String toFileContents() {
        StringBuilder fileContents = new StringBuilder();
        for (Task task : tasks) {
            if (!fileContents.isEmpty()) {
                fileContents.append(System.lineSeparator());
            }
            fileContents.append("[")
                    .append(task.getStatusIcon())
                    .append("] ")
                    .append(task);
        }
        return fileContents.toString();
    }
}
