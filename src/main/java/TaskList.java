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

  /**
   * Adds an item to the fixed-size task array.
   *
   * @throws PotatoBotException if the task array is already full
   */
  public void add(Task item) throws PotatoBotException {
    if (size >= MAX_SIZE) {
      throw new PotatoBotException("Your potato sack is full! It can only hold 100 items.");
    }

    itemList[size] = item;
    size++;
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
