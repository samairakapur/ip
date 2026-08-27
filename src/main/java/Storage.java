import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final Path FILE_PATH =
            Paths.get("data", "leo.txt");

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

    public static void saveTasks(Task[] tasks, int itemCount)
            throws IOException {
        createDataFileIfMissing();

        List<String> lines = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            lines.add(tasks[i].toFileString());
        }

        Files.write(FILE_PATH, lines);
    }

    private static void createDataFileIfMissing() throws IOException {
        Path parentDirectory = FILE_PATH.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        if (!Files.exists(FILE_PATH)) {
            Files.createFile(FILE_PATH);
        }
    }

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