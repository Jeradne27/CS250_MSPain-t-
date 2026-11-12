package Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Polygon;

import javafx.scene.paint.Color;

public class RightTriangles implements Shapes<RightTriangles> {
    private Canvas canvas;
    private GraphicsContext gc;
    private Polygon rightTriangle;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;
    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;

    private boolean dashed;

    public RightTriangles(Canvas canvas) {
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

            double[] xPoints = {anchorX, endX, anchorX};
            double[] yPoints = {anchorY, endY, endY};

            gc.setStroke(color);
            gc.setLineWidth(width);

            if (isFilled) {
                gc.setFill(fillColor);
                gc.fillPolygon(
                    new double[]{anchorX, endX, anchorX},
                    new double[]{anchorY, endY, endY},
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
            gc.strokePolygon(xPoints, yPoints, 3);
        });

        canvas.setOnMouseExited(event -> {
            rightTriangle = null;
        });
    }

    public void deactivateTool() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
    }

    public RightTriangles getTool() {
        return this;
    }
}