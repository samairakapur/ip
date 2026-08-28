import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * Runs the Leo chatbot and coordinates commands, tasks, storage, and UI.
 */
public class Leo {
    public static void main(String[] args) {
        Ui ui = new Ui();

        Task[] toDO = new Task[100];
        int itemCount;

        try {
            itemCount = Storage.loadTasks(toDO);
        } catch (IOException e) {
            ui.showMessage(
                    "I couldn't load your saved tasks: " + e.getMessage()
            );
            itemCount = 0;
        }

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            try {
                if (input.equals("bye")) {
                    ui.showGoodbye();
                    break;

                } else if (input.equals("list")) {
                    ui.showMessage(
                            "Here are the things I've saved "
                                    + "in your to-do list so far:"
                    );

                    for (int i = 0; i < itemCount; i++) {
                        ui.showMessage((i + 1) + ". " + toDO[i]);
                    }

                } else if (input.equals("mark")) {
                    throw new LeoException(
                            "Please specify which task to mark, "
                                    + "for example: mark 2"
                    );

                } else if (input.startsWith("mark ")) {
                    int taskNumber = parseTaskNumber(
                            input.substring(5),
                            "mark"
                    );
                    int taskIndex = taskNumber - 1;

                    if (taskIndex < 0 || taskIndex >= itemCount) {
                        throw new LeoException(
                                "That task number does not exist."
                        );
                    }

                    toDO[taskIndex].markAsDone();
                    Storage.saveTasks(toDO, itemCount);

                    ui.showMessage(
                            "Nice! I've marked this task as done:"
                    );
                    ui.showMessage(toDO[taskIndex].toString());

                } else if (input.equals("unmark")) {
                    throw new LeoException(
                            "Please specify which task to unmark, "
                                    + "for example: unmark 2"
                    );

                } else if (input.startsWith("unmark ")) {
                    int taskNumber = parseTaskNumber(
                            input.substring(7),
                            "unmark"
                    );
                    int taskIndex = taskNumber - 1;

                    if (taskIndex < 0 || taskIndex >= itemCount) {
                        throw new LeoException(
                                "That task number does not exist."
                        );
                    }

                    toDO[taskIndex].markAsNotDone();
                    Storage.saveTasks(toDO, itemCount);

                    ui.showMessage(
                            "OK, I've marked this task as not done yet:"
                    );
                    ui.showMessage(toDO[taskIndex].toString());

                } else if (input.equals("todo")) {
                    throw new LeoException(
                            "The description of a todo cannot be empty."
                    );

                } else if (input.startsWith("todo ")) {
                    String description = input.substring(5).trim();

                    if (description.isEmpty()) {
                        throw new LeoException(
                                "The description of a todo cannot be empty."
                        );
                    }

                    checkListCapacity(itemCount, toDO.length);

                    toDO[itemCount] = new Todo(description);

                    ui.showMessage("Got it. I've added this task:");
                    ui.showMessage("  " + toDO[itemCount]);

                    itemCount++;
                    Storage.saveTasks(toDO, itemCount);

                    ui.showMessage(
                            "Now you have " + itemCount
                                    + " tasks in the list."
                    );

                } else if (input.equals("deadline")) {
                    throw new LeoException(
                            "Please enter a deadline in this format: "
                                    + "deadline DESCRIPTION /by TIME"
                    );

                } else if (input.startsWith("deadline ")) {
                    String taskInformation = input.substring(9).trim();

                    if (!taskInformation.contains(" /by ")) {
                        throw new LeoException(
                                "Please enter a deadline in this format: "
                                        + "deadline DESCRIPTION /by TIME"
                        );
                    }

                    String[] parts = taskInformation.split(" /by ", 2);
                    String description = parts[0].trim();
                    String by = parts[1].trim();

                    if (description.isEmpty()) {
                        throw new LeoException(
                                "The description of a deadline "
                                        + "cannot be empty."
                        );
                    }

                    if (by.isEmpty()) {
                        throw new LeoException(
                                "Please specify when the task is due "
                                        + "after '/by'."
                        );
                    }

                    checkListCapacity(itemCount, toDO.length);

                    toDO[itemCount] = new Deadline(description, by);

                    ui.showMessage("Got it. I've added this task:");
                    ui.showMessage("  " + toDO[itemCount]);

                    itemCount++;
                    Storage.saveTasks(toDO, itemCount);

                    ui.showMessage(
                            "Now you have " + itemCount
                                    + " tasks in the list."
                    );

                } else if (input.equals("event")) {
                    throw new LeoException(
                            "Please enter an event in this format: "
                                    + "event DESCRIPTION /from START /to END"
                    );

                } else if (input.startsWith("event ")) {
                    String taskInformation = input.substring(6).trim();

                    if (!taskInformation.contains(" /from ")) {
                        throw new LeoException(
                                "Please enter an event in this format: "
                                        + "event DESCRIPTION "
                                        + "/from START /to END"
                        );
                    }

                    String[] fromParts =
                            taskInformation.split(" /from ", 2);

                    String description = fromParts[0].trim();
                    String eventTimes = fromParts[1].trim();

                    if (!eventTimes.contains(" /to ")) {
                        throw new LeoException(
                                "Please enter an event in this format: "
                                        + "event DESCRIPTION "
                                        + "/from START /to END"
                        );
                    }

                    String[] timeParts =
                            eventTimes.split(" /to ", 2);

                    String from = timeParts[0].trim();
                    String to = timeParts[1].trim();

                    if (description.isEmpty()) {
                        throw new LeoException(
                                "The description of an event "
                                        + "cannot be empty."
                        );
                    }

                    if (from.isEmpty()) {
                        throw new LeoException(
                                "Please specify the event's starting time "
                                        + "after '/from'."
                        );
                    }

                    if (to.isEmpty()) {
                        throw new LeoException(
                                "Please specify the event's ending time "
                                        + "after '/to'."
                        );
                    }

                    checkListCapacity(itemCount, toDO.length);

                    toDO[itemCount] =
                            new Event(description, from, to);

                    ui.showMessage("Got it. I've added this task:");
                    ui.showMessage("  " + toDO[itemCount]);

                    itemCount++;
                    Storage.saveTasks(toDO, itemCount);

                    ui.showMessage(
                            "Now you have " + itemCount
                                    + " tasks in the list."
                    );

                } else if (input.equals("delete")) {
                    throw new LeoException(
                            "Please specify which task to delete, "
                                    + "for example: delete 2"
                    );

                } else if (input.startsWith("delete ")) {
                    int taskNumber = parseTaskNumber(
                            input.substring(7),
                            "delete"
                    );
                    int taskIndex = taskNumber - 1;

                    if (taskIndex < 0 || taskIndex >= itemCount) {
                        throw new LeoException(
                                "That task number does not exist."
                        );
                    }

                    Task deletedTask = toDO[taskIndex];

                    for (int i = taskIndex;
                         i < itemCount - 1;
                         i++) {
                        toDO[i] = toDO[i + 1];
                    }

                    toDO[itemCount - 1] = null;
                    itemCount--;

                    Storage.saveTasks(toDO, itemCount);

                    ui.showMessage(
                            "Okay, noted. I've removed this task:"
                    );
                    ui.showMessage("  " + deletedTask);
                    ui.showMessage(
                            "Now you have " + itemCount
                                    + " tasks in the list."
                    );

                } else {
                    throw new LeoException(
                            "Sorry, I don't understand "
                                    + "what you are trying to say."
                    );
                }

            } catch (LeoException e) {
                ui.showMessage(e.getMessage());

            } catch (IOException e) {
                ui.showMessage(
                        "I couldn't update the saved task file: "
                                + e.getMessage()
                );

            } catch (DateTimeParseException e) {
                ui.showMessage(
                        "Please enter dates and times in the format "
                                + "yyyy-MM-dd HHmm."
                );
            }
        }

        ui.close();
    }

    /**
     * Converts the task-number portion of a command into an integer.
     *
     * @param numberText text containing the task number
     * @param command command being processed
     * @return the task number
     * @throws LeoException if the text is not a valid positive number
     */
    public static int parseTaskNumber(
            String numberText, String command
    ) throws LeoException {
        try {
            int taskNumber =
                    Integer.parseInt(numberText.trim());

            if (taskNumber <= 0) {
                throw new LeoException(
                        "The task number must be greater than zero."
                );
            }

            return taskNumber;

        } catch (NumberFormatException e) {
            throw new LeoException(
                    "Please enter a valid task number, for example: "
                            + command + " 1"
            );
        }
    }

    /**
     * Checks whether another task can be added to the task list.
     *
     * @param itemCount current number of tasks
     * @param capacity maximum number of tasks
     * @throws LeoException if the task list is full
     */
    public static void checkListCapacity(
            int itemCount, int capacity
    ) throws LeoException {
        if (itemCount >= capacity) {
            throw new LeoException(
                    "Your task list is full, "
                            + "so I cannot add another task."
            );
        }
    }
}