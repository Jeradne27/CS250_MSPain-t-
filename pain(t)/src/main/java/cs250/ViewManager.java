package cs250;

import javafx.scene.image.ImageView;

public class ViewManager {
    private double zoomLvl = 1.0;

    private ImageView imageView;

    @SuppressWarnings("exports")
    public ViewManager(ImageView imageView) {
        this.imageView = imageView;
    }

    public void zoomIn() {
        zoomLvl += 0.1;

        imageView.setScaleX(zoomLvl);
        imageView.setScaleY(zoomLvl);
    }

    public void zoomOut() {
        if (zoomLvl > 0.2) {
            zoomLvl -= 0.1;

            imageView.setScaleX(zoomLvl);
            imageView.setScaleY(zoomLvl);
        }
    }

    public void resetZoom() {
        zoomLvl = 1.0;

        imageView.setScaleX(zoomLvl);
        imageView.setScaleY(zoomLvl);
    }
}