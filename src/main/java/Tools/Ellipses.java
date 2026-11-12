package Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Ellipse;

import javafx.scene.paint.Color;

/**
 * Class representing the Ellipses drawing tool.
 */

// IMPROVEMENTS:
// - Add support for different line styles (e.g., solid, dashed, dotted).
// - Add support for different fill styles (e.g., solid, gradient, pattern).
// - Add a preview of the ellipse while drawing.


// ISSUES:
// - The current implementation does not provide a live preview of the ellipse while drawing.

public class Ellipses implements Shapes<Ellipses> {
    private Canvas canvas;
    private GraphicsContext gc;
    private Ellipse ellipse;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;
    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;

    private boolean dashed;

    /**
     * Constructs an Ellipses drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the ellipse will be drawn.
     */
    public Ellipses(Canvas canvas) {
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
     * Activates the Ellipses drawing tool on the given canvas.
     *
     * @param canvas the canvas on which the ellipse will be drawn.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        
        canvas.setOnMousePressed(event -> {
            anchorX = event.getX();
            anchorY = event.getY();
        });
        // Handle mouse released event to draw the ellipse.
        canvas.setOnMouseReleased(event -> {
            double x = event.getX();
            double y = event.getY();

            double radiusX = Math.abs(x - anchorX) / 2;
            double radiusY = Math.abs(y - anchorY) / 2;

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
                gc.fillOval(anchorX, anchorY, radiusX * 2, radiusY * 2);
            }
            gc.strokeOval(anchorX, anchorY, radiusX * 2, radiusY * 2);
        });
        
        // Handle mouse exited event to clear the current ellipse.
        canvas.setOnMouseExited(event -> {
            // Clear the current ellipse by setting it to null.
            ellipse = null;
        });
    }

    /**
     * Deactivates the Ellipses drawing tool.
     * Clears any active mouse event handlers on the canvas.
     * This should be called when the tool is no longer needed or when switching to a different tool.
     */
    public void deactivateTool() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
    }

    // Returns the current instance of the Ellipses drawing tool.
    public Ellipses getTool() {
        return this;
    }
}