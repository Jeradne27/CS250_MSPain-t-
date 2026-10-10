package cs250;

import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Manages the help and about screens for the application.
 */
// creates separate windows for the help menu item and the about menu item
// without interupting the primary window
public class HelpScreen {
    /**
     * Displays the help screen.
     */
    public void help() {

        // opens the help window
        // will include content explaining how to use Pain(t) inside the 
        // helpLayout VBox.
        Stage helpWindow = new Stage();
        VBox helpLayout = new VBox();
        Scene helpScene = new Scene(helpLayout, 400, 300);

        helpWindow.setTitle("Pain(t) Help");
        helpWindow.setScene(helpScene);
        helpWindow.show();
    }
    /**
     * Displays the about screen.
     */
    public void about() {
        
        // opens the about window
        // will include information about pain(t) (purpose, version, etc,)
        // can be included inside the aboutLayout VBox
        Stage aboutWindow = new Stage();
        VBox aboutLayout = new VBox();
        Scene aboutScene = new Scene(aboutLayout, 400, 300);

        aboutWindow.setTitle("About Pain(t)");
        aboutWindow.setScene(aboutScene);
        aboutWindow.show();
    }
}
