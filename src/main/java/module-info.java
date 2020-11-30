module com.bb {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.bb to javafx.fxml;
    exports com.bb;
}