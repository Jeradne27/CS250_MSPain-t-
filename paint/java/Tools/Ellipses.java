package paint.Tools;

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
// - Add support for different line styles (e.g., solid, dashed, dotted).
// - Add support for different fill styles (e.g., solid, gradient, pattern).
// - Add a preview of the ellipse while drawing.


// ISSUES:
// - The current implementation does not provide a live preview of the ellipse while drawing.

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

    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

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