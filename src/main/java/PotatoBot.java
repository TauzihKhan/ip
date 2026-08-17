import java.util.Scanner;

/**
 * Runs PotatoBot's text-based interaction with the user.
 */
public class PotatoBot {
    private static final String SEPARATOR = "=".repeat(80);
    private static final String EXIT_COMMAND = "bye";
    private static final String BANNER = """
             ____       _        _        ____        _           _...._
            |  _ \\ ___ | |_ __ _| |_ ___ | __ )  ___ | |_     .-'      '-.
            | |_) / _ \\| __/ _` | __/ _ \\|  _ \\ / _ \\| __|   /  .  .   .  \\
            |  __/ (_) | || (_| | || (_) | |_) | (_) | |_    | .    .     .|
            |_|   \\___/ \\__\\__,_|\\__\\___/|____/ \\___/ \\__|    \\__ .   .    /
                                                                 '-.____.-'
            """;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printGreeting();
        while (true) {
            System.out.print("Me: ");
            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine();
            if (EXIT_COMMAND.equals(input)) {
                System.out.println();
                break;
            }

            System.out.println("PotatoBot: " + input + "\n");
    }
        printFarewell();
        scanner.close();
    }

    // Print welcome message
    private static void printGreeting() {
        System.out.println(SEPARATOR);
        System.out.print(BANNER);
        System.out.println("Hello! I'm PotatoBot, your trusty spud assistant.");
        System.out.println("What can I dig up for you? (Say \"bye\" if you want me to leave you alone)");
        System.out.println(SEPARATOR + "\n");
    }

    // Print farewell message
    private static void printFarewell() {
        System.out.println(SEPARATOR);
        System.out.println("Bye. I'm rolling back to the potato patch. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
}
