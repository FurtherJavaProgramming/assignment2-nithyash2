package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;

public class ResetPasswordController {

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    // Handle the Reset Password button click
    @FXML
    public void handleResetPassword() {
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String usernameOrEmail = ForgotPasswordController.getUserIdentifier(); // Get the identifier from the previous controller

        // Validate that neither field is empty
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Password fields cannot be empty.");
            return;
        }

        // Check if the passwords match
        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        // Update the password in the database
        try (Connection conn = Database.connect()) {
            String sql = "UPDATE users SET password = ? WHERE username = ? OR email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword); // Consider hashing the password for security
            pstmt.setString(2, usernameOrEmail);
            pstmt.setString(3, usernameOrEmail);

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                messageLabel.setText("Password reset successfully.");
            } else {
                messageLabel.setText("User not found.");
            }
        } catch (SQLException e) {
            messageLabel.setText("Error updating password.");
            e.printStackTrace();
        }
    }

    // Handle the Go Back button click
    @FXML
    public void handleGoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) newPasswordField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
