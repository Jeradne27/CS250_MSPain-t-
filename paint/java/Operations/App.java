package paint.Operations;

import javafx.application.Application;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.Canvas;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.BorderPane; 
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.stage.Stage;

import javafx.geometry.Pos;

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
import paint.Tools.FourPointStars;
import paint.Tools.FivePointStars;
import paint.Tools.SixPointStars;
import paint.Tools.Text;
import paint.Tools.Eraser;
import paint.Tools.Selection;

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
// - Empty state handling for the tab pane when no files are open.
// - Undo/Redo functionality for drawing operations.
// - Copy/Cut/Paste functionality for drawing operations.
// - live preview of drawing operations.

// ISSUES:
// - When selecting and moving shapes or pieces of an image, there is no preview of the movement
// - When moving elements, if it placed and picked up again, it also picks up another piece unintentionally.
// - There is no confirmation when copying or pasting elements, would like to add a blinking indicator when an element is ready to be pasted.

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
    private FourPointStars fourPointStars;
    private FivePointStars fivePointStars;
    private SixPointStars sixPointStars;
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

        Label titleLabel = new Label("MS Pain(t) by JRC<3");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.getStyleClass().add("title-bar");

        Label autosaveLabel = new Label();
        autosaveLabel.textProperty().bind(files.autosaveStatusAlert());

        var mainMenuBar = mainMenu.getMenuBar();
        mainMenuBar.setMaxWidth(Double.MAX_VALUE);

        HBox menuRow = new HBox(8, mainMenuBar, autosaveLabel);
        menuRow.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(mainMenuBar, Priority.ALWAYS);

        pane.setTop(new VBox(titleLabel, menuRow, top));

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
        StyleMenu styleMenu = new StyleMenu(fileCanvas, file.getDrawingArea());

        Lines lines = new Lines(fileCanvas, file.getPreviewCanvas());
        lines.setCanvasLogger(file::logCanvasAction);

        Pencil pencil = new Pencil(fileCanvas);
        pencil.setCanvasLogger(file::logCanvasAction);

        Eraser eraser = new Eraser(fileCanvas);
        eraser.setCanvasLogger(file::logCanvasAction);

        Rectangles rectangles = new Rectangles(fileCanvas, file.getPreviewCanvas());
        rectangles.setCanvasLogger(file::logCanvasAction);

        Squares squares = new Squares(fileCanvas, file.getPreviewCanvas());
        squares.setCanvasLogger(file::logCanvasAction);

        Circles circles = new Circles(fileCanvas, file.getPreviewCanvas());
        circles.setCanvasLogger(file::logCanvasAction);

        Ellipses ellipses = new Ellipses(fileCanvas, file.getPreviewCanvas());
        ellipses.setCanvasLogger(file::logCanvasAction);

        Triangles triangles = new Triangles(fileCanvas, file.getPreviewCanvas());
        triangles.setCanvasLogger(file::logCanvasAction);

        Diamonds diamonds = new Diamonds(fileCanvas, file.getPreviewCanvas());
        diamonds.setCanvasLogger(file::logCanvasAction);

        Hearts hearts = new Hearts(fileCanvas, file.getPreviewCanvas());
        hearts.setCanvasLogger(file::logCanvasAction);

        Pentagons pentagons = new Pentagons(fileCanvas, file.getPreviewCanvas());
        pentagons.setCanvasLogger(file::logCanvasAction);

        RightTriangles rightTriangles = new RightTriangles(fileCanvas, file.getPreviewCanvas());
        rightTriangles.setCanvasLogger(file::logCanvasAction);

        FourPointStars fourPointStars = new FourPointStars(fileCanvas, file.getPreviewCanvas());
        fourPointStars.setCanvasLogger(file::logCanvasAction);

        FivePointStars fivePointStars = new FivePointStars(fileCanvas, file.getPreviewCanvas());
        fivePointStars.setCanvasLogger(file::logCanvasAction);

        SixPointStars sixPointStars = new SixPointStars(fileCanvas, file.getPreviewCanvas());
        sixPointStars.setCanvasLogger(file::logCanvasAction);

        Text text = new Text(fileCanvas, edit -> file.canvasEdit("Text input", edit));
        
        Selection selection = new Selection(file);

        ToolMenu toolMenu = new ToolMenu(lines, pencil, eraser, rectangles, squares, circles, ellipses, triangles, diamonds, hearts, pentagons, rightTriangles, fourPointStars, fivePointStars, sixPointStars, text, selection, styleMenu, fileCanvas, file);
    
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
        top.getChildren().clear();

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