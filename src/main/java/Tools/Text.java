package Tools;

import java.util.function.Consumer;

import javafx.geometry.VPos;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextField;

import javafx.scene.input.MouseButton;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Text implements Drawing<Text> {
    private Canvas canvas;
    private GraphicsContext gc;

    private final Consumer<Runnable> canvasEdit;
    private Color color = Color.BLACK;
    private Font font = Font.font("Arial", 15);

    public Text(Canvas canvas, Consumer<Runnable> canvasEdit) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.canvasEdit = canvasEdit;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setWidth(double width) {}
    public double getWidth() {
        return 5.0;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setDashed(boolean dashed) {}
    public boolean isDashed() {
        return false;
    }

    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY || !event.isStillSincePress()) {
                return;
            }

            double x = event.getX();
            double y = event.getY();

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Text Input");
            dialog.setHeaderText("Enter text to add to the canvas:");
            
            TextField editor = dialog.getEditor();
            editor.setFont(font);
            editor.setPromptText("Type here");

            dialog.showAndWait().filter(text -> !text.isBlank()).ifPresent(text -> canvasEdit.accept(() -> drawText(canvas, text, x, y)));
        });
    }

    private void drawText(Canvas canvas, String text, double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.save();

        try {
            gc.setFill(color);
            gc.setFont(font);
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.TOP);
            gc.fillText(text, x, y);
        } finally {
            gc.restore();
        }
    }

    public void deactivateTool() {
        canvas.setOnMouseClicked(null);
    }

    public Text getTool() {
        return this;
    }
}