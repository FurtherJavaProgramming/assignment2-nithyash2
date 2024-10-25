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
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email.toLowerCase(); // Convert email to lowercase
        this.username = username;
        this.password = password;

        String sql = "INSERT INTO users(username, email, password, first_name, last_name) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.username);  // Store username
            pstmt.setString(2, this.email);     // Store email in lowercase
            pstmt.setString(3, this.password);
            pstmt.setString(4, this.firstName);
            pstmt.setString(5, this.lastName);
            pstmt.executeUpdate();
            System.out.println("User " + username + " added to the database.");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    // Validate credentials against the database and update User instance
    public boolean validateCredentials(String login, String password) {
        String sql = "SELECT first_name, last_name, email, username, password FROM users WHERE (LOWER(email) = LOWER(?) OR username = ?) AND password = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login.toLowerCase());  // Convert email to lowercase for case-insensitive validation
            pstmt.setString(2, login);                // Check both email and username
            pstmt.setString(3, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Update the User instance with the logged-in user's data
                this.firstName = rs.getString("first_name");
                this.lastName = rs.getString("last_name");
                this.email = rs.getString("email");
                this.username = rs.getString("username");
                this.password = rs.getString("password");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return false;
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
