package seng202.team3.gui;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.Wine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
     * @param filePath given path of file
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
        wineButton.getStyleClass().add("nav-bar-button");
        wineButton.setFont(new Font("System", 20));
        return wineButton;
    }

    /** Fills VBox with wines. This could be search results or contents of a wine list.
     *
     * @param wineList wineList to get wines from
     * @param vBox vBox to fill
     */
    public static void fillVboxGrid(Wine[] wineList, VBox vBox, AnchorPane wineDetailsAnchorPane) {
        int length = wineList.length;
        int rows = (length % 3 == 0)? length / 3 : length / 3 + 1;

        for (int i = 0; i < rows; i++) {
            HBox hbox = new HBox(10); // 10px
            hbox.setSpacing(20);
            hbox.setPadding(new Insets(10, 15, 10, 15));
            hbox.setPrefWidth(800); // Set preferred width for the HBox

            Button button1 = GuiService.generateWineButton(wineList[3 * i], wineDetailsAnchorPane, 240, 240);
            hbox.getChildren().add(button1);

            if (3 * i + 1 < wineList.length) {
                Button button2 = GuiService.generateWineButton(wineList[3 * i  + 1], wineDetailsAnchorPane, 240, 240);
                hbox.getChildren().add(button2);
            }

            if (3 * i + 2 < wineList.length) {
                Button button3 = GuiService.generateWineButton(wineList[3 * i + 2], wineDetailsAnchorPane, 240, 240);
                hbox.getChildren().add(button3);
            }

            vBox.getChildren().add(hbox);
        }
    }

    /**
     * Changes the visibility and ability of anchor panes in the recommendation system
     * @param toTurnOff the anchor pane to make invisible and unable to be interacted
     */
    public static void turnOffPane(AnchorPane toTurnOff){
        toTurnOff.setVisible(false);
        toTurnOff.setDisable(true);
    }
    /**
     * Changes the visibility and ability of anchor panes in the recommendation system
     * @param toTurnOn the anchor pane to make visible and able to be interacted
     */
    public static void turnOnPane(AnchorPane toTurnOn){
        toTurnOn.setVisible(true);
        toTurnOn.setDisable(false);
    }

    public static void setUpPopUp(StackPane overlayPane, AnchorPane popUpAnchorPane) {
        overlayPane.getStyleClass().add("overlay-stackpane");
        popUpAnchorPane.getStyleClass().add("white-wine-pane");
        overlayPane.setOnMouseClicked(event -> {
            Bounds popUpBounds = popUpAnchorPane.localToScene(popUpAnchorPane.getLayoutBounds());
            if (!popUpBounds.contains(event.getSceneX(), event.getSceneY())) {
                FXWrapper.getInstance().removePopUp(overlayPane);
            }
        });
    }
}
