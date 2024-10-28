package controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Order;
import model.OrderItem;
import model.User;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OrderController {

    @FXML
    private TableView<Order> orderTableView;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, String> orderDateColumn;
    @FXML
    private TableColumn<Order, Double> totalPriceColumn;

    @FXML
    private Label selectedOrderTotalLabel;

    @FXML
    private TableView<OrderItem> orderItemsTable;
    @FXML
    private TableColumn<OrderItem, String> bookTitleColumn;
    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderItem, Double> priceColumn;
    @FXML
    private TableColumn<OrderItem, Double> totalColumn;

    private final ObservableList<Order> orders = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the columns for the orders table
        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().orderIdProperty().asObject());
        orderDateColumn.setCellValueFactory(cellData -> cellData.getValue().orderDateProperty().asString());
        totalPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());

        // Initialize the columns for the order items table
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().bookTitleProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        totalColumn.setCellValueFactory(cellData -> {
            double total = cellData.getValue().getPrice() * cellData.getValue().getQuantity();
            return new SimpleDoubleProperty(total).asObject();
        });

        // Load the orders into the table
        loadOrders();

        // Set up a listener to show items for the selected order
        orderTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showOrderItems(newValue));

        // Enable multiple selection for exporting selected orders
        orderTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void loadOrders() {
        // Retrieve the orders for the current user and add them to the observable list
        List<Order> userOrders = Order.getAllOrdersForUser(User.getInstance().getUsername());
        orders.setAll(userOrders);
        orders.sort((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate())); // Reverse chronological order
        orderTableView.setItems(orders);
    }

    private void showOrderItems(Order order) {
        if (order != null) {
            // Retrieve items and display them
            ObservableList<OrderItem> items = FXCollections.observableArrayList(order.getItems());
            orderItemsTable.setItems(items);
            System.out.println("Order ID: " + order.getOrderId() + ", Items loaded for display: " + items);

            // Update the selectedOrderTotalLabel with the total for the selected order
            selectedOrderTotalLabel.setText(String.format("$%.2f", order.getTotalPrice()));
        } else {
            orderItemsTable.setItems(FXCollections.observableArrayList());
            selectedOrderTotalLabel.setText("$0.00");
        }
    }

    @FXML
    public void handleExportOrders() {
        // Get selected orders from the TableView
        ObservableList<Order> selectedOrders = orderTableView.getSelectionModel().getSelectedItems();

        // Check if any orders are selected
        if (selectedOrders.isEmpty()) {
            showAlert("No Orders Selected", "Please select the orders you wish to export.");
            return;
        }

        // Show file chooser for user to save the file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Selected Orders to CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(orderTableView.getScene().getWindow());

        if (file != null) {
            exportOrdersToCSV(file, selectedOrders); // Pass selected orders to export method
        }
    }

    private void exportOrdersToCSV(File file, List<Order> selectedOrders) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Order ID,Date,Total Price,Book Title,Quantity\n");

            // Loop through selected orders only
            for (Order order : selectedOrders) {
                for (OrderItem item : order.getItems()) {
                    writer.write(order.getOrderId() + "," + order.getOrderDate() + "," + order.getTotalPrice() + ",");
                    writer.write(item.getBookTitle() + "," + item.getQuantity() + "\n");
                }
            }
            System.out.println("Selected orders exported successfully.");
        } catch (IOException e) {
            System.out.println("Error exporting orders: " + e.getMessage());
        }
    }

    @FXML
    public void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent dashboardPage = loader.load();
            Stage stage = (Stage) orderTableView.getScene().getWindow();
            stage.setScene(new Scene(dashboardPage));
        } catch (IOException e) {
            System.out.println("Error loading Dashboard page: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
