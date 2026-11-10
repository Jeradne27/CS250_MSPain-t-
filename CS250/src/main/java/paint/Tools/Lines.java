package paint.Tools;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.canvas.Canvas;

import javafx.scene.shape.StrokeLineCap;

import javafx.scene.paint.Color;

/**
 * Class representing the Lines drawing tool.
 */
// IMPROVEMENTS:
// - Add support for different line styles (e.g., solid, dashed).
// - Add a preview of the line while drawing.
// - Could add support for curves and other advanced line shapes.

// ISSUES:
// - The current implementation does not provide a live preview of the line while drawing.

public class Lines implements Drawing<Lines> {
    private Canvas canvas;
    private GraphicsContext gc;

    private double startingX;
    private double startingY;

    private double width = 5.0;
    private Color color = Color.BLACK;

    private boolean dashed = false;

    /**
     * Constructs a Lines drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the lines will be drawn.
     */
    public Lines(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public boolean isDashed() {
        return dashed;
    }

    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * Activates the Lines drawing tool on the given canvas.
     *
     * @param canvas the canvas on which the lines will be drawn.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        gc.setStroke(color);

        canvas.setOnMousePressed(event -> {
            startingX = event.getX();
            startingY = event.getY();
        });
        // Handle mouse released event to draw the line.
        canvas.setOnMouseReleased(event -> {
            gc.setLineWidth(width);
            gc.setStroke(color);

            if (dashed) {
                gc.setLineCap(StrokeLineCap.BUTT);
                gc.setLineDashes(5, 3);
            } else {
                gc.setLineCap(StrokeLineCap.SQUARE);
                gc.setLineDashes();
            }
            gc.strokeLine(startingX, startingY, event.getX(), event.getY());
        });
    }

    /**
     * Deactivates the Lines drawing tool.
     * Clears any active mouse event handlers on the canvas.
     * This should be called when the tool is no longer needed or when switching to a different tool.
     */
    public void deactivateTool() {
        // Clear any active mouse event handlers on the canvas.
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
    }

    /**
     * Returns the current instance of the Lines drawing tool.
     *
     * @return the current instance of the Lines drawing tool.
     */
    public Lines getTool() {
        return this;
    }
}