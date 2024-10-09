package seng202.team3.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import org.controlsfx.control.RangeSlider;
import seng202.team3.models.WineAttribute;
import seng202.team3.repository.Table;
import seng202.team3.services.SearchScreenService;
import seng202.team3.services.WineManager;
import seng202.team3.models.SearchWineList;


import java.util.List;
import java.util.stream.IntStream;

/**
 * Controller class for the search_screen.fxml
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
    private TextField lowPriceTextField;

    @FXML
    private TextField highPriceTextField;

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

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private ScrollPane wineScrollPane;

    @FXML
    private Rectangle searchWinesRectangle;

    @FXML
    private Rectangle filterRectangle;

    @FXML
    private Rectangle searchRectangle;

    @FXML
    private Rectangle infoTextRectangle;

    @FXML
    private Button searchButton;

    @FXML
    private Button filterToggleButton;

    @FXML
    private VBox filterVBox;

    @FXML
    private Label infoTextLabel;

    @FXML
    private VBox rootVBox;

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
     * Boolean to store the state of the sort VBox
     */
    private boolean sortVBoxExpanded = false;

    /**
     * Search results pagination
     */
    private Pagination searchResultsPagination;
    /**
     * Called by JavaFX upon initialisation of the search screen. Sets the values of the price range slider to the low
     * and high values. Adds all the possible attribute values to the combo boxes through searchScreenService. Sets the
     * on actions of the combo boxes to change the selected filters. Collapses the filter VBox, adds the style classes
     * to the widgets, and initialises the date range combo boxes. Loads the previous search into the VBox, if there is
     * no previous search, this is set to all the wines in the database.
     */
    public void initialize() {

        SearchScreenService searchScreenService = new SearchScreenService();

        searchBarTextField.setOnAction(this::onSearchButtonClicked);
        priceRangeSlider.setLowValue(0);
        priceRangeSlider.setHighValue(220);

        // this style class is unfinished so the line of code has been commented out for GUI consistency
        // startDateComboBox.getStyleClass().add("date-combo-box"); //Initialising combo boxes. In deliverable 3, the combo boxes will get the options from the recorded values in the database
        colourComboBox.getItems().addAll(searchScreenService.getAttributeValues(WineAttribute.COLOUR, Table.WINESUPER));
        fullnessComboBox.getItems().addAll(searchScreenService.getAttributeValues(WineAttribute.FULLNESS, Table.WINESUPER));
        countryComboBox.getItems().addAll(searchScreenService.getAttributeValues(WineAttribute.COUNTRY, Table.WINESUPER));
        varietyComboBox.getItems().addAll(searchScreenService.getAttributeValues(WineAttribute.VARIETY, Table.GRAPE));

        colourComboBox.setOnAction(select -> selectedColour = (colourComboBox.getSelectionModel().getSelectedItem().isEmpty()) ? null : colourComboBox.getSelectionModel().getSelectedItem());
        fullnessComboBox.setOnAction(select -> selectedFullness = (fullnessComboBox.getSelectionModel().getSelectedItem().isEmpty()) ? null : fullnessComboBox.getSelectionModel().getSelectedItem());
        countryComboBox.setOnAction(select -> selectedCountry = (countryComboBox.getSelectionModel().getSelectedItem().isEmpty()) ? null : countryComboBox.getSelectionModel().getSelectedItem());
        varietyComboBox.setOnAction(select -> selectedVariety = (varietyComboBox.getSelectionModel().getSelectedItem().isEmpty()) ? null : varietyComboBox.getSelectionModel().getSelectedItem());

        collapseFilterVBox();
        addStyleClasses();
        initialiseDateRangeComboBoxes();

        StringConverter<Number> converter = new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return String.valueOf(number.intValue());
            }
            @Override
            public Number fromString(String s) {
                try {
                    return Integer.parseInt(s);
                }
                catch (NumberFormatException e) {
                    return null;
                }
            }
        };
        lowPriceTextField.textProperty().bindBidirectional(priceRangeSlider.lowValueProperty(), converter);
        highPriceTextField.textProperty().bindBidirectional(priceRangeSlider.highValueProperty(), converter);
        lowPriceTextField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        highPriceTextField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));

        getSearchResults(); //Loads all wines into results box
    }

    /**
     * Method that sets the action of search button. It clears current search results, calls the WineManager
     * search method and displays the current search results
     *
     * @param event ActionEvent for the button being clicked
     */
    @FXML
    void onSearchButtonClicked(ActionEvent event) {
        searchButton.setDisable(true);
        FXWrapper.getInstance().clearPane(wineDetailsAnchorPane);
        getSearchResults();
    }


    private void getSearchResults() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                SearchWineList results = WineManager.getInstance().searchWines(
                        searchBarTextField.getText(),
                        lowYear,
                        highYear,
                        (float) priceRangeSlider.getLowValue(),
                        (float) priceRangeSlider.getHighValue(),
                        !"All".equals(selectedCountry) ? selectedCountry : null,
                        !"All".equals(selectedColour) ? selectedColour : null,
                        !"All".equals(selectedFullness) ? selectedFullness : null,
                        !"All".equals(selectedVariety) ? selectedVariety : null);

                FXWrapper.getInstance().setPreviousSearch(results);


                Platform.runLater(() -> {
                    rootVBox.getChildren().clear();

                    if (results.getWineList().isEmpty()) {
                        infoTextLabel.setText("Unfortunately there were no results for your search. Try checking your spelling or broadening your filters.");
                        infoTextRectangle.setVisible(true);
                        infoTextLabel.setVisible(true);
                    } else {
                        infoTextRectangle.setVisible(false);
                        infoTextLabel.setVisible(false);
                        searchResultsPagination = GuiService.createPagination(results.getWineList(), rootVBox, 4, 3, wineDetailsAnchorPane);
                    }

                });
                return null;
            }
        };

        task.setOnSucceeded(e -> searchButton.setDisable(false));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Used by JavaFX as the OnAction of the filter toggle button. Toggles the vbox between being expanded or collapsed
     */
    @FXML
    void onFilterToggleButtonClicked() {
        if (sortVBoxExpanded) {
            collapseFilterVBox();
            sortVBoxExpanded = false;
        } else {
            expandFilterVBox();
            sortVBoxExpanded = true;
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
                .toList();
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
     * Adds the appropriate style classes to the widgets
     */
    private void addStyleClasses() {
        searchWinesRectangle.getStyleClass().add("red-wine-rectangle");
        filterRectangle.getStyleClass().add("white-wine-rectangle");
        searchRectangle.getStyleClass().add("white-wine-rectangle");
        infoTextRectangle.getStyleClass().add("white-red-wine-rectangle");
        searchButton.getStyleClass().add("nav-bar-button");
        filterToggleButton.getStyleClass().add("nav-bar-button");
        colourComboBox.getStyleClass().add("fifteen-combo-box");
        countryComboBox.getStyleClass().add("fifteen-combo-box");
        endDateComboBox.getStyleClass().add("fifteen-combo-box");
        startDateComboBox.getStyleClass().add("fifteen-combo-box");
        varietyComboBox.getStyleClass().add("fifteen-combo-box");
        fullnessComboBox.getStyleClass().add("fifteen-combo-box");
        searchBarTextField.getStyleClass().add("sign-in-screen-text-field");
        lowPriceTextField.getStyleClass().add("sign-in-screen-text-field");
        highPriceTextField.getStyleClass().add("sign-in-screen-text-field");
    }

    /**
     * Sets the preferred height of the filter Vbox and Rectangle to 84. Sets every child of the vbox other than the
     * toggle button to be unmanaged, disabled, and invisible. Moves the wine details anchor pane to the front of the
     * screen by removing and re-adding it to the root anchor pane.
     */
    private void collapseFilterVBox() {
        filterVBox.setPrefHeight(84);
        filterRectangle.setHeight(84);
        for (Node vBoxChild : filterVBox.getChildren()) {
            vBoxChild.setManaged(false);
            vBoxChild.setDisable(true);
            vBoxChild.setVisible(false);
        }
        filterToggleButton.setManaged(true);
        filterToggleButton.setDisable(false);
        filterToggleButton.setVisible(true);

        rootAnchorPane.getChildren().remove(wineDetailsAnchorPane);
        rootAnchorPane.getChildren().add(wineDetailsAnchorPane);

        filterToggleButton.setText("Filter");
    }

    /**
     * Sets the preferred height of the filter Vbox and Rectangle to 645. Sets every child of the vbox to be managed,
     * enabled, and visible. Moves the filter rectangle and vbox to the front of screen by removing and re-adding them
     * to the root anchor pane.
     */
    private void expandFilterVBox() {
        filterVBox.setPrefHeight(645);
        filterRectangle.setHeight(645);
        for (Node vBoxChild : filterVBox.getChildren()) {
            vBoxChild.setManaged(true);
            vBoxChild.setDisable(false);
            vBoxChild.setVisible(true);
        }

        rootAnchorPane.getChildren().remove(filterRectangle);
        rootAnchorPane.getChildren().add(filterRectangle);
        rootAnchorPane.getChildren().remove(filterVBox);
        rootAnchorPane.getChildren().add(filterVBox);

        filterToggleButton.setText("Close");
    }
}
