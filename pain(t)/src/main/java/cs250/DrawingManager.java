package cs250;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawingManager {
    private Canvas canvas;
    private GraphicsContext gc;

    private double startingX;
    private double startingY;
    
    @SuppressWarnings("exports")
    public DrawingManager(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void drawShapes() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5.0);

        canvas.setOnMousePressed(event -> {
            startingX = event.getX();
            startingY = event.getY();
        });

        canvas.setOnMouseReleased(event -> {
            double endingX = event.getX();
            double endingY = event.getY();

            gc.strokeLine(startingX, startingY, endingX, endingY);
        });
    }
}

