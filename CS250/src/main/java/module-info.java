module paint {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;

    opens paint to javafx.fxml;
    exports paint;
}