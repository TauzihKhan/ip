/**
 * Greets the user as PotatoBot and exits with a farewell message.
 */
public class PotatoBot {
    public static void main(String[] args) {
        String separator = "=".repeat(80);
        String banner = """
                 ____       _        _        ____        _           _...._
                |  _ \\ ___ | |_ __ _| |_ ___ | __ )  ___ | |_     .-'      '-.
                | |_) / _ \\| __/ _` | __/ _ \\|  _ \\ / _ \\| __|   /  .  .   .  \\
                |  __/ (_) | || (_| | || (_) | |_) | (_) | |_    | .    .     .|
                |_|   \\___/ \\__\\__,_|\\__\\___/|____/ \\___/ \\__|    \\__ .   .    /
                                                                     '-.____.-'
                """;

        System.out.println(separator);
        System.out.print(banner);
        System.out.println("Hello! I'm PotatoBot, your trusty spud assistant.");
        System.out.println("What can I dig up for you?");
        System.out.println(separator);
        System.out.println("Bye. I'm rolling back to the potato patch. Hope to see you again soon!");
        System.out.println(separator);
    }
}
