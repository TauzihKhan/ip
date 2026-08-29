package potatobot.task;

/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {
    /**
     * Creates an undated task with the specified description.
     *
     * @param description description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Formats this task with its todo type label.
     *
     * @return task description and todo type label.
     */
    @Override
    public String toString() {
        return super.toString() + " (Todo)";
    }
}
