package model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:sqlite:./databases/your_database.db";

    static {
        File databaseDir = new File("./databases");
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }
    }

    // Method to establish connection with the database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return conn;
    }

    // Method to create or update the users table
    public static void createOrUpdateTables() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT NOT NULL UNIQUE,"  // Ensure username column
                + " email TEXT NOT NULL UNIQUE,"
                + " password TEXT NOT NULL,"
                + " first_name TEXT,"
                + " last_name TEXT"
                + ");";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(createUsersTable)) {
            pstmt.execute();
            System.out.println("Users table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to add a new user to the database
    public static void addUser(String username, String email, String password, String firstName, String lastName) {
        String sql = "INSERT INTO users(username, email, password, first_name, last_name) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);
            pstmt.executeUpdate();
            System.out.println("User added to the database.");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    // Method to update user profile
    public static void updateUserProfile(String email, String firstName, String lastName, String password) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, password = ? WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, password);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
            System.out.println("User profile updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating user profile: " + e.getMessage());
        }
    }
}
