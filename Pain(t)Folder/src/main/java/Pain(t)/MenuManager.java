package cs250;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Manages the menus used throughout the application.
 */
// menumanager creates and manages the menus used throughout Pain(t),
// menumanager is responsible for organizing the apps menu options and 
// connecting the options with the corresponding methods throughout pain(t),
// funcionality is handled throughout the other java files and are called by 
// the menumanager to attatch to the menu items
public class MenuManager {

    // two separate menubars are used in the menumanager,
    // menubar contains the main app menus (file, zoom, help)
    // toolbar contains the drawing tools used by the user (line),
    // these menubars are in different areas of the app window with menubar
    // being at the top and toolbar being on the right edge
    private MenuBar menuBar;
    private MenuBar toolBar;

    // references to the managers responsible for each menus actions
    // these objects are created throughout the program and are passed into
    // this class
    // **SuppressWarnings were added because vscode didn't like it when
    //   they weren't there**

    /**
     * Initializes the MenuManager with the specified managers.
     * @param fileManager The ImageFileManager for handling image file operations.
     * @param viewManager The ViewManager for handling view operations.
     * @param toolManager The ToolManager for handling tool operations.
     * @param helpScreen The HelpScreen for displaying help information.
     */
    @SuppressWarnings("unused")
    private ImageFileManager fileManager;
    @SuppressWarnings("unused")
    private ViewManager viewManager;
    @SuppressWarnings("unused")
    private ToolManager toolManager;
    @SuppressWarnings("unused")
    private HelpScreen helpScreen;

    public MenuManager(ImageFileManager fileManager, ViewManager viewManager, ToolManager toolManager, HelpScreen helpScreen) {

        // initializes the apps menus and connects their actions to the appropriate
        // methods in other classes
        this.fileManager = fileManager;
        this.viewManager = viewManager;
        this.toolManager = toolManager;
        this.helpScreen = helpScreen;

        menuBar = new MenuBar();
        toolBar = new MenuBar();

        // categories displayed in the menubar
        Menu fileMenu = new Menu("File");
        Menu zoomMenu = new Menu("Zoom");
        Menu helpMenu = new Menu("Help");

        // categories displayed in the toolbar
        Menu lineMenu = new Menu("Line");

        // file operations that are handled by the imagefilemanager
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem closeItem = new MenuItem("Close");

        // viewing options handled by the viewmanager
        MenuItem zoomItem = new MenuItem("Zoom");
        MenuItem resetZoomItem = new MenuItem("Reset Zoom");

        // drawing options handled by the toolmanager
        MenuItem widthItem = new MenuItem("Width");
        MenuItem lineColorItem = new MenuItem("Color");

        // informational windows created by the helpscreen
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutItem = new MenuItem("About");

        // adds the menu categories to their respective menubars
        menuBar.getMenus().addAll(fileMenu, zoomMenu, helpMenu);
        toolBar.getMenus().addAll(lineMenu);

        // organizes the menu items into their respective categories
        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem, closeItem);
    
        zoomMenu.getItems().addAll(zoomItem, resetZoomItem);

        lineMenu.getItems().addAll(widthItem, lineColorItem);

        helpMenu.getItems().addAll(helpItem, aboutItem);

        // connects the file menu selections to the imagefilemanager methods
        openItem.setOnAction(event -> fileManager.openImage());
        saveItem.setOnAction(event -> fileManager.saveImage());
        saveAsItem.setOnAction(event-> fileManager.saveImageAs());
        closeItem.setOnAction(event -> fileManager.close());

        // connects the zoom menu selections to the viewmanager methods
        zoomItem.setOnAction(event -> viewManager.zoom());
        resetZoomItem.setOnAction(event -> viewManager.resetZoom());

        // connects the drawing tool menu selections to the toolmanager methods
        widthItem.setOnAction(event -> toolManager.changeWidth());
        lineColorItem.setOnAction(event -> toolManager.changeColor());

        // connects the help menu selections to the methods in the helpscreen class
        // that open the help/about windows
        helpItem.setOnAction(event -> helpScreen.help());
        aboutItem.setOnAction(event -> helpScreen.about());
    }

    /**
     * Returns the menu bar for the application.
     * @return The menu bar.
     */
    // getters used the app file, returns menubar and toolbar in order to place them at the 
    // top and right of the apps borderpane
    @SuppressWarnings("exports")
    public MenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Returns the tool bar for the application.
     * @return The tool bar.
     */
    @SuppressWarnings("exports")
    public MenuBar getToolBar() {
        return toolBar;
    }
}
