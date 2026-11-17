package paint.Tools;

import java.util.function.Consumer; 

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Polygon;

import javafx.scene.paint.Color;

/**
 * A tool for drawing right triangles on a canvas.
 */

// IMPROVEMENTS:
// - Added handler for thread logging

// ISSUES:
// - Since there is no fill menu yet, the fill color cannot be changed yet.

public class RightTriangles implements Shapes<RightTriangles> {
    private Canvas canvas;
    private Canvas previewCanvas;
    private GraphicsContext gc;
    private Polygon rightTriangle;

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
     * Constructs a new RightTriangles tool with the specified canvas and preview canvas.
     *
     * @param canvas the main canvas on which the right triangles will be drawn.
     * @param previewCanvas the preview canvas on which the right triangles will be previewed.
     */
    public RightTriangles(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.previewCanvas = previewCanvas;
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
     * Sets the stroke color of the right triangle.
     *
     * @param color the color to set for the right triangle's stroke.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the stroke color of the right triangle.
     *
     * @return the stroke color of the right triangle.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the width of the right triangle's stroke.
     *
     * @param width the width to set for the right triangle's stroke.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the width of the right triangle's stroke.
     *
     * @return the width of the right triangle's stroke.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets whether the right triangle should be filled.
     *
     * @param isFilled true if the right triangle should be filled, false otherwise.
     */
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    /**
     * Sets the fill color of the right triangle.
     *
     * @param fillColor the color to set for the right triangle's fill.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the fill color of the right triangle.
     *
     * @return the fill color of the right triangle.
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Returns whether the right triangle is filled.
     *
     * @return true if the right triangle is filled, false otherwise.
     */
    public boolean isFilled() {
        return this.isFilled;
    }

    /**
     * Returns whether the right triangle is using a dashed stroke.
     *
     * @return true if the right triangle is using a dashed stroke, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Sets whether the right triangle should use a dashed stroke.
     *
     * @param dashed true if the right triangle should use a dashed stroke, false otherwise.
     */
    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * Activates the right triangle tool on the specified canvas.
     *
     * @param canvas the canvas on which the right triangle tool will be active.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            anchorX = event.getX();
            anchorY = event.getY();
            dragging = true;

            canvasLogger.accept("Right triangle drawn on canvas");
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawRightTriangle(previewCanvas, event.getX(), event.getY());
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawRightTriangle(canvas, event.getX(), event.getY());
            }
        });

        canvas.setOnMouseExited(event -> {
            rightTriangle = null;
            dragging = false;
        });
    }

    /**
     * Deactivates the right triangle tool on the currently active canvas.
     */
    public void deactivateTool() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
        dragging = false;
    }

    /**
     * Clears the preview of the right triangle on the preview canvas.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Draws a right triangle on the specified target canvas.
     *
     * @param targetCanvas the canvas on which to draw the right triangle.
     * @param x the x-coordinate of the third vertex of the right triangle.
     * @param y the y-coordinate of the third vertex of the right triangle.
     */
    private void drawRightTriangle(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        // Define the vertices of the right triangle based on the anchor point and the third vertex.
        double[] xPoints = {anchorX, x, anchorX};
        double[] yPoints = {anchorY, y, y};

        gc.save();

        gc.setStroke(color);
        gc.setLineWidth(width);

        if (isFilled) {
            gc.setFill(fillColor);
            gc.fillPolygon(
                new double[]{anchorX, x, anchorX},
                new double[]{anchorY, y, y},
                3
            );
        }

        if (dashed) {
            gc.setLineCap(StrokeLineCap.BUTT);
            gc.setLineDashes(5, 3);
        } else {
            gc.setLineCap(StrokeLineCap.SQUARE);
            gc.setLineDashes();
        }

        // Draw the outline of the right triangle.
        gc.strokePolygon(xPoints, yPoints, 3);

        gc.restore();
    }

    /**
     * Returns the current instance of the right triangle tool.
     *
     * @return the current instance of the right triangle tool.
     */
    public RightTriangles getTool() {
        return this;
    }
}