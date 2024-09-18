package seng202.team3.gui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.gui.FXWrapper;
import seng202.team3.gui.HelpScreenController;
import seng202.team3.models.Wine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

import static javafx.scene.control.ContentDisplay.TOP;

/**
 * A final service class for the GUI.
 * Used by the FXML Controller classes.
 * Contains a collection of static methods.
 * @author Hannah Botting (hbo51)
 */
public final class GuiService {

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(GuiService.class);

    /**
     * Returns the content of the file at the given path as a String
     * @param filePath
     * @return A String of the file content at the given path
     */
    public static String getContentFromFile(String filePath) {
        try (InputStream inputStream = Objects.requireNonNull(GuiService.class.getResourceAsStream(filePath))) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("IO Exception occured");
            return null;
        }
    }

    /**
     * Adds a graphic to the given Button of the image located at the given path.
     * If isInvisible is true, the ImageView's opacity is set to 0, and it's height is set to 1 pixel.
     * @param button The Button the image graphic is being added to
     * @param imagePath The path the image is located at
     * @param fitWidth The width the image should be
     * @param fitHeight The height the image should be
     * @param isInvisible The truth value of whether the image should be initially invisible
     * @return The ImageView containing the image added to the button
     */
    public static ImageView addImageGraphicToButton(Button button, String imagePath, double fitWidth, double fitHeight, boolean isInvisible) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);

        imageView.setPreserveRatio(true);
        if (isInvisible) {
            imageView.setOpacity(0);
            imageView.setFitHeight(1);
        } else {
            imageView.setFitHeight(fitHeight);
            imageView.setFitWidth(fitWidth);
        }

        button.setGraphic(imageView);

        return imageView;
    }

    /**
     * Creates and returns a Button that contains an image graphic relevant to the colour of the given wine and the
     * title of the given wine. Sets the button's on-action event to load the individual wine view at the given
     * anchor pane of the given wine.
     * @param wineToDisplay The wine to be displayed on the button
     * @param screenAnchorPane The anchor pane that the individual wine view should be loaded on to
     * @return a Button that displays the wine image and loads an individual wine view when clicked.
     */
    public static Button generateWineButton(Wine wineToDisplay, AnchorPane screenAnchorPane, double prefWidth, double prefHeight) {
        Button wineButton = new Button(wineToDisplay.getName());
        wineButton.setPrefSize(prefWidth,prefHeight);
        wineButton.setWrapText(true);
        addImageGraphicToButton(wineButton, "/images/" + wineToDisplay.getColour() + "_wine_image.png", 100, 100, false);
        wineButton.setOnAction(event -> FXWrapper.getInstance().loadIndividualWineView(screenAnchorPane, wineToDisplay));
        wineButton.setContentDisplay(TOP);
        return wineButton;
    }
}
