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

    // Method to create or update the users and shopping_cart tables
    public static void createOrUpdateTables() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT NOT NULL UNIQUE,"
                + " email TEXT NOT NULL UNIQUE,"
                + " password TEXT NOT NULL,"
                + " first_name TEXT,"
                + " last_name TEXT"
                + ");";

        String createShoppingCartTable = "CREATE TABLE IF NOT EXISTS shopping_cart ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " user_id INTEGER,"
                + " book_title TEXT,"
                + " quantity INTEGER,"
                + " FOREIGN KEY(user_id) REFERENCES users(id)"
                + ");";

        try (Connection conn = connect()) {
            PreparedStatement pstmt1 = conn.prepareStatement(createUsersTable);
            pstmt1.execute();
            PreparedStatement pstmt2 = conn.prepareStatement(createShoppingCartTable);
            pstmt2.execute();
            System.out.println("Tables created or already exist.");
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

    // Method to add an item to the shopping cart
    public static void addToCart(int userId, String bookTitle, int quantity) {
        String sql = "INSERT INTO shopping_cart(user_id, book_title, quantity) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, bookTitle);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
            System.out.println("Item added to the cart.");
        } catch (SQLException e) {
            System.out.println("Error adding to cart: " + e.getMessage());
        }
    }

    // Method to update the quantity of an item in the cart
    public static void updateCartQuantity(int userId, String bookTitle, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND book_title = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, userId);
            pstmt.setString(3, bookTitle);
            pstmt.executeUpdate();
            System.out.println("Cart quantity updated.");
        } catch (SQLException e) {
            System.out.println("Error updating cart quantity: " + e.getMessage());
        }
    }

    // Method to remove an item from the shopping cart
    public static void removeFromCart(int userId, String bookTitle) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ? AND book_title = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, bookTitle);
            pstmt.executeUpdate();
            System.out.println("Item removed from the cart.");
        } catch (SQLException e) {
            System.out.println("Error removing from cart: " + e.getMessage());
        }
    }

    // Method to clear the shopping cart for a user
    public static void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("Cart cleared.");
        } catch (SQLException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }
}
