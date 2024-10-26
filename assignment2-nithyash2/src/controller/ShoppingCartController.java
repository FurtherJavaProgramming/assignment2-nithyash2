package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.CartItem;
import model.ShoppingCart;

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

    private final ShoppingCart cart = ShoppingCart.getInstance();
    private ObservableList<CartItem> cartItems;

    @FXML
    public void initialize() {
        loadCartItems();
        updateTotalCost();
    }

    private void loadCartItems() {
        cartItems = FXCollections.observableArrayList(cart.getItems());

        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().titleProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().priceProperty().asObject());
        totalColumn.setCellValueFactory(cellData ->
                cellData.getValue().totalCostProperty().asObject()
        );

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
                    if (quantity > 0 && quantity <= selectedItem.getBook().getStock()) {
                        cart.updateItemQuantity(selectedItem, quantity);
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
            cart.removeItem(selectedItem.getBook());
            cartItems.remove(selectedItem);
            updateTotalCost();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to remove.");
        }
    }

    @FXML
    private void handleCheckout() {
        // Logic for checkout, could involve navigating to a payment page
        showAlert(Alert.AlertType.INFORMATION, "Checkout", "Proceeding to checkout...");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
