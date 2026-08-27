/**
 * Represents a parsed user command and any data needed to execute it.
 */
public final class Command {
    private final CommandType type;
    private final int taskNumber;
    private final Task task;

    Command(CommandType type, int taskNumber, Task task) {
        this.type = type;
        this.taskNumber = taskNumber;
        this.task = task;
    }

    public CommandType getType() {
        return type;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public Task getTask() {
        return task;
    }
}
