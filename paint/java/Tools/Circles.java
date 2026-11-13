package paint.Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;

import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;

/**
 * Represents a circle drawing tool for the canvas.
 * Provides methods to set and get the color, width, fill, and dashed properties of the circle.
 */

// IMPROVEMENTS:
// - Add support for different line styles (e.g., solid, dashed, dotted).
// - Allow the user to change the line style dynamically while drawing.
// - Add support for filling the circle with a color.
// - Add support for different fill patterns (e.g., solid, gradient, pattern).
// - Allow the user to change the fill pattern dynamically while drawing.

// ISSUES:
// - The circle is only drawn on mouse release, not dynamically while dragging.
// - The dashed line style is applied only on the final stroke, not during the drawing process.
// - There is no menu option to fill the circle at the moment, though there are setters for fill color and fill status.

public class Circles implements Shapes<Circles> {

    private Canvas canvas;
    private Canvas previewCanvas;
    private GraphicsContext gc;
    private Circle circle;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;

    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;

    private boolean dashed;

    private boolean dragging;

    /**
     * Constructs a new Circles tool with the given canvas.
     * @param canvas The canvas on which the circle will be drawn.
     */
    public Circles(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.previewCanvas = previewCanvas;
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
     * Activates the circle drawing tool on the given canvas.
     * @param canvas The canvas on which the circle will be drawn.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                anchorX = event.getX();
                anchorY = event.getY();
                dragging = true;
            }   
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawCircle(previewCanvas, event.getX(), event.getY());
            }
        });

        // Draw the circle on mouse release.
        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawCircle(canvas, event.getX(), event.getY());
            }
        });
        // Handle mouse exit event to clear the current circle.
        canvas.setOnMouseExited(event -> {
            circle = null;
            dragging = false;
        });
    }

    /**
     * Deactivates the circle drawing tool, removing its event handlers from the canvas.
     * This should be called when the tool is no longer needed or when switching to another tool.
     */
    public void deactivateTool() {
        // Remove the event handlers from the canvas.
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
        canvas.setOnMouseDragged(null);
        dragging = false;
    }

    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    private void drawCircle(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        // double x = event.getX();
        // double y = event.getY();

        double radius = Math.hypot(x - anchorX, y - anchorY);

        gc.save();
        
        gc.setStroke(color);
        gc.setLineWidth(width);

        // Apply the fill color if the circle is filled.
        if (isFilled) {
            gc.setFill(fillColor);
            gc.fillOval(anchorX - radius, anchorY - radius, 2 * radius, 2 * radius);
        }

        // Apply the stroke color and width.
        if (dashed) {
            gc.setLineCap(StrokeLineCap.BUTT);
            gc.setLineDashes(5, 3);
        } else {
            gc.setLineCap(StrokeLineCap.SQUARE);
            gc.setLineDashes();
        }
        // Draw the circle outline.
        gc.strokeOval(anchorX - radius, anchorY - radius, 2 * radius, 2 * radius);
        gc.restore();
    }
    /**
     * Returns the current instance of the circle drawing tool.
     * @return The current instance of the circle drawing tool.
     */
    public Circles getTool() {
        return this;
    }
}