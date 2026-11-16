package paint.Tools;

import java.util.function.Consumer; 

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Ellipse;

import javafx.scene.paint.Color;

/**
 * Class representing the Ellipses drawing tool.
 */

// IMPROVEMENTS:
// - Added a preview of the ellipse while drawing.


// ISSUES:
// - Fill is not currently supported, though the setFilled and setFillColor methods are provided for future implementation.

public class Ellipses implements Shapes<Ellipses> {
    private Canvas canvas;
    private Canvas previewCanvas;
    private GraphicsContext gc;
    private Ellipse ellipse;

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
     * Constructs an Ellipses drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the ellipse will be drawn.
     */
    public Ellipses(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.previewCanvas = previewCanvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setCanvasLogger(Consumer<String> canvasLogger) {
        this.canvasLogger = canvasLogger;
    }
    
    /**
     * Sets the color of the ellipse's stroke.
     *
     * @param color the color to set for the ellipse's stroke.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the color of the ellipse's stroke.
     *
     * @return the color of the ellipse's stroke.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the width of the ellipse's stroke.
     *
     * @param width the width to set for the ellipse's stroke.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the width of the ellipse's stroke.
     *
     * @return the width of the ellipse's stroke.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets whether the ellipse should be filled.
     *
     * @param isFilled true if the ellipse should be filled, false otherwise.
     */
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    /**
     * Sets the fill color of the ellipse.
     *
     * @param fillColor the color to set for the ellipse's fill.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the fill color of the ellipse.
     *
     * @return the fill color of the ellipse.
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Returns whether the ellipse should be filled.
     *
     * @return true if the ellipse should be filled, false otherwise.
     */
    public boolean isFilled() {
        return this.isFilled;
    }

    /**
     * Returns whether the ellipse's stroke should be dashed.
     *
     * @return true if the ellipse's stroke should be dashed, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Sets whether the ellipse's stroke should be dashed.
     *
     * @param dashed true if the ellipse's stroke should be dashed, false otherwise.
     */
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
            dragging = true;

            canvasLogger.accept("Ellipse drawn on canvas");
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawEllipse(previewCanvas, event.getX(), event.getY());
            }
        });

        // Handle mouse released event to draw the ellipse.
        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawEllipse(canvas, event.getX(), event.getY());
            }
        });
        
        // Handle mouse exited event to clear the current ellipse.
        canvas.setOnMouseExited(event -> {
            // Clear the current ellipse by setting it to null.
            ellipse = null;
            dragging = false;
        });
    }

    /**
     * Deactivates the Ellipses drawing tool.
     * Clears any active mouse event handlers on the canvas.
     * This should be called when the tool is no longer needed or when switching to a different tool.
     */
    public void deactivateTool() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
        dragging = false;
    }

    /**
     * Clears the preview of the ellipse being drawn on the preview canvas.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Draws an ellipse on the target canvas with the specified coordinates.
     *
     * @param targetCanvas the canvas on which the ellipse will be drawn.
     * @param x the x-coordinate of the ellipse's center.
     * @param y the y-coordinate of the ellipse's center.
     */
    private void drawEllipse(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        double radiusX = Math.abs(x - anchorX) / 2;
        double radiusY = Math.abs(y - anchorY) / 2;

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
            gc.fillOval(anchorX, anchorY, radiusX * 2, radiusY * 2);
        }
        gc.strokeOval(anchorX, anchorY, radiusX * 2, radiusY * 2);

        gc.restore();
    }

    // Returns the current instance of the Ellipses drawing tool.
    public Ellipses getTool() {
        return this;
    }
}