import java.util.Scanner;

public class Leo {
    public static void main(String[] args) {
        String[] toDO = new String[100];
        int itemCount = 0;

        String banner = " _                \n"
                + "| |    ___  ___   \n"
                + "| |   / _ \\/ _ \\  \n"
                + "| |__|  __/ (_) | \n"
                + "|_____\\___|\\___/  \n";
        Scanner scanner = new Scanner(System.in);
        System.out.println(banner);
        greeting();

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                leaving();
                break;
            } else if (input.equals("list")) {
                System.out.println("Here are the things I've saved in your to-do list so far: ");

                for (int i = 0; i < itemCount; i++) {
                    System.out.println((i + 1) + ". " + toDO[i]);
                }

            } else {
                toDO[itemCount] = input;
                itemCount++;
                System.out.println("Got it! I've added this to your to-do list: ");
                System.out.println("  " + input);
            }
            //echo(input);
        }
        scanner.close();
    }

    public static void greeting() {
        final String LINE =
                "____________________________________________________________";
        System.out.println(LINE);
        System.out.println("Hello! I'm Leo.");
        System.out.println("How are you doing today, and how may I help?");
        System.out.println(LINE);
    }

    public static void echo(String message) {
        final String LINE =
                "____________________________________________________________";
        System.out.println(LINE);
        System.out.println("I heard you say: " + message);
        System.out.println(LINE);
    }

    public static void leaving() {
        final String LINE =
                "____________________________________________________________";
        System.out.println(LINE);
        System.out.println("Hope to see you again soon! Have a great day ahead");
        System.out.println(LINE);
    }
}

