package model;

import javafx.beans.property.*;

public class CartItem {
    private Book book;
    private IntegerProperty quantity;
    private DoubleProperty totalCost;

    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalCost = new SimpleDoubleProperty(book.getPrice() * quantity);
    }

    // Update total cost based on quantity
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        this.totalCost.set(book.getPrice() * quantity);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getTotalCost() {
        return totalCost.get();
    }

    public DoubleProperty totalCostProperty() {
        return totalCost;
    }

    public Book getBook() {
        return book;
    }
}
