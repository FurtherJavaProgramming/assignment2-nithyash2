package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.ButtonType;
import model.Order;
import model.OrderItem;
import model.PaymentValidator;
import model.ShoppingCart;
import model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CheckoutController {

    @FXML
    private Text totalPrice;  // Display for total price

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField expiryDateField;

    @FXML
    private TextField cvvField;

    private final ShoppingCart cart = ShoppingCart.getInstance();
    private final User currentUser = User.getInstance();

    @FXML
    public void initialize() {
        // Display the total price in the Text field
        totalPrice.setText(String.format("$%.2f", cart.getTotalCost()));
    }

    @FXML
    private void handleCheckout() {
        // Confirm the total price with the user
        if (!confirmOrder()) {
            return;
        }

        // Collect and validate payment information
        String cardNumber = cardNumberField.getText();
        String expiryDate = expiryDateField.getText();
        String cvv = cvvField.getText();

        if (!validatePaymentInfo(cardNumber, expiryDate, cvv)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Payment Info", "Please check your payment details and try again.");
            return;
        }

        // Process the order
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> new OrderItem(cartItem.getBook().getTitle(), cartItem.getQuantity(), cartItem.getBook().getPrice()))
                .collect(Collectors.toList());

        double totalPriceValue = cart.getTotalCost();
        Order order = new Order(currentUser.getUsername(), orderItems, totalPriceValue);
        int orderId = order.saveOrder();  // Assume saveOrder generates and returns a unique order ID

        if (orderId != -1) {
            showAlert(Alert.AlertType.INFORMATION, "Order Successful", "Your order has been placed with Order ID: " + orderId);
            cart.clearCart();
            clearPaymentFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Order Failed", "There was an issue placing your order. Please try again.");
        }
    }

    private boolean confirmOrder() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Your total is " + totalPrice.getText() + ". Do you want to proceed?");
        confirmAlert.setTitle("Confirm Order");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private boolean validatePaymentInfo(String cardNumber, String expiryDate, String cvv) {
        return PaymentValidator.isValid(cardNumber, expiryDate, cvv, LocalDate.now());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearPaymentFields() {
        cardNumberField.clear();
        expiryDateField.clear();
        cvvField.clear();
    }
}
