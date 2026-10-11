package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void sellingReducesStockAndCountsSoldCopies() {
        Book book = new Book("Clean Code", "Robert Martin", 45.0, 5);
        assertTrue(book.sellBook(2));
        assertEquals(3, book.getStock());
        assertEquals(2, book.getSoldCopies());
    }

    @Test
    void cannotSellMoreThanInStock() {
        Book book = new Book("Clean Code", "Robert Martin", 45.0, 1);
        assertFalse(book.sellBook(2));
        assertEquals(1, book.getStock());
    }

    @Test
    void restockAddsToStock() {
        Book book = new Book("Clean Code", "Robert Martin", 45.0, 1);
        book.restockBook(4);
        assertEquals(5, book.getStock());
    }
}
