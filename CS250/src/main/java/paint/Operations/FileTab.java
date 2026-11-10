package paint.Operations;

import java.io.File;

import javafx.scene.canvas.Canvas;

import javafx.scene.control.Tab;

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
// - The tabs don't have a border or visual separation between them. Considering adding a border or background color to distinguish them.
// - The workspace area could have a background color or border to visually separate it from the tab headers.
// - The drawing area could have a border or background color to visually separate it from the workspace.
// - The tab title could be made editable to allow renaming without changing the file name.
// - The tabs sit below the tab headers, which could be confusing for users.

// ISSUES:
// - The only way to rename a tab is to change the file name completely

public class FileTab {
   // each fileTab has its own image and canvas
   private final ImageView imageView = new ImageView();
   private final Canvas canvas = new Canvas(500, 500);

   private final HistoryStack historyStack = new HistoryStack(canvas);

   private final StackPane drawingArea = new StackPane(imageView, canvas);

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

   public ImageView getImageView() {
      return imageView;
   }

   public Canvas getCanvas() {
      return canvas;
   }

   public StackPane getDrawingArea() {
      return drawingArea;
   }

   public ToolMenu getToolMenu() {
      return toolMenu;
   }

   public void setToolMenu(ToolMenu toolMenu) {
      this.toolMenu = toolMenu;
   }

   public StyleMenu getStyleMenu() {
      return styleMenu;
   }

   public void setStyleMenu(StyleMenu styleMenu) {
      this.styleMenu = styleMenu;
   }

   public Tab getTab() {
      return tab;
   }

   public File getFile() {
      return file;
   }

   /**
    * Sets the file associated with this tab.
    *
    * @param file the file to set
    */
   public void setFile(File file) {
      this.file = file;
      setModified(modified);
   }

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

   public void undo() {
      if (historyStack.undo()) {
         setModified(true);
      }
   }

   public void redo() {
      if (historyStack.redo()) {
         setModified(true);
      }
   }

   public void clearCanvas() {
      historyStack.recordChange();

      canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

      setModified(true);
   }

   public void applyEdit(Runnable edit) {
      historyStack.recordChange();
      edit.run();
      setModified(true);
   }
}