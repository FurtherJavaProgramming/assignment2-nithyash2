package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Book;
import model.BookStore;
import model.ShoppingCart;
import model.User;
import java.io.IOException;

public class DashboardController {

    @FXML
    private BorderPane dashboardPane;
    @FXML
    private Label welcomeLabel;
    @FXML
    private ListView<String> booksListView; // ListView to display the top 5 popular books
    @FXML
    private TableView<Book> allBooksTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private TableColumn<Book, Integer> stockColumn;
    @FXML
    private TextField quantityField; // Field to enter quantity
    @FXML
    private Button addToCartButton;  // Button to add books to cart

    private final ShoppingCart cart = ShoppingCart.getInstance();

    @FXML
    public void initialize() {
        User currentUser = User.getInstance();
        welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + "!");
        loadTop5Books();
        loadAllBooks();
    }

    private void loadTop5Books() {
        BookStore bookstore = BookStore.getInstance();
        ObservableList<String> topBooks = FXCollections.observableArrayList();

        bookstore.getTop5Books().forEach(book -> {
            topBooks.add(book.getTitle() + " - Sold: " + book.getSoldCopies());
        });

        booksListView.setItems(topBooks);
    }

    private void loadAllBooks() {
        ObservableList<Book> allBooks = FXCollections.observableArrayList(BookStore.getInstance().getBooks());

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        allBooksTable.setItems(allBooks);
    }

    @FXML
    private void handleAddToCart() {
        Book selectedBook = allBooksTable.getSelectionModel().getSelectedItem();
        User currentUser = User.getInstance();
        if (selectedBook != null) {
            try {
                int quantity = Integer.parseInt(quantityField.getText().trim());
                if (quantity > 0 && quantity <= selectedBook.getStock()) {
                    cart.addItem(currentUser.getUsername(), selectedBook, quantity); // Include username in call
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Book added to cart successfully.");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Stock Limit", "Quantity exceeds stock available.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a valid number for quantity.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) dashboardPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OrderView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) dashboardPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading Orders page: " + e.getMessage());
        }
    }




    @FXML
    public void handleSettings() {
        System.out.println("Navigating to Settings page...");
    }

    @FXML
    public void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("You have successfully logged out.");
        alert.showAndWait();

        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleViewCart() {
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

}
