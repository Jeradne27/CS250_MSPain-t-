package Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class representing the Eraser drawing tool.
 */

// IMPROVEMENTS:
// - Add support for different eraser sizes.
// - Add a preview of the eraser while drawing.
// - could add support for erasing shapes and pieces on the canvas.

// ISSUES:
// - The current implementation does not provide a live preview of the eraser while drawing.
// - The current implementation only erases a small area around the mouse pointer.
// - Width controls don't affect the eraser since it only clears the area.

public class Eraser implements Drawing<Eraser> {
    private Canvas canvas;
    private GraphicsContext gc;

    private double size = 10;

    /**
     * Constructs an Eraser drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the eraser will be used.
     */
    public Eraser(Canvas canvas) {
        this.canvas = canvas;
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

    // Returns the current instance of the Eraser drawing tool.
    public Eraser getTool() {
        return this;
    }

    // Sets the color of the eraser.
    // Color doesn't affect the eraser since it only clears the area.
    public void setColor(Color color) {}

    // Returns the current color of the eraser.
    public Color getColor() {
        return Color.TRANSPARENT;
    }

    public void setWidth(double width) {
        this.size = width;
    }

    public double getWidth() {
        return size;
    }

    // Sets whether the eraser should use a dashed stroke.
    // Dashed stroke doesn't affect the eraser since it only clears the area.
    public void setDashed(boolean dashed) {}

    // Returns whether the eraser is using a dashed stroke.
    // Dashed stroke doesn't affect the eraser since it only clears the area.
    public boolean isDashed() {
        return false;
    }
}