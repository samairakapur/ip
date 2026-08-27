public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    public String getStatus() {
        return isDone ? "X" : " ";
    }

    public String getType() {
        return "T";
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public String toFileString() {
        return getType() + " | "
                + (isDone ? "1" : "0") + " | "
                + description;
    }

    @Override
    public String toString() {
        return "[" + getType() + "][" + getStatus()
                + "] " + description;
    }
}
