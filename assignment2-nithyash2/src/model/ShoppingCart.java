package model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private static ShoppingCart instance;
    private List<CartItem> items;

    // Private constructor to enforce Singleton pattern
    private ShoppingCart() {
        this.items = new ArrayList<>();
    }

    // Method to get the Singleton instance
    public static synchronized ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    // Add an item to the shopping cart
    public void addItem(String username, Book book, int quantity) {
        // Check if the book already exists in the cart
        for (CartItem item : items) {
            if (item.getBook().getTitle().equalsIgnoreCase(book.getTitle())) {
                item.setQuantity(item.getQuantity() + quantity); // Update quantity
                Database.saveCartItem(username, book, item.getQuantity()); // Save updated item to the database
                return;
            }
        }
        // Add new item to the cart
        CartItem newItem = new CartItem(book, quantity);
        items.add(newItem);
        Database.saveCartItem(username, book, quantity); // Save new item to the database
    }

    // Remove an item from the shopping cart
    public void removeItem(String username, Book book) {
        items.removeIf(item -> {
            boolean match = item.getBook().getTitle().equalsIgnoreCase(book.getTitle());
            if (match) {
                Database.saveCartItem(username, book, 0); // Remove the item from the database
            }
            return match;
        });
    }

    // Method to get all items in the cart
    public List<CartItem> getItems() {
        return items;
    }

    // Method to load items from the database
    public void loadItemsFromDatabase(String username) {
        this.items = Database.loadCartItems(username);
    }

    // Method to clear the cart
    public void clearCart() {
        items.clear();
    }

    // Get the total cost of items in the cart
    public double getTotalCost() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getBook().getPrice() * item.getQuantity();
        }
        return total;
    }
}
