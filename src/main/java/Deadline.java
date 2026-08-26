/**
 * Represents a task that must be completed by a given time.
 */
public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return super.toString() + " (Deadline, by: " + by + ")";
    }
}
