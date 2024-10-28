package controller;

import model.Book;
import model.BookStore;
import model.ShoppingCart;
import model.User;  // Import User to retrieve username
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.io.IOException;

public class BookStoreController {

    @FXML
    private ListView<Book> bookListView;

    @FXML
    private TextField quantityField;

    @FXML
    private Button addToCartButton;

    @FXML
    private Button viewCartButton;

    private final BookStore bookStore = BookStore.getInstance();
    private final ShoppingCart shoppingCart = ShoppingCart.getInstance();

    @FXML
    public void initialize() {
        // Populate the ListView with books from the bookstore
        ObservableList<Book> books = FXCollections.observableArrayList(bookStore.getBooks());
        bookListView.setItems(books);
        bookListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    public void handleAddToCart(ActionEvent event) {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                int quantity = Integer.parseInt(quantityField.getText().trim());
                if (quantity > 0) {
                    String username = User.getInstance().getUsername(); // Retrieve the current user's username
                    shoppingCart.addItem(username, selectedBook, quantity); // Pass username to addItem
                    showAlert(AlertType.INFORMATION, "Success", "Book added to cart successfully.");
                } else {
                    showAlert(AlertType.ERROR, "Invalid Quantity", "Please enter a valid quantity.");
                }
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid number for quantity.");
            }
        } else {
            showAlert(AlertType.ERROR, "No Book Selected", "Please select a book to add to the cart.");
        }
    }

    @FXML
    public void handleViewCart(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ShoppingCart.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Shopping Cart");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
