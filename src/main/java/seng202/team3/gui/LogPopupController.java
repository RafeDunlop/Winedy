package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.ToggleSwitch;
import seng202.team3.models.Wine;
import seng202.team3.models.WineLog;
import seng202.team3.services.LogManager;
import seng202.team3.services.WineManager;

import java.sql.Date;
import java.util.List;
import java.util.stream.IntStream;

import static javafx.scene.control.ContentDisplay.TOP;

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
    private Button createPersonalButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label errorDisplayLabel;

    @FXML
    private ToggleSwitch glassesToggle;

    @FXML
    private ComboBox<Integer> hoursComboBox;

    @FXML
    private TextArea logNoteTextArea;

    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    @FXML
    private SearchableComboBox<Wine> searchComboBox;

    @FXML
    private Rectangle searchRectangle;

    @FXML
    private Rectangle selectedWineRectangle;

    @FXML
    private VBox selectedVBox;

    private LogManager logManager;

    private WineLog preExistingLog;

    private Wine selectedWine;

    private Button displayed;

    private int hourSelected;

    private Date dateSelected;

    private float standards;

    private String logNote;

    public LogPopupController(WineLog preExistingLog, Wine preSelectedWine) {
        this.preExistingLog = preExistingLog;
        selectedWine = preSelectedWine;
        this.logManager = LogManager.getInstance();
    }

    public void initialize() {
        addStyleClasses();
        GuiService.setUpPopUp(overlayPane, popUpAnchorPane);
        setupComboBoxes();

        datePicker.setValue(logManager.getCurrentDate().toLocalDate());
        datePicker.setOnAction(date -> dateSelected = new Date(datePicker.getValue().toEpochDay()));

        if (preExistingLog != null) {
            setLogParams();
        }

        if (selectedWine != null) {
            System.out.println("print");
            setSelected();
        }
    }

    private void setupComboBoxes() {
        WineManager wineManager = WineManager.getInstance();
        List<Wine> wines = wineManager.getAllWines();
        searchComboBox.getItems().addAll(wines);
        searchComboBox.setConverter(Wine.getStringConverter());
        searchComboBox.setOnAction(selection -> {
            selectedWine = searchComboBox.getSelectionModel().getSelectedItem();
            setSelected();
        });

        hoursComboBox.getItems().addAll(IntStream.range(0, 24).boxed().toList());
        hoursComboBox.setOnAction(select -> hourSelected = hoursComboBox.getSelectionModel().getSelectedItem());
        hoursComboBox.getSelectionModel().select(logManager.getCurrentTime().toLocalTime().getHour());
        hoursComboBox.setConverter(logManager.getHourConverter());
    }

    private void addStyleClasses() {
        popUpAnchorPane.getStyleClass().add("titled-pane");
        searchRectangle.getStyleClass().add("red-wine-rectangle");
        selectedWineRectangle.getStyleClass().add("red-wine-rectangle");
        searchComboBox.getStyleClass().add("fifteen-combo-box");
        hoursComboBox.getStyleClass().add("fifteen-combo-box");
    }

    private void setLogParams() {

    }

    private void setSelected() {
        System.out.println("called");
        selectedVBox.getChildren().remove(displayed);
        displayed = new Button(selectedWine.getName());
        displayed.setPrefSize(240,240);
        displayed.setWrapText(true);
        GuiService.addImageGraphicToButton(displayed, "/images/" + selectedWine.getColour() + "_wine_image.png", 100, 100, false);
        displayed.setOnAction(event -> {/*show details popup */});
        displayed.setContentDisplay(TOP);
        displayed.getStyleClass().add("nav-bar-button");
        displayed.setFont(new Font("System", 20));
        selectedVBox.getChildren().add(displayed);
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
