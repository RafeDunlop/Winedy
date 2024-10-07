package seng202.team3.gui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
import java.util.List;
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
            log.error("IO Exception occurred");
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
     *
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

    /**
     * Generates an HBox that contains buttons for each wine in the given list. This could be search results or contents of a wine list.
     *
     * @param wineList Contains the wines to generate buttons for
     * @param wineDetailsAnchorPane The anchor pane needed for the onAction() method of the buttons
     * @return An HBox containing wine buttons
     *
     * @see GuiService#generateWineButton(Wine, AnchorPane, double, double)
     * @see GuiService#startButtonGeneration(List, VBox, AnchorPane, int)
     */
    public static HBox GenerateHBox(List<Wine> wineList, AnchorPane wineDetailsAnchorPane) {

        HBox hBox = new HBox(10); // 10px
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(10, 15, 10, 15));
        hBox.setPrefWidth(800); // Set preferred width for the HBox

        for (Wine wine: wineList) {
            Button button = generateWineButton(wine, wineDetailsAnchorPane, 240, 240);
            hBox.getChildren().add(button);
        }

        return hBox;
    }

    /**
     * Creates a task which generates and updates the wine buttons for all given wines and updates the vbox safely on the
     * JavaFX Application thread.
     *
     * @param wineList Contains wines to generate buttons for
     * @param vBox  The VBox that will contain the generated buttons
     * @param wineDetailsAnchorPane The anchor pane which the contains the VBox
     * @param buttonsPerRow The number of buttons to be generated per row/HBox
     *
     * @see GuiService#GenerateHBox(List, AnchorPane)
     */
    public static void startButtonGeneration(List<Wine> wineList, VBox vBox, AnchorPane wineDetailsAnchorPane, int buttonsPerRow) {

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                for (int i = 0; i < wineList.size(); i+=buttonsPerRow) {

                    HBox hBox;
                    if (i+buttonsPerRow >= wineList.size()) {
                        hBox = GenerateHBox(wineList.subList(i,wineList.size()), wineDetailsAnchorPane);
                    } else {
                        hBox = GenerateHBox(wineList.subList(i,i+buttonsPerRow), wineDetailsAnchorPane);
                    }

                    Platform.runLater(() -> vBox.getChildren().add(hBox));
                }

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true); //Make this thread a Daemon thread so that it closes when the main Application thread is closed
        thread.start();
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
