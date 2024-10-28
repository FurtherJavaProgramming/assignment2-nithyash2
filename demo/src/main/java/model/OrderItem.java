package model;

import javafx.beans.property.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItem {

    private final StringProperty bookTitle;
    private final IntegerProperty quantity;
    private final DoubleProperty price;

    public OrderItem(String bookTitle, int quantity, double price) {
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
    }

    // Getter and Property methods
    public String getBookTitle() {
        return bookTitle.get();
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    // Save the order item for a given order ID
    public void save(int orderId) {
        String insertQuery = "INSERT INTO order_items (order_id, book_title, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setInt(1, orderId);
            statement.setString(2, getBookTitle());
            statement.setInt(3, getQuantity());
            statement.setDouble(4, getPrice());
            statement.executeUpdate();
            System.out.println("Order item saved successfully.");
        } catch (SQLException e) {
            System.out.println("Error saving order item: " + e.getMessage());
        }
    }


    public static List<OrderItem> getItemsForOrder(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String query = "SELECT book_title, quantity, price FROM order_items WHERE order_id = ?";
        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("book_title");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                OrderItem item = new OrderItem(bookTitle, quantity, price);
                items.add(item);
                System.out.println("Fetched item for order " + orderId + ": " + item.getBookTitle() + ", Quantity: " + item.getQuantity() + ", Price: " + item.getPrice());
            }
        } catch (SQLException e) {
            System.out.println("Error fetching items for order: " + e.getMessage());
        }
        return items;
    }

    // Method to get items for a specific order ID

}
