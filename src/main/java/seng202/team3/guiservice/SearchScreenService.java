package seng202.team3.guiservice;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
     * @param wineToDisplay The wine to be displayed on the button
     * @return a Button
     */
    public static Button generateWineButton(Wine wineToDisplay) {
        Button wineButton = new Button(wineToDisplay.getName());
        wineButton.setPrefSize(240,240);
        wineButton.setWrapText(true);
        addImageGraphicToButton(wineButton, "/images/" + wineToDisplay.getColour() + "_wine_image.png");
        wineButton.setContentDisplay(TOP);
        return wineButton;
    }

    /**
     * Adds all variety strings to the variety ComboBox.
     * Created to modularise the initialize method in SearchScreenController to make it more readable nad maintainable.
     * TODO: Change to querying the data base for a table of all unique variety types
     * @param varietyComboBox the ComboBox the strings are added to
     */
    public static void setUpVarietyComboBox(ComboBox<String> varietyComboBox) {
        varietyComboBox.getItems().addAll("", "Chardonnay", "Nero d\'Avola", "Viognier", "Sauvignon Blanc", "Zinfandel",
                "Cabernet Sauvignon", "Merlot", "Pinot Noir", "Syrah", "Grenache", "Riesling",
                "Malbec", "Tempranillo", "Sangiovese", "Barbera", "Shiraz", "Pinot Grigio",
                "Chenin Blanc", "Petit Verdot", "Mourvedre", "Gruner Veltliner", "Gewurztraminer",
                "Carmenere", "Albariño", "Cortese", "Fiano", "Nebbiolo", "Gamay",
                "Torrontes", "Cabernet Franc", "Cinsault", "Mourvèdre", "Chenin Blanc", "Pinotage",
                "Marsanne", "Sangiovese", "Carignan", "Roussanne", "Bourboulenc", "Clairette",
                "Semillon", "Gewürztraminer", "Grüner Veltliner", "Verdejo",
                "Melon de Bourgogne", "Harslevelu","Furmint", "Bonarda", "Grenache Blanc",
                "Palomino", "Carménère", "Rioja"); // This is good for now, but what if we add more wines to the database
    }
}
