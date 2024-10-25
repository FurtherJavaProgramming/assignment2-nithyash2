package model;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private double price;
    private int stock;
    private int soldCopies;

    public Book(String title, String author, double price, int stock) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.soldCopies = 0; // Initialize sold copies
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getSoldCopies() {
        return soldCopies;
    }

    public void setSoldCopies(int soldCopies) {
        this.soldCopies = soldCopies;
    }

    // Method to update stock when a book is sold
    public boolean sellBook(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            soldCopies += quantity;
            return true;
        } else {
            return false; // Not enough stock
        }
    }

    // Method to restock books (for admin functionality)
    public void restockBook(int quantity) {
        this.stock += quantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", soldCopies=" + soldCopies +
                '}';
    }
}
