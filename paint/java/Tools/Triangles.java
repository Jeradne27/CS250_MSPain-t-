package paint.Tools;

import java.util.function.Consumer; 

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Polygon;

import javafx.scene.paint.Color;

/**
 * Class representing the Triangles drawing tool.
 * Implements the Shapes interface for drawing triangles on a canvas.
 */

// IMPROVEMENTS:
// - Added a preview of the triangle while the user is dragging the mouse.
// - Supports drawing triangle in different orientations (e.g., pointing up, down, left, right).

// ISSUES:
// - There isn't a fill option yet, so setFill and setFillColor methods are currently unused.// - There isn't a fill option yet, so setFill and setFillColor methods are currently unused.

public class Triangles implements Shapes<Triangles> {
    private Canvas canvas;
    private Canvas previewCanvas;
    private GraphicsContext gc;
    private Polygon triangle;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;
    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;
    private double mid;
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
     * Constructs a new Triangles tool with the specified canvas.
     *
     * @param canvas the canvas on which the triangles will be drawn
     */
    public Triangles(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.previewCanvas = previewCanvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setCanvasLogger(Consumer<String> canvasLogger) {
        this.canvasLogger = canvasLogger;
    }

    /**
     * Sets the color of the triangle to be drawn.
     *
     * @param color the color to set for the triangle.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the current color of the triangle to be drawn.
     *
     * @return the current color of the triangle.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the width of the triangle to be drawn.
     *
     * @param width the width to set for the triangle.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the current width of the triangle to be drawn.
     *
     * @return the current width of the triangle.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets whether the triangle should be filled.
     *
     * @param isFilled true if the triangle should be filled, false otherwise.
     */
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    /**
     * Sets the fill color of the triangle.
     *
     * @param fillColor the fill color to set for the triangle.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the current fill color of the triangle.
     *
     * @return the current fill color of the triangle.
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Returns whether the triangle is set to be filled.
     *
     * @return true if the triangle is set to be filled, false otherwise.
     */
    public boolean isFilled() {
        return this.isFilled;
    }

    /**
     * Returns whether the triangle is set to be drawn with a dashed line.
     *
     * @return true if the triangle is set to be drawn with a dashed line, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Sets whether the triangle should be drawn with a dashed line.
     *
     * @param dashed true if the triangle should be drawn with a dashed line, false otherwise.
     */
    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * Activates the Triangles tool on the specified canvas.
     *
     * @param canvas the canvas on which the triangles will be drawn
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            anchorX = event.getX();
            anchorY = event.getY();
            dragging = true;

            canvasLogger.accept("Triangle drawn on canvas");
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawTriangle(previewCanvas, event.getX(), event.getY());
            }
        });

        // Handle mouse released event to draw the triangle
        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawTriangle(canvas, event.getX(), event.getY());
            }
        });

        canvas.setOnMouseExited(event -> {
            // Clear the current triangle preview
            triangle = null;
            dragging = false;
        });
    }

    /**
     * Deactivates the Triangles tool by removing its event handlers from the canvas.
     */
    public void deactivateTool() {
        // Clear the current triangle preview
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
        dragging = false;
    }

    /**
     * Clears the preview of the triangle on the preview canvas.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Draws a triangle on the specified target canvas with the given coordinates.
     *
     * @param targetCanvas the canvas on which to draw the triangle.
     * @param x the x-coordinate of the second point of the triangle.
     * @param y the y-coordinate of the second point of the triangle.
     */
    private void drawTriangle(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        // Calculate the coordinates of the triangle based on the anchor point and the current mouse position.
        leftX = Math.min(anchorX, x);
        rightX = Math.max(anchorX, x);
        topY = Math.min(anchorY, y);
        bottomY = Math.max(anchorY, y);

        mid = leftX + (rightX - leftX) / 2;

        gc.save();
        
        gc.setStroke(color);
        gc.setLineWidth(width);
        if (isFilled) {
            gc.setFill(fillColor);
            gc.fillPolygon(
                new double[]{mid, leftX, rightX},
                new double[]{topY, bottomY, bottomY},
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

        // Draw the outline of the triangle.
        gc.strokePolygon(
            new double[]{mid, leftX, rightX},
            new double[]{topY, bottomY, bottomY},
            3
        );
        gc.restore();
    }
    /**
     * Returns the current instance of the Triangles tool.
     *
     * @return the current instance of the Triangles tool
     */
    public Triangles getTool() {
        return this;
    }
}