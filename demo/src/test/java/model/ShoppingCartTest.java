package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Uses the books seeded into the local SQLite database on first run. */
class ShoppingCartTest {

    private static final String USER = "cartTestUser";
    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        Database.createTables();
        cart = ShoppingCart.getInstance();
        cart.clearCart();
    }

    @Test
    void addingItemsUpdatesTotal() {
        cart.addItem(USER, new Book("Clean Code", "Robert Martin", 45.0, 100), 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(90.0, cart.getTotalCost(), 0.001);
    }

    @Test
    void addingSameBookAgainIncreasesQuantity() {
        Book book = new Book("Clean Code", "Robert Martin", 45.0, 100);
        cart.addItem(USER, book, 2);
        cart.addItem(USER, book, 1);
        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().get(0).getQuantity());
        assertEquals(135.0, cart.getTotalCost(), 0.001);
    }

    @Test
    void cannotAddMoreThanAvailableStock() {
        // "Java Program Design" is seeded with only 2 copies
        cart.addItem(USER, new Book("Java Program Design", "Cohoon and Davidson", 29.0, 2), 5);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void removingItemEmptiesCart() {
        Book book = new Book("Clean Code", "Robert Martin", 45.0, 100);
        cart.addItem(USER, book, 1);
        cart.removeItem(USER, book);
        assertTrue(cart.getItems().isEmpty());
        assertEquals(0.0, cart.getTotalCost(), 0.001);
    }
}
