package model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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

    // Method to create or update necessary tables
    public static void createOrUpdateTables() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "email TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL, "
                + "first_name TEXT, "
                + "last_name TEXT"
                + ");";

        String createCartItemsTable = "CREATE TABLE IF NOT EXISTS cart_items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL, "
                + "book_title TEXT NOT NULL, "
                + "quantity INTEGER NOT NULL, "
                + "price REAL NOT NULL, "
                + "UNIQUE(username, book_title)"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createCartItemsTable);
            System.out.println("Tables created or already exist.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
}
