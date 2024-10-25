package model;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ShoppingCart implements Serializable {
    private Map<Book, Integer> cartItems; // Book and Quantity

    public ShoppingCart() {
        this.cartItems = new HashMap<>();
    }

    // Add book to cart
    public void addBook(Book book, int quantity) {
        if (cartItems.containsKey(book)) {
            cartItems.put(book, cartItems.get(book) + quantity);
        } else {
            cartItems.put(book, quantity);
        }
    }

    // Remove book from cart
    public void removeBook(Book book) {
        cartItems.remove(book);
    }

    // Update book quantity
    public void updateQuantity(Book book, int quantity) {
        if (cartItems.containsKey(book)) {
            cartItems.put(book, quantity);
        }
    }

    // Check if a book is available in desired quantity
    public boolean checkAvailability(Book book, int requestedQuantity) {
        return book.getStock() >= requestedQuantity;
    }

    // Get total price of items in cart
    public double getTotalPrice() {
        double total = 0.0;
        for (Map.Entry<Book, Integer> entry : cartItems.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public Map<Book, Integer> getCartItems() {
        return cartItems;
    }
}
