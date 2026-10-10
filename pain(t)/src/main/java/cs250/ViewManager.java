package cs250;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Slider;

/**
 * Manages the view options for pain(t).
 */
public class ViewManager{
    // manages the zoom functionality for pain(t)
    // viewmanager controls the visual scaling of the drawingarea,
    // allowing users to zoom in and out without changing the
    // dimensions of the image or canvas, zoom controls are displayed
    // in a separate window

    // default zoom level is 1.0, so 100% of the original file size
    private double zoomLvl = 1.0;

    // reference to the stackpane that contains the imageview and
    // canvas located in the app file, scaling this container makes
    // it so both layers are zoomed together and remain aligned through
    // the process
    private StackPane drawingArea;

    /**
     * Initializes the ViewManager with the specified drawing area.
     * @param drawingArea The StackPane containing the drawing area.
     */
    // receives the drawingarea from app.java, this reference is used
    // to modify the scale of the displayed file without directly 
    // changing the image or drawing contents
    @SuppressWarnings("exports")
    public ViewManager(StackPane drawingArea) {
        this.drawingArea = drawingArea;
    }

    /**
     * Sets the zoom level for the drawing area.
     * @param zoomLvl The zoom level to set.
     */
    // apples the selected zoom level to the drawing area, scalex 
    // controls the x axis scaling, while scaley controls the y axis
    // scaling, assigning them the same value keeps the image from 
    // distorting disproportionately
    public void setZoom(double zoomLvl) {
        this.zoomLvl = zoomLvl;
        drawingArea.setScaleX(zoomLvl);
        drawingArea.setScaleY(zoomLvl);
    }

    /**
     * Resets the zoom level for the drawing area.
     */
    // restores the drawingarea to its original size, zoomlvl 1.0
    // equals 100% scaling, this method is called when the reset zoom
    // item is selected
    public void resetZoom() {
        zoomLvl = 1.0;

        drawingArea.setScaleX(zoomLvl);
        drawingArea.setScaleY(zoomLvl);
    }

    /**
     * Opens the zoom window.
     */
    // opens the separate window containing the zoom slider
    public void zoom() {

        // creates a slider that allows the user to the display scale,
        // the slider goes from 10% to 1000% and starts at the stored
        // zoomlvl (starts at 100% and is modified with the slider),
        // zoom is modified immediately once the slider is triggered
        Slider zoomRange = new Slider(0.1, 10, zoomLvl);
        
        // sets the slider's appearance and behavior
        zoomRange.setShowTickMarks(false);
        zoomRange.setShowTickLabels(false);
        zoomRange.setBlockIncrement(0.1);
        zoomRange.setSnapToTicks(false);

        // displays the zoom percentage onto the zoom window
        Label zoomLabel = new Label("Zoom: 100%");

        // listener listens for changes to the slider's value, when the
        // user moves the slider, the value is retrieved and zoom is updated
        // accordingly. label is also updated, multiplying zoom by 100
        // converts the scale to a percentage so the label is accurate. 
        zoomRange.valueProperty().addListener((observable, oldValue, newValue) -> {
            double zoom = newValue.doubleValue();
            setZoom(zoom);
            zoomLabel.setText("Zoom: " + (int)(zoom * 100) + "%");
            
        });

        // arrange the slider and label correctly
         VBox root = new VBox(15, zoomRange, zoomLabel);
         root.setPadding(new Insets(20));
         root.setAlignment(Pos.CENTER);

         // creates the separate window for zoom controls, allows for zoom
         // controls to be updated without affecting the main app window
         Stage zoomWindow = new Stage();
         Scene scene = new Scene(root, 250, 120);

         zoomWindow.setScene(scene);
         zoomWindow.setTitle("Zoom");

         drawingArea.setScaleX(zoomLvl);
         drawingArea.setScaleY(zoomLvl);
         zoomWindow.show();
    }
}