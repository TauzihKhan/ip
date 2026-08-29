package potatobot.task;

import java.util.ArrayList;
import java.util.List;

import potatobot.exception.PotatoBotException;

/**
 * Stores and manages a bounded list of tasks.
 */
public class TaskList {
    public static final int MAX_SIZE = 100;

    private final List<Task> itemList;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        itemList = new ArrayList<>(MAX_SIZE);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index zero-based index of the task.
     * @return task at the specified index.
     */
    public Task get(int index) {
        return itemList.get(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return number of stored tasks.
     */
    public int size() {
        return itemList.size();
    }

    /**
     * Adds an item to the task list.
     *
     * @param item task to add.
     * @throws PotatoBotException if the task list is already full.
     */
    public void add(Task item) throws PotatoBotException {
        if (itemList.size() >= MAX_SIZE) {
            throw new PotatoBotException("Your potato sack is full! It can only hold 100 items.");
        }

        itemList.add(item);
    }

    /**
     * Indicates whether this task list contains no tasks.
     *
     * @return {@code true} if this list is empty.
     */
    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    /**
     * Marks the task at the specified index as completed.
     *
     * @param index zero-based index of the task to mark.
     */
    public void markDone(int index) {
        itemList.get(index).markDone();
    }

    /**
     * Marks the task at the specified index as incomplete.
     *
     * @param index zero-based index of the task to reset.
     */
    public void markReset(int index) {
        itemList.get(index).markReset();
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index zero-based index of the task to remove.
     * @return removed task.
     */
    public Task delete(int index) {
        return itemList.remove(index);
    }

    /**
     * Formats all tasks as a numbered list for display to the user.
     *
     * @return formatted task list, or an empty-list message if no tasks exist.
     */
    public String printList() {
        if (isEmpty()) {
            return "Nothing to see here...";
        }

        String message = "Here are the tasks in your list:";
        for (int i = 0; i < itemList.size(); i++) {
            message += "\n  " + (i + 1) + ".[" + itemList.get(i).getStatusIcon() + "] " + itemList.get(i);
        }
        return message;
    }

    /**
     * Converts the task list to the text stored in the save file.
     * Each task occupies one line and includes its completion status.
     *
     * @return the task list in its storage format.
     */
    public String toFileContents() {
        StringBuilder fileContents = new StringBuilder();
        for (Task item : itemList) {
            if (!fileContents.isEmpty()) {
                fileContents.append(System.lineSeparator());
            }
            fileContents.append("[")
                    .append(item.getStatusIcon())
                    .append("] ")
                    .append(item);
        }
        return fileContents.toString();
    }
}
