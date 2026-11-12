package Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;


public class Hearts implements Shapes<Hearts> {

    private Canvas canvas;
    private GraphicsContext gc;
    private Hearts hearts;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;

    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;

    private boolean dashed;

    public Hearts(Canvas canvas) {
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

    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            anchorX = event.getX();
            anchorY = event.getY();
        });

        canvas.setOnMouseReleased(event -> {
            double endX = event.getX();
            double endY = event.getY();

            double x = Math.min(anchorX, endX);
            double y = Math.min(anchorY, endY);
            double wide = Math.abs(anchorX - endX);
            double high = Math.abs(anchorY - endY);

            if (dashed) {
                gc.setLineCap(StrokeLineCap.BUTT);
                gc.setLineDashes(5, 3);
            } else {
                gc.setLineCap(StrokeLineCap.SQUARE);
                gc.setLineDashes();
            }

            drawHeart(gc, x, y, wide, high);
        });

        canvas.setOnMouseExited(event -> {
            hearts = null;
        });
    }

    private void drawHeart(GraphicsContext gc, double x, double y, double wide, double high) {
        gc.save();
        if (isFilled) {
            gc.setFill(fillColor);
        }

        gc.setStroke(color);
        gc.setLineWidth(width);

        gc.beginPath();

        gc.moveTo(x + wide / 2.0, y + high);

        gc.bezierCurveTo(
            x - wide * 0.1, y + high * 0.55,
            x, y - high * 0.15,
            x + wide / 2.0, y + high * 0.25
        );

        gc.bezierCurveTo(
            x + wide, y - high * 0.15,
            x + wide * 1.1, y + high * 0.55,
            x + wide / 2.0, y + high
        );

        if (isFilled) {
            gc.fill();
        }

        gc.stroke();
        gc.closePath();

        gc.restore();
    }

    public void deactivateTool() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
    }

    public Hearts getTool() {
        return this;
    }
}