import java.util.ArrayList;
import java.util.List;

/**
 * Represents the in-memory list of tasks that Leo is tracking. Wraps the
 * task data in an {@link ArrayList} so that other classes can add,
 * remove, and look up tasks without working with a raw array (or an
 * artificial capacity limit) directly.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates a task list pre-populated from an existing array of tasks,
     * e.g. as loaded by {@link Storage}.
     *
     * @param initialTasks array containing the initial tasks
     * @param count number of valid entries at the start of initialTasks
     */
    public TaskList(Task[] initialTasks, int count) {
        this.tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            this.tasks.add(initialTasks[i]);
        }
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index zero-based index of the task
     * @return the task at that index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index zero-based index of the task to remove
     * @return the task that was removed
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks whether the given zero-based index refers to an existing
     * task in the list.
     *
     * @param index index to check
     * @return true if the index is within range, false otherwise
     */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

    /**
     * Returns the tasks as an array, in order, e.g. for saving via
     * {@link Storage}.
     *
     * @return a new array containing the current tasks
     */
    public Task[] toArray() {
        return tasks.toArray(new Task[0]);
    }
}
