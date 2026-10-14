package main.java.paint;

import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class About {
    public void about() {
    Stage aboutWindow = new Stage();
    VBox aboutLayout = new VBox();
    Scene aboutScene = new Scene(aboutLayout, 400, 300);

    aboutWindow.setTitle("About Pain(t)");
    aboutWindow.setScene(aboutScene);
    aboutWindow.show();
    }
}