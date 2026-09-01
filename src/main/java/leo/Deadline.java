package leo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private LocalDateTime by;
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDateTime.parse(by, INPUT_FORMAT);
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String toFileString() {
        return super.toFileString()
                + " | " + by.format(INPUT_FORMAT);
    }

    @Override
    public String toString() {
        return super.toString()
                + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}