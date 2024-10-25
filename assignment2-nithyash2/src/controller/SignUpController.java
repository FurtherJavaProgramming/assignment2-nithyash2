package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import java.io.IOException;
import model.Database;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField signUpFirstNameField;

    @FXML
    private TextField signUpLastNameField;

    @FXML
    private TextField signUpEmailField;

    @FXML
    private PasswordField signUpPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label signUpMessageLabel;

    @FXML
    public void handleSignUpButtonAction() {
        String username = usernameField.getText().trim();
        String firstName = signUpFirstNameField.getText().trim();
        String lastName = signUpLastNameField.getText().trim();
        String email = signUpEmailField.getText().trim().toLowerCase();
        String password = signUpPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Validate fields to ensure they are not empty and passwords match
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signUpMessageLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            signUpMessageLabel.setText("Passwords do not match.");
            return;
        }

        // Save the user in the database
        Database.addUser(username, email, password, firstName, lastName);
        signUpMessageLabel.setText("Sign up successful!");
    }

    // Method to handle navigation back to the Login screen
    @FXML
    public void handleBackToLoginAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow(); // Use any existing field to get the current window
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
