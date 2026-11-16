package paint.Tools;

import java.util.function.Consumer; 

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Rectangle;

import javafx.scene.paint.Color;

/**
 * Class representing the Rectangles drawing tool.
 */

// IMPROVEMENTS:
// - Added a live preview of the rectangle while drawing.

// ISSUES:
// - There isn't a fill option yet, so setFill and setFillColor methods are currently unused.

public class Rectangles implements Shapes<Rectangles> {
    private Canvas canvas;
    private GraphicsContext gc;
    private Canvas previewCanvas;
    private Rectangle rectangle;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;
    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;

    private boolean dragging;

    private boolean dashed;

    private Consumer<String> canvasLogger = action -> {};

    /**
     * Constructs a Rectangles drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the rectangles will be drawn.
     */
    public Rectangles(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.previewCanvas = previewCanvas;
    }

    public void setCanvasLogger(Consumer<String> canvasLogger) {
        this.canvasLogger = canvasLogger;
    }

    /**
     * Sets the color of the rectangle's stroke.
     *
     * @param color the color to set for the rectangle's stroke.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the color of the rectangle's stroke.
     *
     * @return the color of the rectangle's stroke.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the width of the rectangle's stroke.
     *
     * @param width the width to set for the rectangle's stroke.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the width of the rectangle's stroke.
     *
     * @return the width of the rectangle's stroke.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets whether the rectangle should be filled.
     *
     * @param isFilled true if the rectangle should be filled, false otherwise.
     */
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    /**
     * Sets the fill color of the rectangle.
     *
     * @param fillColor the color to set for the rectangle's fill.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the fill color of the rectangle.
     *
     * @return the fill color of the rectangle.
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Returns whether the rectangle is filled.
     *
     * @return true if the rectangle is filled, false otherwise.
     */
    public boolean isFilled() {
        return this.isFilled;
    }

    /**
     * Returns whether the rectangle is using a dashed stroke.
     *
     * @return true if the rectangle is using a dashed stroke, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Sets whether the rectangle should use a dashed stroke.
     *
     * @param dashed true if the rectangle should use a dashed stroke, false otherwise.
     */
    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * Activates the Rectangles drawing tool on the given canvas.
     *
     * @param canvas the canvas on which the rectangles will be drawn.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            anchorX = event.getX();
            anchorY = event.getY();
            dragging = true;

            canvasLogger.accept("Rectangle drawn on canvas");
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawRectangle(previewCanvas, event.getX(), event.getY());
            }
        });
        // Handle mouse released event to draw the rectangle.
        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawRectangle(canvas, event.getX(), event.getY());
            }
        });

        // Handle mouse exited event to clear the current rectangle.
        canvas.setOnMouseExited(event -> {
            rectangle = null;
            dragging = false;
        });
    }

    /**
     * Deactivates the Rectangles drawing tool.
     * Clears any active mouse event handlers on the canvas.
     * This should be called when the tool is no longer needed or when switching to a different tool.
     */
    public void deactivateTool() {
        // Clear any active mouse event handlers on the canvas.
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
        dragging = false;
    }

    /**
     * Clears the preview of the rectangle on the preview canvas.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Draws a rectangle on the specified canvas at the given coordinates.
     *
     * @param targetCanvas the canvas on which to draw the rectangle.
     * @param x the x-coordinate of the mouse pointer.
     * @param y the y-coordinate of the mouse pointer.
     */
    private void drawRectangle(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        double l = Math.min(anchorX, x);
        double t = Math.min(anchorY, y);
        double w = Math.abs(x - anchorX);
        double h = Math.abs(y - anchorY);

        gc.save();

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
            gc.fillRect(l, t, w, h);
        }
        gc.strokeRect(l, t, w, h);
        gc.restore();
    }
    /**
     * Returns the current instance of the Rectangles drawing tool.
     *
     * @return the current instance of the Rectangles drawing tool.
     */
    public Rectangles getTool() {
        return this;
    }
}