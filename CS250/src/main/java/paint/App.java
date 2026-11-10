package paint;

import javafx.application.Application;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Pos;

import javafx.scene.canvas.Canvas;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.BorderPane; 
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import paint.Operations.FileTab;
import paint.Operations.ImageFiles;

import paint.TopMenuBar.About;
import paint.TopMenuBar.Help;
import paint.TopMenuBar.Zoom;

import paint.Menus.MainMenu;
import paint.Menus.ToolMenu;
import paint.Menus.StyleMenu;

import paint.Tools.Lines;
import paint.Tools.Pencil;
import paint.Tools.Rectangles;
import paint.Tools.Squares;
import paint.Tools.Circles;
import paint.Tools.Ellipses;
import paint.Tools.Triangles;
import paint.Tools.Diamonds;
import paint.Tools.Hearts;
import paint.Tools.Pentagons;
import paint.Tools.RightTriangles;
import paint.Tools.Eraser;
import paint.Tools.Text;

import javafx.scene.Scene;

/**
 * The main application class for Paint.
 * Initializes and manages the main application window, including the tab pane, top menu bar, and active file tab.
 * Provides methods to manage the active file tab and add new image tabs.
 * Handles the layout and appearance of the main application window.
 * Manages the interaction between different components, such as the tab pane, top menu bar, and active file tab.
 * Provides a central point for coordinating the behavior of the application's various managers and UI elements.
 * Ensures that the application's state is consistent and that the UI reflects the current state of the active file tab.
 * Provides a mechanism for updating the UI when the active file tab changes, such as updating the top menu bar and zoom controls.
 * Manages the lifecycle of the application, including initialization, running, and closing of the main window.
 * Provides a central point for handling user interactions and coordinating the behavior of the application's components.
 * Ensures that the application remains responsive and that the UI updates correctly in response to changes in the active file tab.
 */

// IMPROVEMENTS:
// - Improve handling of active file tab changes to ensure UI consistency.
// - Optimize performance for large images and complex drawings.
// - Implement undo/redo functionality for drawing operations.
// - Enhance user interface for better usability and accessibility.

// ISSUES:
// - Ensure proper handling of edge cases, such as empty tabs or missing images.

public class App extends Application {
    private final BorderPane pane = new BorderPane();
    private final Scene scene = new Scene(pane);

    private final TabPane tabPane = new TabPane();
    private final VBox top = new VBox();

    private ImageFiles files;
    private Zoom zoom;
    private Help helpScreen;
    private About aboutScreen;

    private MainMenu mainMenu;
    private ToolMenu toolMenu;
    private StyleMenu styleMenu;
    private Lines lines;
    private Pencil pencil;
    private Rectangles rectangles;
    private Squares squares;
    private Circles circles;
    private Ellipses ellipses;
    private Triangles triangles;
    private Diamonds diamonds;
    private Hearts hearts;
    private Pentagons pentagons;
    private RightTriangles rightTriangles;
    private Text text;

    /**
     * Starts the application.
     * @param primaryStage The primary stage for the application.
     */
    public void start(Stage primaryStage) {
        
        // Initialize the image files manager with the primary stage, tab pane, and callback methods for active file tab and adding image tabs.
        files = new ImageFiles(primaryStage, tabPane, this::getActiveFileTab, this::addImageTab);

        // Initialize the zoom manager with the callback method for the active file tab.
        zoom = new Zoom(this::getActiveFileTab);

        helpScreen = new Help();
        aboutScreen = new About();

        mainMenu = new MainMenu(files, zoom, helpScreen, aboutScreen);

        tabPane.setMinSize(0, 0);
        pane.setCenter(tabPane);
        pane.setTop(top);


        // Add a listener to the tab selection model to update the tool and style menus when a different tab is selected.
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {showTabMenus();});

        // Add an initial image tab with the title "Untitled".
        addImageTab("Untitled");

        primaryStage.setScene(scene);
        primaryStage.setTitle("MS Paint");

        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Handle the close request for the primary stage to ensure proper cleanup of image files.
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            files.close();
        });

        primaryStage.show();
    }
    
    /**
     * Retrieves the currently active file tab.
     * @return The active file tab, or null if no tab is active.
     */
    private FileTab getActiveFileTab() {
        // Get the currently selected tab from the tab pane.
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        // If no tab is selected, return null.
        if (selectedTab == null) {
            return null;
        }

        // Return the FileTab instance associated with the selected tab.
        return (FileTab) selectedTab.getUserData();
    }

    /**
     * Adds a new image tab with the specified title.
     * @param title The title of the new image tab.
     * @return The newly created FileTab instance.
     */
    private FileTab addImageTab(String title) {
        FileTab file = new FileTab(title);
        Tab tab = file.getTab();
        Canvas fileCanvas = file.getCanvas();

        // Initialize the style menu for the new file tab.
        StyleMenu styleMenu = new StyleMenu(fileCanvas, file.getDrawingArea(), file);

        Lines lines = new Lines(fileCanvas);
        Pencil pencil = new Pencil(fileCanvas);
        Eraser eraser = new Eraser(fileCanvas);

        Rectangles rectangles = new Rectangles(fileCanvas);
        Squares squares = new Squares(fileCanvas);
        Circles circles = new Circles(fileCanvas);
        Ellipses ellipses = new Ellipses(fileCanvas);
        Triangles triangles = new Triangles(fileCanvas);
        Diamonds diamonds = new Diamonds(fileCanvas);
        Hearts hearts = new Hearts(fileCanvas);
        Pentagons pentagons = new Pentagons(fileCanvas);
        RightTriangles rightTriangles = new RightTriangles(fileCanvas);
        Text text = new Text(file, file::applyEdit);

        ToolMenu toolMenu = new ToolMenu(lines, pencil, eraser, rectangles, squares, circles, ellipses, triangles, diamonds, hearts, pentagons, rightTriangles, text, styleMenu, fileCanvas);
    
        file.setToolMenu(toolMenu);
        file.setStyleMenu(styleMenu);

        // Add a mouse released event handler to mark the file as modified if any drawing action occurred.
        fileCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            if (fileCanvas.getOnMouseReleased() != null
                || fileCanvas.getOnMouseDragged() != null) {
                    file.setModified(true);
            }
        });

        // Add a close request handler for the tab to prompt the user to save changes if necessary.
        tab.setOnCloseRequest(event -> {
           if (!files.confirmClose(file)) {
                event.consume();
           } 
        });

        // Add the new tab to the tab pane and make it the active tab.
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

        return file;
    }

    /**
     * Updates the top menu bar to reflect the currently active file tab's tool and style menus.
     */
    private void showTabMenus() {
        top.getChildren().setAll(mainMenu.getMenuBar());

        FileTab file = getActiveFileTab();

        // If no active file tab exists, do not update the top menu bar.
        if (file == null) {
            return;
        }

        // Add the active file tab's tool and style menus to the top menu bar.
        top.getChildren().add(file.getToolMenu().getToolMenu());
        top.getChildren().add(file.getStyleMenu().getToolBar());
    }
    
    /**
     * The main method for the application.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}