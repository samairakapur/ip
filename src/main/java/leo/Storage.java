package leo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading tasks from, and saving tasks to, the save file on
 * disk (data/leo.txt, relative to the project root).
 */
public class Storage {
    private static final Path FILE_PATH =
            Paths.get("data", "leo.txt");

    /**
     * Loads previously-saved tasks from the save file into the given
     * array, creating an empty save file first if one does not already
     * exist.
     *
     * @param tasks array to load tasks into, starting at index 0
     * @return the number of tasks loaded
     * @throws IOException if the save file cannot be read, or contains
     *     a line that cannot be parsed as a task
     */
    public static int loadTasks(Task[] tasks) throws IOException {
        createDataFileIfMissing();

        List<String> lines = Files.readAllLines(FILE_PATH);
        int itemCount = 0;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            Task task = parseTask(line);
            tasks[itemCount] = task;
            itemCount++;
        }

        return itemCount;
    }

    /**
     * Saves the first {@code itemCount} tasks in the given array to the
     * save file, overwriting its previous contents.
     *
     * @param tasks array containing the tasks to save
     * @param itemCount number of tasks to save, starting at index 0
     * @throws IOException if the save file cannot be written to
     */
    public static void saveTasks(Task[] tasks, int itemCount)
            throws IOException {
        createDataFileIfMissing();

        List<String> lines = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            lines.add(tasks[i].toFileString());
        }

        Files.write(FILE_PATH, lines);
    }

    // Creates the save file (and its parent data/ directory) if either
    // does not already exist, so loadTasks/saveTasks never have to
    // handle a missing file themselves.
    private static void createDataFileIfMissing() throws IOException {
        Path parentDirectory = FILE_PATH.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        if (!Files.exists(FILE_PATH)) {
            Files.createFile(FILE_PATH);
        }
    }

    // Parses one save-file line (e.g. "D | 0 | return book | ...")
    // back into the matching Task subclass.
    private static Task parseTask(String line) throws IOException {
        String[] parts = line.split(" \\| ", -1);

        if (parts.length < 3) {
            throw new IOException("Invalid task data: " + line);
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        Task task;

        switch (type) {
            case "T":
                task = new Todo(parts[2]);
                break;

            case "D":
                if (parts.length < 4) {
                    throw new IOException("Invalid deadline data: " + line);
                }
                task = new Deadline(parts[2], parts[3]);
                break;

            case "E":
                if (parts.length < 5) {
                    throw new IOException("Invalid event data: " + line);
                }
                task = new Event(parts[2], parts[3], parts[4]);
                break;

            default:
                throw new IOException("Unknown task type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}
