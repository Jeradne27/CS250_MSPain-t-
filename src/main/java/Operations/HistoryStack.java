package Operations;

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

public class HistoryStack {
    private static final int historyLimit = 300;

    private final Canvas canvas;
    private final Deque<WritableImage> undoStack = new ArrayDeque<>();
    private final Deque<WritableImage> redoStack = new ArrayDeque<>();

    public HistoryStack(Canvas canvas) {
        this.canvas = canvas;
    }

    public void recordChange() {
        undoStack.push(snapshot());

        if (undoStack.size() > historyLimit) {
            undoStack.removeLast();
        }

        redoStack.clear();
    }

    public boolean undo() {
        return restoreFrom(undoStack, redoStack);
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean redo() {
        return restoreFrom(redoStack, undoStack);
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

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

    private WritableImage snapshot() {
        SnapshotParameters param = new SnapshotParameters();
        param.setFill(Color.TRANSPARENT);
        return canvas.snapshot(param, null);
    }

}