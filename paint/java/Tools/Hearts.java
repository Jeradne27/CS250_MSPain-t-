package paint.Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;

import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;


public class Hearts implements Shapes<Hearts> {

    private Canvas canvas;
    private Canvas previewCanvas;
    private GraphicsContext gc;
    private Hearts hearts;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;

    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;

    private boolean dashed;

    private boolean dragging;
    
    public Hearts(Canvas canvas, Canvas previewCanvas) {
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
                drawHeart(previewCanvas, event.getX(), event.getY());
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (dragging && event.getButton() == MouseButton.PRIMARY) {
                dragging = false;
                clearPreview();
                drawHeart(canvas, event.getX(), event.getY());
            }
        });

        canvas.setOnMouseExited(event -> {
            hearts = null;
            dragging = false;
        });
    }

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

    private void drawHeart(Canvas targetCanvas, double x, double y) {
        GraphicsContext gc = targetCanvas.getGraphicsContext2D();

        double l = Math.min(anchorX, x);
        double w = Math.min(anchorY, y);
        double wide = Math.abs(anchorX - x);
        double high = Math.abs(anchorY - y);

        if (dashed) {
            gc.setLineCap(StrokeLineCap.BUTT);
            gc.setLineDashes(5, 3);
        } else {
            gc.setLineCap(StrokeLineCap.SQUARE);
            gc.setLineDashes();
        }

        gc.save();

        if (isFilled) {
            gc.setFill(fillColor);
        }

        gc.setStroke(color);
        gc.setLineWidth(width);

        gc.beginPath();

        gc.moveTo(l + wide / 2.0, w + high);

        gc.bezierCurveTo(
            l - wide * 0.1, w + high * 0.55,
            l, w - high * 0.15,
            l + wide / 2.0, w + high * 0.25
        );

        gc.bezierCurveTo(
            l + wide, w - high * 0.15,
            l + wide * 1.1, w + high * 0.55,
            l + wide / 2.0, w + high
        );

        if (isFilled) {
            gc.fill();
        }

        gc.stroke();
        gc.closePath();

        gc.restore();
    }

    public Hearts getTool() {
        return this;
    }
}