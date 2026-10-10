package cs250;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;

// The App.java file is responsible for creating the main window
// Pain(t) and for connecting the different manager classes. App
// doesn't handle much functionality. Open MenuManager.java for menu
// item funcionality.

/**
 * The main application class for Pain(t).
 */
public class App extends Application {
    // main layout and scene of the application window
    BorderPane pane = new BorderPane();      
    Scene scene = new Scene(pane);

    // ImageView displays the opened file, while canvas sits on
    // top of imageView to allow the user to draw on the image.
    // ImageView stays unchanged when the user draws on the canvas.
    ImageView imageView = new ImageView();
    Canvas canvas = new Canvas();

    // drawingArea layers the image and canvas.
    // workSpace keeps the drawingArea centered.
    StackPane workSpace = new StackPane();
    StackPane drawingArea = new StackPane();

    // To divide the program into smaller pieces, each manager
    // handles a different responsiblity.
    private ImageFileManager fileManager; // opening, saving, closing images
    private MenuManager menuManager;      // menu bars, menus, and menu items
                                          //    with connected actions
    private ViewManager viewManager;      // viewing options and zoom controls
    private DrawingManager drawingManager;// drawings operations on the canvas
    private ToolManager toolManager;      // holds drawing tools and settings,
                                          //    includes width and color 
    private HelpScreen helpScreen;        // displays help and about windows

    /**
     * Starts the application.
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        
        // constructors for the app managers and access to JavaFX components or
        // other managers if necessary
        fileManager = new ImageFileManager(primaryStage, imageView, canvas, drawingArea);
        drawingManager = new DrawingManager(canvas);

        // allows mouse events for the canvas, used by line tool in ToolManager
        drawingManager.drawLine();

        toolManager = new ToolManager(drawingManager);
        viewManager = new ViewManager(drawingArea);
        helpScreen = new HelpScreen();

        // menuManager receives the other managers to allow events from the menubar
        // to trigger funcionality
        menuManager = new MenuManager(fileManager, viewManager, toolManager, helpScreen);
        
        // layering of the canvas over the imageview
        drawingArea.getChildren().addAll(imageView, canvas);

        // prevents the drawing area from expanding to fill the window
        drawingArea.setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);

        // centering the drawing area inside the workspace
        workSpace.getChildren().add(drawingArea);
        workSpace.setAlignment(Pos.CENTER);

        // CSS class is used to style the workspace and other features of Pain(t),
        // inside style.css file
        workSpace.getStyleClass().add("workspace");

        pane.setCenter(workSpace);

        // two containers are used at the top and right of pain(t),
        // the top container holds the menubar with file control, zoom controls, 
        // and help controls, the right container holds the drawing tools, currently
        // includes line menu with width and color
        VBox top = new VBox();
        VBox right = new VBox();

        top.getChildren().add(menuManager.getMenuBar());
        right.getChildren().add(menuManager.getToolBar());
        
        pane.setTop(top);
        pane.setRight(right);

        // primary stage and title
        primaryStage.setScene(scene);
        primaryStage.setTitle("MS Paint");

        // default window size, when resized, workspace remains centererd, the window is large
        // to allow for larger files before resizing
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        
        // applies styling from style.css file
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // comsuming the close resquest event stops the app from closing immediately, sends
        // program to the close filemanager for smart save options
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            fileManager.close(); 
        });

        primaryStage.show();
    }
    
    /**
     * The main method for the application.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
