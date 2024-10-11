package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Database;
import model.User;

import java.io.IOException;

public class ProfileController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private User currentUser;

    @FXML
    public void initialize() {
        // Load current user info from singleton or database
        currentUser = User.getInstance();

        // Display current user information
        firstNameField.setText(currentUser.getFirstName());
        lastNameField.setText(currentUser.getLastName());
        emailField.setText(currentUser.getEmail());
    }

    @FXML
    public void handleSaveProfile() {
        // Save updated profile info
        String updatedFirstName = firstNameField.getText();
        String updatedLastName = lastNameField.getText();
        String updatedPassword = passwordField.getText();

        // Update in the database
        Database.updateUserProfile(currentUser.getEmail(), updatedFirstName, updatedLastName, updatedPassword);

        // Update in the User singleton
        currentUser.setFirstName(updatedFirstName);
        currentUser.setLastName(updatedLastName);
        currentUser.setPassword(updatedPassword);
    }

    @FXML
    public void handleBackToDashboard() {
        try {
            // Load the Dashboard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) firstNameField.getScene().getWindow(); // Use any control to get the current window
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
