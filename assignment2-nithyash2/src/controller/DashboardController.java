package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Book;
import model.BookStore;
import model.User;
import java.io.IOException;

public class DashboardController {

    @FXML
    private BorderPane dashboardPane;

    @FXML
    private Label welcomeLabel;

    // ListView to display the top 5 popular books
    @FXML
    private ListView<String> booksListView;

    // TableView and columns for displaying all available books
    @FXML
    private TableView<Book> allBooksTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, Double> priceColumn;

    @FXML
    private TableColumn<Book, Integer> stockColumn;

    // Initialize the Dashboard with User Information and book lists
    @FXML
    public void initialize() {
        User currentUser = User.getInstance();
        welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + "!");

        // Load the top 5 popular books
        loadTop5Books();

        // Load all available books in the bookstore
        loadAllBooks();
    }

    @FXML
    public void handleProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Profile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) dashboardPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method to load the top 5 books and display them in the ListView
    private void loadTop5Books() {
        BookStore bookstore = BookStore.getInstance();
        ObservableList<String> topBooks = FXCollections.observableArrayList();

        bookstore.getTop5Books().forEach(book -> {
            topBooks.add(book.getTitle() + " - Sold: " + book.getSoldCopies());
        });

        booksListView.setItems(topBooks);
    }

    // Method to load all available books in the bookstore and display them in the TableView
    private void loadAllBooks() {
        ObservableList<Book> allBooks = FXCollections.observableArrayList(BookStore.getInstance().getBooks());

        // Set cell value factories for each column
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        allBooksTable.setItems(allBooks);
    }



    @FXML
    public void handleBooks() {
        // Navigate to the Books page
        System.out.println("Navigating to Books page...");
        // Implement books navigation logic
    }

    @FXML
    public void handleOrders() {
        // Navigate to the Orders page
        System.out.println("Navigating to Orders page...");
        // Implement orders navigation logic
    }

    @FXML
    public void handleSettings() {
        // Navigate to the Settings page
        System.out.println("Navigating to Settings page...");
        // Implement settings navigation logic
    }

    @FXML
    public void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("You have successfully logged out.");
        alert.showAndWait();

        // Close the dashboard stage
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.close();
    }
}
