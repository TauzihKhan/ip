import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

/**
 * Runs PotatoBot's text-based interaction with the user.
 */
public class PotatoBot {
    private static final TaskList itemList = new TaskList();
    private static final String SEPARATOR = "=".repeat(80);
    private static final String EXIT_COMMAND = "bye";
    private static final String SAVE_FILE_NAME = "./data/potatabot.txt";
    private static final Path SAVE_FILE = Path.of(SAVE_FILE_NAME);
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
                handleEnd();
                break;
            }

            // For non breaking tasks
            try {
                handleInput(input);
            } catch (PotatoBotException exception) {
                printMessageBox(exception.getMessage());
            }
        }
        scanner.close();
    }

    // Print welcome message
    private static void printGreeting() {
        System.out.println(SEPARATOR);
        System.out.print(BANNER);
        System.out.println("Hello! I'm PotatoBot, your trusty spud assistant.");
        System.out
                .println("What tasks do you want me to store for you? (Say \"bye\" if you want me to leave you alone)");
        System.out.println(SEPARATOR + "\n");
    }

    // Print farewell message
    private static void printFarewell() {
        System.out.println(SEPARATOR);
        System.out.println("Bye. I'm rolling back to the potato patch. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }

    // Saves the current task list before ending the application.
    private static void handleEnd() {
        try {
            Path dataDirectory = SAVE_FILE.getParent();
            if (Files.notExists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }

            if (Files.notExists(SAVE_FILE)) {
                Files.createFile(SAVE_FILE);
            }

            Files.writeString(
                    SAVE_FILE,
                    itemList.toFileContents(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            printMessageBox("Tasks saved to " + SAVE_FILE_NAME);
        } catch (IOException exception) {
            printMessageBox("I couldn't save your tasks: " + exception.getMessage());
        }

        printFarewell();
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

    private static void deleteItem(int index) throws PotatoBotException {
        if (index <= 0) {
            throw new PotatoBotException("Do you hear yourself??");
        }

        if (index > itemList.size()) {
            throw new PotatoBotException(
                    "Think again... We only got " + itemList.size() + " items in the list...");
        }

        Task deletedTask = itemList.delete(index - 1);
        printMessageBox("Task deleted: " + deletedTask + "\nKeep it going!");
    }

    private static int parseItemNumber(String input) throws PotatoBotException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            throw new PotatoBotException("Do you hear yourself?? Task number must be a whole number.");
        }
    }

    private static void handleInput(String input) throws PotatoBotException {
        String[] inputSplit = input.split(" ", 2);
        CommandType command = CommandType.parse(inputSplit[0]);

        if (command == CommandType.LIST && inputSplit.length == 1) {
            printMessageBox(itemList.printList());
            return;
        }

        if (inputSplit.length < 2) {
            throw new PotatoBotException("Me no gets?");
        }

        else if (command == CommandType.MARK) {
            int itemNumber = parseItemNumber(inputSplit[1]);
            markItem(itemNumber);
            return;
        }

        else if (command == CommandType.UNMARK) {
            int itemNumber = parseItemNumber(inputSplit[1]);
            unmarkItem(itemNumber);
            return;
        }

        else if (command == CommandType.DELETE) {
            int itemNumber = parseItemNumber(inputSplit[1]);
            deleteItem(itemNumber);
            return;
        }

        else if (command == CommandType.TODO) {
            addToList(new Todo(inputSplit[1]));
            return;
        }

        else if (command == CommandType.DEADLINE) {
            String[] deadlineDetails = inputSplit[1].split(" /by ", 2);
            addToList(new Deadline(deadlineDetails[0], deadlineDetails[1]));
            return;
        }

        else if (command == CommandType.EVENT) {
            String[] eventDetails = inputSplit[1].split(" /from ", 2);
            String[] eventTimes = eventDetails[1].split(" /to ", 2);
            addToList(new Event(eventDetails[0], eventTimes[0], eventTimes[1]));
            return;
        }

        else if (command == CommandType.ADD) {
            addToList(new Task(inputSplit[1]));
            return;
        }

        throw new PotatoBotException("Me no gets?");
    }
}
