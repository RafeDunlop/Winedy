package seng202.team3.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.RangeSlider;
import seng202.team3.services.WineManager;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Controller class for the search screen
 *
 * @author Sophia Copley (sco207)
 */
public class SearchScreenController {

    @FXML
    private ComboBox<String> colourComboBox;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private ComboBox<String> fullnessComboBox;

    @FXML
    private RangeSlider priceRangeSlider;

    @FXML
    private TextField searchBarTextField;

    @FXML
    private VBox searchResultsVBox;

    @FXML
    private ComboBox<Integer> startDateComboBox;

    @FXML
    private ComboBox<Integer> endDateComboBox;

    @FXML
    private ComboBox<String> varietyComboBox;

    @FXML
    private AnchorPane wineDetailsAnchorPane;
    /**
     * Current wine colour filter selected by the Wine Drinker
     */
    private String selectedColour = null;
    /**
     * Current grape variety filter selected by the Wine Drinker
     */
    private String selectedVariety = null;
    /**
     * Current wine fullness filter selected by the Wine Drinker
     */
    private String selectedFullness = null;
    /**
     * Current country filter selected by the Wine Drinker
     */
    private String selectedCountry = null;
    /**
     * Earliest year filter selected by the Wine Drinker
     */
    private Integer lowYear = null;
    /**
     * Latest year filter selected by the Wine Drinker
     */
    private Integer highYear = null;

    /**
     * Method to initialise the search screen with the required combo boxes for
     * TODO: initialise the combo boxes so that they get the possible options from the database instead of hard coding the options
     */
    public void initialize() {
        searchBarTextField.setOnAction(this::onSearchButtonClicked);
        priceRangeSlider.setLowValue(0);
        priceRangeSlider.setHighValue(220);

        // this style class is unfinished so the line of code has been commented out for GUI consistency
        // startDateComboBox.getStyleClass().add("date-combo-box"); //Initialising combo boxes. In deliverable 3, the combo boxes will get the options from the recorded values in the database
        colourComboBox.getItems().addAll("", "White", "Rose", "Red");
        fullnessComboBox.getItems().addAll("", "DRY", "LIGHT", "FULL", "MEDIUM", "SWEET", "OFF DRY");
        countryComboBox.getItems().addAll("", "USA", "Italy", "France", "New Zealand", "Portugal", "Spain", "Argentina",
                "Australia", "Chile", "Romania", "South Africa", "Lebanon", "Germany",
                "Hungary", "Austria", "UK", "Macedonia", "Greece");
        setUpVarietyComboBox(varietyComboBox);

        colourComboBox.setOnAction(select -> selectedColour = (colourComboBox.getSelectionModel().getSelectedItem() == "") ? null : colourComboBox.getSelectionModel().getSelectedItem());
        fullnessComboBox.setOnAction(select -> selectedFullness = (fullnessComboBox.getSelectionModel().getSelectedItem() == "") ? null : fullnessComboBox.getSelectionModel().getSelectedItem());
        countryComboBox.setOnAction(select -> selectedCountry = (countryComboBox.getSelectionModel().getSelectedItem() == "") ? null : countryComboBox.getSelectionModel().getSelectedItem());
        varietyComboBox.setOnAction(select -> selectedVariety = (varietyComboBox.getSelectionModel().getSelectedItem() == "") ? null : varietyComboBox.getSelectionModel().getSelectedItem());

        initialiseDateRangeComboBoxes();
    }

    /**
     * Method that sets the action of search button. It clears current search results, calls the WineManager
     * search method and displays the current search results
     * @param event ActionEvent for the button being clicked
     */
    @FXML
    void onSearchButtonClicked(ActionEvent event) {
        searchResultsVBox.getChildren().clear();
        FXWrapper.getInstance().clearPane(wineDetailsAnchorPane);
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
        List<Wine> resultsList = results.getWineList();
        Wine[] resultsArray = new Wine[resultsList.size()];
        resultsArray = resultsList.toArray(resultsArray);

        fillVbox(resultsArray);
    }

    /**
     * Method to fill the search result VBox with buttons corresponding to the wines that are in the search results
     * @param searchResults an array of the search results
     */
    private void fillVbox(Wine[] searchResults) {
        int length = searchResults.length;
        int rows = (length % 3 == 0)? length / 3 : length / 3 + 1;

        for (int i = 0; i < rows; i++) {
            HBox hbox = new HBox(10); // 10px
            hbox.setSpacing(20);
            hbox.setPadding(new Insets(10, 15, 10, 15));
            hbox.setPrefWidth(800); // Set preferred width for the HBox

            Button button1 = GuiService.generateWineButton(searchResults[3 * i], wineDetailsAnchorPane, 240, 240);
            hbox.getChildren().add(button1);

            if (3 * i + 1 < searchResults.length) {
                Button button2 = GuiService.generateWineButton(searchResults[3 * i  + 1], wineDetailsAnchorPane, 240, 240);
                hbox.getChildren().add(button2);
            }

            if (3 * i + 2 < searchResults.length) {
                Button button3 = GuiService.generateWineButton(searchResults[3 * i + 2], wineDetailsAnchorPane, 240, 240);
                hbox.getChildren().add(button3);
            }

            searchResultsVBox.getChildren().add(hbox);
        }

    }

    /**
     * Method to initialise the date range combination box.
     * Initialises the boxes so that the displayed options for the end date combo box are all later than the currently selected
     * and vice versa and allows the WineDrinker unselect the date filters.
     */
    private void initialiseDateRangeComboBoxes() {

        List<Integer> years = IntStream.rangeClosed(2007, 2019)
                .boxed()
                .collect(Collectors.toList());
        ObservableList<Integer> yearList = FXCollections.observableArrayList();
        yearList.add(null);
        yearList.addAll(years);

        startDateComboBox.getItems().addAll(yearList);
        endDateComboBox.getItems().addAll(yearList);

        startDateComboBox.setOnAction(event -> {
            lowYear = startDateComboBox.getSelectionModel().getSelectedItem();
            EventHandler<ActionEvent> endDateComboBoxOnAction= endDateComboBox.getOnAction();
            endDateComboBox.setOnAction(null);
            endDateComboBox.setItems(yearList.filtered(year -> year == null || lowYear == null || year >= lowYear));
            endDateComboBox.setOnAction(endDateComboBoxOnAction);
        });

        endDateComboBox.setOnAction(event -> {
            highYear = endDateComboBox.getSelectionModel().getSelectedItem();
            EventHandler<ActionEvent> startDateComboBoxOnAction = startDateComboBox.getOnAction();
            startDateComboBox.setOnAction(null);
            startDateComboBox.setItems(yearList.filtered(year ->  year == null || highYear == null || year <= highYear));
            startDateComboBox.setOnAction(startDateComboBoxOnAction);
        });

    }

    /**
     * Adds all variety strings to the variety ComboBox.
     * Created to modularise the initialize method in SearchScreenController to make it more readable nad maintainable.
     * TODO: Change to querying the data base for a table of all unique variety types
     * @param varietyComboBox the ComboBox the strings will be added to
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
