package model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validates payment details entered at checkout. Kept separate from the JavaFX controller
 * so the rules can be unit tested.
 */
public final class PaymentValidator {

    private static final DateTimeFormatter EXPIRY_FORMAT = DateTimeFormatter.ofPattern("MM/yy");

    private PaymentValidator() {
    }

    /** Card number must be exactly 16 digits. */
    public static boolean isValidCardNumber(String cardNumber) {
        return cardNumber != null && cardNumber.matches("\\d{16}");
    }

    /** CVV must be exactly 3 digits. */
    public static boolean isValidCvv(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    /** Expiry must be in MM/yy format and not before the current month. */
    public static boolean isValidExpiry(String expiry, LocalDate today) {
        if (expiry == null || !expiry.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        try {
            YearMonth expiryMonth = YearMonth.parse(expiry, EXPIRY_FORMAT);
            return !expiryMonth.isBefore(YearMonth.from(today));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /** True when the card number, expiry and CVV are all valid. */
    public static boolean isValid(String cardNumber, String expiry, String cvv, LocalDate today) {
        return isValidCardNumber(cardNumber) && isValidExpiry(expiry, today) && isValidCvv(cvv);
    }
}
