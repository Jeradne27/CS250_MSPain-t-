package Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

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
// - Allow the user to change the fill pattern dynamically while drawing.
// - Previews before the shape is added to the canvas while dragging the mouse.
// - There is no menu option to fill the diamond at the moment, though there are setters for fill color and fill status.

// ISSUES:
// - The diamond is only drawn on mouse release, not dynamically while dragging.
// - The dashed line style is applied only on the final stroke, not during the drawing process.
// - There is no menu option to fill the diamond at the moment, though there are setters for fill color and fill status.

public class Diamonds implements Shapes<Diamonds> {
    private Canvas canvas;
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
    
    /**
     * Constructs a new Diamonds tool with the given canvas.
     * @param canvas The canvas on which the diamond will be drawn.
     */
    public Diamonds(Canvas canvas) {
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

    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

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
        });
        // Handle mouse release event to draw the diamond.
        canvas.setOnMouseReleased(event -> {
            endX = event.getX();
            endY = event.getY();
            leftX = Math.min(anchorX, endX);
            rightX = Math.max(anchorX, endX);
            topY = Math.min(anchorY, endY);
            bottomY = Math.max(anchorY, endY);

            midX = leftX + ((rightX - leftX) / 2.0);
            midY = topY + ((bottomY - topY) / 2.0);

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

            // Apply the stroke style (dashed or solid) before drawing the outline.
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
        });
        // Handle mouse exit event to clear the current diamond.
        canvas.setOnMouseExited(event -> {
            diamond = null;
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
    }

    /**
     * Returns the current instance of the Diamonds tool.
     * @return The current Diamonds tool instance.
     */
    public Diamonds getTool() {
        return this;
    }
}