package paint.TopMenuBar;

import java.util.function.Consumer; 

import javafx.scene.layout.VBox;

import javafx.scene.Scene;

import javafx.stage.Stage;

/**
 * Represents the "Help" window in the paint application.
 */

// IMPROVEMENTS:
// - N/A

// ISSUES:
// - The window has no information or content, which will be added towards the end of the project.

public class Help {
    /**
     * Displays the help screen.
     */
    public void help() {
        Stage helpWindow = new Stage();
        VBox helpLayout = new VBox();
        Scene helpScene = new Scene(helpLayout, 400, 300);

        helpWindow.setTitle("Paint Help");
        helpWindow.setScene(helpScene);
        helpWindow.show();
    }
}