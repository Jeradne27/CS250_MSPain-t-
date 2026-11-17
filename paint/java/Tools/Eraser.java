package paint.Tools;

import java.util.function.Consumer; 

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class representing the Eraser drawing tool.
 */

// IMPROVEMENTS:
// - N/A

// ISSUES:
// - The current implementation does not provide a live preview of the eraser while drawing.
// - The current implementation only erases a small area around the mouse pointer.
// - Width controls don't affect the eraser since it only clears the area, though there are still methods to set and get the width.

public class Eraser implements Drawing<Eraser> {
    private Canvas canvas;
    private GraphicsContext gc;

    private double size = 10;

    private Consumer<String> canvasLogger = action -> {};

    /**
     * Constructs an Eraser drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the eraser will be used.
     */
    public Eraser(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * sets the logger for the canvas
     * 
     * @param canvasLogger
     */
    public void setCanvasLogger(Consumer<String> canvasLogger) {
        this.canvasLogger = canvasLogger;
    }

    /**
     * Activates the Eraser drawing tool on the given canvas.
     *
     * @param canvas the canvas on which the eraser will be used.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();

        // Handle mouse pressed event to start erasing.
        canvas.setOnMousePressed(event -> {
            gc.clearRect(
                // Clear the area around the mouse pointer.
                event.getX() - size / 2,
                event.getY() - size / 2,
                size,
                size
            );
            canvasLogger.accept("Eraser used on canvas");
        });

        // Handle mouse dragged event to continue erasing.
        canvas.setOnMouseDragged(event -> {
            gc.clearRect(
                event.getX() - size / 2,
                event.getY() - size / 2,
                size,
                size
            );
        });

        // Handle mouse released event to stop erasing.
        canvas.setOnMouseReleased(null);

    }

    /**
     * Deactivates the Eraser drawing tool.
     * Clears any active mouse event handlers on the canvas.
     * This should be called when the tool is no longer needed or when switching to a different tool.
     */
    public void deactivateTool() {
        // Clear any active mouse event handlers on the canvas.
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
    }

    /**
     * Returns the current instance of the Eraser drawing tool.
     *
     * @return the current instance of the Eraser drawing tool.
     */
    public Eraser getTool() {
        return this;
    }

    /**
     * Sets the color of the eraser.
     * Color doesn't affect the eraser since it only clears the area.
     *
     * @param color the color to set for the eraser.
     */
    public void setColor(Color color) {}

    /**
     * Returns the current color of the eraser.
     * Color doesn't affect the eraser since it only clears the area.
     *
     * @return the current color of the eraser.
     */
    public Color getColor() {
        return Color.TRANSPARENT;
    }

    /**
     * Sets the width of the eraser.
     *
     * @param width the width to set for the eraser.
     */
    public void setWidth(double width) {
        this.size = width;
    }

    /**
     * Returns the current width of the eraser.
     *
     * @return the current width of the eraser.
     */
    public double getWidth() {
        return size;
    }

    /**
     * Sets whether the eraser should use a dashed stroke.
     * Dashed stroke doesn't affect the eraser since it only clears the area.
     *
     * @param dashed true if the eraser should use a dashed stroke, false otherwise.
     */
    public void setDashed(boolean dashed) {}

    /**
     * Returns whether the eraser is using a dashed stroke.
     * Dashed stroke doesn't affect the eraser since it only clears the area.
     *
     * @return true if the eraser is using a dashed stroke, false otherwise.
     */
    public boolean isDashed() {
        return false;
    }
}