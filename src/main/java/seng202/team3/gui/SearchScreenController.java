package seng202.team3.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import org.jetbrains.annotations.NotNull;
import seng202.team3.WineManager;
import seng202.team3.guiservice.SearchScreenService;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.awt.Color.red;

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
    private Button searchButton;

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

        List<Wine> resultsList = results.getWineList();
        Wine[] resultsArray = new Wine[resultsList.size()];
        resultsArray = resultsList.toArray(resultsArray);

        fillVbox(resultsArray);
    }

    private void fillVbox(Wine[] searchResults) {
        int length = searchResults.length;
        int rows = (length % 3 == 0)? length / 3 : length / 3 + 1;

        for (int i = 0; i < rows; i++) {
            HBox hbox = new HBox(10); // 10px
            hbox.setSpacing(20);
            hbox.setPadding(new Insets(10, 15, 10, 15));
            hbox.setPrefWidth(800); // Set preferred width for the HBox

            Button button1 = SearchScreenService.generateWineButton(searchResults[3*i]);
            hbox.getChildren().add(button1);

            if (3 * i + 1 < searchResults.length) {
                Button button2 = SearchScreenService.generateWineButton(searchResults[3 * i + 1]);
                hbox.getChildren().add(button2);
            }

            if (3 * i + 2 < searchResults.length) {
                Button button3 = SearchScreenService.generateWineButton(searchResults[3 * i + 2]);
                hbox.getChildren().add(button3);
            }

            searchResultsVBox.getChildren().add(hbox);
        }

    }

    public void initialize() {
        priceRangeSlider.setLowValue(0);
        priceRangeSlider.setHighValue(200);

        startDateComboBox.getStyleClass().add("date-combo-box");
        colourComboBox.getItems().addAll("", "White", "Rose", "Red");
        fullnessComboBox.getItems().addAll("", "DRY", "LIGHT", "FULL", "MEDIUM", "SWEET", "OFF DRY");
        countryComboBox.getItems().addAll("", "USA", "Italy", "France", "New Zealand", "Portugal", "Spain", "Argentina",
        "Australia", "Chile", "Romania", "South Africa", "Lebanon", "Germany",
        "Hungary", "Austria", "UK", "Macedonia", "Greece");
        SearchScreenService.setUpVarietyComboBox(varietyComboBox);

        colourComboBox.setOnAction(select -> selectedColour = (colourComboBox.getSelectionModel().getSelectedItem() == "") ? null : colourComboBox.getSelectionModel().getSelectedItem());
        fullnessComboBox.setOnAction(select -> selectedFullness = (fullnessComboBox.getSelectionModel().getSelectedItem() == "") ? null : fullnessComboBox.getSelectionModel().getSelectedItem());
        countryComboBox.setOnAction(select -> selectedCountry = (countryComboBox.getSelectionModel().getSelectedItem() == "") ? null : countryComboBox.getSelectionModel().getSelectedItem());
        varietyComboBox.setOnAction(select -> selectedVariety = (varietyComboBox.getSelectionModel().getSelectedItem() == "") ? null : varietyComboBox.getSelectionModel().getSelectedItem());

        initialiseDateRangeComboBoxes();
    }

    private void initialiseDateRangeComboBoxes() {
        startDateComboBox.getStyleClass().add("date-combo-box");
        endDateComboBox.getStyleClass().add("date-combo-box");

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
}
