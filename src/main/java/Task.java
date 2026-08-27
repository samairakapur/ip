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

    @Override
    public String toString() {
        return "[" + getType() + "][" + getStatus()
                + "] " + description;
    }
}
