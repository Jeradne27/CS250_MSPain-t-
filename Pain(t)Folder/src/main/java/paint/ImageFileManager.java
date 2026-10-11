package paint;

import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Manages file operations for image handling in the application.
 */
public class ImageFileManager {
    
    // imagefilemanager is responsible for opening images, saving images, and
    // and saving images under a new name/format. it also holds the close method,
    // which stops the program from closing if a file hasnt been saved yet/
    private File currentFile;

    // default format used when saving the current image,
    // is overridden later if another format is determined when
    // openning a file or using Save As
    private String currentFormat = "png";

    // tracks whether the current file has been saved, close() uses this value
    // to determine whether it is safe to close the program or not.
    private boolean isSaved = true; 

    private Stage primaryStage; 
    // displays current image
    private ImageView imageView;
    // canvas containing drawingarea over the opened image
    private Canvas canvas;
    // contains bother the image and canvas
    // taking a snapshot of the stackpane combines both layers
    private StackPane drawingArea;

    /**
     * Initializes the ImageFileManager with the specified components.
     * @param primaryStage The primary stage for the application.
     * @param imageView The ImageView for displaying images.
     * @param canvas The Canvas for drawing operations.
     * @param drawingArea The StackPane containing the image and canvas.
     */
    @SuppressWarnings("exports")
    // receives references to components needed to perform file operations
    // from App.java
    public ImageFileManager(Stage primaryStage, ImageView imageView, Canvas canvas, StackPane drawingArea) {
        this.primaryStage = primaryStage;
        this.imageView = imageView;
        this.canvas = canvas;
        this.drawingArea = drawingArea;
    }
    /**
     * Opens an image file.
     */
    public void openImage() {
        
        // opens an existing png, jpg/jpeg, or bmp image
        // the selected file becomes the current file and its format is saved for
        // saves, the canvas is also resized to match the size of the image
        // IMPROVEMENT: allow for canvas to be resized to make the drawing area
        //              bigger than the image

        // creates file selection window
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        // adds restrictions to png, jpg/jpeg, and bmp formats
        FileChooser.ExtensionFilter fileTypes = new FileChooser.ExtensionFilter(
            "Image Files (*.jpg, *.jpeg, *.bmp, *.png)",
                "*.jpg", "*.jpeg", "*.bmp", "*.png"
        );

        fileChooser.getExtensionFilters().add(fileTypes);

        // display file chooser
        File file = fileChooser.showOpenDialog(primaryStage);
        
        // continue only if the user selected a file
        // currentformat is used later by saveImage() to keep the original image
        // image format
        if (file != null) {
            currentFile = file;

            String fileName = file.getName().toLowerCase();

            if (fileName.endsWith(".png")) {
                currentFormat = "png";
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                currentFormat = "jpg";
            } else if (fileName.endsWith(".bmp")) {
                currentFormat = "bmp";
            }

            // load the image into the imageview
            Image image = new Image(file.toURI().toString());

            imageView.setImage(image);
            imageView.setPreserveRatio(true);

            // match the canvas dimensions to the opened image so the mouse coordinates
            // and drawn lines line up correctly
            canvas.setWidth(image.getWidth());
            canvas.setHeight(image.getHeight());

            // mark the current file as unsaved, allows close() to send a warning before
            // the user exits the app
            isSaved = false;
        }
    }

    /**
     * Saves the current image to the current file.
     */
    public void saveImage() { 

        // save the current image to currentfile
        // if not current file exists yet, save acts like Save As and asks the user
        // to select a name and format type before saving
        if (currentFile == null) {
            saveImageAs();
            return;
        } 
        
        try {

            // capture the entire drawingarea instead of just the imageview or just the canvas
            // since the drawingarea holds both the canvas and imageview in a stackpane, taking
            // a snapshot of the drawing area creates a combination of the two layers
            WritableImage combinedImage = drawingArea.snapshot(null, null);

            // because we use ImageIO to write the final image onto the disk, we have to convert
            // the JavaFX writableimage into a bufferedimage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(combinedImage, null);

            // write the new image using the combined bufferedImage and the current format
            ImageIO.write(
                bufferedImage, 
                currentFormat,
                currentFile
            );

            // this version has been saved as it was written to the disk
            isSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Saves the current image with a new file name and format.
     */
    public void saveImageAs() {

        // SaveImageAs allows the user to save the current image a new file name and format
        // (png, jpeg, bmp) 
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");

        // creates the image format choices that are displayed in the save window
        FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG Image (*.png)", "*.png"); 
        FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPEG Image (*.jpg)", "*.jpg"); 
        FileChooser.ExtensionFilter bmp = new FileChooser.ExtensionFilter("BMP Image (*.bmp)", "*.bmp");

        fileChooser.getExtensionFilters().addAll(png, jpg, bmp);

        // askes the user for the destination of the file
        File file = fileChooser.showSaveDialog(primaryStage);

        // continue only if the user selected a destination
        if (file != null) {

            // determine the output format based on the format selected in the save
            // window
            FileChooser.ExtensionFilter pickedFormat = fileChooser.getSelectedExtensionFilter();

            if (pickedFormat == png) {
                currentFormat = "png";
            } else if (pickedFormat == jpg) {
                currentFormat = "jpg";
            } else if (pickedFormat == bmp) {
                currentFormat = "bmp";
            }

            // construct the file extension that would be expected for the file that is being 
            // saved (untitled.***)
            String extension = "." + currentFormat;

            // automatically add the extension if the user didn't do so
            if (!file.getName().toLowerCase().endsWith(extension)) {
                file = new File(file.getAbsolutePath() + extension);
            }

            // make this the new current file and use the saveimage method to actually save the snapshot
            currentFile = file;
            saveImage();
        }
    }

    /**
     * Closes the application.
     */
    public void close() {
 
        // close safely closes the main application window
        // if the current file is marked as unsaved, the user is warned that they haven't saved
        // when they initially click the closeitem option. it only closes if the user confirms they want
        // to close without saving
        if (!isSaved) {

            // warn the user before discarding unsaved materials
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes.");
            alert.setContentText("Are you sure you want to close?");

            // wait for the user's response
            // if no selection is made, the default is cancel
            ButtonType pick = alert.showAndWait().orElse(ButtonType.CANCEL);

            // close upon confirmation
            if (pick == ButtonType.OK) {
                primaryStage.close();
            } 
        } else {
            // if the work is saved, no need to warn the user, so close is 
            // safe
            primaryStage.close();
        }
    }
}