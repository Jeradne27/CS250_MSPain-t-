package paint.Menus;

import paint.Operations.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import paint.Operations.ImageFiles;
import paint.TopMenuBar.About;
import paint.TopMenuBar.Help;
import paint.TopMenuBar.Zoom;


/**
 * MainMenu class represents the main menu bar for the application.
 * It manages the menu items and their actions, including file operations, zooming, and accessing help and about screens.
 * Provides keyboard shortcuts for common actions like open, save, and zoom.
 * Ensures that the menu items are properly organized and accessible for a better user experience.
 * Handles the dynamic updating of the zoom menu to reflect the current zoom level.
 * Provides a consistent and user-friendly interface for accessing various application features.
 */

// IMPROVEMENTS:
// - Add more keyboard shortcuts for frequently used actions.
// - Improve the layout and organization of the menu items for better user experience.
// - Add more options for the zoom menu.

// ISSUES: 
// - The zoom menu currently only has a reset option and lacks keyboard shortcuts for zooming in and out.
// - The help and about menu items do not have keyboard shortcuts.
// - The close menu item does not have a keyboard shortcut.
// - The reset zoom menu item does not have a keyboard shortcut.
// - The zoom slider doesn't start at the middle position, which is bothersome.

public class MainMenu {
    
    private MenuBar menuBar;

    // calls to other classes for handling specific operations
    private ImageFiles files;
    private Zoom zoom;
    private Help helpScreen;
    private About aboutScreen;

    /**
     * Creates a new main menu for the application.
     * Initializes the main menu with the specified managers and screens.
     * @param files the image files manager
     * @param zoom the zoom manager
     * @param helpScreen the help screen
     * @param aboutScreen the about screen
     */
    public MainMenu(ImageFiles files,  Zoom zoom, Help helpScreen, About aboutScreen) {

        
        this.files = files;
        this.zoom = zoom;
        this.helpScreen = helpScreen;
        this.aboutScreen = aboutScreen;

        menuBar = new MenuBar();

        // categories displayed in the menubar
        Menu fileMenu = new Menu("File");
        Menu zoomMenu = new Menu("Zoom");
        Menu helpMenu = new Menu("Help");

        // file operations that are handled by the imagefiles manager
        MenuItem openItem = new MenuItem("Open");
        MenuItem newTabItem = new MenuItem("New Tab");
        MenuItem openTabItem = new MenuItem("Open In New Tab");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem closeItem = new MenuItem("Close");

        // viewing options handled by the viewmanager
        MenuItem resetZoomItem = new MenuItem("Reset Zoom");

        // informational windows created by the helpscreen
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutItem = new MenuItem("About");

        menuBar.getMenus().addAll(fileMenu, zoomMenu, helpMenu);

        fileMenu.getItems().addAll(openItem, newTabItem, openTabItem, saveItem, saveAsItem, closeItem);
    
        zoomMenu.getItems().addAll(resetZoomItem);

        // allows for zoom to be an on-screen menu instead of opening a seprate window to operate.
        zoomMenu.setOnShowing(event -> {
            zoomMenu.getItems().setAll(zoom.zoom(), resetZoomItem);
        });

        helpMenu.getItems().addAll(helpItem, aboutItem);

        // KEYBOARD SHORTCUTS: Open, New Tab, Save, Save as
        openItem.setOnAction(event -> files.openImage());
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        newTabItem.setOnAction(event -> files.newImage());
        newTabItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

        openTabItem.setOnAction(event -> files.openImageInNewTab());
        openTabItem.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));

        saveItem.setOnAction(event -> files.saveImage());
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        saveAsItem.setOnAction(event-> files.saveImageAs());
        saveAsItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        closeItem.setOnAction(event -> files.close());
        closeItem.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));

        resetZoomItem.setOnAction(event -> zoom.resetZoom());
        resetZoomItem.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));

        // connects the help menu selections to the methods in the helpscreen class
        // that open the help/about windows
        helpItem.setOnAction(event -> helpScreen.help());
        helpItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        aboutItem.setOnAction(event -> aboutScreen.about());
        aboutItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
    }

    /**
     * Returns the menu bar for the application.
     * @return The menu bar.
     */
    @SuppressWarnings("exports")
    public MenuBar getMenuBar() {
        return menuBar;
    }
}
