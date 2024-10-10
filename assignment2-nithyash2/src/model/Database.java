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

    // Method to drop and create the users table
    public static void createTables() {
        String dropUsersTable = "DROP TABLE IF EXISTS users;";
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " email TEXT NOT NULL UNIQUE,"
                + " password TEXT NOT NULL,"
                + " first_name TEXT,"
                + " last_name TEXT"
                + ");";

        try (Connection conn = connect();
             PreparedStatement dropStmt = conn.prepareStatement(dropUsersTable);
             PreparedStatement createStmt = conn.prepareStatement(createUsersTable)) {

            // Drop the users table if it exists
            dropStmt.execute();

            // Create the new users table
            createStmt.execute();
            System.out.println("Users table created or already exists.");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to add a new user to the database
    public static void addUser(String email, String password, String firstName, String lastName) {
        String sql = "INSERT INTO users(email, password, first_name, last_name) VALUES(?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.executeUpdate();
            System.out.println("User added to the database.");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }
}
