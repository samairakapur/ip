package leo;

import java.util.Scanner;

/**
 * Handles interactions between Leo and the user.
 */
public class Ui {
    private static final String LINE =
            "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a Ui that reads user input from the console.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return the user's command
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays Leo's banner and greeting.
     */
    public void showWelcome() {
        String banner = " _                \n"
                + "| |    ___  ___   \n"
                + "| |   / _ \\/ _ \\  \n"
                + "| |__|  __/ (_) | \n"
                + "|_____\\___|\\___/  \n";

        showMessage(
                banner,
                LINE,
                "Hello! I'm Leo.",
                "How are you doing today, and how may I help?",
                LINE
        );
    }

    /**
     * Displays one or more messages to the user, one per line, in the
     * order given. Accepting a variable number of arguments (varargs)
     * lets a single call replace what would otherwise be several
     * separate {@code showMessage} calls whenever a command needs to
     * show more than one line of related output (e.g. "task added" +
     * the task itself + the new task count) - see the call sites in
     * {@link Leo} for examples. A single message still works exactly
     * as before, since Java treats one argument as a one-element array.
     *
     * @param messages message(s) to display, each on its own line
     */
    public void showMessage(String... messages) {
        for (String message : messages) {
            System.out.println(message);
        }
    }

    /**
     * Displays Leo's goodbye message.
     */
    public void showGoodbye() {
        showMessage(
                LINE,
                "Hope to see you again soon! Have a great day ahead.",
                LINE
        );
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }
}