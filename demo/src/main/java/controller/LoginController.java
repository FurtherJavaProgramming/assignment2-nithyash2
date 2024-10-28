package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameOrEmailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    public void handleLoginButtonAction() {
        String usernameOrEmail = usernameOrEmailField.getText();
        String password = passwordField.getText();

        // Ensure username or email and password fields are not empty
        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter your username/email and password.");
            return;
        }

        // Authenticate the user
        if (authenticateUser(usernameOrEmail, password)) {
            messageLabel.setText("Login Successful!");

            // Navigate based on user type
            try {
                User user = User.getInstance();
                FXMLLoader loader;

                if (user.isAdmin()) {  // Use the isAdmin flag to determine if it's an admin
                    // Navigate to Admin Dashboard
                    loader = new FXMLLoader(getClass().getResource("/view/AdminDashboard.fxml"));
                } else {
                    // Navigate to User Dashboard
                    loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
                }

                Parent root = loader.load();
                Stage stage = (Stage) usernameOrEmailField.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    public void handleForgotPasswordAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ForgotPassword.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameOrEmailField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSignUpLinkAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameOrEmailField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticateUser(String usernameOrEmail, String password) {
        User user = User.getInstance();
        return user.validateCredentials(usernameOrEmail, password);
    }
}
