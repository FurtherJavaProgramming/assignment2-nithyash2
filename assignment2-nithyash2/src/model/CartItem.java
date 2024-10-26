package model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Book book;
    private int quantity;

    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Calculate total price for this cart item
    public double getTotalPrice() {
        return book.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return book.getTitle() + " - " + quantity + " pcs";
    }
}
