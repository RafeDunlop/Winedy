package seng202.team3.guiservice;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A final service class for the Home Screen.
 * Used by HomeScreenController.
 * Contains a collection of static methods.
 * @author Hannah Botting (hbo51)
 */
public final class HomeScreenService {

    /**
     * Adds a graphic to the given Button of the image located at the given path.
     * Used by setUpButton.
     * @param button The Button the image graphic is being added to
     * @param imagePath The path the image is located at
     */
    private static void addImageGraphicToButton(Button button, String imagePath) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);
    }

    /**
     * Sets up the given button to have the given CSS style and image.
     * Used by HomeScreenController's initialize method to set up the home screen buttons on launch.
     * @param button The button to be set up
     * @param imagePath The path the image is located at
     * @param styleName The name of the CSS style to be added to the button
     */
    public static void setUpButton(Button button, String imagePath, String styleName) {
        button.getStyleClass().add(styleName);
        addImageGraphicToButton(button, imagePath);
    }
}
