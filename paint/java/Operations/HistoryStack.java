package paint.Operations;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.function.Supplier;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.SnapshotParameters;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.image.WritableImage;

import javafx.scene.paint.Color;

/**
 * Manages the history of changes made to a canvas, allowing for undo and redo operations.
 */

// IMPROVEMENTS:
// - History is held in a stack of snapshots of the canvas.

// ISSUES:
// - Memory usage can increase with many snapshots, could further limit the number of stored snapshots.

public class HistoryStack {
    private static final int historyLimit = 300;

    private final Canvas canvas;
    private final Deque<WritableImage> undoStack = new ArrayDeque<>();
    private final Deque<WritableImage> redoStack = new ArrayDeque<>();

    /**
     * Constructs a HistoryStack for the given canvas.
     *
     * @param canvas the canvas to track changes for
     */
    public HistoryStack(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Records a change to the canvas by taking a snapshot and adding it to the undo stack.
     */
    public void recordChange() {
        undoStack.push(snapshot());

        if (undoStack.size() > historyLimit) {
            undoStack.removeLast();
        }

        redoStack.clear();
    }

    /**
     * Undoes the last change made to the canvas.
     *
     * @return true if an undo was performed, false if there was nothing to undo
     */
    public boolean undo() {
        return restoreFrom(undoStack, redoStack);
    }

    /**
     * Checks if there is a change that can be undone.
     *
     * @return true if an undo is possible, false otherwise
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Redoes the last undone change made to the canvas.
     *
     * @return true if a redo was performed, false if there was nothing to redo
     */
    public boolean redo() {
        return restoreFrom(redoStack, undoStack);
    }

    /**
     * Checks if there is a change that can be redone.
     *
     * @return true if a redo is possible, false otherwise
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Clears the history stack, removing all recorded changes.
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    /**
     * Restores the canvas to a previous state from the source stack, pushing the current state to the destination stack.
     *
     * @param source the stack to restore from (e.g., undoStack or redoStack)
     * @param destination the stack to push the current state to (e.g., redoStack or undoStack)
     * @return true if a state was restored, false if the source stack was empty
     */
    private boolean restoreFrom(Deque<WritableImage> source, Deque<WritableImage> destination) {
        if (source.isEmpty()) {
            return false;
        }
        
        destination.push(snapshot());
        WritableImage previous = source.pop();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previous, 0, 0);
        return true;
    }

    /**
     * Takes a snapshot of the current canvas state.
     *
     * @return a WritableImage representing the current canvas state
     */
    private WritableImage snapshot() {
        SnapshotParameters param = new SnapshotParameters();
        param.setFill(Color.TRANSPARENT);
        return canvas.snapshot(param, null);
    }

}