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
import javafx.scene.control.Tooltip;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
// - Added icons for tools
// - Hover labels

//ISSUES:
// - Some hover labels take a minute to show up, but I don't know how to fix it

public class StyleMenu {

    // Calls to the currently selected drawing tool to update its style properties.
    private Drawing<?> selectedTool;
    private final ToolBar toolbar;
    private Canvas canvas;
    private final StackPane drawingArea;

    // UI components for the style menu.
    private final ColorPicker colorPicker = new ColorPicker();
    private final Button colorDropper = new Button();
    private final Slider widthSlider = new Slider(1, 20, 5);
    private final MenuButton widthMenu = new MenuButton(); 
    private final CheckBox dashedCheckBox = new CheckBox();


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
        
        colorPicker.setTooltip(new Tooltip("Color"));

        colorDropper.setTooltip(new Tooltip("Color Dropper"));
        colorDropper.setGraphic(loadIcon("/Icons/dropper.png"));

        widthMenu.setGraphic(loadIcon("/Icons/width.png"));
        widthMenu.setTooltip(new Tooltip("Width"));

        dashedCheckBox.setGraphic(loadIcon("/Icons/dash.png"));
        dashedCheckBox.setTooltip(new Tooltip("Dashed"));

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
        
        // Creates the VBox containing the width slider and apply button.
        VBox widthOptions = new VBox(10, widthSlider, applyWidth);
        widthOptions.getStyleClass().add("width-options");
        
        // Adds padding to the width options container.
        widthOptions.setPadding(new Insets(10));

        // Creates the custom menu item for the width options. CustoMenuItem allows the width option to be a port of the app
        // window instead of a separate window like before. 
        CustomMenuItem widthItem = new CustomMenuItem(widthOptions, false);

        // Adds the custom menu item to the width menu.
        widthMenu.getItems().add(widthItem);

        // Creates the toolbar containing the style menu items.
        toolbar = new ToolBar(colorPicker, colorDropper, widthMenu, dashedCheckBox);

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

        // Sets the mouse click handler for the drawing area to pick a color.
        drawingArea.setOnMouseClicked(event -> {
            var snapshot = drawingArea.snapshot(null, null);
            var pixelReader = snapshot.getPixelReader();
            
            // Gets the pixel reader from the snapshot of the drawing area.
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
     * Loads an icon from the specified path.
     * @param path The path to the icon file.
     * @return The loaded ImageView representing the icon.
     */

    private ImageView loadIcon(String path) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(path)));

        icon.setFitWidth(16);
        icon.setFitHeight(16);
        icon.setPreserveRatio(true);

        return icon;
    }

    /**
     * Returns the toolbar containing the style menu.
     * @return The toolbar.
     */
    public ToolBar getToolBar() {
        return toolbar;
    }

}
