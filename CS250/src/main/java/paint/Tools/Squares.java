package paint.Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Rectangle;

import javafx.scene.paint.Color;

/**
 * Class representing the Squares drawing tool.
 * Implements the Shapes interface for drawing squares on a canvas.
 */

// IMPROVEMENTS:
// - Add a live preview of the square while drawing.

// ISSUES:
// - The current implementation does not provide a live preview of the square while drawing.
public class Squares implements Shapes<Squares> {
    private Canvas canvas;
    private GraphicsContext gc;
    private Rectangle square;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;
    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;

    private boolean dashed;

    /**
     * Constructs a Squares drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the squares will be drawn.
     */
    public Squares(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWidth() {
        return this.width;
    }

    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getFillColor() {
        return this.fillColor;
    }

    public boolean isFilled() {
        return this.isFilled;
    }

    public boolean isDashed() {
        return dashed;
    }

    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * Activates the Squares drawing tool on the given canvas.
     *
     * @param canvas the canvas on which the squares will be drawn.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            anchorX = event.getX();
            anchorY = event.getY();
        });

        // Handle mouse released event to draw the square.
        canvas.setOnMouseReleased(event -> {
            double x = Math.min(anchorX, event.getX());
            double y = Math.min(anchorY, event.getY());
            double sides = Math.abs(event.getX() - anchorX);
            // Calculate the side length of the square based on the mouse drag distance.
            gc.setStroke(color);
            gc.setLineWidth(width);

            if (dashed) {
                gc.setLineCap(StrokeLineCap.BUTT);
                gc.setLineDashes(5, 3);
            } else {
                gc.setLineCap(StrokeLineCap.SQUARE);
                gc.setLineDashes();
            }

            if (isFilled) {
                gc.setFill(fillColor);
                gc.fillRect(x, y, sides, sides);
            }
            gc.strokeRect(x, y, sides, sides);
        });
        // Handle mouse exited event to clear the current square.
        canvas.setOnMouseExited(event -> {
            // Clear the current square by setting it to null.
            square = null;
        });
    }

    /**
     * Deactivates the Squares drawing tool.
     * Clears any active mouse event handlers on the canvas.
     * This should be called when the tool is no longer needed or when switching to a different tool.
     */
    public void deactivateTool() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
    }

    /**
     * Returns the current instance of the Squares drawing tool.
     *
     * @return the current instance of the Squares drawing tool.
     */
    public Squares getTool() {
        return this;
    }
}