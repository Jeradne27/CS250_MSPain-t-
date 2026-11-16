package paint.Tools;

import java.util.function.Consumer; 

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;

import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;

/**
 * Class representing the Hearts drawing tool.
 */

// IMPROVEMENTS:
// - Newest shape to the tool menu
// - Added view preview for the heart while drawing

// ISSUES:
// - There isn't a menu item to fill in the heart shape yet, though the methods are ready for it.

public class Hearts implements Shapes<Hearts> {

    private Canvas canvas;
    private Canvas previewCanvas;
    private GraphicsContext gc;
    private Hearts hearts;

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
     * Constructs a Hearts drawing tool with the given canvas and preview canvas.
     *
     * @param canvas the main canvas on which the hearts will be drawn.
     * @param previewCanvas the preview canvas on which the live preview of the heart will be drawn.
     */
    public Hearts(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.previewCanvas = previewCanvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setCanvasLogger(Consumer<String> canvasLogger) {
        this.canvasLogger = canvasLogger;
    }

    /**
     * Sets the color of the heart.
     *
     * @param color the color to set for the heart.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the current color of the heart.
     *
     * @return the current color of the heart.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the width of the heart.
     *
     * @param width the width to set for the heart.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the current width of the heart.
     *
     * @return the current width of the heart.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets whether the heart should be filled.
     *
     * @param isFilled true if the heart should be filled, false otherwise.
     */
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    /**
     * Sets the fill color of the heart.
     *
     * @param fillColor the fill color to set for the heart.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the current fill color of the heart.
     *
     * @return the current fill color of the heart.
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Returns whether the heart is filled.
     *
     * @return true if the heart is filled, false otherwise.
     */
    public boolean isFilled() {
        return this.isFilled;
    }

    /**
     * Returns whether the heart is using a dashed stroke.
     *
     * @return true if the heart is using a dashed stroke, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Sets whether the heart should use a dashed stroke.
     *
     * @param dashed true if the heart should use a dashed stroke, false otherwise.
     */
    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * Activates the Hearts drawing tool on the given canvas.
     *
     * @param canvas the canvas on which the hearts will be drawn.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            anchorX = event.getX();
            anchorY = event.getY();
            dragging = true;

            canvasLogger.accept("Heart drawn on canvas");
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawHeart(previewCanvas, event.getX(), event.getY());
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawHeart(canvas, event.getX(), event.getY());
            }
        });

        canvas.setOnMouseExited(event -> {
            hearts = null;
            dragging = false;
        });
    }

    /**
     * Deactivates the Hearts drawing tool.
     */
    public void deactivateTool() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
        dragging = false;
    }

    /**
     * Clears the preview of the heart on the preview canvas.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Draws a heart on the specified canvas at the given coordinates.
     *
     * @param targetCanvas the canvas on which the heart will be drawn.
     * @param x the x-coordinate of the heart.
     * @param y the y-coordinate of the heart.
     */
    private void drawHeart(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        double l = Math.min(anchorX, x);
        double w = Math.min(anchorY, y);
        double wide = Math.abs(anchorX - x);
        double high = Math.abs(anchorY - y);

        if (dashed) {
            gc.setLineCap(StrokeLineCap.BUTT);
            gc.setLineDashes(5, 3);
        } else {
            gc.setLineCap(StrokeLineCap.SQUARE);
            gc.setLineDashes();
        }

        gc.save();

        if (isFilled) {
            gc.setFill(fillColor);
        }

        gc.setStroke(color);
        gc.setLineWidth(width);

        gc.beginPath();

        // Start drawing the heart from the bottom point.
        // Mathematical calculations to determine the control points for the Bezier curves that form the heart shape.
        gc.moveTo(l + wide / 2.0, w + high);

        gc.bezierCurveTo(
            l - wide * 0.1, w + high * 0.55,
            l, w - high * 0.15,
            l + wide / 2.0, w + high * 0.25
        );

        gc.bezierCurveTo(
            l + wide, w - high * 0.15,
            l + wide * 1.1, w + high * 0.55,
            l + wide / 2.0, w + high
        );

        if (isFilled) {
            gc.fill();
        }

        gc.stroke();
        gc.closePath();

        gc.restore();
    }

    /**
     * Returns the current Hearts tool instance.
     *
     * @return the current Hearts tool instance.
     */
    public Hearts getTool() {
        return this;
    }
}