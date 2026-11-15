package paint.Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Rectangle;

import javafx.scene.paint.Color;

/**
 * Class representing the Squares drawing tool.
 * Implements the Shapes interface for drawing squares on a canvas.
 */

// IMPROVEMENTS:
// - Added a live preview of the square while drawing.

// ISSUES:
// - There isn't a fill option yet, so setFill and setFillColor methods are currently unused.

public class Squares implements Shapes<Squares> {
    private Canvas canvas;
    private GraphicsContext gc;
    private Canvas previewCanvas;
    private Rectangle square;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;
    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;

    private boolean dragging;
    
    private boolean dashed;

    /**
     * Constructs a Squares drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the squares will be drawn.
     */
    public Squares(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.previewCanvas = previewCanvas;
    }

    /**
     * Sets the color of the square drawing tool.
     *
     * @param color the color to set for the square drawing tool.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the color of the square drawing tool.
     *
     * @return the color of the square drawing tool.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the width of the square drawing tool.
     *
     * @param width the width to set for the square drawing tool.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets the width of the square drawing tool.
     *
     * @return the width of the square drawing tool.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets the fill status of the square drawing tool.
     *
     * @param isFilled true if the square should be filled, false otherwise.
     */
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    /**
     * Sets the fill color of the square drawing tool.
     *
     * @param fillColor the fill color to set for the square drawing tool.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Gets the fill color of the square drawing tool.
     *
     * @return the fill color of the square drawing tool.
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Checks if the square drawing tool is filled.
     *
     * @return true if the square drawing tool is filled, false otherwise.
     */
    public boolean isFilled() {
        return this.isFilled;
    }

    /**
     * Checks if the square drawing tool is dashed.
     *
     * @return true if the square drawing tool is dashed, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Sets the dashed status of the square drawing tool.
     *
     * @param dashed true if the square should be dashed, false otherwise.
     */
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
            dragging = true;
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawSquare(previewCanvas, event.getX(), event.getY());
            }
        });

        // Handle mouse released event to draw the square.
        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawSquare(canvas, event.getX(), event.getY());
            }
        });

        
        // Handle mouse exited event to clear the current square.
        canvas.setOnMouseExited(event -> {
            // Clear the current square by setting it to null.
            square = null;
            dragging = false;
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
        canvas.setOnMouseDragged(null);
        dragging = false;
    }

    /**
     * Clears the preview of the square being drawn.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Draws a square on the target canvas at the specified coordinates.
     *
     * @param targetCanvas the canvas on which the square will be drawn.
     * @param x the x-coordinate of the square's top-left corner.
     * @param y the y-coordinate of the square's top-left corner.
     */
    private void drawSquare(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        // Calculate the top-left corner and side length of the square based on the anchor and current mouse position.
        double l = Math.min(anchorX, x);
        double w = Math.min(anchorY, y);
        double sides = Math.abs(x - anchorX);

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
            gc.fillRect(l, w, sides, sides);
        }

        // Draw the square outline on the target canvas.
        gc.strokeRect(l, w, sides, sides);
        gc.restore();
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