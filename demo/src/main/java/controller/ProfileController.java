package controller;

import model.Database;
import model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfileController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField; // Read-Only

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button backButton; // Ensure this matches the fx:id in Profile.fxml

    @FXML
    public void initialize() {
        User user = User.getInstance();
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail()); // Display email but make it read-only
    }

    @FXML
    public void handleSaveChanges() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty()) {
            messageLabel.setText("First name and last name cannot be empty.");
            return;
        }

        if (!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            // If new password fields are filled, ensure they match
            if (!newPassword.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match.");
                return;
            }
        }

        // Update the profile in the database
        User user = User.getInstance();
        try (Connection conn = Database.connect()) {
            String updateSql = "UPDATE users SET first_name = ?, last_name = ?, password = ? WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);

            // Use the new password if provided, otherwise keep the existing one
            String passwordToSave = newPassword.isEmpty() ? user.getPassword() : newPassword;
            pstmt.setString(3, passwordToSave);
            pstmt.setString(4, user.getEmail());

            pstmt.executeUpdate();

            // Update the User singleton
            user.setFirstName(firstName);
            user.setLastName(lastName);
            if (!newPassword.isEmpty()) {
                user.setPassword(newPassword);
            }

            messageLabel.setText("Profile updated successfully.");
        } catch (SQLException e) {
            messageLabel.setText("Error updating profile.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow(); // Use the backButton to get the current stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
