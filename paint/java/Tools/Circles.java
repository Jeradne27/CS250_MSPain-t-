package paint.Tools;

import java.util.function.Consumer; 

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
// - Added handlers for logging

// ISSUES:
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

    private Consumer<String> canvasLogger = action -> {};

    /**
     * Constructs a new Circles tool with the given canvas.
     * @param canvas The canvas on which the circle will be drawn.
     */
    public Circles(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.previewCanvas = previewCanvas;
    }

    /**
     * Sets the logger for the canvas.
     * @param canvasLogger The logger to set for the canvas.
     */
    public void setCanvasLogger(Consumer<String> canvasLogger) {
        this.canvasLogger = canvasLogger;
    }

    /**
     * Sets the color of the circle.
     * @param color The color to set for the circle.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the color of the circle.
     * @return The color of the circle.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the width of the circle's stroke.
     * @param width The width to set for the circle's stroke.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets the width of the circle's stroke.
     * @return The width of the circle's stroke.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets whether the circle should be filled.
     * @param isFilled True if the circle should be filled, false otherwise.
     */
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    /**
     * Sets the fill color of the circle.
     * @param fillColor The color to use for filling the circle.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Gets the fill color of the circle.
     * @return The fill color of the circle.
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Checks if the circle is filled.
     * @return True if the circle is filled, false otherwise.
     */
    public boolean isFilled() {
        return this.isFilled;
    }

    /**
     * Checks if the circle's stroke is dashed.
     * @return True if the circle's stroke is dashed, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Sets whether the circle's stroke should be dashed.
     * @param dashed True if the circle's stroke should be dashed, false otherwise.
     */
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

                canvasLogger.accept("Circle drawn on canvas");
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

    /**
     * Clears the preview of the circle being drawn on the preview canvas.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Draws a circle on the target canvas with the specified center and radius.
     * @param targetCanvas The canvas on which to draw the circle.
     * @param x The x-coordinate of the circle's center.
     * @param y The y-coordinate of the circle's center.
     */
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