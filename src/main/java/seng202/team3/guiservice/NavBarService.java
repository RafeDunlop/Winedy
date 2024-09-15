package seng202.team3.guiservice;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.gui.MainWindow;

/**
 * A final service class for the Nav Bar.
 * Used by NavBarController.
 * Contains a collection of static methods.
 * @author Hannah Botting (hbo51)
 */
public class NavBarService {

    private static final Logger log = LogManager.getLogger(NavBarService.class);

    /**
     * Adds a graphic to the given Button of the image located at the given path.
     * Used by setUpButton.
     * @param button The Button the image graphic is being added to
     * @param imagePath The path the image is located at
     * @param isInvisible A boolean with the truth value of the image being initially invisible
     * @return The image view object created
     */
    private static ImageView addImageGraphicToButton(Button button, String imagePath, boolean isInvisible) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);

        imageView.setPreserveRatio(true);
        if (isInvisible) {
            imageView.setOpacity(0);
            imageView.setFitHeight(1);
        } else {
            imageView.setFitHeight(50);
        }

        button.setGraphic(imageView);

        return imageView;
    }

    /**
     * Sets up the given button to have the given CSS style and image.
     * Used by HomeScreenController's initialize method to set up the home screen buttons on launch.
     * @param button The button to be set up
     * @param imagePath The path the image is located at
     * @param styleName The name of the CSS style to be added to the button
     * @param isInvisible A boolean with the truth value of the button being initially invisible
     * @return The ImageView object added to the button
     */
    public static ImageView setUpButton(Button button, String imagePath, String styleName, boolean isInvisible) {
        button.getStyleClass().add(styleName);
        button.setText("");

        return addImageGraphicToButton(button, imagePath, isInvisible);
    }
}
