package paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.shape.Line;



public class Lines {
    private Canvas canvas;
    private GraphicsContext gc;

    private double startingX;
    private double startingY;

    private double lineWidth = 5.0;
    private Color lineColor = Color.BLACK;

    public Lines(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void drawLine() {
        gc.setStroke(lineColor);

        canvas.setOnMousePressed(event -> {
            startingX = event.getX();
            startingY = event.getY();
        });

        canvas.setOnMouseReleased(event -> {
            gc.setLineWidth(lineWidth);
            gc.setStroke(lineColor);
            gc.strokeLine(startingX, startingY, event.getX(), event.getY());
        });
    }
}