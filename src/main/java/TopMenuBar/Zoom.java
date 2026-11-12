package TopMenuBar;

import java.util.function.Supplier;

import javafx.scene.Scene;

import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.CustomMenuItem;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import Operations.FileTab;

/**
 * Represents the "Zoom" functionality in the paint application.
 */
// IMPROVEMENTS:
// - The zoom functionality currently only adjusts the scale of the drawing area.
// - Consider adding options to zoom in/out by fixed increments or to fit the drawing area to the window.

// ISSUES:
// - The zoom slider only affects the active file tab, and if no file tab is active, the zoom option is disabled.

public class Zoom{

    // Supplier to get the currently active file tab.
    // This allows the Zoom class to access the active file tab without directly holding a reference to it.
    private final Supplier<FileTab> active;

    /**
     * Constructs a Zoom object with the given supplier for the active file tab.
     *
     * @param active a supplier that provides the currently active file tab
     */
    public Zoom(Supplier<FileTab> active) {
        this.active = active;
    }

    /**
     * Sets the zoom level of the active file tab's drawing area.
     *
     * @param zoomLvl the desired zoom level (e.g., 1.0 for 100%, 2.0 for 200%)
     */
    public void setZoom(double zoomLvl) {
        FileTab fileTab = active.get();

        if (fileTab == null) {
            return;
        }

        StackPane drawingArea = fileTab.getDrawingArea();
        drawingArea.setScaleX(zoomLvl);
        drawingArea.setScaleY(zoomLvl);
    }

    /**
     * Resets the zoom level of the active file tab's drawing area to 100%.
     */
    public void resetZoom() {
        setZoom(1.0);
    }

    /**
     * Creates a custom menu item for adjusting the zoom level of the active file tab's drawing area.
     *
     * @return a CustomMenuItem containing a slider and label for zoom adjustment
     */
    public CustomMenuItem zoom() {
        FileTab fileTab = active.get();

        double zoomLvl = 1.0;

        // Initialize the zoom level to 1.0 (100%) if no active file tab exists.
        if (fileTab != null) {
            zoomLvl = fileTab.getDrawingArea().getScaleX();
        }

        Slider zoomRange = new Slider(0.1, 10, zoomLvl);
        
        zoomRange.setShowTickMarks(false);
        zoomRange.setShowTickLabels(false);
        zoomRange.setBlockIncrement(0.1);
        zoomRange.setSnapToTicks(false);

        Label zoomLabel = new Label("Zoom: " + (int) (zoomLvl * 100) + "%");

        // Add a listener to update the zoom level and label when the slider value changes.
        zoomRange.valueProperty().addListener((obs, oldValue, newValue) -> {
            double zoom = newValue.doubleValue();

            // Update the zoom level of the active file tab's drawing area.
            if (fileTab != null) {
                StackPane drawingArea = fileTab.getDrawingArea();
                drawingArea.setScaleX(zoom);
                drawingArea.setScaleY(zoom);
            }

            // Update the label to reflect the new zoom level.
            zoomLabel.setText("Zoom: " + (int)(zoom * 100) + "%");
        });

        VBox zoomOpt = new VBox(15, zoomRange, zoomLabel);
        zoomOpt.setPadding(new Insets(10));

        CustomMenuItem zoomItem = new CustomMenuItem(zoomOpt, false);

        // Disable the zoom menu item if no active file tab exists.
        zoomItem.setDisable(fileTab == null);

        return zoomItem;
    }
}