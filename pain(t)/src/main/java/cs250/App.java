package cs250;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    BorderPane pane = new BorderPane();      
    Scene scene = new Scene(pane);
    ImageView imageView = new ImageView();

    private ImageFileManager fileManager;
    private MenuManager menuManager;

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        
        fileManager = new ImageFileManager(primaryStage, imageView);
        menuManager = new MenuManager(fileManager);

        primaryStage.setScene(scene);
        primaryStage.setTitle("MS Paint");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        pane.setCenter(imageView);
        
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
