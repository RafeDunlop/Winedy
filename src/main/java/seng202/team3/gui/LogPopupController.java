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
import seng202.team3.models.LogDiff;
import seng202.team3.models.Wine;
import seng202.team3.models.WineLog;
import seng202.team3.services.LogManager;
import seng202.team3.services.WineManager;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
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

    private Wine preSelectedWine;

    private Button displayed;

    private float standards;

    private boolean logValid;

    private boolean validAmount;

    private LogDiff oldLog;

    private LogDiff newLog;

    private boolean editMode;

    public LogPopupController(WineLog preExistingLog, Wine preSelectedWine) {
        this.preExistingLog = preExistingLog;
        this.preSelectedWine = preSelectedWine;
        this.logManager = LogManager.getInstance();
    }

    public void initialize() {

        if (preExistingLog != null) {
            setLogParams();
        } else {
            setDefaultLogParams();
        }
        editMode = preExistingLog != null;

        if (preSelectedWine != null) {
            setSelected();
        }
        fullDisable(selectedButtonLabel, preSelectedWine == null);

        GuiService.setUpPopUp(overlayPane, popUpAnchorPane);
        setupComboBoxes();
        setupToggleButtons();
        setupInputTextField();
        setupCoreButtons();
        setupDatePicker();

        logNoteTextArea.textProperty().addListener((observable, oldNote, newNote) -> newLog.setNote(newNote));


        addStyleClasses();
        runValidationSequence();
    }

    private void setupDatePicker() {
        datePicker.setValue(newLog.getDate().toLocalDate());
        datePicker.setOnAction(date -> newLog.setDate(new Date(datePicker.getValue().toEpochDay())));
    }

    private void setLogParams() {
        addLogButton.setText("Save log");
        oldLog = new LogDiff().setAmt(
                        logManager.getAmt(WineManager.getInstance().getWineById(preExistingLog.getUniqueWineId()),
                                preExistingLog.getStandards(),
                                preExistingLog.getIsBottles()))
                .setIsBottles(preExistingLog.getIsBottles())
                .setDate(preExistingLog.getDate())
                .setWine(preSelectedWine)
                .setHour(preExistingLog.getTime().toLocalTime().getHour());
        newLog = new LogDiff(oldLog);
    }

    private void setDefaultLogParams() {
        oldLog = new LogDiff().setAmt(0)
                .setIsBottles(false)
                .setDate(logManager.getCurrentDate())
                .setWine(null)
                .setHour(logManager.getCurrentTime().toLocalTime().getHour());
        newLog = new LogDiff(oldLog);
    }

    private void setupCoreButtons() {
        addLogButton.setOnAction(event -> {
            if (logValid) {
                logManager.addLog(newLog);
                closeThis();
            }
        });

        cancelLogButton.setOnAction(event -> {
            if (oldLog.equals(newLog)) {
                closeThis();
            } else {
            }
        });
    }

    private void closeThis() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        FXWrapper.getInstance().loadProfileTabPane(2);
    }

    private void setupInputTextField() {
        fullDisable(qtyDisplayLabel, true);
        amountTextField.textProperty().addListener((observable, sOld, sNew) ->
        {
            fullDisable(qtyDisplayLabel, false);
            Pair<Boolean, String> validityPair = logManager.validateAmount(sNew);
            validAmount = validityPair.getKey();
            if (!validAmount) {
                qtyDisplayLabel.setText(validityPair.getValue());
                qtyDisplayLabel.setStyle("-fx-text-fill: -fx-dark-red-wine-colour; -fx-font-size: 15");
                qtyDisplayLabel.setWrapText(true);
                newLog.setAmt(0);
                fullDisable(qtyDisplayLabel, true);
            } else {
                newLog.setAmt(Float.parseFloat(amountTextField.getText()));
                qtyDisplayLabel.setStyle("-fx-text-fill: Black; -fx-font-size: 15");
                qtyDisplayLabel.setWrapText(true);

            }
            runValidationSequence();
        });
    }

    private void runValidationSequence() {
        logValid = newLog.isValid();
        if (logValid) {
            qtyDisplayLabel.setText(String.format("You are logging %.1f %s of %s at %s on %s.",
                    Float.parseFloat(amountTextField.getText()),
                    (newLog.getIsBottles()) ? "bottles" : "glasses",
                    newLog.getWine().getName(),
                    logManager.getHourConverter().toString(newLog.getHour()),
                    logManager.getDateString(newLog.getDate()))
            );
        } else {
            fullDisable(qtyDisplayLabel, true);
        }
        addLogButton.setDisable((editMode) ? oldLog.equals(newLog) : !logValid);
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
        newLog.setIsBottles(!toSet);
        runValidationSequence();
    }

    private void setupComboBoxes() {
        WineManager wineManager = WineManager.getInstance();
        List<Wine> wines = wineManager.getAllWines();

        searchComboBox.getItems().addAll(wines);
        searchComboBox.setOnAction(selection -> {
            newLog.setWine(searchComboBox.getSelectionModel().getSelectedItem());
            fullDisable(selectedButtonLabel, newLog.getWine() == null);
            if (newLog.getWine() != null) {
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
        hoursComboBox.getItems().addAll(IntStream.range(0, 24).boxed().toList());
        hoursComboBox.setOnAction(select -> newLog.setHour(hoursComboBox.getSelectionModel().getSelectedItem()));
        hoursComboBox.getSelectionModel().select(newLog.getHour());
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

    private void setSelected() {
        Wine wine = newLog.getWine();
        selectedVBox.getChildren().remove(displayed);
        displayed = new Button(wine.getName());
        displayed.setPrefSize(260,240);
        displayed.setWrapText(true);
        GuiService.addImageGraphicToButton(displayed, "/images/" + wine.getColour() + "_wine_image.png", 100, 100, false);
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
