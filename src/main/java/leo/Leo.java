package leo;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Entry point for the Leo chatbot. Loads any previously-saved tasks,
 * then repeatedly reads a command from the user, executes it against
 * the in-memory task list, and saves the result back to disk.
 */
public class Leo {
    public static void main(String[] args) {
        Ui ui = new Ui();

        Task[] loadedTasks = new Task[100];
        int loadedCount;

        try {
            loadedCount = Storage.loadTasks(loadedTasks);
        } catch (IOException e) {
            ui.showMessage(
                    "I couldn't load your saved tasks: " + e.getMessage()
            );
            loadedCount = 0;
        }

        TaskList tasks = new TaskList(loadedTasks, loadedCount);

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            String commandWord = Parser.getCommandWord(input);
            String arguments = Parser.getArguments(input);

            try {
                if (commandWord.equals("bye")) {
                    ui.showGoodbye();
                    break;

                } else if (commandWord.equals("list")) {
                    ui.showMessage(
                            "Here are the things I've saved in your to-do list so far:"
                    );

                    for (int i = 0; i < tasks.size(); i++) {
                        ui.showMessage((i + 1) + ". " + tasks.get(i));
                    }

                } else if (commandWord.equals("mark")) {
                    if (arguments.isEmpty()) {
                        throw new LeoException(
                                "Please specify which task to mark, for example: mark 2"
                        );
                    }

                    int taskIndex = parseTaskNumber(arguments, "mark") - 1;

                    if (!tasks.isValidIndex(taskIndex)) {
                        throw new LeoException(
                                "That task number does not exist."
                        );
                    }

                    tasks.get(taskIndex).markAsDone();
                    Storage.saveTasks(tasks.toArray(), tasks.size());

                    ui.showMessage("Nice! I've marked this task as done:");
                    ui.showMessage(tasks.get(taskIndex).toString());

                } else if (commandWord.equals("unmark")) {
                    if (arguments.isEmpty()) {
                        throw new LeoException(
                                "Please specify which task to unmark, for example: unmark 2"
                        );
                    }

                    int taskIndex = parseTaskNumber(arguments, "unmark") - 1;

                    if (!tasks.isValidIndex(taskIndex)) {
                        throw new LeoException(
                                "That task number does not exist."
                        );
                    }

                    tasks.get(taskIndex).markAsNotDone();
                    Storage.saveTasks(tasks.toArray(), tasks.size());

                    ui.showMessage("OK, I've marked this task as not done yet:");
                    ui.showMessage(tasks.get(taskIndex).toString());

                } else if (commandWord.equals("todo")) {
                    if (arguments.isEmpty()) {
                        throw new LeoException(
                                "The description of a todo cannot be empty."
                        );
                    }

                    tasks.add(new Todo(arguments));

                    ui.showMessage("Got it. I've added this task:");
                    ui.showMessage("  " + tasks.get(tasks.size() - 1));

                    Storage.saveTasks(tasks.toArray(), tasks.size());
                    ui.showMessage(
                            "Now you have " + tasks.size() + " tasks in the list."
                    );

                } else if (commandWord.equals("deadline")) {
                    if (arguments.isEmpty() || !arguments.contains(" /by ")) {
                        throw new LeoException(
                                "Please enter a deadline in this format: deadline DESCRIPTION /by TIME"
                        );
                    }

                    String[] parts = arguments.split(" /by ", 2);
                    String description = parts[0].trim();
                    String by = parts[1].trim();

                    if (description.isEmpty()) {
                        throw new LeoException(
                                "The description of a deadline cannot be empty."
                        );
                    }

                    if (by.isEmpty()) {
                        throw new LeoException(
                                "Please specify when the task is due after '/by'."
                        );
                    }

                    tasks.add(new Deadline(description, by));

                    ui.showMessage("Got it. I've added this task:");
                    ui.showMessage("  " + tasks.get(tasks.size() - 1));

                    Storage.saveTasks(tasks.toArray(), tasks.size());
                    ui.showMessage(
                            "Now you have " + tasks.size() + " tasks in the list."
                    );

                } else if (commandWord.equals("event")) {
                    if (arguments.isEmpty() || !arguments.contains(" /from ")) {
                        throw new LeoException(
                                "Please enter an event in this format: event DESCRIPTION /from START /to END"
                        );
                    }

                    String[] fromParts = arguments.split(" /from ", 2);
                    String description = fromParts[0].trim();
                    String eventTimes = fromParts[1].trim();

                    if (!eventTimes.contains(" /to ")) {
                        throw new LeoException(
                                "Please enter an event in this format: event DESCRIPTION /from START /to END"
                        );
                    }

                    String[] timeParts = eventTimes.split(" /to ", 2);
                    String from = timeParts[0].trim();
                    String to = timeParts[1].trim();

                    if (description.isEmpty()) {
                        throw new LeoException(
                                "The description of an event cannot be empty."
                        );
                    }

                    if (from.isEmpty()) {
                        throw new LeoException(
                                "Please specify the event's starting time after '/from'."
                        );
                    }

                    if (to.isEmpty()) {
                        throw new LeoException(
                                "Please specify the event's ending time after '/to'."
                        );
                    }

                    tasks.add(new Event(description, from, to));

                    ui.showMessage("Got it. I've added this task:");
                    ui.showMessage("  " + tasks.get(tasks.size() - 1));

                    Storage.saveTasks(tasks.toArray(), tasks.size());
                    ui.showMessage(
                            "Now you have " + tasks.size() + " tasks in the list."
                    );

                } else if (commandWord.equals("delete")) {
                    if (arguments.isEmpty()) {
                        throw new LeoException(
                                "Please specify which task to delete, for example: delete 2"
                        );
                    }

                    int taskIndex = parseTaskNumber(arguments, "delete") - 1;

                    if (!tasks.isValidIndex(taskIndex)) {
                        throw new LeoException(
                                "That task number does not exist."
                        );
                    }

                    Task deletedTask = tasks.remove(taskIndex);
                    Storage.saveTasks(tasks.toArray(), tasks.size());

                    ui.showMessage("Okay, noted. I've removed this task:");
                    ui.showMessage("  " + deletedTask);
                    ui.showMessage(
                            "Now you have " + tasks.size() + " tasks in the list."
                    );

                } else if (commandWord.equals("find")) {
                    if (arguments.isEmpty()) {
                        throw new LeoException(
                                "Please specify a keyword to search for, for example: find book"
                        );
                    }

                    List<Task> matches = tasks.find(arguments);

                    if (matches.isEmpty()) {
                        ui.showMessage(
                                "I couldn't find any matching tasks in your list."
                        );
                    } else {
                        ui.showMessage("Here are the matching tasks in your list:");

                        for (int i = 0; i < matches.size(); i++) {
                            ui.showMessage((i + 1) + ". " + matches.get(i));
                        }
                    }
                } else {
                    throw new LeoException(
                            "Sorry, I don't understand what you are trying to say."
                    );
                }

            } catch (LeoException e) {
                ui.showMessage(e.getMessage());
            } catch (IOException e) {
                ui.showMessage(
                        "I couldn't update the saved task file: " + e.getMessage()
                );
            } catch (DateTimeParseException e) {
                ui.showMessage(
                        "Please enter dates and times in the format yyyy-MM-dd HHmm."
                );
            }
        }

        ui.close();
    }

    /**
     * Converts a task-number argument (e.g. the "2" in "mark 2") into
     * a positive integer.
     *
     * @param numberText the raw argument text to parse
     * @param command name of the command this argument belongs to,
     *     used only to build a helpful error message
     * @return the parsed task number
     * @throws LeoException if numberText is not a valid positive
     *     integer
     */
    public static int parseTaskNumber(String numberText, String command)
            throws LeoException {
        try {
            int taskNumber = Integer.parseInt(numberText.trim());

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
}