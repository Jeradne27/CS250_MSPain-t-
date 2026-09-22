package cs250;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuManager {
    private MenuBar menuBar;

    @SuppressWarnings("unused")
    private ImageFileManager fileManager;
    @SuppressWarnings("unused")
    private ViewManager viewManager;

    
    public MenuManager(ImageFileManager fileManager, ViewManager viewManager) { // sets up the menu items and connects them to action
        this.fileManager = fileManager;
        this.viewManager = viewManager;

        menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        Menu zoomMenu = new Menu("Zoom");

        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem zoomInItem = new MenuItem("Zoom In");
        MenuItem zoomOutItem = new MenuItem("Zoom Out");
        MenuItem resetZoomItem = new MenuItem("Reset Zoom");

        menuBar.getMenus().addAll(fileMenu, zoomMenu);

        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem, closeItem);
    
        zoomMenu.getItems().addAll(zoomInItem, zoomOutItem, resetZoomItem);

        openItem.setOnAction(event -> fileManager.openImage());
        
        saveItem.setOnAction(event -> fileManager.saveImage());
        saveAsItem.setOnAction(event-> fileManager.saveImageAs());
    
        closeItem.setOnAction(event -> fileManager.close());

        zoomInItem.setOnAction(event -> viewManager.zoomIn());
        zoomOutItem.setOnAction(event -> viewManager.zoomOut());
        resetZoomItem.setOnAction(event -> viewManager.resetZoom());
    }

    @SuppressWarnings("exports")
    public MenuBar getMenuBar() {
        return menuBar;
    }
}
