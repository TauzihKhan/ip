import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads tasks from and saves tasks to PotatoBot's data file.
 */
public class Storage {
    private static final String TODO_SUFFIX = " (Todo)";
    private static final String DEADLINE_MARKER = " (Deadline, by: ";
    private static final String EVENT_MARKER = " (Event, from: ";
    private static final String EVENT_TO_MARKER = " to: ";

    private final Path filePath;
    private final String displayFilePath;

    /**
     * Creates a storage manager for the specified data file.
     *
     * @param filePath path of the data file.
     */
    public Storage(String filePath) {
        this(filePath, filePath);
    }

    /**
     * Creates a storage manager with separate physical and user-visible paths.
     *
     * @param filePath physical path of the data file.
     * @param displayFilePath data-file path shown to the user.
     */
    public Storage(String filePath, String displayFilePath) {
        this.filePath = Path.of(filePath);
        this.displayFilePath = displayFilePath;
    }

    public String getDisplayFilePath() {
        return displayFilePath;
    }

    /**
     * Loads all tasks from the data file.
     * A missing file represents a new user with an empty task list.
     *
     * @return tasks reconstructed from the data file.
     * @throws IOException if the data file cannot be read.
     * @throws PotatoBotException if the stored task data is invalid.
     */
    public List<Task> load() throws IOException, PotatoBotException {
        if (Files.notExists(filePath)) {
            return new ArrayList<>();
        }

        List<Task> savedTasks = new ArrayList<>();
        for (String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)) {
            if (!line.isBlank()) {
                savedTasks.add(parseSavedTask(line));
            }
        }

        if (savedTasks.size() > TaskList.MAX_SIZE) {
            throw new PotatoBotException(
                    "The save file contains more than " + TaskList.MAX_SIZE + " tasks.");
        }
        return savedTasks;
    }

    /**
     * Saves all tasks to the data file, replacing its previous contents.
     *
     * @param tasks tasks to save.
     * @throws IOException if the data file cannot be written.
     */
    public void save(TaskList tasks) throws IOException {
        Path dataDirectory = filePath.getParent();
        if (dataDirectory != null) {
            Files.createDirectories(dataDirectory);
        }

        Files.writeString(
                filePath,
                tasks.toFileContents(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Converts one stored line back into a task.
     *
     * @param line line containing a saved task.
     * @return task represented by the line.
     * @throws PotatoBotException if the line does not use a valid storage format.
     */
    private static Task parseSavedTask(String line) throws PotatoBotException {
        boolean isMarked;
        String taskDetails;
        if (line.startsWith("[X] ")) {
            isMarked = true;
            taskDetails = line.substring(4);
        } else if (line.startsWith("[ ] ")) {
            isMarked = false;
            taskDetails = line.substring(4);
        } else {
            throw new PotatoBotException("Invalid completion status in line: " + line);
        }

        Task task = parseTaskDetails(taskDetails);
        if (isMarked) {
            task.markDone();
        }
        return task;
    }

    /**
     * Reconstructs a task and its subtype-specific details from stored text.
     *
     * @param taskDetails stored task text without its completion status.
     * @return reconstructed task.
     * @throws PotatoBotException if subtype-specific details are incomplete.
     */
    private static Task parseTaskDetails(String taskDetails) throws PotatoBotException {
        if (taskDetails.endsWith(TODO_SUFFIX)) {
            String description = taskDetails.substring(0, taskDetails.length() - TODO_SUFFIX.length());
            return new Todo(description);
        }

        int deadlineMarkerIndex = taskDetails.lastIndexOf(DEADLINE_MARKER);
        if (deadlineMarkerIndex >= 0 && taskDetails.endsWith(")")) {
            String description = taskDetails.substring(0, deadlineMarkerIndex);
            String by = taskDetails.substring(
                    deadlineMarkerIndex + DEADLINE_MARKER.length(), taskDetails.length() - 1);
            return new Deadline(description, by);
        }

        int eventMarkerIndex = taskDetails.lastIndexOf(EVENT_MARKER);
        if (eventMarkerIndex >= 0 && taskDetails.endsWith(")")) {
            String description = taskDetails.substring(0, eventMarkerIndex);
            String eventTimes = taskDetails.substring(
                    eventMarkerIndex + EVENT_MARKER.length(), taskDetails.length() - 1);
            int toMarkerIndex = eventTimes.lastIndexOf(EVENT_TO_MARKER);
            if (toMarkerIndex < 0) {
                throw new PotatoBotException("Invalid event details: " + taskDetails);
            }
            String from = eventTimes.substring(0, toMarkerIndex);
            String to = eventTimes.substring(toMarkerIndex + EVENT_TO_MARKER.length());
            return new Event(description, from, to);
        }

        return new Task(taskDetails);
    }
}
