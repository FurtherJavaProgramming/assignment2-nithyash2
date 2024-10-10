package model;

public class Book {
    private String title;
    private String author;
    private double price;
    private int stock;
    private int soldCopies;

    // Constructor with parameters
    public Book(String title, String author, double price, int stock, int soldCopies) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.soldCopies = soldCopies;
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
