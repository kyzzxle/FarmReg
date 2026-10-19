/**
 * InvalidInputException.java
 *
 * Thrown when a value breaks a rule of the system: a required field is empty,
 * a number is negative, a date is not a real date, a Product ID is already
 * taken, and so on. The message is written for the user, so the GUI can show
 * it as-is.
 *
 * It is a CHECKED exception (extends Exception) on purpose: any code that
 * creates or changes a product is forced by the compiler to either handle
 * the problem with try/catch or declare it with "throws" - invalid data can
 * never slip through unnoticed.
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }

    /** Keeps the original low-level error (e.g. NumberFormatException) as the cause. */
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
