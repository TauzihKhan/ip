import java.io.IOException;
import java.util.List;

/**
 * Runs PotatoBot's text-based interaction with the user.
 */
public class PotatoBot {
    private static final String SAVE_FILE_NAME = "./data/potatabot.txt";
    private static final String SAVE_FILE_ENVIRONMENT_VARIABLE = "POTATOBOT_SAVE_FILE";
    private final TaskList itemList = new TaskList();
    private final Parser parser = new Parser();
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

            try {
                Command command = parser.parse(input);
                if (!executeCommand(command)) {
                    break;
                }
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

    /**
     * Executes a parsed command.
     *
     * @param command command to execute.
     * @return {@code false} when PotatoBot should stop; {@code true} otherwise.
     * @throws PotatoBotException if the command cannot be applied to the task list.
     */
    private boolean executeCommand(Command command) throws PotatoBotException {
        switch (command.getType()) {
        case BYE -> {
            ui.showBlankLine();
            handleEnd();
            return false;
        }
        case LIST -> ui.showMessage(itemList.printList());
        case MARK -> markItem(command.getTaskNumber());
        case UNMARK -> unmarkItem(command.getTaskNumber());
        case DELETE -> deleteItem(command.getTaskNumber());
        case ADD, TODO, DEADLINE, EVENT -> addToList(command.getTask());
        }
        return true;
    }
}
