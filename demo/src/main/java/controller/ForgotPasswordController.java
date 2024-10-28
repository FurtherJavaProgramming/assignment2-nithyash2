package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class ForgotPasswordController {

    @FXML
    private TextField usernameOrEmailField;

    @FXML
    private Label messageLabel;

    // Static variable to store the identifier
    private static String userIdentifier;

    @FXML
    public void handleForgotPassword() {
        String usernameOrEmail = usernameOrEmailField.getText().trim();

        if (usernameOrEmail.isEmpty()) {
            messageLabel.setText("Please enter your username or email.");
            return;
        }

        // Save the identifier for use in ResetPasswordController
        userIdentifier = usernameOrEmail;
        navigateToResetPassword();
    }

    // Method to navigate to the Reset Password screen
    private void navigateToResetPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ResetPassword.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameOrEmailField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Public method to allow ResetPasswordController access to the identifier
    public static String getUserIdentifier() {
        return userIdentifier;
    }
}
