package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.getInstance();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@domain.com");
        user.setUsername("testUser");
        user.setPassword("password123");
    }

    // Test to validate regular user credentials (not admin)
    @Test
    void testValidateCredentials() {
        user.signUp("testUser", "test@domain.com", "password123", "Test", "User");
        assertTrue(user.validateCredentials("testUser", "password123"), "Credentials should be valid.");
        assertFalse(user.validateCredentials("testUser", "wrongPassword"), "Credentials should be invalid.");
    }

    // Test to validate admin credentials
    @Test
    void testAdminCredentials() {
        assertTrue(user.validateCredentials("admin", "reading_admin"), "Admin credentials should be valid.");
        assertTrue(user.isAdmin(), "User should be an admin after admin login.");
    }

    // Test email getter and setter with normalization
    @Test
    void testGetAndSetEmail() {
        assertEquals("test@domain.com", user.getEmail(), "Initial email should match.");

        user.setEmail("New@Domain.COM");
        assertEquals("new@domain.com", user.getEmail(), "Email should be normalized to lowercase.");
    }

    // Test email validation by checking that setting an improperly formatted email throws an exception
    @Test
    void testSetInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("invalidEmail"),
                "Setting an invalid email should throw an exception.");
    }

    // Test username getter and setter
    @Test
    void testGetUsername() {
        assertEquals("testUser", user.getUsername(), "Username should match the initialized value.");
        user.setUsername("newUser");
        assertEquals("newUser", user.getUsername(), "Username should be updated to the new value.");
    }

    // Passwords must be stored as salted hashes, never as plain text
    @Test
    void testPasswordIsStoredHashed() throws Exception {
        String name = "hashUser" + System.nanoTime();
        user.signUp(name, name + "@domain.com", "secret123", "Hash", "User");

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "User should be saved.");
            String stored = rs.getString("password");
            assertNotEquals("secret123", stored, "Password must not be stored as plain text.");
            assertTrue(PasswordHasher.verify("secret123", stored), "Stored hash should match the password.");
        }
        assertTrue(user.validateCredentials(name, "secret123"));
        assertFalse(user.validateCredentials(name, "wrong"));
    }
}
