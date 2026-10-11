package model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentValidatorTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 11);

    @Test
    void cardNumberMustBeSixteenDigits() {
        assertTrue(PaymentValidator.isValidCardNumber("4111111111111111"));
        assertFalse(PaymentValidator.isValidCardNumber("411111111111111"));    // 15 digits
        assertFalse(PaymentValidator.isValidCardNumber("4111-1111-1111-1111"));
        assertFalse(PaymentValidator.isValidCardNumber(null));
    }

    @Test
    void cvvMustBeThreeDigits() {
        assertTrue(PaymentValidator.isValidCvv("123"));
        assertFalse(PaymentValidator.isValidCvv("12"));
        assertFalse(PaymentValidator.isValidCvv("12a"));
    }

    @Test
    void expiryInFutureOrCurrentMonthIsValid() {
        assertTrue(PaymentValidator.isValidExpiry("12/27", TODAY));
        assertTrue(PaymentValidator.isValidExpiry("10/26", TODAY)); // card works until the end of its expiry month
    }

    @Test
    void expiredOrBadlyFormattedExpiryIsRejected() {
        assertFalse(PaymentValidator.isValidExpiry("09/26", TODAY));
        assertFalse(PaymentValidator.isValidExpiry("13/27", TODAY));
        assertFalse(PaymentValidator.isValidExpiry("1/27", TODAY));
        assertFalse(PaymentValidator.isValidExpiry("", TODAY));
    }

    @Test
    void allFieldsMustBeValidTogether() {
        assertTrue(PaymentValidator.isValid("4111111111111111", "12/27", "123", TODAY));
        assertFalse(PaymentValidator.isValid("4111111111111111", "09/26", "123", TODAY));
    }
}
