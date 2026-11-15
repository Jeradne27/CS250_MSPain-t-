package paint.Tools;

import javafx.scene.canvas.Canvas;

import javafx.scene.shape.StrokeLineCap;

import javafx.scene.paint.Color;

/**
 * Interface representing a generic shape drawing tool.
 *
 * @param <T> the type of the shape drawing tool.
 */

// IMPROVEMENTS:
// - N/A

// ISSUES:
// - N/A

// Implemented by:
// - Circles.java
// - Diamonds.java
// - Ellipses.java
// - Hearts.java
// - Pentagons.java
// - Rectangles.java
// - RightTriangles.java
// - Squares.java
// - Triangles.java

public interface Shapes<T> extends Drawing<T> {
    // Methods related to the shape's fill and stroke properties.
    void setFilled(boolean filled);
    boolean isFilled();

    void setFillColor(Color fillColor);
    Color getFillColor();

    void setDashed(boolean dashed);
    boolean isDashed();

}