package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.RangeSlider;
import seng202.team3.WineManager;
import seng202.team3.models.SearchWineList;
import java.util.ArrayList;

public class SearchScreenController {

    @FXML
    private ComboBox<String> colourComboBox;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<String> fullnessComboBox;

    @FXML
    private RangeSlider priceRangeSlider;

    @FXML
    private TextField searchBarTextField;

    @FXML
    private Button searchButton;

    @FXML
    private VBox searchResultsVBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<String> styleComboBox;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<String> varietyComboBox;

    @FXML
    private AnchorPane wineDetailsAnchorPane;

    private String selectedColour = null;

    private String selectedVariety = null;

    private String selectedFullness = null;

    private String selectedStyle = null;

    private String selectedCountry = null;

    private Integer lowYear = null;

    private Integer highYear = null;

    @FXML
    void onSearchButtonClicked(ActionEvent event) {
        searchResultsVBox.getChildren().clear();
        WineManager wineManager = WineManager.getInstance();
        SearchWineList results = wineManager.searchWines(
                searchBarTextField.getText(),
                lowYear,
                highYear,
                (float) priceRangeSlider.getLowValue(),
                (float) priceRangeSlider.getHighValue(),
                selectedCountry,
                selectedColour,
                selectedFullness,
                selectedVariety);
        ArrayList<String> stringList = new ArrayList<>();
        results.getWineList().forEach(wine -> stringList.add(wine.getName()));
        String[] stringArray = new String[stringList.size()];
        stringArray = stringList.toArray(stringArray);
        fillVbox(stringArray);
    }

    private void fillVbox(String[] searchResults) {
        int rows = searchResults.length / 3 + searchResults.length % 3;
        for (int i = 0; i < rows; i++) {
            HBox hbox = new HBox(10); // 10px
            hbox.setSpacing(20);
            hbox.setPadding(new Insets(10, 15, 10, 15));
            hbox.setPrefWidth(797); // Set preferred width for the HBox
            Button button1 = new Button(searchResults[3*i]);
            button1.setPrefSize(240,240);
            hbox.getChildren().add(button1);
            if (3 * i + 1 < searchResults.length) {
                Button button2 = new Button(searchResults[3 * i + 1]);
                hbox.getChildren().add(button2);
                button2.setPrefSize(240,240);
            }
            if (3 * i + 2 < searchResults.length) {
                Button button3 = new Button(searchResults[3 * i + 2]);
                hbox.getChildren().add(button3);
                button3.setPrefSize(240,240);
            }
            searchResultsVBox.getChildren().add(hbox);
        }

    }
    public void initialize() {
        colourComboBox.getItems().addAll("White", "Rose", "Red", "Dessert & Fortified");
        fullnessComboBox.getItems().addAll("DRY", "LIGHT", "FULL", "MEDIUM", "SWEET", "OFF DRY");
        countryComboBox.getItems().addAll("USA", "Italy", "France", "New Zealand", "Portugal", "Spain", "Argentina",
        "Australia", "Chile", "Romania", "South Africa", "Lebanon", "Germany",
        "Hungary", "Austria", "UK", "Macedonia", "Greece");
        varietyComboBox.getItems().addAll("Chardonnay", "Nero d\'Avola", "Viognier", "Sauvignon Blanc", "Zinfandel",
                "Cabernet Sauvignon", "Merlot", "Pinot Noir", "Syrah", "Grenache", "Riesling",
                "Malbec", "Tempranillo", "Sangiovese", "Barbera", "Shiraz", "Pinot Grigio",
                "Chenin Blanc", "Petit Verdot", "Mourvedre", "Gruner Veltliner", "Gewurztraminer",
                "Carmenere", "Albariño", "Cortese", "Fiano", "Nebbiolo", "Gamay",
                "Torrontes", "Cabernet Franc", "Cinsault", "Mourvèdre", "Chenin Blanc", "Pinotage",
                "Marsanne", "Sangiovese", "Carignan", "Roussanne", "Bourboulenc", "Clairette",
                "Semillon", "Gewürztraminer", "Grüner Veltliner", "Verdejo",
                "Melon de Bourgogne", "Harslevelu","Furmint", "Bonarda", "Grenache Blanc",
                "Palomino", "Carménère", "Rioja");
        colourComboBox.setOnAction(select -> selectedColour = colourComboBox.getSelectionModel().getSelectedItem());
        fullnessComboBox.setOnAction(select -> selectedFullness = fullnessComboBox.getSelectionModel().getSelectedItem());
        countryComboBox.setOnAction(select -> selectedCountry = countryComboBox.getSelectionModel().getSelectedItem());
        varietyComboBox.setOnAction(select -> selectedVariety = varietyComboBox.getSelectionModel().getSelectedItem());
        startDatePicker.setOnAction(event -> lowYear = startDatePicker.getValue().getYear());
        endDatePicker.setOnAction(event -> highYear = endDatePicker.getValue().getYear());
    }
}
