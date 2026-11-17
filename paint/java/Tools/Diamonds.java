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
// - Added handler for thread logging

// ISSUES:
// - There is no menu option to fill the diamond at the moment, though there are setters for fill color and fill status.

public class Diamonds implements Shapes<Diamonds> {
    private Canvas canvas;
    private Canvas previewCanvas;
    private GraphicsContext gc;
    private Polygon diamond;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;
    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;
    private double midX;
    private double midY;
    private double endX;
    private double endY;
    private double leftX;
    private double rightX;
    private double topY;
    private double bottomY;

    private boolean dashed;

    private boolean dragging;
    
    private Consumer<String> canvasLogger = action -> {};

    /**
     * Constructs a new Diamonds tool with the given canvas.
     * @param canvas The canvas on which the diamond will be drawn.
     */
    public Diamonds(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.previewCanvas = previewCanvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    /**
     * Sets the logger for the canvas.
     * @param canvasLogger The logger to set for the canvas.
     */
    public void setCanvasLogger(Consumer<String> canvasLogger) {
        this.canvasLogger = canvasLogger;
    }

    /**
     * Sets the color of the diamond.
     * @param color The color to use for the diamond.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the color of the diamond.
     * @return The color of the diamond.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the width of the diamond's stroke.
     * @param width The width to use for the diamond's stroke.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets the width of the diamond's stroke.
     * @return The width of the diamond's stroke.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets whether the diamond should be filled.
     * @param isFilled True if the diamond should be filled, false otherwise.
     */
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    /**
     * Sets the fill color of the diamond.
     * @param fillColor The color to use for filling the diamond.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Gets the fill color of the diamond.
     * @return The fill color of the diamond.
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Checks if the diamond is filled.
     * @return True if the diamond is filled, false otherwise.
     */
    public boolean isFilled() {
        return this.isFilled;
    }

    /**
     * Sets whether the diamond's stroke should be dashed.
     * @param dashed True if the diamond's stroke should be dashed, false otherwise.
     */
    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * Checks if the diamond's stroke is dashed.
     * @return True if the diamond's stroke is dashed, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Activates the diamond drawing tool on the given canvas.
     * @param canvas The canvas on which the diamond will be drawn.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            anchorX = event.getX();
            anchorY = event.getY();
            dragging = true;

            canvasLogger.accept("Diamond drawn on canvas");
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawDiamond(previewCanvas, event.getX(), event.getY());
            }
        });
        // Handle mouse release event to draw the diamond.
        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawDiamond(canvas, event.getX(), event.getY());
            }
        });
        // Handle mouse exit event to clear the current diamond.
        canvas.setOnMouseExited(event -> {
            diamond = null;
            dragging = false;
        });
    }

    /**
     * Deactivates the diamond drawing tool, removing its event handlers from the canvas.
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
     * Clears the preview of the diamond being drawn on the preview canvas.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Draws a diamond on the target canvas with the specified center and radius.
     * @param targetCanvas The canvas on which to draw the diamond.
     * @param x The x-coordinate of the diamond's center.
     * @param y The y-coordinate of the diamond's center.
     */
    private void drawDiamond(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        leftX = Math.min(anchorX, x);
        rightX = Math.max(anchorX, x);
        topY = Math.min(anchorY, y);
        bottomY = Math.max(anchorY, y);

        midX = leftX + ((rightX - leftX) / 2.0);
        midY = topY + ((bottomY - topY) / 2.0);

        gc.save();

        gc.setStroke(color);
        gc.setLineWidth(width);
        if (isFilled) {
            gc.setFill(fillColor);
            gc.fillPolygon(
                new double[]{midX, rightX, midX, leftX},
                new double[]{topY, midY, bottomY, midY},
                4
            );
        }

        if (dashed) {
            gc.setLineCap(StrokeLineCap.BUTT);
            gc.setLineDashes(5, 3);
        } else {
            gc.setLineCap(StrokeLineCap.SQUARE);
            gc.setLineDashes();
        }

        gc.strokePolygon(
            new double[]{midX, rightX, midX, leftX},
            new double[]{topY, midY, bottomY, midY},
            4
        );

        gc.restore();
    }

    /**
     * Returns the current instance of the Diamonds tool.
     * @return The current Diamonds tool instance.
     */
    public Diamonds getTool() {
        return this;
    }
}