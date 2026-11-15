package paint.Tools;

import java.awt.Rectangle;

import javafx.event.EventHandler;

import javafx.geometry.Rectangle2D;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.SnapshotParameters;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;

import paint.Operations.FileTab;

/**
 * Represents the selection tool in the paint application.
 */

// IMPROVEMENTS:
// - Added support for selecting and moving pixels on the canvas.
// - Added support for copying and pasting selected pixels.
// - Added support for cutting selected pixels.

// ISSUES:
// - There is no confirmation that a section was successfully copied and is ready for pasting, so I want to add a visual indicator or message to inform the user.
// - When you move a selection, set it down, and pick it up again, the selection picks up what was at the drop off location too, working on fixing that
// - The pasted image is a bit blurry or distorted, working on improving the quality.

public class Selection implements Drawing<Selection> {
    private final FileTab file;
    
    private Canvas canvas;
    private Canvas previewCanvas;

    private Rectangle2D selection;
    private boolean selecting;

    private double pressedX;
    private double pressedY;

    private static WritableImage copiedPixels;
    private WritableImage movingPixels;
    private Image pendingPaste;

    private final EventHandler<MouseEvent> pressHandler = this::onPress;
    private final EventHandler<MouseEvent> draggingHandler = this::onDrag;
    private final EventHandler<MouseEvent> releaseHandler = this::onRelease;

    /**
     * Constructs a new Selection tool with the specified FileTab.
     *
     * @param file the FileTab instance associated with this selection tool.
     */
    public Selection(FileTab file) {
        this.file = file;
        this.previewCanvas = file.getPreviewCanvas();
    }

    /**
     * Activates the selection tool on the specified canvas.
     *
     * @param canvas the canvas on which the selection tool will be active.
     */
    public void activateTool(Canvas canvas) {
        if (this.canvas != null) {
            deactivateTool();
        }

        this.canvas = canvas;

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, draggingHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, releaseHandler);
    }

    /**
     * Handles the mouse press event for the selection tool.
     *
     * @param event the mouse press event.
     */
    private void onPress(MouseEvent event) {
        // Implementation for mouse press event
        if (event.getButton() != MouseButton.PRIMARY) {
            return;
        }

        pressedX = event.getX();
        pressedY = event.getY();

        // Handle paste if there is a pending paste image.
        if (pendingPaste != null) {
            // Implementation for handling paste
            Image image = pendingPaste;
            pendingPaste = null;
            selection = null;
            clearPreview();

            int x = (int) Math.floor(pressedX);
            int y = (int) Math.floor(pressedY);

            file.canvasEdit(() -> canvas.getGraphicsContext2D().drawImage(image, x, y));
            return;
        }

        // Handle selection if the press occurred within the existing selection.
        if (selection != null && selection.contains(pressedX, pressedY)) {
            // Implementation for handling selection
            movingPixels = captureSelection();
        } else {
            selecting = true;
            selection = null;
            clearPreview();
        }
    }

    /**
     * Handles the mouse drag event for the selection tool.
     *
     * @param event the mouse drag event.
     */
    private void onDrag(MouseEvent event) {
        // Implementation for mouse drag event
        if (!event.isPrimaryButtonDown()) {
            return;
        }

        // Handle drag of selected pixels if there are any.
        if (movingPixels != null) {
            // Implementation for handling drag of selected pixels
            Rectangle2D destination = moveDestination(event.getX(), event.getY());
            
            showPreview(destination, movingPixels);
        } else if (selecting) {
            selection = selectionBounds(event.getX(), event.getY());
            showPreview(selection, null);
        }
    }

    /**
     * Handles the mouse release event for the selection tool.
     *
     * @param event the mouse release event.
     */
    private void onRelease(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY) {
            return;
        }

        // Handle release of selected pixels if there are any.
        if (movingPixels != null) {
            // Implementation for handling release of selected pixels
            Rectangle2D source = selection;
            Rectangle2D destination = moveDestination(event.getX(), event.getY());
            
            WritableImage pixels = movingPixels;
            movingPixels = null;

            // Clear the original selection area before moving the pixels.
            if (!source.equals(destination)) {
                file.canvasEdit(() -> {
                    clearPixels(source);
                    canvas.getGraphicsContext2D().drawImage(pixels, destination.getMinX(), destination.getMinY());
                });
                selection = destination;
            }
            showPreview(selection, null);
        } else if (selecting) {
            selecting = false;
            selection = selectionBounds(event.getX(), event.getY());
            showPreview(selection, null);
        }
    }

    /**
     * Calculates the selection bounds based on the start and end points of the selection.
     *
     * @param endX the x-coordinate of the end point of the selection.
     * @param endY the y-coordinate of the end point of the selection.
     * @return a Rectangle2D representing the selection bounds, or null if the selection is invalid.
     */
    private Rectangle2D selectionBounds(double endX, double endY) {
        // Implementation for calculating selection bounds
        double left = clamp(Math.min(pressedX, endX), 0, canvas.getWidth());
        double top = clamp(Math.min(pressedY, endY), 0, canvas.getHeight());
        double right = clamp(Math.max(pressedX, endX), 0, canvas.getWidth());
        double bottom = clamp(Math.max(pressedY, endY), 0, canvas.getHeight());

        // Check if the calculated selection bounds are valid.
        if (right <= left || bottom <= top) {
            return null;
        }

        // Round the selection bounds to the nearest integer values.
        left = Math.floor(left);
        top = Math.floor(top);
        right = Math.floor(right);
        bottom = Math.floor(bottom);

        // Ensure the selection bounds are within the canvas dimensions.
        return new Rectangle2D(left, top, right - left, bottom - top);
    }

    /**
     * Calculates the destination rectangle for moving the selected pixels.
     *
     * @param mouseX the x-coordinate of the mouse pointer.
     * @param mouseY the y-coordinate of the mouse pointer.
     * @return a Rectangle2D representing the destination rectangle for the selection.
     */
    private Rectangle2D moveDestination(double mouseX, double mouseY) {
        // Implementation for calculating move destination
        double x = selection.getMinX() + Math.round(mouseX - pressedX);
        double y = selection.getMinY() + Math.round(mouseY - pressedY);

        // Ensure the destination rectangle stays within the canvas boundaries.
        x = clamp(x, 0, Math.ceil(canvas.getWidth() - selection.getWidth()));
        y = clamp(y, 0, Math.ceil(canvas.getHeight() - selection.getHeight()));

        // Return the calculated destination rectangle.
        return new Rectangle2D(x, y, selection.getWidth(), selection.getHeight());
    }

    /**
     * Clamps the given value between the specified minimum and maximum values.
     *
     * @param value the value to clamp.
     * @param min the minimum allowed value.
     * @param max the maximum allowed value.
     * @return the clamped value.
     */
    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Captures the pixels within the current selection.
     *
     * @return a WritableImage containing the captured pixels, or null if no selection exists.
     */
    private WritableImage captureSelection() {
        // Implementation for capturing selected pixels
        SnapshotParameters param = new SnapshotParameters();
        param.setFill(Color.TRANSPARENT);

        param.setViewport(selection);
        return canvas.snapshot(param, null);
    }

    /**
     * Copies the current selection to the clipboard.
     *
     * @return true if the selection was successfully copied, false otherwise.
     */
    public boolean copySelection() {
        if (canvas == null || selection == null || selecting || movingPixels != null) {
            return false;
        }

        copiedPixels = captureSelection();
        return true;
    }

    /**
     * Cuts the current selection, copying it to the clipboard and clearing the original pixels.
     */
    public void cutSelection() {
        if (!copySelection()) {
            return;
        }

        Rectangle2D source = selection;
        file.canvasEdit(() -> clearPixels(source));
        selection = null;
        clearPreview();
    }

    /**
     * Pastes the copied selection onto the canvas.
     */
    public void pasteSelection() {
        if (canvas == null || copiedPixels == null) {
            return;
        }

        WritableImage image = copiedPixels;

        selecting = false;
        movingPixels = null;
        pendingPaste = null;

        // Perform the paste operation on the canvas.
        file.canvasEdit(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();;

            gc.save();

            try {
                gc.setImageSmoothing(false);
                gc.drawImage(image, 0, 0);
            } finally {
                gc.restore();   
            }
        });

        // Update the selection bounds to reflect the pasted image.
        selection = new Rectangle2D(0, 0, Math.min(image.getWidth(), canvas.getWidth()), Math.min(image.getHeight(), canvas.getHeight()));

        showPreview(selection, null);
    }

    /**
     * Clears the pixels within the specified bounds on the canvas.
     *
     * @param bounds the rectangle defining the area to clear.
     */
    public void clearPixels(Rectangle2D bounds) {
        // Implementation for clearing pixels in the specified bounds
        canvas.getGraphicsContext2D().clearRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }

    /**
     * Shows a preview of the selection with the specified bounds and pixels.
     *
     * @param bounds the rectangle defining the area of the selection.
     * @param pixels the image containing the pixels to display in the preview.
     */
    public void showPreview(Rectangle2D bounds, Image pixels) {
        // Implementation for showing preview
        clearPreview();
        if(bounds == null) {
            return;
        }

        // Implementation for showing preview
        GraphicsContext gc = previewCanvas.getGraphicsContext2D();
        gc.save();

        try {
            if (pixels != null) {
                gc.drawImage(pixels, bounds.getMinX(), bounds.getMinY());
            }

            gc.setStroke(Color.DARKGRAY);
            gc.setLineWidth(1.0);
            gc.setLineDashes(5, 4);
            gc.strokeRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
        } finally {
            gc.restore();
        }
    }

    /**
     * Clears the preview of the selection.
     */
    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

    /**
     * Deactivates the selection tool, removing event handlers and clearing the preview.
     */
    public void deactivateTool() {
        if (canvas != null) {
            canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
            canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED, draggingHandler);
            canvas.removeEventHandler(MouseEvent.MOUSE_RELEASED, releaseHandler);
            clearPreview();
            canvas = null;
        }   

        selection = null;
        selecting = false;
        movingPixels = null;
        pendingPaste = null;
    }

    /**
     * Returns the current selection tool instance.
     *
     * @return the selection tool instance.
     */
    public Selection getTool() {
        return this;
    }
    
    /**
     * Sets the color of the selection tool.
     *
     * @param color the color to set for the selection tool.
     */
    public void setColor(Color color) {}

    /**
     * Returns the color of the selection tool.
     *
     * @return the color of the selection tool.
     */
    public Color getColor() {
        return Color.DARKGRAY;
    }

    /**
     * Sets the width of the selection tool.
     *
     * @param width the width to set for the selection tool.
     */
    public void setWidth(double width) {}

    /**
     * Returns the width of the selection tool.
     *
     * @return the width of the selection tool.
     */
    public double getWidth() {
        return 1.0;
    }

    /**
     * Sets whether the selection tool should use a dashed line.
     *
     * @param dashed true to use a dashed line, false otherwise.
     */
    public void setDashed(boolean dashed) {}

    /**
     * Returns whether the selection tool is using a dashed line.
     *
     * @return true if the selection tool is using a dashed line, false otherwise.
     */
    public boolean isDashed() {
        return true;
    }
}
