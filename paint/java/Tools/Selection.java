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

    public Selection(FileTab file) {
        this.file = file;
        this.previewCanvas = file.getPreviewCanvas();
    }

    public void activateTool(Canvas canvas) {
        if (this.canvas != null) {
            deactivateTool();
        }

        this.canvas = canvas;

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, draggingHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, releaseHandler);
    }

    private void onPress(MouseEvent event) {
        // Implementation for mouse press event
        if (event.getButton() != MouseButton.PRIMARY) {
            return;
        }

        pressedX = event.getX();
        pressedY = event.getY();

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

        if (selection != null && selection.contains(pressedX, pressedY)) {
            // Implementation for handling selection
            movingPixels = captureSelection();
        } else {
            selecting = true;
            selection = null;
            clearPreview();
        }
    }

    private void onDrag(MouseEvent event) {
        // Implementation for mouse drag event
        if (!event.isPrimaryButtonDown()) {
            return;
        }

        if (movingPixels != null) {
            // Implementation for handling drag of selected pixels
            Rectangle2D destination = moveDestination(event.getX(), event.getY());
            
            showPreview(destination, movingPixels);
        } else if (selecting) {
            selection = selectionBounds(event.getX(), event.getY());
            showPreview(selection, null);
        }
    }

    private void onRelease(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY) {
            return;
        }

        if (movingPixels != null) {
            // Implementation for handling release of selected pixels
            Rectangle2D source = selection;
            Rectangle2D destination = moveDestination(event.getX(), event.getY());
            
            WritableImage pixels = movingPixels;
            movingPixels = null;

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

    private Rectangle2D selectionBounds(double endX, double endY) {
        // Implementation for calculating selection bounds
        double left = clamp(Math.min(pressedX, endX), 0, canvas.getWidth());
        double top = clamp(Math.min(pressedY, endY), 0, canvas.getHeight());
        double right = clamp(Math.max(pressedX, endX), 0, canvas.getWidth());
        double bottom = clamp(Math.max(pressedY, endY), 0, canvas.getHeight());

        if (right <= left || bottom <= top) {
            return null;
        }

        left = Math.floor(left);
        top = Math.floor(top);
        right = Math.floor(right);
        bottom = Math.floor(bottom);

        return new Rectangle2D(left, top, right - left, bottom - top);
    }

    private Rectangle2D moveDestination(double mouseX, double mouseY) {
        // Implementation for calculating move destination
        double x = selection.getMinX() + Math.round(mouseX - pressedX);
        double y = selection.getMinY() + Math.round(mouseY - pressedY);

        x = clamp(x, 0, Math.ceil(canvas.getWidth() - selection.getWidth()));
        y = clamp(y, 0, Math.ceil(canvas.getHeight() - selection.getHeight()));

        return new Rectangle2D(x, y, selection.getWidth(), selection.getHeight());
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    private WritableImage captureSelection() {
        // Implementation for capturing selected pixels
        SnapshotParameters param = new SnapshotParameters();
        param.setFill(Color.TRANSPARENT);

        param.setViewport(selection);
        return canvas.snapshot(param, null);
    }

    public boolean copySelection() {
        if (canvas == null || selection == null || selecting || movingPixels != null) {
            return false;
        }

        copiedPixels = captureSelection();
        return true;
    }

    public void cutSelection() {
        if (!copySelection()) {
            return;
        }

        Rectangle2D source = selection;
        file.canvasEdit(() -> clearPixels(source));
        selection = null;
        clearPreview();
    }

    public void pasteSelection() {
        if (canvas == null || copiedPixels == null) {
            return;
        }

        WritableImage image = copiedPixels;

        selecting = false;
        movingPixels = null;
        pendingPaste = null;

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

        selection = new Rectangle2D(0, 0, Math.min(image.getWidth(), canvas.getWidth()), Math.min(image.getHeight(), canvas.getHeight()));

        showPreview(selection, null);
    }

    public void clearPixels(Rectangle2D bounds) {
        // Implementation for clearing pixels in the specified bounds
        canvas.getGraphicsContext2D().clearRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }

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

    private void clearPreview() {
        previewCanvas.getGraphicsContext2D().clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }

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

    public Selection getTool() {
        return this;
    }
    
    public void setColor(Color color) {}

    public Color getColor() {
        return Color.DARKGRAY;
    }

    public void setWidth(double width) {}

    public double getWidth() {
        return 1.0;
    }

    public void setDashed(boolean dashed) {}

    public boolean isDashed() {
        return true;
    }
}
