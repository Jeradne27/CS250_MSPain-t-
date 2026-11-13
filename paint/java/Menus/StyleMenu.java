package paint.Menus;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.control.ToolBar;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Slider;

import javafx.scene.canvas.Canvas;

import javafx.scene.paint.Color;

import javafx.geometry.Insets;

import paint.Tools.Eraser;
import paint.Tools.Drawing;

/**
 * StyleMenu class represents the style menu for the application.
 * It manages the style options for the drawing tools, including color, width, and dashed line settings.
 * Provides a toolbar with controls for selecting colors, adjusting line width, and toggling dashed lines.
 * Handles the interaction between the style menu and the currently selected drawing tool.
 */

// IMPROVEMENTS: 
// - Add keyboard shortcuts for frequently used style options.
// - Add more options for line styles and patterns.
// - Change location of the style menu for better user experience. Moving it the the left or right side of the app window.
// - Dashed line options are currently limited to a checkbox. Could add it to a menu for more line styles.

//ISSUES:
// - No keyboard shortcuts for style options.

public class StyleMenu {

    // Calls to the currently selected drawing tool to update its style properties.
    private Drawing<?> selectedTool;
    private final ToolBar toolbar;
    private Canvas canvas;
    private final StackPane drawingArea;

    // UI components for the style menu.
    private final ColorPicker colorPicker = new ColorPicker();
    private final Button colorDropper = new Button("Color Dropper");
    private final Slider widthSlider = new Slider(1, 20, 5);
    private final MenuButton widthMenu = new MenuButton("Width"); 
    // private final Button eraserButton = new Button("Eraser");
    private final CheckBox dashedCheckBox = new CheckBox("Dashed");

    /**
     * Sets the currently selected drawing tool.
     * @param selectedTool The selected tool.
     */
    public void setSelectedTool(Drawing<?> selectedTool) {
        this.selectedTool = selectedTool;
        colorPicker.setValue(selectedTool.getColor());
        widthSlider.setValue(selectedTool.getWidth());

        // Updates the dashed checkbox based on the selected tool's dashed property.
        dashedCheckBox.setSelected(selectedTool.isDashed());
    }

    /**
     * Creates a new style menu.
     * @param canvas The canvas to draw on.
     * @param drawingArea The area where the canvas is displayed.
     */
    public StyleMenu(Canvas canvas, StackPane drawingArea) {
        this.canvas = canvas;
        this.drawingArea = drawingArea;

        colorDropper.setOnAction(event -> startColorDropper());
        colorPicker.setOnAction(event -> {
            if (selectedTool != null) {
                selectedTool.setColor(colorPicker.getValue());
            }
        });

        dashedCheckBox.setOnAction(event -> {
            if (selectedTool != null) {
                selectedTool.setDashed(dashedCheckBox.isSelected());
            }
        });

        widthSlider.setShowTickLabels(true);
        widthSlider.setShowTickMarks(true);

        // Creates the apply button for the width menu.
        Button applyWidth = new Button("Apply");

        applyWidth.setOnAction(event -> {
            if (selectedTool != null) {
                selectedTool.setWidth(widthSlider.getValue());
                widthMenu.setText(String.format("Width: %.2f px", widthSlider.getValue()));
            }
            widthMenu.hide();
        });
        
        VBox widthOptions = new VBox(10, widthSlider, applyWidth);
        
        widthOptions.setPadding(new Insets(10));

        // Creates the custom menu item for the width options. CustoMenuItem allows the width option to be a port of the app
        // window instead of a separate window like before. 
        CustomMenuItem widthItem = new CustomMenuItem(widthOptions, false);

        widthMenu.getItems().add(widthItem);

        // eraserButton.setOnAction(event -> {
        //     // Deactivates the currently selected tool before activating the eraser.
        //     if (selectedTool != null) {
        //         selectedTool.deactivateTool();
        //     }
        //     Eraser eraser  = new Eraser(canvas);
        //     eraser.activateTool(canvas);
        // });

        toolbar = new ToolBar(
            colorPicker,
            colorDropper,
            widthMenu,
            // eraserButton,
            dashedCheckBox
        );

        // Ensures the toolbar is enabled.
        toolbar.setDisable(false); 
    }

   
    /**
     * Starts the color dropper tool, allowing the user to pick a color from the drawing area.
     */
    public void startColorDropper() {
        if (selectedTool != null) {
            selectedTool.deactivateTool();
        }

        drawingArea.setOnMouseClicked(event -> {
            var snapshot = drawingArea.snapshot(null, null);
            var pixelReader = snapshot.getPixelReader();
            
            int x = (int) event.getX();
            int y = (int) event.getY();
            
            Color pickedColor = pixelReader.getColor(x, y);

            colorPicker.setValue(pickedColor);
            selectedTool.setColor(pickedColor);

            drawingArea.setOnMouseClicked(null);
            // Reactivates the previously selected tool after picking a color.
            selectedTool.activateTool(canvas);
        });
    }

    /**
     * Returns the toolbar containing the style menu.
     * @return The toolbar.
     */
    public ToolBar getToolBar() {
        return toolbar;
    }

}
