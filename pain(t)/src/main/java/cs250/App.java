package cs250;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.stage.Stage;

// sets the stage for Pain(t)
// creates canvas and starts application
public class App extends Application {

    BorderPane pane = new BorderPane();      
    Scene scene = new Scene(pane);
    ImageView imageView = new ImageView();
    Canvas canvas = new Canvas();
    GraphicsContext gc = canvas.getGraphicsContext2D();

    StackPane drawingArea = new StackPane();


    private ImageFileManager fileManager;
    private MenuManager menuManager; //calls in the MenuManager.java file
    private ViewManager viewManager;
    private DrawingManager drawingManager;

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        
        fileManager = new ImageFileManager(primaryStage, imageView, canvas, drawingArea);
        viewManager = new ViewManager(imageView);
        menuManager = new MenuManager(fileManager, viewManager);
        drawingManager = new DrawingManager(canvas);
        drawingManager.drawShapes();
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("MS Paint");
        primaryStage.setWidth(800); // starting window is bigger to accomodate most files
        primaryStage.setHeight(600); 

        drawingArea.setAlignment(Pos.CENTER);
        drawingArea.getChildren().addAll(imageView, canvas);// keeping this initial window size and resizing for larger files

        pane.setCenter(drawingArea);
        
        pane.setTop(menuManager.getMenuBar());
        
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            fileManager.close(); 
        });

        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
