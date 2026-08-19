import java.util.Scanner;

/**
 * Runs PotatoBot's text-based interaction with the user.
 */
public class PotatoBot {
  private static final TaskList itemList = new TaskList();
  private static final String SEPARATOR = "=".repeat(80);
  private static final String EXIT_COMMAND = "bye";
  private static final String BANNER = """
       ____       _        _        ____        _           _...._
      |  _ \\ ___ | |_ __ _| |_ ___ | __ )  ___ | |_     .-'      '-.
      | |_) / _ \\| __/ _` | __/ _ \\|  _ \\ / _ \\| __|   /  .  .   .  \\
      |  __/ (_) | || (_| | || (_) | |_) | (_) | |_    | .    .     .|
      |_|   \\___/ \\__\\__,_|\\__\\___/|____/ \\___/ \\__|    \\__ .   .    /
                                                           '-.____.-'
      """;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    printGreeting();

    while (true) {
      System.out.print("Me: ");
      if (!scanner.hasNextLine()) {
        break;
      }
      String input = scanner.nextLine();

      // Special Exit command
      if (EXIT_COMMAND.equals(input)) {
        System.out.println();
        break;
      }

      // For non breaking tasks
      try {
        handleInput(input);
      } catch (PotatoBotException exception) {
        printMessageBox(exception.getMessage());
      }
    }
    printFarewell();
    scanner.close();
  }

  // Print welcome message
  private static void printGreeting() {
    System.out.println(SEPARATOR);
    System.out.print(BANNER);
    System.out.println("Hello! I'm PotatoBot, your trusty spud assistant.");
    System.out.println("What tasks do you want me to store for you? (Say \"bye\" if you want me to leave you alone)");
    System.out.println(SEPARATOR + "\n");
  }

  // Print farewell message
  private static void printFarewell() {
    System.out.println(SEPARATOR);
    System.out.println("Bye. I'm rolling back to the potato patch. Hope to see you again soon!");
    System.out.println(SEPARATOR);
  }

  // Prints a message inside PotatoBot's standard reply box.
  private static void printMessageBox(String message) {
    String indentedMessage = message.replace("\n", "\n  ");
    System.out.println("PotatoBot:\n  " + indentedMessage + "\n");
  }

  private static void addToList(Task addition) throws PotatoBotException {
    itemList.add(addition);
    printMessageBox("Added: " + addition);
  }

  private static void markItem(int index) throws PotatoBotException {
    if (index <= 0) {
      throw new PotatoBotException("Do you hear yourself??");
    }

    if (index > itemList.size()) {
      throw new PotatoBotException(
          "Think again... We only got " + itemList.size() + " items in the list...");
    }
    itemList.markDone(index - 1);
    printMessageBox("Task completed: " + itemList.get(index - 1) + "\nKeep going!");
  }

  private static void unmarkItem(int index) throws PotatoBotException {
    if (index <= 0) {
      throw new PotatoBotException("Do you hear yourself??");
    }

    if (index > itemList.size()) {
      throw new PotatoBotException(
          "Think again... We only got " + itemList.size() + " items in the list...");
    }

    itemList.markReset(index - 1);
    printMessageBox("Task Reset: " + itemList.get(index - 1) + "\nKeep going!");
  }

  private static void handleInput(String input) throws PotatoBotException {
    if (input.equals("list")) {
      printMessageBox(itemList.printList());
      return;
    }

    else if (input.split(" ")[0].equals("mark")) {
      int itemNumber = Integer.parseInt(input.split(" ")[1]);
      markItem(itemNumber);
      return;
    }

    else if (input.split(" ")[0].equals("unmark")) {
      int itemNumber = Integer.parseInt(input.split(" ")[1]);
      unmarkItem(itemNumber);
      return;
    }

    else if (input.split(" ")[0].equals("todo")) {
      String description = input.substring("todo ".length());
      addToList(new Todo(description));
      return;
    }

    else if (input.split(" ")[0].equals("deadline")) {
      String[] deadlineDetails = input.substring("deadline ".length()).split(" /by ", 2);
      addToList(new Deadline(deadlineDetails[0], deadlineDetails[1]));
      return;
    }

    else if (input.split(" ")[0].equals("event")) {
      String[] eventDetails = input.substring("event ".length()).split(" /from ", 2);
      String[] eventTimes = eventDetails[1].split(" /to ", 2);
      addToList(new Event(eventDetails[0], eventTimes[0], eventTimes[1]));
      return;
    }
    // Add to list by default
    addToList(new Task(input));
  }
}
