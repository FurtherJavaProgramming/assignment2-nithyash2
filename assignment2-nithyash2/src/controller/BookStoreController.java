package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import model.Book;
import model.BookStore;
import model.ShoppingCart;

public class BookStoreController {

    @FXML
    private ListView<Book> bookListView; // List to display available books

    @FXML
    private Button addToCartButton; // Button to add selected book to the cart

    @FXML
    private TextField quantityField; // Field to specify quantity of the book to be added

    @FXML
    private Button viewCartButton; // Button to view shopping cart

    private BookStore bookStore;
    private ShoppingCart shoppingCart;

    @FXML
    public void initialize() {
        bookStore = BookStore.getInstance();
        shoppingCart = ShoppingCart.getInstance();

        // Populate the book list view with books from the store
        ObservableList<Book> bookObservableList = FXCollections.observableArrayList(bookStore.getBooks());
        bookListView.setItems(bookObservableList);
        bookListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    // Handle adding a book to the cart
    @FXML
    public void handleAddToCart() {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                if (quantity > 0 && selectedBook.getStock() >= quantity) {
                    shoppingCart.addItem(selectedBook, quantity);
                    selectedBook.setStock(selectedBook.getStock() - quantity); // Decrease stock after adding to cart
                } else {
                    // Handle error (insufficient stock or invalid quantity)
                    System.out.println("Invalid quantity or insufficient stock.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid quantity.");
            }
        }
    }

    // Navigate to view cart page
    @FXML
    public void handleViewCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ShoppingCart.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) viewCartButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

