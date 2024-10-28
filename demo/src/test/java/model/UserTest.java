import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}
