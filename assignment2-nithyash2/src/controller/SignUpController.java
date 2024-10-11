package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Database;
import model.User;

public class SignUpController {

    @FXML
    private TextField usernameField; // Add this

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    public void handleSignUpButtonAction() {
        String username = usernameField.getText();  // Retrieve username
        String email = emailField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        // Save the user in the database
        Database.addUser(username, email, password, firstName, lastName);
    }
}
