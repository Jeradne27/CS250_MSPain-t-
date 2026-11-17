package paint.Tools;

import javafx.scene.canvas.Canvas;

import javafx.scene.paint.Color;

/**
 * Interface representing a drawing tool with color, width, and dashed properties.
 */

// IMPROVEMENTS:
// - N/A

// ISSUES:
// - N/A

// Implementeded by:
// - Lines.java
// - Pencil.java
// - Eraser.java
// - Selection.java
// - Text.java
// - Shapes.java (extends)

public interface Drawing<T> {

    // Activate and deactivate the drawing tool on the given canvas.
    // This should be called when the tool is no longer needed or when switching to a different tool.
    void activateTool(Canvas canvas);
    void deactivateTool();

    // Returns the current instance of the drawing tool.
    // This is useful for chaining method calls or for type-specific operations.
    T getTool();
    
    // Set and get the color of the drawing tool.
    void setColor(Color color);
    Color getColor();

    // Set and get the width of the drawing tool.
    void setWidth(double width);
    double getWidth();

    // Set and get the dashed property of the drawing tool.
    void setDashed(boolean dashed);
    boolean isDashed();
}
