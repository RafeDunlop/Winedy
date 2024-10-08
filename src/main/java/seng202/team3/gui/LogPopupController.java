package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.RangeSlider;
import org.controlsfx.control.ToggleSwitch;
import seng202.team3.models.Wine;
import seng202.team3.models.WineLog;

public class LogPopupController {

    @FXML
    private Button addLogButton;

    @FXML
    private TextField amountTextField;

    @FXML
    private ToggleSwitch bottlesToggle;

    @FXML
    private Button cancelLogButton;

    @FXML
    private ComboBox<?> colourComboBox;

    @FXML
    private ComboBox<?> countryComboBox;

    @FXML
    private Button createPersonalButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<?> endDateComboBox;

    @FXML
    private Label errorDisplayLabel;

    @FXML
    private Rectangle filterRectangle;

    @FXML
    private Button filterToggleButton;

    @FXML
    private VBox filterVBox;

    @FXML
    private ComboBox<?> fullnessComboBox;

    @FXML
    private ToggleSwitch glassesToggle;

    @FXML
    private ComboBox<?> hoursComboBox;

    @FXML
    private TextArea logNoteTextArea;

    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    @FXML
    private RangeSlider priceRangeSlider;

    @FXML
    private ListView<?> resultsListView;

    @FXML
    private TextField searchBarTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Rectangle searchRectangle;

    @FXML
    private HBox selectedHBox;

    @FXML
    private Rectangle selectedWineRectangle;

    @FXML
    private ComboBox<?> startDateComboBox;

    @FXML
    private ComboBox<?> varietyComboBox;

    private WineLog preExistingLog;

    private Wine preSelectedWine;

    public LogPopupController(WineLog preExistingLog, Wine preSelectedWine) {
        this.preExistingLog = preExistingLog;
        this.preSelectedWine = preSelectedWine;
    }

    public void initialize() {
        GuiService.setUpPopUp(overlayPane, popUpAnchorPane);
    }

    @FXML
    void onCreatePersonalWineButtonClicked(ActionEvent event) {

    }

    @FXML
    void onFilterToggleButtonClicked(ActionEvent event) {

    }

    @FXML
    void onSearchButtonClicked(ActionEvent event) {

    }

}
