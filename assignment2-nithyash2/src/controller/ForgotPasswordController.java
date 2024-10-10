package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    public void handleSubmitAction() {
        String email = emailField.getText();

        if (email.isEmpty()) {
            messageLabel.setText("Please enter your email.");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            messageLabel.setText("Please enter a valid email address.");
        } else {
            // Instead of sending an email, we just display a success message
            messageLabel.setText("Password recovery instructions would be sent to " + email);
        }
    }
}
