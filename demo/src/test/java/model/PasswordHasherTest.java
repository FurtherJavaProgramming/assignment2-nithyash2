package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    void hashIsNotThePlainPassword() {
        String hash = PasswordHasher.hash("password123");
        assertNotEquals("password123", hash);
        assertTrue(PasswordHasher.isHashed(hash));
    }

    @Test
    void verifyAcceptsCorrectPasswordAndRejectsWrongOne() {
        String hash = PasswordHasher.hash("password123");
        assertTrue(PasswordHasher.verify("password123", hash));
        assertFalse(PasswordHasher.verify("Password123", hash));
        assertFalse(PasswordHasher.verify("", hash));
    }

    @Test
    void samePasswordGetsDifferentHashesBecauseOfSalt() {
        assertNotEquals(PasswordHasher.hash("password123"), PasswordHasher.hash("password123"));
    }

    @Test
    void verifyRejectsPlainTextOrMalformedValues() {
        assertFalse(PasswordHasher.verify("password123", "password123"));
        assertFalse(PasswordHasher.verify("password123", "pbkdf2$not-a-number$abc$def"));
        assertFalse(PasswordHasher.verify("password123", null));
    }

    @Test
    void emptyPasswordCannotBeHashed() {
        assertThrows(IllegalArgumentException.class, () -> PasswordHasher.hash(""));
    }
}
