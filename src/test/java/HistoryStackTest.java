package paint.Operations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javafx.scene.canvas.Canvas;

/**
 * Tests for the HistoryStack class.
 */
class HistoryStackTest {
    @Test
    void testHistoryStack() {
        Canvas canvas = new Canvas(100, 100);
        HistoryStack historyStack = new HistoryStack(canvas);
        boolean canUndo = historyStack.canUndo();
        assertFalse(canUndo);
    }
}
