import java.util.ArrayList;
import java.util.List;

// Stores up to 100 tasks.
public class TaskList {
  private static final int MAX_SIZE = 100;

  private final List<Task> itemList;

  public TaskList() {
    itemList = new ArrayList<>(MAX_SIZE);
  }

  public Task get(int index) {
    return itemList.get(index);
  }

  public int size() {
    return itemList.size();
  }

  /**
   * Adds an item to the task list.
   *
   * @throws PotatoBotException if the task array is already full
   */
  public void add(Task item) throws PotatoBotException {
    if (itemList.size() >= MAX_SIZE) {
      throw new PotatoBotException("Your potato sack is full! It can only hold 100 items.");
    }

    itemList.add(item);
  }

  public boolean isEmpty() {
    return itemList.isEmpty();
  }

  public void markDone(int index) {
    itemList.get(index).markDone();
  }

  public void markReset(int index) {
    itemList.get(index).markReset();
  }

  public Task delete(int index) {
    return itemList.remove(index);
  }

  public String printList() {
    if (isEmpty()) {
      return "";
    }

    String message = "Here are the tasks in your list:";
    for (int i = 0; i < itemList.size(); i++) {
      message += "\n  " + (i + 1) + ".[" + itemList.get(i).getStatusIcon() + "] " + itemList.get(i);
    }
    return message;
  }
}
