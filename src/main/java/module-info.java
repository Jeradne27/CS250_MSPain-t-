module paint {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;

    opens Operations to javafx.fxml;
    exports Operations;
    exports TopMenuBar;
    exports Menus;
    exports Tools;  
}