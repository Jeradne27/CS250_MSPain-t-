package cs250;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;


public class ToolManager {

    // manages the drawing tools for pain(t),
    // toolmanager creates separate windows that allow users to adjust
    // the width and color of drawing tools on the canvas,
    // drawing operations are handled in the drawingmanager file
    // IMPROVEMENTS: could be renamed for clarity
    //               windows for adjustments don't automatically close
    //                  when selections have been made
    //               could eventually become side panels instead of full
    //                  windows
    //               width resets to 5 every time changewidth() runs
    //               color resets to black every time colorPicker() runs

    // references the drawingmanager, which is responsible for drawing 
    // operations on the canvas
    private DrawingManager drawingManager;

    // ensures that the changes are applied to the tool settings that are
    // currently being used on the main app
    public ToolManager(DrawingManager drawingManager) {
        this.drawingManager = drawingManager;    
    }

    public void changeWidth() {

        // opens a separate window with a slider to change the width of
        // lines, tells the user what the width of the line is in the window,
        // changes affect future lines, not lines already on the canvas

        // slider ranges from 0.1 to 10 pixels, defaulting to 5 pixels
        Slider widthRange = new Slider(0.1, 10, 5);

        // configures the appearance and adjustment behavior of the slider
        widthRange.setShowTickMarks(false);
        widthRange.setShowTickLabels(false);
        widthRange.setBlockIncrement(0.1);
        widthRange.setSnapToTicks(false);

        // displays the current width as a whole integer in unit px
        Label widthLabel = new Label("Width (px): " + (int) widthRange.getValue());

        // listener listens for changes to the slider's value, 
        // each time the slider moves, the width display is updated to the
        // current value and that value is also sent to the drawingmanager
        // for execution, the widthlabel displays an integer, but drawingmanager
        // receives the complete decimal value for precise line widths
        widthRange.valueProperty().addListener((observable, oldValue, newValue) -> {
            widthLabel.setText("Width (px): " + newValue.intValue());
            drawingManager.setLineWidth(newValue.doubleValue());
        });

        // arrange the slider and the label correctly in the window
        VBox root = new VBox(15, widthRange, widthLabel);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // creates the separate window for width control, allows no
        // interruptions to the main app window
        Stage widthWindow = new Stage();
        Scene scene = new Scene(root, 250, 120);

        widthWindow.setScene(scene);
        widthWindow.setTitle("Line Width");
        widthWindow.show();
    }

    public void changeColor() {

        // opens a new window containing a color selection menu,
        // the user selects a new line color, which is passed to the
        // drawingmanager for execution

        // creates the color picker with a default color of black
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);

        // when the user selects a color it is retrieved and the color
        // is sent to the drawingmanager to update the line's color,
        // this color will only be applied to new lines, not lines
        // already on the canvas
        colorPicker.setOnAction(event -> {drawingManager.setLineColor(colorPicker.getValue());});

        // arranges the color picker correctly in the window
        VBox root = new VBox(15, colorPicker);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // creates the separate window for color control, allows no
        // interruptions to the main app window 
        Stage colorWindow = new Stage();
        Scene scene = new Scene(root, 250, 120);


        colorWindow.setScene(scene);
        colorWindow.setTitle("Line Color");
        colorWindow.show();
    }
}
