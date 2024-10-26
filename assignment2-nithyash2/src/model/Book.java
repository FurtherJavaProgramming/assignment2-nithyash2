package model;

import javafx.beans.property.*;

public class Book {
    private final StringProperty title;
    private final StringProperty author;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final IntegerProperty soldCopies;

    public Book(String title, String author, double price, int stock) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.price = new SimpleDoubleProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.soldCopies = new SimpleIntegerProperty(0); // Initialize sold copies
    }

    // Getters and Setters for JavaFX properties
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public StringProperty authorProperty() {
        return author;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public int getStock() {
        return stock.get();
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public int getSoldCopies() {
        return soldCopies.get();
    }

    public void setSoldCopies(int soldCopies) {
        this.soldCopies.set(soldCopies);
    }

    public IntegerProperty soldCopiesProperty() {
        return soldCopies;
    }

    // Method to update stock when a book is sold
    public boolean sellBook(int quantity) {
        if (stock.get() >= quantity) {
            stock.set(stock.get() - quantity);
            soldCopies.set(soldCopies.get() + quantity);
            return true;
        } else {
            return false; // Not enough stock
        }
    }

    // Method to restock books (for admin functionality)
    public void restockBook(int quantity) {
        this.stock.set(this.stock.get() + quantity);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title.get() + '\'' +
                ", author='" + author.get() + '\'' +
                ", price=" + price.get() +
                ", stock=" + stock.get() +
                ", soldCopies=" + soldCopies.get() +
                '}';
    }
}
