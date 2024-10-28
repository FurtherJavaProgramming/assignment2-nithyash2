package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Database;  // Import the Database class
import view.LoginView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the necessary tables before starting the login view
        Database.createTables();
        // Start the login view after ensuring the database tables exist
        LoginView loginView = new LoginView();
        loginView.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
