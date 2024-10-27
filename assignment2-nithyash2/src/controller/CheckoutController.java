package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.ShoppingCart;
import model.Order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CheckoutController {

    @FXML
    private Label totalPriceLabel;
    @FXML
    private TextField cardNumberField;
    @FXML
    private TextField expiryDateField;
    @FXML
    private TextField cvvField;

    private final ShoppingCart cart = ShoppingCart.getInstance();

    @FXML
    public void initialize() {
        // Display the total price of items in the cart
        double totalPrice = cart.getTotalCost();
        totalPriceLabel.setText(String.format("$%.2f", totalPrice));
    }

    @FXML
    private void handleConfirmOrder() {
        // Validate payment details
        if (validatePaymentDetails()) {
            // Proceed with order placement
            processOrder();
            showAlert(Alert.AlertType.INFORMATION, "Order Confirmed", "Your order has been placed successfully.");
        }
    }

    private boolean validatePaymentDetails() {
        // Validate card number
        String cardNumber = cardNumberField.getText().trim();
        if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Card Number", "Please enter a 16-digit credit card number.");
            return false;
        }

        // Validate expiry date
        String expiryDate = expiryDateField.getText().trim();
        if (!isValidExpiryDate(expiryDate)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Expiry Date", "Please enter a valid future expiry date (MM/YY).");
            return false;
        }

        // Validate CVV
        String cvv = cvvField.getText().trim();
        if (cvv.length() != 3 || !cvv.matches("\\d{3}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid CVV", "Please enter a 3-digit CVV.");
            return false;
        }

        return true;  // All validations passed
    }

    private boolean isValidExpiryDate(String expiryDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            LocalDate enteredDate = LocalDate.parse("01/" + expiryDate, DateTimeFormatter.ofPattern("dd/MM/yy"));
            return enteredDate.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void processOrder() {
        // Deduct stock for each book in the cart and mark the order in the database
        Order order = new Order(cart.getItems(), cart.getTotalCost());
        order.save();  // Assumes Order class has save() method to record details in the database

        // Clear the cart after successful order
        cart.clearCart();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
