/**
 * Stores up to 100 tasks.
 */
public class TaskList {
  private static final int MAX_SIZE = 100;

  private final Task[] itemList;
  private int size;

  public TaskList() {
    itemList = new Task[MAX_SIZE];
    size = 0;
  }

  public Task get(int index) {
    return itemList[index];
  }

  public int size() {
    return size;
  }

  // Add item
  public boolean add(Task item) {
    if (size >= MAX_SIZE) {
      return false;
    }

    itemList[size] = item;
    size++;
    return true;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void markDone(int index) {
    itemList[index].markDone();
  }

  public void markReset(int index) {
    itemList[index].markReset();
  }

  public String printList() {
    if (isEmpty()) {
      return "";
    }

    String message = "Here are the tasks in your list:";
    for (int i = 0; i < size; i++) {
      message += "\n  " + (i + 1) + ".[" + itemList[i].getStatusIcon() + "] " + itemList[i];
    }
    return message;
  }
}
