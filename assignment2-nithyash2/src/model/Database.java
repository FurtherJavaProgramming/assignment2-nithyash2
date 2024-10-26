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
        createTables();  // Ensure tables are created on initialization
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
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    // Fetch all books for admin view
    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT title, author, price, stock, sold_copies FROM books";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                int soldCopies = rs.getInt("sold_copies");

                Book book = new Book(title, author, price, stock);
                book.setSoldCopies(soldCopies);
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books: " + e.getMessage());
        }

        return books;
    }

    // Update stock of a specific book
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

    // Check if a book has sufficient stock for the requested quantity
    public static boolean checkBookStock(String title, int quantity) {
        String sql = "SELECT stock FROM books WHERE title = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("stock");
                return stock >= quantity;
            }
        } catch (SQLException e) {
            System.out.println("Error checking book stock: " + e.getMessage());
        }
        return false;
    }

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
            pstmt.setInt(5, quantity);  // Update quantity if the item already exists

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
}
