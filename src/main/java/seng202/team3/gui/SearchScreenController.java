package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.RangeSlider;

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
    private ListView<?> searchResultsListView;

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

    @FXML
    void onSearchButtonClicked(ActionEvent event) {

    }

    public void initialize() {
        colourComboBox.getItems().addAll("White", "Rose", "Red", "Dessert & Fortified");
        fullnessComboBox.getItems().addAll("DRY", "LIGHT", "FULL", "MEDIUM", "SWEET", "OFF DRY");
        styleComboBox.getItems().addAll("Rich", "Big", "Fruity", "Smooth", "Rose", "Crisp", "Sweet", "Dessert & Fortified");
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
    }
}
