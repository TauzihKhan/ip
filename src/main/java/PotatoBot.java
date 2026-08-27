import java.io.IOException;
import java.util.List;

/**
 * Runs PotatoBot's text-based interaction with the user.
 */
public class PotatoBot {
    private static final String EXIT_COMMAND = "bye";
    private static final String SAVE_FILE_NAME = "./data/potatabot.txt";
    private static final String SAVE_FILE_ENVIRONMENT_VARIABLE = "POTATOBOT_SAVE_FILE";
    private final TaskList itemList = new TaskList();
    private final Storage storage = new Storage(
            System.getenv().getOrDefault(SAVE_FILE_ENVIRONMENT_VARIABLE, SAVE_FILE_NAME));
    private final Ui ui = new Ui();

    public static void main(String[] args) {
        new PotatoBot().run();
    }

    /**
     * Runs the command loop until the user exits or standard input ends.
     */
    public void run() {
        ui.showGreeting();
        handleStart();

        while (true) {
            String input = ui.readCommand();
            if (input == null) {
                break;
            }

            // Special Exit command
            if (EXIT_COMMAND.equals(input)) {
                ui.showBlankLine();
                handleEnd();
                break;
            }

            // For non breaking tasks
            try {
                handleInput(input);
            } catch (PotatoBotException exception) {
                ui.showMessage(exception.getMessage());
            }
        }
        ui.close();
    }

    // Saves the current task list before ending the application.
    private void handleEnd() {
        try {
            storage.save(itemList);
            ui.showMessage("Tasks saved to " + SAVE_FILE_NAME);
        } catch (IOException exception) {
            ui.showMessage("I couldn't save your tasks: " + exception.getMessage());
        }

        ui.showFarewell();
    }

    /**
     * Loads the saved task list when the application starts.
     * A missing save file represents a new user with an empty task list.
     */
    private void handleStart() {
        try {
            List<Task> loadedTasks = storage.load();
            for (Task savedTask : loadedTasks) {
                itemList.add(savedTask);
            }
        } catch (IOException | PotatoBotException exception) {
            ui.showMessage("I couldn't load your saved tasks: " + exception.getMessage());
        }
    }

    private void addToList(Task addition) throws PotatoBotException {
        itemList.add(addition);
        ui.showMessage("Added: " + addition);
    }

    private void markItem(int index) throws PotatoBotException {
        if (index <= 0) {
            throw new PotatoBotException("Do you hear yourself??");
        }

        if (index > itemList.size()) {
            throw new PotatoBotException(
                    "Think again... We only got " + itemList.size() + " items in the list...");
        }
        itemList.markDone(index - 1);
        ui.showMessage("Task completed: " + itemList.get(index - 1) + "\nKeep going!");
    }

    private void unmarkItem(int index) throws PotatoBotException {
        if (index <= 0) {
            throw new PotatoBotException("Do you hear yourself??");
        }

        if (index > itemList.size()) {
            throw new PotatoBotException(
                    "Think again... We only got " + itemList.size() + " items in the list...");
        }

        itemList.markReset(index - 1);
        ui.showMessage("Task Reset: " + itemList.get(index - 1) + "\nKeep going!");
    }

    private void deleteItem(int index) throws PotatoBotException {
        if (index <= 0) {
            throw new PotatoBotException("Do you hear yourself??");
        }

        if (index > itemList.size()) {
            throw new PotatoBotException(
                    "Think again... We only got " + itemList.size() + " items in the list...");
        }

        Task deletedTask = itemList.delete(index - 1);
        ui.showMessage("Task deleted: " + deletedTask + "\nKeep it going!");
    }

    private static int parseItemNumber(String input) throws PotatoBotException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            throw new PotatoBotException("Do you hear yourself?? Task number must be a whole number.");
        }
    }

    private void handleInput(String input) throws PotatoBotException {
        String[] inputSplit = input.split(" ", 2);
        CommandType command = CommandType.parse(inputSplit[0]);

        if (command == CommandType.LIST && inputSplit.length == 1) {
            ui.showMessage(itemList.printList());
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
