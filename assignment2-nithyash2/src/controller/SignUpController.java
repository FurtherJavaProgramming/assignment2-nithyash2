package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Database;

import java.io.IOException;
import java.util.regex.Pattern;

public class SignUpController {

    @FXML
    private TextField signUpEmailField;

    @FXML
    private TextField signUpFirstNameField;

    @FXML
    private TextField signUpLastNameField;

    @FXML
    private PasswordField signUpPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label signUpMessageLabel;

    // Regular expression to validate a Gmail address
    private static final String GMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";

    @FXML
    public void handleSignUpButtonAction() {
        String email = signUpEmailField.getText();
        String firstName = signUpFirstNameField.getText();
        String lastName = signUpLastNameField.getText();
        String password = signUpPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if any fields are empty
        if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signUpMessageLabel.setText("Please fill in all fields.");
            return;
        }

        // Validate if the email is a Gmail address
        if (!Pattern.matches(GMAIL_REGEX, email)) {
            signUpMessageLabel.setText("Please enter a valid Gmail address.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            signUpMessageLabel.setText("Passwords do not match.");
            return;
        }

        // Insert user into the database
        Database.addUser(email, password, firstName, lastName);  // Call with all four arguments

        signUpMessageLabel.setText("Sign-Up Successful! You can now log in.");
    }

    // Handle "Back to Login" action to navigate back to the Login page
    @FXML
    public void handleBackToLoginAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signUpEmailField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
