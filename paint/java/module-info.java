module paint {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;

    opens paint.Operations to javafx.fxml;
    exports paint.Operations;
    exports paint.TopMenuBar;
    exports paint.Menus;
    exports paint.Tools;
}