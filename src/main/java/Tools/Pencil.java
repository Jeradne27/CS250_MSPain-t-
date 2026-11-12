package Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.shape.StrokeLineCap;

import javafx.scene.paint.Color;

/**
 * Class representing the Pencil drawing tool.
 */
// IMPROVEMENTS:
// - Add a preview of the pencil while drawing.
// - The pencil is a bit choppy when drawing quickly, which could be improved. 

// ISSUES:
// - The current implementation does not provide a live preview of the pencil while drawing.

public class Pencil implements Drawing<Pencil> {
    private Canvas canvas;
    private GraphicsContext gc;

    private double width = 5.0;
    private Color color = Color.BLACK;

    private boolean dashed = false;

    /**
     * Constructs a Pencil drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the pencil will draw.
     */
    public Pencil(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {   
        return color;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public boolean isDashed() {
        return dashed;
    }

    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }
    /**
     * Activates the Pencil drawing tool on the given canvas.
     *
     * @param canvas the canvas on which the pencil will draw.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        gc.setStroke(color);
        // Handle mouse dragged event to draw with the pencil.
        canvas.setOnMouseDragged(event -> {
            gc.beginPath();
            gc.moveTo(event.getX(), event.getY());
            gc.lineTo(event.getX(), event.getY());

            gc.stroke();
            gc.setLineWidth(width);
            gc.setStroke(color);
        });
    }

    /**
     * Deactivates the Pencil drawing tool.
     * Clears any active mouse event handlers on the canvas.
     * This should be called when the tool is no longer needed or when switching to a different tool.
     */
    public void deactivateTool() {
        // Clear any active mouse event handlers on the canvas.
        canvas.setOnMouseDragged(null);
    }

    /**
     * Returns the current instance of the Pencil drawing tool.
     *
     * @return the current instance of the Pencil drawing tool.
     */
    public Pencil getTool() {
        return this;
    }
}