package paint.Operations;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.util.function.Function;
import java.util.function.Supplier;

import javafx.embed.swing.SwingFXUtils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javafx.scene.canvas.Canvas;

import javafx.scene.paint.Color;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Manages file operations for image handling in the application.
 */

// IMPROVEMENTS:
// - There is a lot of code duplication in the image handling methods. Could refactor to create common utility methods for loading, saving, and displaying images.
// - The error handling for image loading could be improved to provide user-friendly messages instead of just printing stack traces.
// - the way images are displayed and scaled could be improved for better user experience.

// ISSUES:
// - The way that loading errors are handled is simplistic and could be improved for user experience.
// - images are loaded in as whatever size they are, without resizing, could be improved by scaling them to fit the available space.

public class ImageFiles {

    private final Stage primaryStage; 
    private final TabPane tabPane;

    // The supplier for the active FileTab and the function to add a new image tab.
    private final Supplier<FileTab> active;
    private final Function<String, FileTab> addImage;

    /**
     * Constructs an ImageFiles instance with the specified components.
     * @param primaryStage The primary stage for the application.
     * @param tabPane The TabPane for managing image tabs.
     * @param active The supplier for the active FileTab.
     * @param addImage The function to add a new image tab.
     */
    public ImageFiles(Stage primaryStage, TabPane tabPane, Supplier<FileTab> active, Function<String, FileTab> addImage) {
        this.primaryStage = primaryStage;
        this.tabPane = tabPane;
        this.active = active;
        this.addImage = addImage;
    }

    // Creates a new image tab with a default name.
    public void newImage() {
        addImage.apply("Untitled");
    }

    // Opens an image in the current tab.
    public void openImage() {
        openImage(false);
    }

    // Opens an image in a new tab.
    public void openImageInNewTab() {
        openImage(true);
    }

    /**
     * Opens an image file in the current or a new tab.
     * @param newTab Whether to open the image in a new tab.
     */
    private void openImage(boolean newTab) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        FileChooser.ExtensionFilter fileTypes = new FileChooser.ExtensionFilter(
            "Image Files (*.jpg, *.jpeg, *.bmp, *.png)",
                "*.jpg", "*.jpeg", "*.bmp", "*.png"
        );

        fileChooser.getExtensionFilters().add(fileTypes);

        File file = fileChooser.showOpenDialog(primaryStage);

        // If no file is selected, return early.
        if (file == null) {
            return;
        } 
        
        Image image = new Image(file.toURI().toString());

        // Check if the image was loaded successfully.
        if (image.isError()) {
            image.getException().printStackTrace();
            return;
        }

        FileTab fileTab = active.get();

        // Determine whether to open the image in a new tab or the current tab.
        if (newTab || fileTab == null) {
            fileTab = addImage.apply(file.getName());
        } else if (!confirmClose(fileTab)) {
            return;
        }

        // Update the image view with the loaded image.
        fileTab.getImageView().setImage(image);
        fileTab.getImageView().setPreserveRatio(true);

        Canvas canvas = fileTab.getCanvas();

        canvas.setWidth(image.getWidth());
        canvas.setHeight(image.getHeight());

        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        fileTab.getDrawingArea().setScaleX(1);
        fileTab.getDrawingArea().setScaleY(1);

        fileTab.setFile(file);

        // Clear any previous modifications to the image tab.
        fileTab.clearModified();
    }

    public void saveImage() { 
        FileTab fileTab = active.get();
        // If there is an active image tab, save it.
        if (fileTab != null) {
            saveImage(fileTab);
        } 
    }

    /**
     * Saves the image in the given file tab to its current file.
     * @param fileTab The file tab containing the image to save.
     * @return True if the image was saved successfully, false otherwise.
     */
    private boolean saveImage(FileTab fileTab) {
        // If the file tab has no associated file, prompt to save as.
        if (fileTab.getFile() == null) {
            return saveImageAs(fileTab);
        }

        // Otherwise, save the image to the current file.
        return saveImage(fileTab, fileTab.getFile());
    }

    /**
     * Saves the image in the given file tab to the specified file.
     * @param fileTab The file tab containing the image to save.
     * @param file The file to save the image to.
     * @return True if the image was saved successfully, false otherwise.
     */
    public boolean saveImage(FileTab fileTab, File file) {
        double scaleX = fileTab.getDrawingArea().getScaleX();
        double scaleY = fileTab.getDrawingArea().getScaleY();

        // Temporarily reset the drawing area scale to capture the image at its actual size.
        try {
            fileTab.getDrawingArea().setScaleX(1);
            fileTab.getDrawingArea().setScaleY(1);

            // Capture the image from the drawing area.
            WritableImage comboImage = fileTab.getDrawingArea().snapshot(null, null);
            BufferedImage bufImage = SwingFXUtils.fromFXImage(comboImage, null);
            String currentFormat = getFormat(file);

            // If the image is not in PNG format, convert it to RGB to avoid transparency issues.
            if (!currentFormat.equals("png")) {
                BufferedImage opImage = new BufferedImage(
                    bufImage.getWidth(),
                    bufImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB
                 );

                 java.awt.Graphics2D graphics = opImage.createGraphics();

                 // Draw a white background to handle transparency.
                 graphics.setColor(java.awt.Color.WHITE);
                 graphics.fillRect(0, 0, opImage.getWidth(), opImage.getHeight());
                 graphics.drawImage(bufImage, 0, 0, null);

                 bufImage = opImage;
            }

            boolean saved = ImageIO.write(bufImage, currentFormat, file);
            // Check if the image was saved successfully.
            if (saved) {
                // Update the file tab with the saved file and clear any modifications.
                fileTab.setFile(file);
                fileTab.clearModified();
            }

            return saved;
        } catch (Exception e) {
            // Print the exception stack trace for debugging purposes.
            e.printStackTrace();
            return false;
        } finally {
            // Restore the original drawing area scale.
            fileTab.getDrawingArea().setScaleX(scaleX);
            fileTab.getDrawingArea().setScaleY(scaleY); 
        }
    }

    public void saveImageAs() {
        FileTab fileTab = active.get();
        // If there is an active file tab, prompt to save the image as.
        if (fileTab != null) {
            saveImageAs(fileTab);
        }
    }

    /**
     * Prompts the user to save the image in the given file tab as a new file.
     * @param fileTab The file tab containing the image to save.
     * @return True if the image was saved successfully, false otherwise.
     */
    private boolean saveImageAs(FileTab fileTab) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");

        FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG Image (*.png)", "*.png"); 
        FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPEG Image (*.jpg)", "*.jpg"); 
        FileChooser.ExtensionFilter bmp = new FileChooser.ExtensionFilter("BMP Image (*.bmp)", "*.bmp");

        fileChooser.getExtensionFilters().addAll(png, jpg, bmp);

        String currentFormat = "png";

        // Determine the current format of the file if it exists.
        if (fileTab.getFile() != null) {
            currentFormat = getFormat(fileTab.getFile());
        }

        // Set the selected extension filter based on the current format.
        if (currentFormat.endsWith(".png")) {
            fileChooser.setSelectedExtensionFilter(png);
        } else if (currentFormat.endsWith(".jpg") || currentFormat.endsWith(".jpeg")) {
            fileChooser.setSelectedExtensionFilter(jpg);
        } else if (currentFormat.endsWith(".bmp")) {
            fileChooser.setSelectedExtensionFilter(bmp);
        }

        File file = fileChooser.showSaveDialog(primaryStage);

        if (file == null) {
            return false;
        }

        // Get the selected extension filter from the file chooser.
        FileChooser.ExtensionFilter pickedFormat = fileChooser.getSelectedExtensionFilter();

        if (pickedFormat == png) {
            currentFormat = "png";
        } else if (pickedFormat == jpg) {
            currentFormat = "jpg";
        } else if (pickedFormat == bmp) {
            currentFormat = "bmp";
        }

        String ext = "." + currentFormat;
        String fileName = file.getName().toLowerCase();

        // Check if the file name has the correct extension based on the selected format.
        boolean correctExt = fileName.endsWith(ext);

        // Special case for JPEG files, as they can have either .jpg or .jpeg extension.
        if (currentFormat.equals("jpg") && fileName.endsWith(".jpeg")) {
            correctExt = true;
        }

        // If the file name does not have the correct extension, append the correct extension.
        if (!correctExt) {
            file = new File(file.getAbsolutePath() + ext);
            // Check if the file with the appended extension already exists.
            if (file.exists()) {
                // Prompt the user to confirm replacing the existing file.
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.initOwner(primaryStage);
                alert.setTitle("Replace File");
                alert.setHeaderText("File already exists");
                alert.setContentText("Do you want to replace the existing file?");

                ButtonType pick = alert.showAndWait().orElse(ButtonType.CANCEL);

                if (pick != ButtonType.OK) {
                    // User chose not to replace the file, abort the save operation.
                    return false;
                }
            }   
            
        }
        // Save the image to the file.
        return saveImage(fileTab, file);
    }
    
    /**
     * Determines the format of the given file based on its extension.
     * @param file The file to check.
     * @return The format of the file as a string (e.g., "jpg", "bmp", "png").
     */
    private String getFormat(File file) {
        String fileName = file.getName().toLowerCase();
        
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "jpg";
        } else if (fileName.endsWith(".bmp")) {
            return "bmp";
        }
        // Default to "png" if the file does not have a recognized extension.
        return "png";
    }

    /**
     * Confirms whether the user wants to close the given file tab, prompting to save if there are unsaved changes.
     * @param fileTab The file tab to confirm closing.
     * @return True if the file tab can be closed, false otherwise.
     */
    public boolean confirmClose(FileTab fileTab) {
        if (!fileTab.isModified()) {
            return true;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // Prompt the user to save changes before closing the file tab.
        alert.initOwner(primaryStage);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText(
            fileTab.getTab().getText() + " has unsaved changes."
        );
        alert.setContentText("Do you want to save before closing?");

        ButtonType save = new ButtonType("Save");
        ButtonType dontSave = new ButtonType("Don't Save");

        alert.getButtonTypes().setAll(save, dontSave, ButtonType.CANCEL);

        ButtonType pick = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (pick == save) {
            // User chose to save changes before closing.
            return saveImage(fileTab);
        }

        // User chose either "Don't Save" or "Cancel".
        return pick == dontSave;
    }

    
    public void close() {
        // Prompt the user to save changes for all open file tabs before closing the application.
        for (Tab tab : tabPane.getTabs()) {
            FileTab fileTab = (FileTab) tab.getUserData();

            if (!confirmClose(fileTab)) {
                // User chose not to save or cancel, abort the close operation.
                return;
            }
        }
        // All file tabs have been confirmed for closing, proceed to close the application.
        primaryStage.close();
    }
}