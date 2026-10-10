package cs250;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Manages drawing operations on the application's canvas.
 */
// handles drawing performed on the apps canvas,
// drawingmanager stores the current drawing settings,
// like width and color, and uses mouse events to
// determine where to start and stop lines, toolmanager
// can update line width and color through setters
//
// IMPROVEMENT: make lines visible as mouse events are happening
//
public class DrawingManager {

    // canvas receives the user's mouse input and displays
    // the drawings, grachicscontect is used to execute the
    // drawings onto the canvas
    private Canvas canvas;
    private GraphicsContext gc;

    // coordinates from the user's first mouse press are saved.
    // they are stored until the mouse is release so a line can
    // be connected between the two points
    private double startingX;
    private double startingY;
    
    // default drawing settings
    private double lineWidth = 5.0;
    private Color lineColor = Color.BLACK;

    /**
     * Initializes the DrawingManager with the specified canvas.
     * @param canvas The canvas on which to perform drawing operations.
     */
    @SuppressWarnings("exports")
    public DrawingManager(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    /**
     * Sets the line width for future drawing operations.
     * @param lineWidth The width of the line to draw.
     */
    // updates the width for future lines, this method is called
    // on by the toolManager when the user changes the width using
    // the widthitem
    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Sets the line color for future drawing operations.
     * @param lineColor The color of the line to draw.
     */
    // updates the color that will be used for future lines,
    // this method is called by toolmanager when the user selects
    // a different color from the coloritem
    public void setLineColor(@SuppressWarnings("exports") Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * Draws a line on the canvas from the starting point to the ending point.
     */
    // sets the mouse events used for drawing a line, a line starts
    // at x when the mouse is pressed, then ends at y when the mouse is
    // released
    public void drawLine() {
        // sets default color 
        gc.setStroke(lineColor);

        // save mouse starting coordinates when the mouse is pressed
        canvas.setOnMousePressed(event -> {
            startingX = event.getX();
            startingY = event.getY();
        });

        // when the mouse is released, save the ending coordinates and
        // create a line using the starting and ending coordinates.
        canvas.setOnMouseReleased(event -> {
            double endingX = event.getX();
            double endingY = event.getY();

            gc.setLineWidth(lineWidth);
            gc.setStroke(lineColor);

            gc.strokeLine(startingX, startingY, endingX, endingY);
        });
    }
}

