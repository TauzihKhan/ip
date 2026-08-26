/**
 * Represents an error that PotatoBot can report to the user.
 */
public class PotatoBotException extends Exception {
    public PotatoBotException(String message) {
        super(message);
    }
}
