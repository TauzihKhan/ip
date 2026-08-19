// Represents a task and whether it has been completed.
public class Task {
  private final String description;
  private boolean isMarked;

  public Task(String description) {
    this.description = description;
    isMarked = false;
  }

  public void markDone() {
    isMarked = true;
  }

  public void markReset() {
    isMarked = false;
  }

  public String getStatusIcon() {
    return isMarked ? "X" : " ";
  }

  @Override
  public String toString() {
    return description;
  }
}
