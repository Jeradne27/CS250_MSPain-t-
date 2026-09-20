package cs250;

import java.io.File;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageFileManager {
    
    private File currentFile;
    private String currentFormat = "png";

    private boolean isSaved = true;

    private Stage primaryStage;
    private ImageView imageView;

    @SuppressWarnings("exports")
    public ImageFileManager(Stage primaryStage, ImageView imageView) {
        this.primaryStage = primaryStage;
        this.imageView = imageView;
    }
    // opening methods
    public void openImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        FileChooser.ExtensionFilter fileTypes = new FileChooser.ExtensionFilter(
            "Image Files (*.jpg, *.bmp, *.png)",
                "*.jpg", "*.bmp", "*.png"
        );

        fileChooser.getExtensionFilters().add(fileTypes);

        File file = fileChooser.showOpenDialog(primaryStage);
        
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

            Image image = new Image(file.toURI().toString());

            imageView.setImage(image);
            imageView.setFitWidth(400);
            imageView.setPreserveRatio(true);
            
            isSaved = false;
        }
    }

    // saving methods
    public void saveImage() {
        if (currentFile == null) {
            saveImageAs();
            return;
        } 
        
        Image image = imageView.getImage();

        try {
            ImageIO.write(
                SwingFXUtils.fromFXImage(image, null), 
                currentFormat, 
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

            if (!file.getName().toLowerCase().endsWith(extension)) {
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

    // getters and setters for saved status and current file
}