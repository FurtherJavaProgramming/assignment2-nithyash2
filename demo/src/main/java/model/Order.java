package model;

import javafx.beans.property.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private String username;
    private LocalDateTime orderDate;
    private double totalPrice;
    private List<OrderItem> items;

    // Constructor for creating a new order
    public Order(String username, List<OrderItem> items, double totalPrice) {
        this.username = username;
        this.items = items;
        this.totalPrice = totalPrice;
        this.orderDate = LocalDateTime.now();
    }

    // Constructor for loading order details (includes order ID and order date)
    public Order(int orderId, String username, List<OrderItem> items, double totalPrice, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.username = username;
        this.items = items;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    // JavaFX Property methods for data binding
    public IntegerProperty orderIdProperty() {
        return new SimpleIntegerProperty(orderId);
    }

    public ObjectProperty<LocalDateTime> orderDateProperty() {
        return new SimpleObjectProperty<>(orderDate);
    }

    public DoubleProperty totalPriceProperty() {
        return new SimpleDoubleProperty(totalPrice);
    }

    // Save the order to the database and assign a unique order number
    public int saveOrder() {
        int orderId = -1;
        String sql = "INSERT INTO orders (username, order_date, total_price) VALUES (?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, orderDate.toString());
            pstmt.setDouble(3, totalPrice);
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
                this.orderId = orderId; // Set the ID for this order instance
            }

            for (OrderItem item : items) {
                item.save(orderId);  // Save each item with the associated order ID
            }

            System.out.println("Order saved successfully with ID: " + orderId);
        } catch (SQLException e) {
            System.out.println("Error saving order: " + e.getMessage());
        }

        return orderId;
    }

    // Method to retrieve all orders for a specific user from the database
    public static List<Order> getAllOrdersForUser(String username) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE username = ? ORDER BY order_date DESC";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                LocalDateTime orderDate = LocalDateTime.parse(rs.getString("order_date"));
                double totalPrice = rs.getDouble("total_price");

                List<OrderItem> items = OrderItem.getItemsForOrder(orderId);  // Load items for this order
                orders.add(new Order(orderId, username, items, totalPrice, orderDate));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving orders: " + e.getMessage());
        }

        return orders;
    }
}
