module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.sql;  // Added to access java.sql package

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;

    // Open and export other packages as needed
    opens controller to javafx.fxml;
    exports controller;

    opens model to javafx.fxml;
    exports model;

    opens view to javafx.fxml;
    exports view;

    // Export model package for test visibility

}
