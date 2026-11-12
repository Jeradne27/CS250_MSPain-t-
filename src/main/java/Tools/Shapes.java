package Tools;

import javafx.scene.canvas.Canvas;

import javafx.scene.shape.StrokeLineCap;

import javafx.scene.paint.Color;

/**
 * Interface representing a generic shape drawing tool.
 *
 * @param <T> the type of the shape drawing tool.
 */

// IMPROVEMENTS:
// - Add a live preview of the shape while drawing.

// ISSUES:
// - The current implementation does not provide a live preview of the shape while drawing.
public interface Shapes<T> extends Drawing<T> {
    // Methods related to the shape's fill and stroke properties.
    void setFilled(boolean filled);
    boolean isFilled();

    void setFillColor(Color fillColor);
    Color getFillColor();

    void setDashed(boolean dashed);
    boolean isDashed();

}