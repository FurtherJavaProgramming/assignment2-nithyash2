package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class User {

    private static User instance;
    private String firstName;
    private String lastName;
    private Map<String, String> userDatabase; // For in-memory testing

    private User() {
        userDatabase = new HashMap<>();
        // Add a default admin user for testing
        userDatabase.put("admin", "password");
        this.firstName = "Admin";
        this.lastName = "Admin";
    }

    // Singleton instance getter
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public void signUp(String username, String password, String firstName, String lastName) {
        userDatabase.put(username, password);
        this.firstName = firstName;
        this.lastName = lastName;

        // Add to database (assumes the database has first_name and last_name fields)
        String sql = "INSERT INTO users(email, password, first_name, last_name) VALUES(?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.executeUpdate();
            System.out.println("User " + username + " added to the database.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean validateCredentials(String username, String password) {
        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            return true;
        }

        // Database validation
        String sql = "SELECT first_name, last_name FROM users WHERE email = ? AND password = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.firstName = rs.getString("first_name");
                this.lastName = rs.getString("last_name");
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
