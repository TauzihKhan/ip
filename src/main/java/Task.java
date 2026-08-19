/**
 * Stores up to 100 task descriptions and their completion states.
 */
public class Task {
  private static final int MAX_SIZE = 100;

  private final String[] itemList;
  private final boolean[] isMarked;
  private int size;

  public Task() {
    itemList = new String[MAX_SIZE];
    isMarked = new boolean[MAX_SIZE];
    size = 0;
  }

  public String get(int index) {
    return itemList[index];
  }

  public int size() {
    return size;
  }

  // Add item
  public boolean add(String item) {
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
    isMarked[index] = true;
  }

  public void markReset(int index) {
    isMarked[index] = false;
  }

  public String printList() {
    if (isEmpty()) {
      return "";
    }

    String message = "Here are the items in your potato sack:";
    for (int i = 0; i < size; i++) {
      String marked = isMarked[i] ? "X" : " ";
      message += "\n  " + (i + 1) + ".[" + marked + "] " + itemList[i];
    }
    return message;
  }
}
