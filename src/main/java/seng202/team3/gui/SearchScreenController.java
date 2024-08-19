package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for the search_screen.fxml window
 * @author
 */

public class SearchScreenController {
    @FXML
    private ComboBox<?> StyleCombo;

    @FXML
    private Label colourComboBox;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Slider priceRangeSlider;

    @FXML
    private ComboBox<?> producerComboBox;

    @FXML
    private Button recommendWinesButton;

    @FXML
    private ComboBox<?> regionComboBox;

    @FXML
    private TextField searchBarTextField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<?> searchListView;

    @FXML
    private Button searchWithFiltersButton;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<?> varietyComboBox;

    @FXML
    private AnchorPane wineDetailsAnchorPane;

    @FXML
    void onRecommendWinesButton(ActionEvent event) {

    }

    @FXML
    void onSearchButtonClicked(ActionEvent event) {

    }

    @FXML
    void onSearchWithFiltersButton(ActionEvent event) {

    }
}
