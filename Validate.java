import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Validate.java
 *
 * Small stateless helper holding the validation rules used by Product,
 * Vegetable, Fruit, Animal, Farmer and the GUI. It is NOT a manager class -
 * it keeps no data - it only exists so the same "is this empty / negative /
 * a real date" checks are written once instead of copy-pasted into every
 * constructor and setter. Every method either returns the cleaned value or
 * throws InvalidInputException with a user-readable message.
 */
public final class Validate {

    private Validate() { }

    /** Required text: not null, not blank. Returns the trimmed text. */
    public static String text(String value, String label) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException("Please fill in the required field: " + label + ".");
        }
        return value.trim();
    }

    /** Text field -> double. Turns NumberFormatException into a friendly message. */
    public static double number(String value, String label) throws InvalidInputException {
        String clean = text(value, label);
        try {
            double number = Double.parseDouble(clean);
            if (Double.isNaN(number) || Double.isInfinite(number)) {
                throw new InvalidInputException(label + " must be a valid number.");
            }
            return number;
        } catch (NumberFormatException ex) {
            throw new InvalidInputException(label + " must be a valid number.", ex);
        }
    }

    /** Text field -> int. */
    public static int wholeNumber(String value, String label) throws InvalidInputException {
        String clean = text(value, label);
        try {
            return Integer.parseInt(clean);
        } catch (NumberFormatException ex) {
            throw new InvalidInputException(label + " must be a valid whole number.", ex);
        }
    }

    public static double nonNegative(double value, String label) throws InvalidInputException {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new InvalidInputException(label + " must be a valid number.");
        }
        if (value < 0) {
            throw new InvalidInputException(label + " cannot be negative.");
        }
        return value;
    }

    public static int nonNegative(int value, String label) throws InvalidInputException {
        if (value < 0) {
            throw new InvalidInputException(label + " cannot be negative.");
        }
        return value;
    }

    /** Strict YYYY-MM-DD; rejects things like 2026-02-30. */
    public static LocalDate date(String value, String label) throws InvalidInputException {
        String clean = text(value, label);
        try {
            return LocalDate.parse(clean);
        } catch (DateTimeParseException ex) {
            throw new InvalidInputException(label + " must be a valid date (YYYY-MM-DD).", ex);
        }
    }

    /** The value must be one of the allowed choices (case-insensitive); returns the allowed spelling. */
    public static String oneOf(String value, String label, String... allowed) throws InvalidInputException {
        String clean = text(value, label);
        for (String option : allowed) {
            if (option.equalsIgnoreCase(clean)) {
                return option;
            }
        }
        throw new InvalidInputException(label + " must be one of: " + String.join(", ", allowed) + ".");
    }
}
