package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import model.Order;
import model.User;

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
    private TableView<OrderItem> orderItemsTable;
    @FXML
    private TableColumn<OrderItem, String> bookTitleColumn;
    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;

    private final ObservableList<Order> orders = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the columns
        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().orderIdProperty().asObject());
        orderDateColumn.setCellValueFactory(cellData -> cellData.getValue().orderDateProperty().asString());
        totalPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());

        // Set up orders
        loadOrders();

        orderTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showOrderItems(newValue));
    }

    private void loadOrders() {
        List<Order> userOrders = Order.getAllOrdersForUser(User.getInstance().getUsername());
        orders.setAll(userOrders);
        orders.sort((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate())); // Reverse chronological order
        orderTableView.setItems(orders);
    }

    private void showOrderItems(Order order) {
        if (order != null) {
            ObservableList<OrderItem> items = FXCollections.observableArrayList(order.getItems());
            orderItemsTable.setItems(items);
        } else {
            orderItemsTable.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void handleExportOrders() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Orders to CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(orderTableView.getScene().getWindow());

        if (file != null) {
            exportOrdersToCSV(file);
        }
    }

    private void exportOrdersToCSV(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Order ID,Date,Total Price,Book Title,Quantity\n");

            for (Order order : orders) {
                for (OrderItem item : order.getItems()) {
                    writer.write(order.getOrderId() + "," + order.getOrderDate() + "," + order.getTotalPrice() + ",");
                    writer.write(item.getBookTitle() + "," + item.getQuantity() + "\n");
                }
            }
            System.out.println("Orders exported successfully.");
        } catch (IOException e) {
            System.out.println("Error exporting orders: " + e.getMessage());
        }
    }
}
