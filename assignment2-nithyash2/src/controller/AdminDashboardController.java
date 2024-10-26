package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Book;
import model.BookStore;
import model.Database;

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

    private ObservableList<Book> books;

    @FXML
    public void initialize() {
        // Initialize the columns in the TableView
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        soldCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().soldCopiesProperty().asObject());
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());

        // Load books from the BookStore
        books = FXCollections.observableArrayList(BookStore.getInstance().getBooks());
        bookTableView.setItems(books);
    }

    @FXML
    private void handleIncreaseStock() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                int adjustment = Integer.parseInt(stockAdjustmentField.getText());
                if (adjustment > 0) {
                    int newStock = selectedBook.getStock() + adjustment;
                    selectedBook.setStock(newStock);  // Update local object
                    Database.updateBookStock(selectedBook.getTitle(), newStock);  // Update in DB
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
                    selectedBook.setStock(newStock);  // Update local object
                    Database.updateBookStock(selectedBook.getTitle(), newStock);  // Update in DB
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
