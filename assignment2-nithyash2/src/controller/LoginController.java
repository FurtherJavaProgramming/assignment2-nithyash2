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

    private static LoginController instance;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    public LoginController() {
        // Constructor remains empty because JavaFX calls it when FXML is loaded
    }

    @FXML
    public void initialize() {
        instance = this;  // Set the instance when the controller is initialized
    }

    public static LoginController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Controller has not been instantiated yet!");
        }
        return instance;
    }

    @FXML
    public void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticateUser(username, password)) {
            messageLabel.setText("Login Successful!");

            // Navigate to Dashboard
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) usernameField.getScene().getWindow();
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
            Stage stage = (Stage) usernameField.getScene().getWindow();
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
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticateUser(String username, String password) {
        User user = User.getInstance();
        return user.validateCredentials(username, password);
    }
}
