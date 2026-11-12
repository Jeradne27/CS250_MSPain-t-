package Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;

import javafx.scene.shape.StrokeLineCap;

public class Pentagons implements Shapes<Pentagons> {

    private Canvas canvas;
    private GraphicsContext gc;
    private Pentagons pentagons;

    private double width = 5.0;
    private Color color = Color.BLACK;
    private Color fillColor = null;

    private boolean isFilled = false;

    private double anchorX;
    private double anchorY;

    private boolean dashed;

    public Pentagons(Canvas canvas) {
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
            double radius = Math.hypot(event.getX() - anchorX, event.getY() - anchorY);

            double[] xPoints = new double[5];
            double[] yPoints = new double[5];

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
        });

        canvas.setOnMouseExited(event -> {
            pentagons = null;
        });
    }

    public void deactivateTool() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseExited(null);
    }

    public Pentagons getTool() {
        return this;
    }
}