package paint.Tools;

import java.util.function.Consumer; 

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.shape.StrokeLineCap;

import javafx.scene.paint.Color;

/**
 * Class representing the Pencil drawing tool.
 */

// IMPROVEMENTS:
// - Added handler for thread logging

// ISSUES:
// - The pencil is still slightly choppy when drawing quickly, but it has improved from the first version

public class Pencil implements Drawing<Pencil> {
    private Canvas canvas;
    private GraphicsContext gc;

    private double width = 5.0;
    private Color color = Color.BLACK;

    private boolean dashed = false;

    private Consumer<String> canvasLogger = action -> {};


    /**
     * Constructs a Pencil drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the pencil will draw.
     */
    public Pencil(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    /**
     * Sets the logger for the canvas.
     * @param canvasLogger
     */
    public void setCanvasLogger(Consumer<String> canvasLogger) {
        this.canvasLogger = canvasLogger;
    }

    /**
     * Sets the color of the pencil.
     *
     * @param color the color to set for the pencil.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the current color of the pencil.
     *
     * @return the current color of the pencil.
     */
    public Color getColor() {   
        return color;
    }

    /**
     * Sets the width of the pencil.
     *
     * @param width the width to set for the pencil.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the current width of the pencil.
     *
     * @return the current width of the pencil.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns whether the pencil is using a dashed stroke.
     *
     * @return true if the pencil is using a dashed stroke, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Sets whether the pencil should use a dashed stroke.
     *
     * @param dashed true if the pencil should use a dashed stroke, false otherwise.
     */
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

        canvasLogger.accept("Pencil used on canvas");
    }

    /**
     * Deactivates the Pencil drawing tool.
     * Clears any active mouse event handlers on the canvas.
     * This should be called when the tool is no longer needed or when switching to a different tool.
     */
    public void deactivateTool() {
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