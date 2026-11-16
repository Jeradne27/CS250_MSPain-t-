package paint.Tools;

import java.util.function.Consumer; 

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;

import javafx.scene.paint.Color;

import javafx.scene.shape.StrokeLineCap;

/**
 * Class representing the Pentagons drawing tool.
 */

// IMPROVEMENTS:
// - Newest shape to the shape menu
// - Added a live preview of the pentagon while drawing.

// ISSUES:
// - Some weird stuff happens when you drag the mouse backwards while drawing the pentagon, hope to get to the bottom of that later

public class Pentagons implements Shapes<Pentagons> {

    private Canvas canvas;
    private Canvas previewCanvas;
    private GraphicsContext gc;
    private Pentagons pentagons;

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
     * Constructs a Pentagons drawing tool with the given canvas and preview canvas.
     *
     * @param canvas the main canvas on which the pentagon will be drawn.
     * @param previewCanvas the preview canvas on which the live preview of the pentagon will be drawn.
     */
    public Pentagons(Canvas canvas, Canvas previewCanvas) {
        this.canvas = canvas;
        this.previewCanvas = previewCanvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setCanvasLogger(Consumer<String> canvasLogger) {
        this.canvasLogger = canvasLogger;
    }

    /**
     * Sets the color of the pentagon.
     *
     * @param color the color to set for the pentagon.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the current color of the pentagon.
     *
     * @return the current color of the pentagon.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the width of the pentagon.
     *
     * @param width the width to set for the pentagon.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the current width of the pentagon.
     *
     * @return the current width of the pentagon.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets whether the pentagon should be filled.
     *
     * @param isFilled true if the pentagon should be filled, false otherwise.
     */
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    /**
     * Sets the fill color of the pentagon.
     *
     * @param fillColor the fill color to set for the pentagon.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the current fill color of the pentagon.
     *
     * @return the current fill color of the pentagon.
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * Returns whether the pentagon is filled.
     *
     * @return true if the pentagon is filled, false otherwise.
     */
    public boolean isFilled() {
        return this.isFilled;
    }

    /**
     * Returns whether the pentagon is using a dashed stroke.
     *
     * @return true if the pentagon is using a dashed stroke, false otherwise.
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Sets whether the pentagon should use a dashed stroke.
     *
     * @param dashed true if the pentagon should use a dashed stroke, false otherwise.
     */
    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * Activates the Pentagons drawing tool on the given canvas.
     *
     * @param canvas the canvas on which the pentagon will be drawn.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            anchorX = event.getX();
            anchorY = event.getY();
            dragging = true;

            canvasLogger.accept("Pentagon drawn on canvas");
        });

        canvas.setOnMouseDragged(event -> {
            if (dragging && event.isPrimaryButtonDown()) {
                clearPreview();
                drawPentagon(previewCanvas, event.getX(), event.getY());
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawPentagon(canvas, event.getX(), event.getY());
            }
        });

        canvas.setOnMouseExited(event -> {
            pentagons = null;
            dragging = false;
        });
    }

    /**
     * Deactivates the Pentagons drawing tool.
     */
    public void deactivateTool() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
        dragging = false;
    }

    /**
     * Clears the preview of the pentagon on the preview canvas.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Draws a pentagon on the specified canvas at the given coordinates.
     *
     * @param targetCanvas the canvas on which to draw the pentagon.
     * @param x the x-coordinate of the mouse pointer.
     * @param y the y-coordinate of the mouse pointer.
     */
    private void drawPentagon(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        double radius = Math.hypot(x - anchorX, y - anchorY);

        gc.save();

        // Initialize the arrays to hold the x and y coordinates of the pentagon's vertices.
        double[] xPoints = new double[5];
        double[] yPoints = new double[5];

        // Calculate the vertices of the pentagon based on the anchor point and radius.
        for (int i = 0; i < 5; i++) {
            double angle = (2 * Math.PI * i / 5.0) - (Math.PI / 2.0);
            
            xPoints[i] = anchorX  + radius * Math.cos(angle);
            yPoints[i] = anchorY + radius * Math.sin(angle);
        }

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
            gc.fillPolygon(xPoints, yPoints, 5);
        }

        gc.strokePolygon(xPoints, yPoints, 5);
        gc.restore();
    }

    /**
     * Returns the current instance of the Pentagons tool.
     *
     * @return the current instance of the Pentagons tool.
     */
    public Pentagons getTool() {
        return this;
    }
}