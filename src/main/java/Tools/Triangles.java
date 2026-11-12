package Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Polygon;

import javafx.scene.paint.Color;

/**
 * Class representing the Triangles drawing tool.
 * Implements the Shapes interface for drawing triangles on a canvas.
 */

// IMPROVEMENTS:
// - Add support for different types of triangles (e.g., equilateral, isosceles).
// - Add support for drawing triangles with rounded corners.
// - Add support for drawing triangles with different orientations (e.g., pointing up, down, left, right).
// - Add a preview of the triangle while the user is dragging the mouse.

// ISSUES:
// - The current implementation only supports drawing triangles with the base at the bottom.
// - The triangle is only drawn on mouse release, not during the drag.

public class Triangles implements Shapes<Triangles> {
    private Canvas canvas;
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

    /**
     * Constructs a new Triangles tool with the specified canvas.
     *
     * @param canvas the canvas on which the triangles will be drawn
     */
    public Triangles(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWidth() {
        return this.width;
    }

    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getFillColor() {
        return this.fillColor;
    }

    public boolean isFilled() {
        return this.isFilled;
    }

    public boolean isDashed() {
        return dashed;
    }

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
        });

        // Handle mouse released event to draw the triangle
        canvas.setOnMouseReleased(event -> {
            endX = event.getX();
            endY = event.getY();

            leftX = Math.min(anchorX, endX);
            rightX = Math.max(anchorX, endX);
            topY = Math.min(anchorY, endY);
            bottomY = Math.max(anchorY, endY);

            mid = leftX + (rightX - leftX) / 2;

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

            gc.strokePolygon(
                new double[]{mid, leftX, rightX},
                new double[]{topY, bottomY, bottomY},
                3
            );
        });

        canvas.setOnMouseExited(event -> {
            // Clear the current triangle preview
            triangle = null;
        });
    }

    /**
     * Deactivates the Triangles tool by removing its event handlers from the canvas.
     */
    public void deactivateTool() {
        // Clear the current triangle preview
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
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