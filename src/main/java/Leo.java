import java.util.Scanner;
import java.io.IOException;
import java.time.format.DateTimeParseException;

public class Leo {
    public static void main(String[] args) {
        Task[] toDO = new Task[100];
        int itemCount;

        try {
            itemCount = Storage.loadTasks(toDO);
        } catch (IOException e) {
            System.out.println(
                    "I couldn't load your saved tasks: " + e.getMessage()
            );
            itemCount = 0;
        }

        String banner = " _                \n"
                + "| |    ___  ___   \n"
                + "| |   / _ \\/ _ \\  \n"
                + "| |__|  __/ (_) | \n"
                + "|_____\\___|\\___/  \n";

        Scanner scanner = new Scanner(System.in);

        System.out.println(banner);
        greeting();

        while (true) {
            String input = scanner.nextLine().trim();

            try {
                if (input.equals("bye")) {
                    leaving();
                    break;

                } else if (input.equals("list")) {
                    System.out.println(
                            "Here are the things I've saved in your to-do list so far:"
                    );

                    for (int i = 0; i < itemCount; i++) {
                        System.out.println((i + 1) + ". " + toDO[i]);
                    }

                } else if (input.equals("mark")) {
                    throw new LeoException(
                            "Please specify which task to mark, for example: mark 2"
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

                    System.out.println(
                            "Nice! I've marked this task as done:"
                    );
                    System.out.println(toDO[taskIndex]);

                } else if (input.equals("unmark")) {
                    throw new LeoException(
                            "Please specify which task to unmark, " + "for example: unmark 2"
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

                    System.out.println(
                            "OK, I've marked this task as not done yet:"
                    );
                    System.out.println(toDO[taskIndex]);

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

                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + toDO[itemCount]);

                    itemCount++;
                    Storage.saveTasks(toDO, itemCount);
                    System.out.println(
                            "Now you have " + itemCount + " tasks in the list."
                    );

                } else if (input.equals("deadline")) {
                    throw new LeoException(
                            "Please enter a deadline in this format: " + "deadline DESCRIPTION /by TIME"
                    );

                } else if (input.startsWith("deadline ")) {
                    String taskInformation = input.substring(9).trim();

                    if (!taskInformation.contains(" /by ")) {
                        throw new LeoException(
                                "Please enter a deadline in this format: " + "deadline DESCRIPTION /by TIME"
                        );
                    }

                    String[] parts = taskInformation.split(" /by ", 2);
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

                    checkListCapacity(itemCount, toDO.length);

                    toDO[itemCount] = new Deadline(description, by);

                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + toDO[itemCount]);

                    itemCount++;
                    Storage.saveTasks(toDO, itemCount);
                    System.out.println(
                            "Now you have " + itemCount + " tasks in the list."
                    );

                } else if (input.equals("event")) {
                    throw new LeoException(
                            "Please enter an event in this format: " + "event DESCRIPTION /from START /to END"
                    );

                } else if (input.startsWith("event ")) {
                    String taskInformation = input.substring(6).trim();

                    if (!taskInformation.contains(" /from ")) {
                        throw new LeoException(
                                "Please enter an event in this format: " + "event DESCRIPTION /from START /to END"
                        );
                    }

                    String[] fromParts =
                            taskInformation.split(" /from ", 2);

                    String description = fromParts[0].trim();
                    String eventTimes = fromParts[1].trim();

                    if (!eventTimes.contains(" /to ")) {
                        throw new LeoException(
                                "Please enter an event in this format: " + "event DESCRIPTION /from START /to END"
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
                                "Please specify the event's starting time " + "after '/from'."
                        );
                    }

                    if (to.isEmpty()) {
                        throw new LeoException(
                                "Please specify the event's ending time " + "after '/to'."
                        );
                    }

                    checkListCapacity(itemCount, toDO.length);

                    toDO[itemCount] = new Event(description, from, to);

                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + toDO[itemCount]);

                    itemCount++;
                    Storage.saveTasks(toDO, itemCount);
                    System.out.println(
                            "Now you have " + itemCount + " tasks in the list."
                    );

                } else if (input.equals("delete")) {
                    throw new LeoException(
                            "Please specify which task to delete, for example: delete 2"
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

                    for (int i = taskIndex; i < itemCount - 1; i++) {
                        toDO[i] = toDO[i + 1];
                    }

                    toDO[itemCount - 1] = null;
                    itemCount--;
                    Storage.saveTasks(toDO, itemCount);

                    System.out.println("Okay, noted. I've removed this task:");
                    System.out.println("  " + deletedTask);
                    System.out.println(
                            "Now you have " + itemCount + " tasks in the list."
                    );
                } else {
                    throw new LeoException(
                            "Sorry, I don't understand what you are trying to say."
                    );
                }

            } catch (LeoException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(
                        "I couldn't update the saved task file: " + e.getMessage()
                );
            } catch (DateTimeParseException e) {
            System.out.println(
                    "Please enter dates and times in the format yyyy-MM-dd HHmm."
            );
            }
        }

        scanner.close();
    }

    // Converts the task-number portion of a command into an integer.
    public static int parseTaskNumber(String numberText, String command)
            throws LeoException {
        try {
            int taskNumber = Integer.parseInt(numberText.trim());

            if (taskNumber <= 0) {
                throw new LeoException(
                        "The task number cannot be greater than zero."
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

    // Checks whether another task can be added to the task list.
    public static void checkListCapacity(int itemCount, int capacity)
            throws LeoException {
        if (itemCount >= capacity) {
            throw new LeoException(
                    "Your task list is full, so I cannot add another task."
            );
        }
    }

    public static void greeting() {
        final String line =
                "____________________________________________________________";

        System.out.println(line);
        System.out.println("Hello! I'm Leo.");
        System.out.println("How are you doing today, and how may I help?");
        System.out.println(line);
    }

    public static void echo(String message) {
        final String line =
                "____________________________________________________________";

        System.out.println(line);
        System.out.println("I heard you say: " + message);
        System.out.println(line);
    }

    public static void leaving() {
        final String line =
                "____________________________________________________________";

        System.out.println(line);
        System.out.println(
                "Hope to see you again soon! Have a great day ahead."
        );
        System.out.println(line);
    }
}