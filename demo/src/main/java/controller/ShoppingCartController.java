package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.CartItem;
import model.ShoppingCart;
import model.Database;
import javafx.scene.control.Button;
import model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class ShoppingCartController {

    @FXML
    private TableView<CartItem> cartTableView;
    @FXML
    private TableColumn<CartItem, String> titleColumn;
    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;
    @FXML
    private TableColumn<CartItem, Double> priceColumn;
    @FXML
    private TableColumn<CartItem, Double> totalColumn;
    @FXML
    private Label totalCostLabel;
    @FXML
    private Button proceedToCheckoutButton;

    private final ShoppingCart cart = ShoppingCart.getInstance();
    private final User currentUser = User.getInstance();
    private ObservableList<CartItem> cartItems;

    @FXML
    public void initialize() {
        loadCartItemsFromDatabase();
        updateTotalCost();
    }

    private void loadCartItemsFromDatabase() {
        // Load items directly from the database for the current user
        cart.loadItemsFromDatabase(currentUser.getUsername());
        cartItems = FXCollections.observableArrayList(cart.getItems());

        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().titleProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().priceProperty().asObject());
        totalColumn.setCellValueFactory(cellData -> {
            double totalCost = cellData.getValue().getBook().getPrice() * cellData.getValue().getQuantity();
            return new javafx.beans.property.SimpleDoubleProperty(totalCost).asObject();
        });

        cartTableView.setItems(cartItems);
    }

    private void updateTotalCost() {
        double totalCost = cart.getTotalCost();
        totalCostLabel.setText(String.format("$%.2f", totalCost));
    }



    @FXML
    private void handleUpdateQuantity() {
        CartItem selectedItem = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedItem.getQuantity()));
            dialog.setTitle("Update Quantity");
            dialog.setHeaderText("Update quantity for " + selectedItem.getBook().getTitle());
            dialog.setContentText("Enter new quantity:");

            dialog.showAndWait().ifPresent(quantityStr -> {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    String title = selectedItem.getBook().getTitle();

                    // Check against real-time stock from the database
                    if (quantity > 0 && Database.checkBookStock(title, quantity)) {
                        cart.updateItemQuantity(currentUser.getUsername(), selectedItem, quantity); // Update cart
                        cartTableView.refresh();
                        updateTotalCost();
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Invalid Quantity", "Quantity exceeds available stock.");
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to update.");
        }
    }


    @FXML
    private void handleRemoveItem() {
        CartItem selectedItem = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cart.removeItem(currentUser.getUsername(), selectedItem.getBook());
            cartItems.remove(selectedItem);
            updateTotalCost();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to remove.");
        }
    }

    @FXML
    private void proceedToCheckout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Checkout.fxml"));
            Parent checkoutPage = loader.load();
            Stage stage = (Stage) proceedToCheckoutButton.getScene().getWindow();
            stage.setScene(new Scene(checkoutPage));
        } catch (IOException e) {
            System.out.println("Error loading Checkout page: " + e.getMessage());
        }
    }





    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
