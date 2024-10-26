package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;
import model.BookStore;
import model.ShoppingCart;
import model.User; // Make sure this import exists for accessing the user

import java.io.IOException;

public class BooksController {

    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, Double> priceColumn;

    @FXML
    private TableColumn<Book, Integer> stockColumn;

    @FXML
    private Button addToCartButton;

    @FXML
    private Button backButton;

    private ShoppingCart cart;

    @FXML
    public void initialize() {
        // Set up the BookStore and ShoppingCart
        cart = ShoppingCart.getInstance();

        // Configure table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Load books from the BookStore into the TableView
        loadBooks();
    }

    private void loadBooks() {
        BookStore bookStore = BookStore.getInstance();
        ObservableList<Book> booksList = FXCollections.observableArrayList(bookStore.getBooks());
        booksTable.setItems(booksList);
    }

    @FXML
    public void handleAddToCart() {
        // Get the selected book from the table
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Get the username from the User instance
            String username = User.getInstance().getUsername();

            // Add the book to the shopping cart with the username
            cart.addItem(username, selectedBook, 1); // Assuming quantity is 1 for simplicity
            System.out.println("Book added to cart: " + selectedBook.getTitle());
        }
    }

    @FXML
    public void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error navigating back to Dashboard.");
        }
    }
}
