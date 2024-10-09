package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Pair;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.ToggleSwitch;
import seng202.team3.models.Wine;
import seng202.team3.models.WineLog;
import seng202.team3.services.LogManager;
import seng202.team3.services.WineManager;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static javafx.scene.control.ContentDisplay.TOP;
import static seng202.team3.gui.GuiService.fullDisable;

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
    private Label qtyDisplayLabel;

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

    @FXML
    private Label selectedButtonLabel;

    private LogManager logManager;

    private WineLog preExistingLog;

    private Wine selectedWine;

    private Button displayed;

    private int hourSelected;

    private Date dateSelected;

    private float standards;

    private String logNote;

    private boolean isBottles;

    private boolean logValid;

    private boolean validAmount;

    public LogPopupController(WineLog preExistingLog, Wine preSelectedWine) {
        this.preExistingLog = preExistingLog;
        selectedWine = preSelectedWine;
        this.logManager = LogManager.getInstance();
    }

    public void initialize() {
        GuiService.setUpPopUp(overlayPane, popUpAnchorPane);
        setupComboBoxes();
        setupToggleButtons();
        setupInputTextField();
        setupCoreButtons();

        logNoteTextArea.textProperty().addListener((observable, oldNote, newNote) -> logNote = newNote);

        dateSelected = logManager.getCurrentDate();
        datePicker.setValue(dateSelected.toLocalDate());
        datePicker.setOnAction(date -> dateSelected = new Date(datePicker.getValue().toEpochDay()));

        if (preExistingLog != null) {
            setLogParams();
        }

        if (selectedWine != null) {
            setSelected();
        }
        fullDisable(selectedButtonLabel, selectedWine == null);

        addStyleClasses();
        runValidationSequence();
    }

    private void setupCoreButtons() {
        addLogButton.setOnAction(event -> {
            if (logValid) {
                logManager.addLog(
                        selectedWine,
                        logNote,
                        dateSelected,
                        hourSelected,
                        isBottles,
                        Float.parseFloat(amountTextField.getText()));
                FXWrapper.getInstance().loadProfileTabPane(3);
            }
        });
    }

    private void setupInputTextField() {
        fullDisable(qtyDisplayLabel, true);
        amountTextField.textProperty().addListener((observable, sOld, sNew) ->
        {
            Pair<Boolean, String> validityPair = validateAmount(sNew);
            boolean valid = validityPair.getKey();
            fullDisable(qtyDisplayLabel, valid);
            validAmount = valid;
            if (!valid) {
                qtyDisplayLabel.setText(validityPair.getValue());
                qtyDisplayLabel.setStyle("-fx-text-fill: -fx-dark-red-wine-colour; -fx-font-size: 15");
                qtyDisplayLabel.setWrapText(true);
            }
            runValidationSequence();
        });
    }

    private void runValidationSequence() {
        if (selectedWine != null && validAmount) {
            logValid = true;
            qtyDisplayLabel.setText(String.format("You are logging %.1f %s of %s at %s on %s.",
                    Float.parseFloat(amountTextField.getText()),
                    (isBottles) ? "bottles" : "glasses",
                    selectedWine.getName(),
                    logManager.getHourConverter().toString(hourSelected),
                    getDateString(dateSelected))
            );
            qtyDisplayLabel.setStyle("-fx-text-fill: Black; -fx-font-size: 15");
            qtyDisplayLabel.setWrapText(true);
            fullDisable(qtyDisplayLabel, false);
        } else {
            logValid = false;
        }
        addLogButton.setDisable(!logValid);
    }

    private String getDateString(Date date) {
        LocalDate lDate = date.toLocalDate();
        int dayOM = lDate.getDayOfMonth();
        return String.format("the %d%s of %s %d",
                dayOM,
                getDaySuffix(dayOM),
                lDate.getMonth().toString(),
                lDate.getYear());
    }

    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }

        return switch (day % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    private Pair<Boolean, String> validateAmount(String toValidate) {
        int maxLength = 21;
        if (toValidate.isEmpty()) {
            return new Pair<>(false, "");
        } else if (toValidate.length() > maxLength) {
            return new Pair<>(false, "your amount entry is too long");
        }
        try {
            float value = Float.parseFloat(toValidate);
            if (value < 0) {
                return new Pair<>(false, "Amount must be positive");
            } else if (value == 0) {
                return new Pair<>(false, "Amount cannot be 0");
            } else {
                return new Pair<>(true,"errorDisplayLabel");
            }
        } catch (NumberFormatException e) {
            return new Pair<>(false, String.format("%s is not a valid number", toValidate));
        }
    }


    private void setupToggleButtons() {
        glassesToggle.setSelected(true);
        glassesToggle.selectedProperty().addListener((observable, old, newVal) -> toggle(newVal));
        bottlesToggle.setSelected(false);
        bottlesToggle.selectedProperty().addListener((observable, old, newVal) -> toggle(!newVal));

    }

    private void toggle(boolean toSet) {
        glassesToggle.setSelected(toSet);
        bottlesToggle.setSelected(!toSet);
        isBottles = !toSet;
    }

    private void setupComboBoxes() {
        WineManager wineManager = WineManager.getInstance();
        List<Wine> wines = wineManager.getAllWines();

        searchComboBox.getItems().addAll(wines);
        searchComboBox.setOnAction(selection -> {
            selectedWine = searchComboBox.getSelectionModel().getSelectedItem();
            fullDisable(selectedButtonLabel, selectedWine == null);
            if (selectedWine != null) {
                setSelected();
            }
            runValidationSequence();
        });
        ListCell<Wine> cellFactory = new ListCell<>() {
            @Override
            protected void updateItem(Wine wine, boolean isEmpty) {
                super.updateItem(wine, isEmpty);
                if (isEmpty || wine == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(wine.toString());
                    setWrapText(true);
                    setStyle("-fx-wrap-text: true");
                }
            }
        };
        searchComboBox.setButtonCell(cellFactory);
        hourSelected = logManager.getCurrentTime().toLocalTime().getHour();
        hoursComboBox.getItems().addAll(IntStream.range(0, 24).boxed().toList());
        hoursComboBox.setOnAction(select -> hourSelected = hoursComboBox.getSelectionModel().getSelectedItem());
        hoursComboBox.getSelectionModel().select(hourSelected);
        hoursComboBox.setConverter(logManager.getHourConverter());
    }

    private void addStyleClasses() {
        popUpAnchorPane.getStyleClass().add("titled-pane");
        searchRectangle.getStyleClass().add("red-wine-rectangle");
        selectedWineRectangle.getStyleClass().add("red-wine-rectangle");
        searchComboBox.getStyleClass().add("fifteen-combo-box");
        hoursComboBox.getStyleClass().add("fifteen-combo-box");
        createPersonalButton.getStyleClass().add("nav-bar-button");
        glassesToggle.getStyleClass().add("nav-bar-button");
        bottlesToggle.getStyleClass().add("nav-bar-button");
        cancelLogButton.getStyleClass().add("nav-bar-button");
        addLogButton.getStyleClass().add("nav-bar-button");
    }

    private void setLogParams() {

    }

    private void setSelected() {
        selectedVBox.getChildren().remove(displayed);
        displayed = new Button(selectedWine.getName());
        displayed.setPrefSize(260,240);
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
}
