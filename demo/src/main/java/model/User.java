package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private static User instance;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private boolean isAdmin;

    // Private constructor for Singleton
    private User() {
        // No default values to prevent incorrect data on the profile page
    }

    // Singleton instance getter
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    // Sign up method to add user to the database
    public void signUp(String username, String email, String password, String firstName, String lastName) {
        // Check if the email or username is already registered
        if (isEmailRegistered(email) || isUsernameRegistered(username)) {
            System.out.println("Error: Email or username is already registered.");
            return; // Exit without adding the user
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email.toLowerCase(); // Convert email to lowercase
        this.username = username;
        this.password = null;   // Never keep the plain-text password in memory
        this.isAdmin = false;  // Default users are not admin

        String sql = "INSERT INTO users(username, email, password, first_name, last_name) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.username);  // Store username
            pstmt.setString(2, this.email);     // Store email in lowercase
            pstmt.setString(3, PasswordHasher.hash(password)); // Store a salted hash, never the plain password
            pstmt.setString(4, this.firstName);
            pstmt.setString(5, this.lastName);
            pstmt.executeUpdate();
            System.out.println("User " + username + " added to the database.");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    // Helper method to check if email is already registered
    private boolean isEmailRegistered(String email) {
        String sql = "SELECT email FROM users WHERE email = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email.toLowerCase());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  // Returns true if email is found
        } catch (SQLException e) {
            System.out.println("Error checking email: " + e.getMessage());
            return false;
        }
    }

    // Helper method to check if username is already registered
    private boolean isUsernameRegistered(String username) {
        String sql = "SELECT username FROM users WHERE username = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  // Returns true if username is found
        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
            return false;
        }
    }

    // Validate credentials against the database and update User instance
    public boolean validateCredentials(String login, String password) {
        // Check if admin credentials match
        if (login.equalsIgnoreCase("admin") && password.equals("reading_admin")) {
            // Set admin properties
            this.username = "admin";
            this.firstName = "Admin";
            this.lastName = "Admin";
            this.email = "admin@example.com";
            this.password = password;
            this.isAdmin = true;
            return true;
        }

        // Look up the user by email or username, then verify the password against the stored hash
        String sql = "SELECT first_name, last_name, email, username, password FROM users WHERE LOWER(email) = LOWER(?) OR username = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login.toLowerCase());  // Case-insensitive email match
            pstmt.setString(2, login);                // Or exact username match
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String stored = rs.getString("password");
                boolean matches;
                if (PasswordHasher.isHashed(stored)) {
                    matches = PasswordHasher.verify(password, stored);
                } else {
                    // Older accounts stored plain text: accept once, then upgrade to a hash
                    matches = stored != null && stored.equals(password);
                    if (matches) {
                        Database.updatePassword(rs.getString("username"), password);
                    }
                }
                if (matches) {
                    // Update the User instance with the logged-in user's data
                    this.firstName = rs.getString("first_name");
                    this.lastName = rs.getString("last_name");
                    this.email = rs.getString("email");
                    this.username = rs.getString("username");
                    this.password = null; // Never keep the password in memory
                    this.isAdmin = false; // Regular users are not admin
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return false;
    }

    // Check if the current user is an admin
    public boolean isAdmin() {
        return isAdmin;
    }

    // Getters and Setters for User fields
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.toLowerCase(); // Normalize email to lowercase
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
