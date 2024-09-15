package seng202.team3.guiservice;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import seng202.team3.gui.FXWrapper;
import seng202.team3.gui.NestedScreen;
import seng202.team3.models.Wine;

import static javafx.scene.control.ContentDisplay.TOP;

/**
 * A final service class for the Search Screen.
 * Used by SearchScreenController.
 * Contains a collection of static methods.
 * @author Hannah Botting (hbo51)
 */
public class SearchScreenService {

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
     * Creates and returns a Button that contains an image graphic relevant to the colour of the given wine and the
     * title of the given wine.
     *
     * @param wineToDisplay    The wine to be displayed on the button
     * @param screenAnchorPane
     * @return a Button
     */
    public static Button generateWineButton(Wine wineToDisplay, AnchorPane screenAnchorPane) {
        Button wineButton = new Button(wineToDisplay.getName());
        wineButton.setPrefSize(240,240);
        wineButton.setWrapText(true);
        addImageGraphicToButton(wineButton, "/images/" + wineToDisplay.getColour() + "_wine_image.png");
        wineButton.setContentDisplay(TOP);
        wineButton.setOnAction(event -> FXWrapper.getInstance().loadIndividualWineView(screenAnchorPane, wineToDisplay));
        return wineButton;
    }


    /**
     * Adds all variety strings to the variety ComboBox.
     * Created to modularise the initialize method in SearchScreenController to make it more readable nad maintainable.
     * TODO: Change to querying the data base for a table of all unique variety types
     * @param varietyComboBox the ComboBox the strings are added to
     */
    public static void setUpVarietyComboBox(ComboBox<String> varietyComboBox) {
        varietyComboBox.getItems().addAll("", "Albariño", "Barbera", "Bonarda", "Bourboulenc", "Cabernet Franc",
                "Cabernet Sauvignon", "Carignan", "Carmenere", "Carménère", "Chardonnay", "Chenin Blanc", "Chenin Blanc",
                "Cinsault", "Clairette", "Cortese", "Fiano","Furmint", "Gamay", "Gewurztraminer", "Gewürztraminer",
                "Grenache", "Grenache Blanc", "Gruner Veltliner", "Grüner Veltliner", "Harslevelu", "Malbec", "Marsanne",
                "Melon de Bourgogne", "Merlot", "Mourvedre", "Mourvèdre", "Nebbiolo", "Nero d\'Avola", "Palomino",
                "Petit Verdot", "Pinotage", "Pinot Grigio", "Pinot Noir", "Riesling", "Rioja", "Roussanne", "Sangiovese",
                "Sangiovese", "Sauvignon Blanc", "Semillon", "Shiraz", "Syrah", "Tempranillo",
                "Torrontes", "Verdejo", "Viognier", "Zinfandel"); // This is good for now, but what if we add more wines to the database
    }
}
