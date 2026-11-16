package paint.Tools;

import java.util.function.Consumer; 

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Polygon;

import javafx.scene.paint.Color;

/**
 * A tool for drawing diamond shapes on a canvas.
 * Provides methods to set the color, width, fill status, and dashed style of the diamond.
 * The diamond is drawn on the canvas when the mouse is released.
 * The tool can be activated and deactivated to enable or disable drawing.
 * The current instance of the tool can be retrieved using the getTool() method.
 */

// IMPROVEMENTS:
// - Added preview of the diamond while dragging the mouse.

// ISSUES:
// - There is no menu option to fill the diamond at the moment, though there are setters for fill color and fill status.

public class FourPointStars implements Shapes<FourPointStars> {
    private Canvas canvas;
    private Canvas previewCanvas;
    private GraphicsContext gc;
    private Polygon fourPointStar;

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
     * Constructs a new FourPointStars tool with the given canvas.
     * @param canvas The canvas on which the four-point star will be drawn.
     */
    public FourPointStars(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.previewCanvas = previewCanvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setCanvasLogger(Consumer<String> canvasLogger) {
        this.canvasLogger = canvasLogger;
    }

    /**
     * Sets the color of the four-point star.
     * @param color The color to use for the four-point star.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the color of the four-point star.
     * @return The color of the four-point star.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the width of the four-point star's stroke.
     * @param width The width to use for the four-point star's stroke.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets the width of the four-point star's stroke.
     * @return The width of the four-point star's stroke.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets whether the four-point star should be filled.
     * @param isFilled True if the four-point star should be filled, false otherwise.
     */
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    /**
     * Sets the fill color of the four-point star.
     * @param fillColor The color to use for filling the four-point star.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Gets the fill color of the four-point star.
     * @return The fill color of the four-point star.
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Checks if the four-point star is filled.
     * @return True if the four-point star is filled, false otherwise.
     */
    public boolean isFilled() {
        return this.isFilled;
    }

    /**
     * Sets whether the four-point star's stroke should be dashed.
     * @param dashed True if the four-point star's stroke should be dashed, false otherwise.
     */
    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * Checks if the four-point star's stroke is dashed.
     * @return True if the four-point star's stroke is dashed, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Activates the four-point star drawing tool on the given canvas.
     * @param canvas The canvas on which the four-point star will be drawn.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            anchorX = event.getX();
            anchorY = event.getY();
            dragging = true;

            canvasLogger.accept("Four-Point star drawn on canvas");
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawFourPointStar(previewCanvas, event.getX(), event.getY());
            }
        });
        // Handle mouse release event to draw the four-point star.
        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawFourPointStar(canvas, event.getX(), event.getY());
            }
        });
        // Handle mouse exit event to clear the current four-point star.
        canvas.setOnMouseExited(event -> {
            fourPointStar = null;
            dragging = false;
        });
    }

    /**
     * Deactivates the four-point star drawing tool, removing its event handlers from the canvas.
     * This should be called when the tool is no longer needed or when switching to a different tool.
     */
    public void deactivateTool() {
        // Remove all event handlers from the canvas.
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
        canvas.setOnMouseDragged(null);
        dragging = false;
    }

    /**
     * Clears the preview of the four-point star being drawn on the preview canvas.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Draws a four-point star on the target canvas with the specified center and radius.
     * @param targetCanvas The canvas on which to draw the four-point star.
     * @param x The x-coordinate of the four-point star's center.
     * @param y The y-coordinate of the four-point star's center.
     */
    private void drawFourPointStar(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        double left = Math.min(anchorX, x);
        double top = Math.min(anchorY, y);
        double wide = Math.abs(x - anchorX);
        double height = Math.abs(y - anchorY);

        double midX = left + wide / 2.0;
        double midY = top + height / 2.0;

        double insetX = wide * 0.15;
        double insetY = height * 0.15;

        double [] xPoints = {
            midX,
            midX + insetX,
            left + wide,
            midX + insetX,
            midX,
            midX - insetX,
            left,
            midX - insetX
        };

        double [] yPoints = {
            top,
            midY - insetY,
            midY,
            midY + insetY,
            top + height,
            midY + insetY,
            midY,
            midY - insetY
        };

        gc.save();

        try {
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
                gc.fillPolygon(xPoints, yPoints, 8);
            }

            gc.strokePolygon(xPoints, yPoints, 8);
        } finally {
            gc.restore();
        }
    }

    /**
     * Returns the current instance of the Four-Point Stars tool.
     * @return The current Four-Point Stars tool instance.
     */
    public FourPointStars getTool() {
        return this;
    }
}