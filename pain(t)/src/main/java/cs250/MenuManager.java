package cs250;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuManager {
    private MenuBar menuBar;

    @SuppressWarnings("unused")
    private ImageFileManager fileManager;
    
    public MenuManager(ImageFileManager fileManager) {
        this.fileManager = fileManager;

        menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");

        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem closeItem = new MenuItem("Close");

        menuBar.getMenus().add(fileMenu);

        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem, closeItem);

        openItem.setOnAction(event -> fileManager.openImage());
        
        saveItem.setOnAction(event -> fileManager.saveImage());
        saveAsItem.setOnAction(event-> fileManager.saveImageAs());
    
        closeItem.setOnAction(event -> fileManager.close());
    }

    @SuppressWarnings("exports")
    public MenuBar getMenuBar() {
        return menuBar;
    }
}
