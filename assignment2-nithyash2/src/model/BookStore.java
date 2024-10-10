package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BookStore {
    private static BookStore instance;
    private List<Book> books;

    // Private constructor for Singleton pattern
    private BookStore() {
        books = new ArrayList<>();
        books.add(new Book("Absolute Java", "Savitch", 50.0, 10, 142));
        books.add(new Book("Java: How to Program", "Deitel and Deitel", 70.0, 100, 475));
        books.add(new Book("Computing Concepts with JAVA 8 Essentials", "Horstman", 89.0, 500, 60));
        books.add(new Book("Java Software Solutions", "Lewis and Loftus", 99.0, 500, 12));
        books.add(new Book("Java Program Design", "Cohoon and Davidson", 29.0, 2, 86));
        books.add(new Book("Clean Code", "Robert Martin", 45.0, 100, 300));
        books.add(new Book("Gray Hat C#", "Brandon Perry", 68.0, 300, 178));
        books.add(new Book("Python Basics", "David Amos", 49.0, 1000, 79));
        books.add(new Book("Bayesian Statistics The Fun Way", "Will Kurt", 42.0, 600, 155));
    }

    // Get Singleton instance
    public static synchronized BookStore getInstance() {
        if (instance == null) {
            instance = new BookStore();
        }
        return instance;
    }

    // Get all books
    public List<Book> getBooks() {
        return books;
    }

    // Get the top 5 best-selling books based on sold copies
    public List<Book> getTop5Books() {
        return books.stream()
                .sorted(Comparator.comparingInt(Book::getSoldCopies).reversed())
                .limit(5)
                .toList();
    }

    // Update stock of a book by its title
    public boolean updateStock(String title, int newStock) {
        Optional<Book> bookOptional = books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst();

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setStock(newStock);
            return true; // Stock updated
        }
        return false; // Book not found
    }

    // Find a book by title
    public Book findBookByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    // Sell a book (reduce stock, increase sold copies)
    public boolean sellBook(String title, int quantity) {
        Book book = findBookByTitle(title);
        if (book != null && book.getStock() >= quantity) {
            book.setStock(book.getStock() - quantity);
            book.setSoldCopies(book.getSoldCopies() + quantity);
            return true; // Sale successful
        }
        return false; // Insufficient stock
    }
}
