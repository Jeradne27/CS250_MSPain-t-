package paint.Tools;

import java.util.function.Consumer;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import paint.Operations.FileTab;

public class Text implements Drawing<Text> {
    private static final double fontSize = 24;
    
    private final Consumer<Runnable> applyEdit;
    private Canvas canvas;
    private Color color = Color.BLACK;
    private FileTab fileTab;

    public Text(FileTab fileTab, Consumer<Runnable> applyEdit) {
        this.fileTab = fileTab;
        this.applyEdit = applyEdit;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setWidth(double width) {}
    
    public double getWidth() {
        return 1.0;
    }

    public void setDashed(boolean dashed) {}

    public boolean isDashed() {
        return false;
    }

    public void activateTool(Canvas canvas) {
        this.canvas = canvas;

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY || !event.isStillSincePress()) {
                return;
            }
            
            double x = event.getX();
            double y = event.getY();

            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Text Tool");
            dialog.setHeaderText("Enter text:");
            dialog.setContentText("Text:");

            dialog.showAndWait().filter(text -> !text.isBlank()).ifPresent(text -> applyEdit.accept(() -> drawText(canvas, text, x, y)));
        });
    }

    private void drawText(Canvas canvas, String text, double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.save();

        try {
            gc.setFill(color);
            gc.setFont(Font.font(fontSize));
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.TOP);
            gc.fillText(text, x, y);
        } finally {
            gc.restore();
        }
    }

    public void deactivateTool() {
        if (canvas != null) {
            canvas.setOnMouseClicked(null);
            canvas = null;
        }
    }

    public Text getTool() {
        return this;
    }
}