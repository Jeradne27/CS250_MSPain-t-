package paint.Operations;

import java.util.UUID;

import java.io.File;
import java.time.LocalDateTime;

import javafx.scene.canvas.Canvas;

import javafx.scene.control.Tab;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.scene.image.ImageView;

import javafx.scene.layout.StackPane;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import paint.Menus.ToolMenu;
import paint.Menus.StyleMenu;

/**
 * Represents a single file tab in the application, including its image, canvas, and associated menus.
 */

// IMPROVEMENTS:
// - The tab pane could have a visual indicator for the active tab, such as a highlighted border or background color.
// - The workspace area could have a background color or border to visually separate it from the tab headers.
// - The drawing area could have a border or background color to visually separate it from the workspace.
// - The tab title could be made editable to allow renaming without changing the file name.
// - The tabs sit below the tab headers, which could be confusing for users.

// ISSUES:
// - The only way to rename a tab is to change the file name completely
// - The tab pane could have a visual indicator for the active tab, such as a highlighted border or background color.
// - The workspace area could have a background color or border to visually separate it from the tab headers.

public class FileTab {
   // each fileTab has its own image and canvas
   private final ImageView imageView = new ImageView();
   private final Canvas canvas = new Canvas(500, 500);
   private final Canvas previewCanvas = new Canvas();

   private final HistoryStack historyStack = new HistoryStack(canvas);

   private final StackPane drawingArea = new StackPane(imageView, canvas, previewCanvas);

   // The tab representing this file in the tab pane.
   private final Tab tab;
   private final String title;

   // The file associated with this tab.
   // The modified flag indicates whether the file has unsaved changes.
   private File file;
   private boolean modified;

   // keep file's tools and settings together
   // The toolMenu and styleMenu are associated with this specific file tab.
   private ToolMenu toolMenu;
   private StyleMenu styleMenu;

   private UUID recoveryID = UUID.randomUUID();

   public UUID getRecoveryID() {
      return recoveryID;
   }

   public void resetRecoveryID() {
      recoveryID = UUID.randomUUID();
   }

   /**
    * Constructs a new FileTab with the given title.
    *
    * @param title the title of the tab
    */
   public FileTab(String title) {
      this.title = title;

      drawingArea.setMinSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
      drawingArea.setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);

      drawingArea.getStyleClass().add("drawingarea");

      previewCanvas.widthProperty().bind(canvas.widthProperty());
      previewCanvas.heightProperty().bind(canvas.heightProperty());

      previewCanvas.setMouseTransparent(true);

      StackPane workSpace = new StackPane(drawingArea);
      workSpace.setMinSize(0, 0);
      workSpace.getStyleClass().add("workspace");

      tab = new Tab(title, workSpace);
      tab.setClosable(true);
      tab.setUserData(this);

      canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
         if (event.getButton() == MouseButton.PRIMARY && (canvas.getOnMousePressed() != null || canvas.getOnMouseDragged() != null || canvas.getOnMouseReleased() != null)) {
            historyStack.recordChange();
         }
      });

   }

   /**
    * Returns the ImageView associated with this file tab.
    *
    * @return the ImageView
    */
   public ImageView getImageView() {
      return imageView;
   }

   /**
    * Returns the Canvas associated with this file tab.
    *
    * @return the Canvas
    */
   public Canvas getCanvas() {
      return canvas;
   }

   /**
    * Returns the preview Canvas associated with this file tab.
    *
    * @return the preview Canvas
    */
   public Canvas getPreviewCanvas() {
      return previewCanvas;
   }

   /**
    * Returns the drawing area associated with this file tab.
    *
    * @return the drawing area
    */
   public StackPane getDrawingArea() {
      return drawingArea;
   }

   /**
    * Returns the ToolMenu associated with this file tab.
    *
    * @return the ToolMenu
    */
   public ToolMenu getToolMenu() {
      return toolMenu;
   }

   /**
    * Sets the ToolMenu associated with this file tab.
    *
    * @param toolMenu the ToolMenu to set
    */
   public void setToolMenu(ToolMenu toolMenu) {
      this.toolMenu = toolMenu;
   }

   /**
    * Returns the StyleMenu associated with this file tab.
    *
    * @return the StyleMenu
    */
   public StyleMenu getStyleMenu() {
      return styleMenu;
   }

   /**
    * Sets the StyleMenu associated with this file tab.
    *
    * @param styleMenu the StyleMenu to set
    */
   public void setStyleMenu(StyleMenu styleMenu) {
      this.styleMenu = styleMenu;
   }

   /**
    * Returns the Tab associated with this file tab.
    *
    * @return the Tab
    */
   public Tab getTab() {
      return tab;
   }

   /**
    * Returns the File associated with this file tab.
    *
    * @return the File
    */
   public File getFile() {
      return file;
   }

   /**
    * Sets the file associated with this file tab.
    *
    * @param file the file to set
    */
   public void setFile(File file) {
      this.file = file;
      setModified(modified);
   }

   /**
    * Returns whether the file associated with this file tab has unsaved changes.
    *
    * @return true if the file has unsaved changes, false otherwise
    */
   public boolean isModified() {
      return modified;
   }

   /**
    * Sets the modified flag for this tab.
    *
    * @param modified true if the file has unsaved changes, false otherwise
    */
   public void setModified(boolean modified) {
      this.modified = modified;

      String title;

      // Determine the title to display based on whether a file is associated with this tab.
      if(file == null) {
         title = this.title;
      } else {
         title = file.getName();
      }

      // * is for unsaved changes
      // Update the tab title to reflect the modified state.
      if(modified) {
         tab.setText(title + "*");
      } else {
         tab.setText(title);
      }
   }

   /**
    * Clears the modified flag for this tab, indicating that the file has no unsaved changes.
    * This is typically called after the file has been saved.
    */
   public void clearModified() {
      setModified(false);
   }

   /**
    * Undoes the last change made to the canvas.
    */
   public void undo() {
      if (historyStack.undo()) {
         setModified(true);
      }
   }

   /**
    * Redoes the last undone change made to the canvas.
    */
   public void redo() {
      if (historyStack.redo()) {
         setModified(true);
      }
   }

   /**
    * Clears the canvas, removing all drawings.
    * Prompts the user for confirmation before clearing the canvas.
    */
   public void clearCanvas() {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Clear Canvas");
      alert.setHeaderText("Are you sure you want to clear the canvas?");
      alert.setContentText("This action cannot be undone.");

      if (alert.showAndWait().get() != ButtonType.OK) {
         return;
      }

      historyStack.recordChange();

      canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

      setModified(true);
   }

   /**
    * Clears the history stack, removing all recorded changes.
    */
   public void clearHistory() {
      historyStack.clear();
    }

   /**
    * Executes a canvas edit operation, recording the change in the history stack and updating the modified flag.
    *
    * @param edit the edit operation to perform
    */
   public void canvasEdit(String action, Runnable edit) {
      historyStack.recordChange();
      edit.run();
      setModified(true);  
      logCanvasAction(action);  
   }

   public void logCanvasAction(String event) {
        LocalDateTime now = LocalDateTime.now();
        String fileName = getFile() == null ? title : getFile().getName();

        System.out.printf("%tF %<tT ~ [%s]: %s ~ %s%n", now, "Canvas", event, Thread.currentThread().getName());
    }
}
