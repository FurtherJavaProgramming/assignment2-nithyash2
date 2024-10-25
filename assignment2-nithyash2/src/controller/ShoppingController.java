package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;
import model.ShoppingCart;

import java.io.IOException;

public class ShoppingCartController {

    @FXML
    private TableView<Book> cartTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, Double> priceColumn;

    @FXML
    private TableColumn<Book, Integer> quantityColumn;

    @FXML
    private Label totalLabel;

    @FXML
    private Button checkoutButton;

    @FXML
    private Button backButton;

    private ShoppingCart cart;

    @FXML
    public void initialize() {
        cart = ShoppingCart.getInstance();

        // Set up the table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Load cart items into the table
        loadCartItems();

        // Calculate and display total
        updateTotal();
    }

    private void loadCartItems() {
        cartTable.getItems().setAll(cart.getItems());
    }

    private void updateTotal() {
        double total = cart.calculateTotal();
        totalLabel.setText("Total: $" + total);
    }

    @FXML
    public void handleCheckout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Checkout.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) checkoutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackToBookstore() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BookStore.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
