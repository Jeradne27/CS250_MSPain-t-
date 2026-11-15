package paint.TopMenuBar;

import javafx.scene.layout.VBox;

import javafx.scene.Scene;

import javafx.stage.Stage;

/**
 * Represents the "About" window in the paint application.
 */

// IMPROVEMENTS:
// - N/A

// ISSUES:
// - The window has no information or content, which will be added towards the end of the project.

public class About {
    /**
     * Displays the "About" window.
     */
    public void about() {
        Stage aboutWindow = new Stage();
        VBox aboutLayout = new VBox();
        Scene aboutScene = new Scene(aboutLayout, 400, 300);

        aboutWindow.setTitle("About Paint");
        aboutWindow.setScene(aboutScene);
        aboutWindow.show();
    }
}