package seng202.team3.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.controlsfx.control.RangeSlider;
import seng202.team3.models.WineAttribute;
import seng202.team3.repository.Table;
import seng202.team3.services.SearchScreenService;
import seng202.team3.services.WineListManager;
import seng202.team3.services.WineManager;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;

import java.util.List;
import java.util.stream.Collectors;
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
     * Label containing the text of the filterToggleButton inside of it's graphic
     */
    private Label filterToggleButtonLabel;

    /**
     * HBox that contains the label and imageView which is set as the graphic of the filterToggleButton
     */
    HBox filterToggleButtonHbox;

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

        searchBarTextField.setOnAction(this::onSearchButtonClicked);
        priceRangeSlider.setLowValue(0);
        priceRangeSlider.setHighValue(220);

        initialiseAttributeComboBoxes();
        initialiseDateRangeComboBoxes();

        filterToggleButtonHbox = new HBox();
        ImageView filterToggleButtonArrowImageView = new ImageView(new Image("/images/drop_down_arrow.png"));
        filterToggleButtonArrowImageView.setFitWidth(30);
        filterToggleButtonArrowImageView.setFitHeight(15);
        filterToggleButtonArrowImageView.setPreserveRatio(false);
        filterToggleButtonLabel = new Label("Filter");
        filterToggleButtonLabel.setFont(new Font("System", 20));
        filterToggleButtonHbox.getChildren().addAll(filterToggleButtonLabel, filterToggleButtonArrowImageView);
        filterToggleButton.setGraphic(filterToggleButtonHbox);
        filterToggleButton.setText("");

        collapseFilterVBox();
        addStyleClasses();

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

        searchButton.setDisable(true);
        getSearchResults(true); //Loads all wines into results box
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
        getSearchResults(false);
    }

    /**
     * If usePreviousSearch is false, gets the results of the search from the database. Otherwise, uses the previous
     * search stored in FXWrapper. Loads the wine list view with the search results. This is set up to run on a separate
     * thread to keep the JavaFX application responsive while it runs in the background
     *
     * @param usePreviousSearch the truth value of whether the previous search should be used
     */
    private void getSearchResults(boolean usePreviousSearch) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                SearchWineList results;

                if (WineListManager.getInstance().getLastSearched() == null | !usePreviousSearch) {
                    results = WineManager.getInstance().searchWines(
                            searchBarTextField.getText(),
                            lowYear,
                            highYear,
                            (float) priceRangeSlider.getLowValue(),
                            (float) priceRangeSlider.getHighValue(),
                            !"All".equals(selectedCountry) ? selectedCountry : null,
                            !"All".equals(selectedColour) ? selectedColour : null,
                            !"All".equals(selectedFullness) ? selectedFullness : null,
                            !"All".equals(selectedVariety) ? selectedVariety : null);
                } else {
                    results = WineListManager.getInstance().getLastSearched();
                    setUpPreviousSearchValues();
                }

                WineListManager.getInstance().setLastSearched(results);

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
        filterToggleButton.setPrefWidth(320);
        filterToggleButtonLabel.setText("Filter");
        HBox.setMargin(filterToggleButtonLabel, new Insets(0, 200, 0, 0));

        rootAnchorPane.getChildren().remove(wineDetailsAnchorPane);
        rootAnchorPane.getChildren().add(wineDetailsAnchorPane);
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

        filterToggleButton.setPrefWidth(130);
        filterToggleButtonLabel.setText("Close");
        HBox.setMargin(filterToggleButtonLabel, new Insets(0, 10, 0, 0));

        rootAnchorPane.getChildren().remove(filterRectangle);
        rootAnchorPane.getChildren().add(filterRectangle);
        rootAnchorPane.getChildren().remove(filterVBox);
        rootAnchorPane.getChildren().add(filterVBox);
    }

    /**
     * Adds all the attribute values from the database to the attribute combo boxes. Sets the on action of the
     * attribute combo boxes to update the value of the selected attribute to the selected item
     */
    private void initialiseAttributeComboBoxes() {
        colourComboBox.setPromptText("All");
        fullnessComboBox.setPromptText("All");
        countryComboBox.setPromptText("All");
        varietyComboBox.setPromptText("All");

        colourComboBox.getItems().addAll(SearchScreenService.getAttributeValues(WineAttribute.COLOUR, Table.WINESUPER));
        fullnessComboBox.getItems().addAll(SearchScreenService.getAttributeValues(WineAttribute.FULLNESS, Table.WINESUPER));
        countryComboBox.getItems().addAll(SearchScreenService.getAttributeValues(WineAttribute.COUNTRY, Table.WINESUPER));
        varietyComboBox.getItems().addAll(SearchScreenService.getAttributeValues(WineAttribute.VARIETY, Table.GRAPE));

        colourComboBox.setOnAction(select -> selectedColour = (colourComboBox.getSelectionModel().getSelectedItem().isEmpty()) ? null : colourComboBox.getSelectionModel().getSelectedItem());
        fullnessComboBox.setOnAction(select -> selectedFullness = (fullnessComboBox.getSelectionModel().getSelectedItem().isEmpty()) ? null : fullnessComboBox.getSelectionModel().getSelectedItem());
        countryComboBox.setOnAction(select -> selectedCountry = (countryComboBox.getSelectionModel().getSelectedItem().isEmpty()) ? null : countryComboBox.getSelectionModel().getSelectedItem());
        varietyComboBox.setOnAction(select -> selectedVariety = (varietyComboBox.getSelectionModel().getSelectedItem().isEmpty()) ? null : varietyComboBox.getSelectionModel().getSelectedItem());
    }

    private void setUpPreviousSearchValues() {
        SearchWineList previousSearch = WineListManager.getInstance().getLastSearched();

        searchBarTextField.setText(previousSearch.getKeywords());
        colourComboBox.setValue(previousSearch.getColour());
        varietyComboBox.setValue(previousSearch.getGrapeName());
        countryComboBox.setValue(previousSearch.getCountry());
        fullnessComboBox.setValue(previousSearch.getFullness());
        startDateComboBox.setValue(previousSearch.getMinYear());
        endDateComboBox.setValue(previousSearch.getMaxYear());
        priceRangeSlider.setHighValue(previousSearch.getMaxPrice());
        priceRangeSlider.setLowValue(previousSearch.getMinPrice());

        selectedColour = previousSearch.getColour();
        selectedVariety = previousSearch.getGrapeName();
        selectedCountry = previousSearch.getCountry();
        selectedFullness = previousSearch.getFullness();
        lowYear = previousSearch.getMinYear();
        highYear = previousSearch.getMaxYear();
    }
}
