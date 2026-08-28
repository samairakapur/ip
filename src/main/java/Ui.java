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

        System.out.println(banner);
        System.out.println(LINE);
        System.out.println("Hello! I'm Leo.");
        System.out.println("How are you doing today, and how may I help?");
        System.out.println(LINE);
    }

    /**
     * Displays a message to the user.
     *
     * @param message message to display
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays Leo's goodbye message.
     */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println(
                "Hope to see you again soon! Have a great day ahead."
        );
        System.out.println(LINE);
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }
}