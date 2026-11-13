package paint.Tools;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.canvas.Canvas;

import javafx.scene.input.MouseButton;

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
    private Canvas previewCanvas;
    private GraphicsContext gc;

    private double startingX;
    private double startingY;

    private double width = 5.0;
    private Color color = Color.BLACK;

    private boolean dashed = false;

    private boolean dragging;

    /**
     * Constructs a Lines drawing tool with the given canvas.
     *
     * @param canvas the canvas on which the lines will be drawn.
     */
    public Lines(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.previewCanvas = previewCanvas;
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
            dragging = true;
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawLine(previewCanvas, event.getX(), event.getY());
            }
        });
        // Handle mouse released event to draw the line.
        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawLine(canvas, event.getX(), event.getY());
            }
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

    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    private void drawLine(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();
        
        gc.save();
        
        gc.setLineWidth(width);
        gc.setStroke(color);

        if (dashed) {
            gc.setLineCap(StrokeLineCap.BUTT);
            gc.setLineDashes(5, 3);
        } else {
            gc.setLineCap(StrokeLineCap.SQUARE);
            gc.setLineDashes();
        }
        gc.strokeLine(startingX, startingY, x, y);
        
        gc.restore();
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