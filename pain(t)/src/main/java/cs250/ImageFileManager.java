package cs250;

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

// handles file types
// holds file management methods (ie. Save, Save As, Close, etc.)
public class ImageFileManager {
    
    private File currentFile;
    private String currentFormat = "png"; // default image 

    private boolean isSaved = true; 

    private Stage primaryStage; 
    private ImageView imageView;
    private Canvas canvas;
    private StackPane drawingArea;

    @SuppressWarnings("exports")
    public ImageFileManager(Stage primaryStage, ImageView imageView, Canvas canvas, StackPane drawingArea) {
        this.primaryStage = primaryStage;
        this.imageView = imageView;
        this.canvas = canvas;
        this.drawingArea = drawingArea;
    }
    public void openImage() { // opens .jpg/.jpeg, .bmp, .png
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        FileChooser.ExtensionFilter fileTypes = new FileChooser.ExtensionFilter(
            "Image Files (*.jpg, *.bmp, *.png)",
                "*.jpg", "*.bmp", "*.png"
        );

        fileChooser.getExtensionFilters().add(fileTypes);

        File file = fileChooser.showOpenDialog(primaryStage);
        
        if (file != null) { // depending on the ending of the name of the file, change currentFormat to match format
            currentFile = file;

            String fileName = file.getName().toLowerCase();

            if (fileName.endsWith(".png")) {
                currentFormat = "png";
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                currentFormat = "jpg";
            } else if (fileName.endsWith(".bmp")) {
                currentFormat = "bmp";
            }

            Image image = new Image(file.toURI().toString());

            imageView.setImage(image);

            canvas.setWidth(image.getWidth());
            canvas.setHeight(image.getHeight());
            imageView.setPreserveRatio(true);

            isSaved = false;
        }
    }

    public void saveImage() { 
        if (currentFile == null) {
            saveImageAs();
            return;
        } 
        
        try {
            WritableImage combinedImage = drawingArea.snapshot(null, null);

            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(combinedImage, null);

            ImageIO.write(
                bufferedImage, 
                currentFormat, // save as original format 
                currentFile
            );
            isSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveImageAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");

        // add selection options for .png, .jpg, and .bmp formats
        FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG Image (*.png)", "*.png"); 
        FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPEG Image (*.jpg)", "*.jpg"); 
        FileChooser.ExtensionFilter bmp = new FileChooser.ExtensionFilter("BMP Image (*.bmp)", "*.bmp");

        fileChooser.getExtensionFilters().addAll(png, jpg, bmp);
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            FileChooser.ExtensionFilter pickedFormat = fileChooser.getSelectedExtensionFilter();

            if (pickedFormat == png) {
                currentFormat = "png";
            } else if (pickedFormat == jpg) {
                currentFormat = "jpg";
            } else if (pickedFormat == bmp) {
                currentFormat = "bmp";
            }

            String extension = "." + currentFormat;

            if (!file.getName().toLowerCase().endsWith(extension)) { // names the file with format extension 
                file = new File(file.getAbsolutePath() + extension);
            }
            currentFile = file;
            saveImage();
        }
    }

    public void close() {
 
        if (!isSaved) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes.");
            alert.setContentText("Are you sure you want to close?");

            ButtonType pick = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (pick == ButtonType.OK) {
                primaryStage.close();
            } 
        } else {
            primaryStage.close();
        }
    }
}