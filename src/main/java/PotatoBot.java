import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs PotatoBot's text-based interaction with the user.
 */
public class PotatoBot {
  private static final List<String> itemList = new ArrayList<>();

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
      if (EXIT_COMMAND.equals(input)) {
        System.out.println();
        break;
      }

      if (input.equals("list")) {
        printList();
        continue;
      }

      addToList(input);
      System.out.println("PotatoBot: \n  Added: " + input + "\n");
    }
    printFarewell();
    scanner.close();
  }

  // Print welcome message
  private static void printGreeting() {
    System.out.println(SEPARATOR);
    System.out.print(BANNER);
    System.out.println("Hello! I'm PotatoBot, your trusty spud assistant.");
    System.out.println("What can I dig up for you? (Say \"bye\" if you want me to leave you alone)");
    System.out.println(SEPARATOR + "\n");
  }

  // Print farewell message
  private static void printFarewell() {
    System.out.println(SEPARATOR);
    System.out.println("Bye. I'm rolling back to the potato patch. Hope to see you again soon!");
    System.out.println(SEPARATOR);
  }

  private static void addToList(String addition) {
    itemList.add(addition);
  }

  // Print sack of items
  private static void printList() {
    System.out.println("PotatoBot:");
    if (itemList.isEmpty()) {
      System.out.println("  Your potato sack is empty.\n");
      return;
    }

    System.out.println("  Here are the items in your potato sack:");
    for (int i = 0; i < itemList.size(); i++) {
      System.out.println("    " + (i + 1) + ". " + itemList.get(i));
    }
    System.out.println();
  }
}
