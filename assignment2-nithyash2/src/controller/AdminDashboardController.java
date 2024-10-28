package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Book;
import model.BookStore;
import model.Database;
import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private TableColumn<Book, Integer> soldCopiesColumn;
    @FXML
    private TableColumn<Book, Integer> stockColumn;
    @FXML
    private TextField stockAdjustmentField;

    @FXML
    private Button logoutButton;  // Add reference to the Logout button

    private ObservableList<Book> books;

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        soldCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().soldCopiesProperty().asObject());
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());

        books = FXCollections.observableArrayList(BookStore.getInstance().getBooks());
        bookTableView.setItems(books);
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent loginPage = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading Login page: " + e.getMessage());
        }
    }

    @FXML
    private void handleIncreaseStock() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                int adjustment = Integer.parseInt(stockAdjustmentField.getText());
                if (adjustment > 0) {
                    int newStock = selectedBook.getStock() + adjustment;
                    selectedBook.setStock(newStock);
                    Database.updateBookStock(selectedBook.getTitle(), newStock);
                    bookTableView.refresh();
                } else {
                    showAlert("Enter a positive number.");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid number format.");
            }
        }
    }

    @FXML
    private void handleDecreaseStock() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                int adjustment = Integer.parseInt(stockAdjustmentField.getText());
                if (adjustment > 0 && selectedBook.getStock() - adjustment >= 0) {
                    int newStock = selectedBook.getStock() - adjustment;
                    selectedBook.setStock(newStock);
                    Database.updateBookStock(selectedBook.getTitle(), newStock);
                    bookTableView.refresh();
                } else {
                    showAlert("Invalid stock adjustment.");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid number format.");
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
