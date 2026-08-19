/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {
  public Todo(String description) {
    super(description);
  }

  @Override
  public String toString() {
    return super.toString() + " (Todo)";
  }
}
