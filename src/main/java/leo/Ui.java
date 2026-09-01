package leo;

import java.util.Scanner;

/**
 * Handles interactions between Leo and the user.
 */
public class Ui {
    private static final String LINE =
            "____________________________________________________________";

    // Created lazily (on the first readCommand() call) rather than in the
    // constructor, so that a Ui created only for its output side (e.g. by
    // GuiUi, which is never asked to read a command) does not open a
    // Scanner on System.in that it will never use or close. See A-Varargs
    // vs Level-10.
    private Scanner scanner;

    /**
     * Reads the next command entered by the user.
     *
     * @return the user's command
     */
    public String readCommand() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
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

        print(banner);
        print(LINE);
        print("Hello! I'm Leo.");
        print("How are you doing today, and how may I help?");
        print(LINE);
    }

    /**
     * Displays a message to the user.
     *
     * @param message message to display
     */
    public void showMessage(String message) {
        print(message);
    }

    /**
     * Displays Leo's goodbye message.
     */
    public void showGoodbye() {
        print(LINE);
        print("Hope to see you again soon! Have a great day ahead.");
        print(LINE);
    }

    /**
     * Closes the input scanner, if one was ever opened.
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }

    /**
     * Writes one line of output. This is the single place all of Ui's
     * other display methods funnel through, so that a subclass (like
     * {@link GuiUi}) can override just this one method to redirect
     * Leo's replies into a GUI instead of the console, without having
     * to duplicate any of the command-handling logic in {@link Leo}
     * that decides what those replies should say.
     *
     * @param line the line of text to write
     */
    protected void print(String line) {
        System.out.println(line);
    }
}