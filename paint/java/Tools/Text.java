package paint.Tools;

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

/**
 * Represents the Text drawing tool.
 */

// IMPROVEMENT: 
// - Allows to add text to the canvas at a specified location

// ISSUES:
// - Text doesn't support changes in font, font size, or color, hoping to implement that in the future.
// - Text can't be edited after it is added to the canvas.

public class Text implements Drawing<Text> {
    private Canvas canvas;
    private GraphicsContext gc;

    private final Consumer<Runnable> canvasEdit;
    private Color color = Color.BLACK;
    private Font font = Font.font("Arial", 15);

    /**
     * Constructs a Text drawing tool with the specified canvas and canvas edit handler.
     *
     * @param canvas the canvas on which text will be drawn.
     * @param canvasEdit a consumer that accepts a runnable to handle canvas edits.
     */
    public Text(Canvas canvas, Consumer<Runnable> canvasEdit) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.canvasEdit = canvasEdit;
    }

    /**
     * Sets the color of the text to be drawn.
     *
     * @param color the color to set for the text.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the current color of the text to be drawn.
     *
     * @return the current color of the text.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the width of the text to be drawn.
     *
     * @param width the width to set for the text.
     */
    public void setWidth(double width) {}

    /**
     * Returns the current width of the text to be drawn.
     *
     * @return the current width of the text.
     */
    public double getWidth() {
        return 5.0;
    }

    /**
     * Returns the current font of the text to be drawn.
     *
     * @return the current font of the text.
     */
    public Font getFont() {
        return font;
    }

    /**
     * Sets the font of the text to be drawn.
     *
     * @param font the font to set for the text.
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Sets whether the text should be drawn with a dashed line.
     *
     * @param dashed true if the text should be drawn with a dashed line, false otherwise.
     */
    public void setDashed(boolean dashed) {}

    /**
     * Returns whether the text is set to be drawn with a dashed line.
     *
     * @return true if the text is set to be drawn with a dashed line, false otherwise.
     */
    public boolean isDashed() {
        return false;
    }

    /**
     * Activates the text drawing tool on the specified canvas.
     *
     * @param canvas the canvas on which the text drawing tool will be active.
     */
    public void activateTool(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY || !event.isStillSincePress()) {
                return;
            }

            double x = event.getX();
            double y = event.getY();

            // Prompt the user to enter the text to be drawn on the canvas.
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Text Input");
            dialog.setHeaderText("Enter text to add to the canvas:");
            
            // Get the text input editor from the dialog.
            TextField editor = dialog.getEditor();
            editor.setFont(font);
            editor.setPromptText("Type here");

            // Show the dialog and wait for the user to enter text.
            dialog.showAndWait().filter(text -> !text.isBlank()).ifPresent(text -> canvasEdit.accept(() -> drawText(canvas, text, x, y)));
        });
    }

    /**
     * Draws the specified text on the canvas at the given coordinates.
     *
     * @param canvas the canvas on which to draw the text.
     * @param text the text to draw.
     * @param x the x-coordinate of the text.
     * @param y the y-coordinate of the text.
     */
    private void drawText(Canvas canvas, String text, double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.save();

        // Apply the current text drawing settings (color, font, alignment, baseline) before drawing the text.
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

    /**
     * Deactivates the text drawing tool on the canvas.
     */
    public void deactivateTool() {
        canvas.setOnMouseClicked(null);
    }

    /**
     * Returns the current text drawing tool instance.
     *
     * @return the current text drawing tool instance.
     */
    public Text getTool() {
        return this;
    }
}