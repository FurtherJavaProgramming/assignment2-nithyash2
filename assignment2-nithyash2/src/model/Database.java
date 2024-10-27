package model;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;




public class Database {

    private static final String URL = "jdbc:sqlite:./databases/your_database.db";

    static {
        File databaseDir = new File("./databases");
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }
        createTables();
    }

    // Establish connection with the database
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

    // Create necessary tables for users, books, and cart items if they don’t exist
    public static void createTables() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "email TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL, "
                + "first_name TEXT, "
                + "last_name TEXT);";

        String createCartTable = "CREATE TABLE IF NOT EXISTS cart_items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL, "
                + "book_title TEXT NOT NULL, "
                + "quantity INTEGER NOT NULL, "
                + "price REAL NOT NULL, "
                + "UNIQUE(username, book_title), "
                + "FOREIGN KEY(username) REFERENCES users(username));";

        String createBooksTable = "CREATE TABLE IF NOT EXISTS books ("
                + "title TEXT PRIMARY KEY, "
                + "author TEXT, "
                + "price REAL NOT NULL, "
                + "stock INTEGER NOT NULL, "
                + "sold_copies INTEGER DEFAULT 0);";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createCartTable);
            stmt.execute(createBooksTable);
            System.out.println("Tables created or already exist.");
            insertInitialBookData();  // Insert initial data if the books table is empty
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    // Insert initial book data if the table is empty
    public static void insertInitialBookData() {
        String checkBooksTable = "SELECT COUNT(*) AS total FROM books";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkBooksTable)) {

            // Check if the table is empty
            if (rs.next() && rs.getInt("total") == 0) {
                // Insert initial data
                String insertBooks = """
                        INSERT INTO books (title, author, price, stock, sold_copies) VALUES
                        ('Absolute Java', 'Savitch', 50.0, 10, 0),
                        ('Java: How to Program', 'Deitel and Deitel', 70.0, 100, 0),
                        ('Computing Concepts with JAVA 8 Essentials', 'Horstman', 89.0, 500, 0),
                        ('Java Software Solutions', 'Lewis and Loftus', 99.0, 500, 0),
                        ('Java Program Design', 'Cohoon and Davidson', 29.0, 2, 0),
                        ('Clean Code', 'Robert Martin', 45.0, 100, 0),
                        ('Gray Hat C#', 'Brandon Perry', 68.0, 300, 0),
                        ('Python Basics', 'David Amos', 49.0, 1000, 0),
                        ('Bayesian Statistics The Fun Way', 'Will Kurt', 42.0, 600, 0);
                        """;
                stmt.executeUpdate(insertBooks);
                System.out.println("Initial book data inserted into books table.");
            } else {
                System.out.println("Books table already contains data; no insertions made.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting initial book data: " + e.getMessage());
        }
    }

    // Other methods (saveCartItem, loadCartItems, etc.) remain the same...


    // Save or update a cart item in the database
    public static void saveCartItem(String username, Book book, int quantity) {
        String sql = "INSERT INTO cart_items (username, book_title, quantity, price) VALUES (?, ?, ?, ?) "
                + "ON CONFLICT(username, book_title) DO UPDATE SET quantity = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, book.getTitle());
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, book.getPrice());
            pstmt.setInt(5, quantity); // Update quantity if the item already exists

            pstmt.executeUpdate();
            System.out.println("Cart item saved/updated for user: " + username);
        } catch (SQLException e) {
            System.out.println("Error saving cart item: " + e.getMessage());
        }
    }

    // Load cart items from the database for a specific user
    public static List<CartItem> loadCartItems(String username) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT book_title, quantity, price FROM cart_items WHERE username = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("book_title");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                Book book = new Book(title, "", price, 0);  // Assuming author and stock are not needed here
                cartItems.add(new CartItem(book, quantity));
            }
        } catch (SQLException e) {
            System.out.println("Error loading cart items: " + e.getMessage());
        }

        return cartItems;
    }

    // Add a new user to the database
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
            System.out.println("User " + username + " added to the database.");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    public static boolean checkBookStock(String title, int requestedQuantity) {
        String sql = "SELECT stock FROM books WHERE title = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int availableStock = rs.getInt("stock");
                return availableStock >= requestedQuantity;
            }
        } catch (SQLException e) {
            System.out.println("Error checking book stock: " + e.getMessage());
        }
        return false;
    }

    public static void updateBookStock(String title, int newStock) {
        String sql = "UPDATE books SET stock = ? WHERE title = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newStock);
            pstmt.setString(2, title);
            pstmt.executeUpdate();
            System.out.println("Updated stock for book: " + title);
        } catch (SQLException e) {
            System.out.println("Error updating book stock: " + e.getMessage());
        }
    }
}
