package seng202.team3.gui;

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

import static javafx.scene.control.ContentDisplay.TOP;
import static seng202.team3.gui.GuiService.fullDisable;

public class LogPopupController {

    @FXML
    private Button finishLogButton;

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

    @FXML
    private Button deleteLogButton;

    private final LogManager logManager;

    private final WineLog preExistingLog;

    private final Wine preSelectedWine;

    private Button displayed;

    private boolean logValid;

    private boolean validAmount;

    private LogDiff oldLog;

    private LogDiff newLog;

    private boolean editMode;

    private final Screen toReturnTo;

    public LogPopupController(WineLog preExistingLog, Wine preSelectedWine, Screen toReturnTo) {
        this.preExistingLog = preExistingLog;
        this.preSelectedWine = preSelectedWine;
        this.logManager = LogManager.getInstance();
        this.toReturnTo = toReturnTo;
    }

    public void initialize() {
        System.out.println("intialize started");
        if (preExistingLog != null) {
            System.out.println("log exists");
            setLogParams();
            System.out.println("params set");
        } else {
            setDefaultLogParams();
            fullDisable(deleteLogButton, true);
        }
        editMode = preExistingLog != null;

        if (preSelectedWine != null) {
            newLog.setWine(preSelectedWine);
            setSelected();
        }
        fullDisable(selectedButtonLabel, newLog.getWine() == null);

        GuiService.setUpPopUp(overlayPane, popUpAnchorPane, () -> {
            if (!newLog.equals(oldLog)) {
                FXWrapper.getInstance().loadLogChangesPopup(
                        () -> FXWrapper.getInstance().removePopUp(overlayPane));
            } else {
                FXWrapper.getInstance().removePopUp(overlayPane);
            }
        });
        setupComboBoxes();
        setupToggleButtons();
        setupInputTextField();
        setupCoreButtons();
        setupDatePicker();
        setupLogNoteTextArea();

        System.out.println("pre validation");
        addStyleClasses();
        runValidationSequence();
        System.out.println("initialize finsihed");
    }

    private void setupLogNoteTextArea() {
        logNoteTextArea.setWrapText(true);
        logNoteTextArea.textProperty().addListener((observable, oldNote, newNote) -> {
            newLog.setNote(newNote);
            runValidationSequence();
        });
    }

    private void setupDatePicker() {
        LocalDate currentDate = logManager.getCurrentDate().toLocalDate();
        datePicker.setEditable(false);
        datePicker.getEditor().setOnMouseClicked(event -> datePicker.show());
        datePicker.setValue(newLog.getDate().toLocalDate());
        datePicker.setOnAction(date -> {
            LocalDate selected = datePicker.getValue();
            System.out.println(selected);
            if (selected != null) {
                newLog.setDate(Date.valueOf(selected));
                resetHoursComboBox();
            }
        });

        datePicker.setDayCellFactory(factory ->
            new DateCell() {

                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    if (date != null && !empty) {
                        if(date.isAfter(currentDate)) {
                            setDisable(true);
                        }
                    }
                }
            });
    }

    private void setLogParams() {
        finishLogButton.setText("Save log");
        oldLog = new LogDiff().setAmt(
                        logManager.getAmt(WineManager.getInstance().getWineById(preExistingLog.getUniqueWineId()),
                                preExistingLog.getStandards(),
                                preExistingLog.getIsBottles()))
                .setIsBottles(preExistingLog.getIsBottles())
                .setDate(preExistingLog.getDate())
                .setWine(WineManager.getInstance().getWineById(preExistingLog.getUniqueWineId()))
                .setHour(preExistingLog.getTime().toLocalTime().getHour())
                .setNote((preExistingLog.getNote() == null) ? "" : preExistingLog.getNote());

        newLog = new LogDiff(oldLog);
        amountTextField.setText(String.format("%.1f", newLog.getAmt()));

        setSelected();
    }

    private void setDefaultLogParams() {
        oldLog = new LogDiff().setAmt(0)
                .setIsBottles(false)
                .setDate(logManager.getCurrentDate())
                .setWine(null)
                .setHour(logManager.getCurrentTime().toLocalTime().getHour())
                .setNote("");
        newLog = new LogDiff(oldLog);
    }

    private void setupCoreButtons() {
        finishLogButton.setOnAction(event -> {
            if (logValid) {
                if (editMode) {
                    logManager.update(preExistingLog, newLog);
                } else {
                    logManager.addLog(newLog);
                }
                closeThis();
            }
        });

        cancelLogButton.setOnAction(event -> {
            if (oldLog.equals(newLog)) {
                closeThis();
            } else {
                FXWrapper.getInstance().loadLogChangesPopup(
                        () -> FXWrapper.getInstance().removePopUp(overlayPane));
            }
        });

        deleteLogButton.setOnAction(event -> FXWrapper.getInstance().loadLogDeletePopup(
                () -> {
                    logManager.deleteLog(preExistingLog);
                    FXWrapper.getInstance().removePopUp(overlayPane);
                    FXWrapper.getInstance().loadProfileTabPane(2);
                }));
    }

    private void closeThis() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        if (toReturnTo == Screen.TRACKINGCONSUMPTIONSCREEN) {
            FXWrapper.getInstance().loadProfileTabPane(2);
        } else {
            FXWrapper.getInstance().loadScreen(toReturnTo);
        }
    }

    private void setupInputTextField() {
        fullDisable(qtyDisplayLabel, true);
        amountTextField.textProperty().addListener((observable, sOld, sNew) ->
        {
            fullDisable(qtyDisplayLabel, true);
            Pair<Boolean, String> validityPair = logManager.validateAmount(sNew);
            validAmount = validityPair.getKey();
            if (!validAmount) {
                qtyDisplayLabel.setText(validityPair.getValue());
                qtyDisplayLabel.setStyle("-fx-text-fill: -fx-dark-red-wine-colour; -fx-font-size: 15");
                qtyDisplayLabel.setWrapText(true);
                newLog.setAmt(0);
                fullDisable(qtyDisplayLabel, false);
            } else {
                newLog.setAmt(Float.parseFloat(amountTextField.getText()));
            }
            runValidationSequence();
        });
    }

    private void runValidationSequence() {
        logValid = newLog.isValid();
        System.out.println("log is valid: " + logValid);
        if (logValid) {
            fullDisable(qtyDisplayLabel, false);
            qtyDisplayLabel.setStyle("-fx-text-fill: Black; -fx-font-size: 15");
            qtyDisplayLabel.setWrapText(true);
            qtyDisplayLabel.setText(newLog.toString());
        }
        finishLogButton.setDisable((editMode) ? oldLog.equals(newLog) : !logValid);
    }

    private void setupToggleButtons() {
        boolean isBottles = newLog.getIsBottles();
        glassesToggle.setSelected(!isBottles);
        glassesToggle.selectedProperty().addListener((observable, old, newVal) -> toggle(newVal));
        bottlesToggle.setSelected(isBottles);
        bottlesToggle.selectedProperty().addListener((observable, old, newVal) -> toggle(!newVal));
    }

    private void toggle(boolean toSet) {
        glassesToggle.setSelected(toSet);
        bottlesToggle.setSelected(!toSet);
        newLog.setIsBottles(!toSet);
        amountTextField.setPromptText(logManager.getAmtPromptText(!toSet));
        runValidationSequence();
    }

    private void setupComboBoxes() {
        List<Wine> wines = WineManager.getInstance().getAllWines();

        searchComboBox.getItems().addAll(wines);
        searchComboBox.setOnAction(selection -> {
            newLog.setWine(searchComboBox.getSelectionModel().getSelectedItem());
            if (newLog.getWine() != null) {
                setSelected();
            }
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

        hoursComboBox.setOnAction(select -> newLog.setHour(hoursComboBox.getSelectionModel().getSelectedItem()));
        hoursComboBox.setConverter(logManager.getHourConverter());
        resetHoursComboBox();
    }

    private void resetHoursComboBox() {
        hoursComboBox.getItems().clear();
        logManager.setValidHours(newLog, hour -> hoursComboBox.getItems().add(hour));
        hoursComboBox.getSelectionModel().select(newLog.getHour());
    }

    private void setSelected() {
        Wine wine = newLog.getWine();
        selectedVBox.getChildren().remove(displayed);
        displayed = new Button(wine.getName());
        displayed.setPrefSize(260,240);
        displayed.setWrapText(true);
        GuiService.addImageGraphicToButton(displayed, "/images/" + wine.getColour() + "_wine_image.png", 100, 100, false, false);
        displayed.setOnAction(event -> FXWrapper.getInstance().loadIndividualWineViewPopup(wine));
        displayed.setContentDisplay(TOP);
        displayed.getStyleClass().add("nav-bar-button");
        displayed.setFont(new Font("System", 20));
        selectedVBox.getChildren().add(displayed);

        fullDisable(selectedButtonLabel, newLog.getWine() == null);

        runValidationSequence();
    }

    private void addStyleClasses() {
        popUpAnchorPane.getStyleClass().add("titled-pane");
        searchRectangle.getStyleClass().add("red-wine-rectangle");
        selectedWineRectangle.getStyleClass().add("red-wine-rectangle");
        searchComboBox.getStyleClass().add("fifteen-combo-box");
        hoursComboBox.getStyleClass().add("fifteen-combo-box");
        createPersonalButton.getStyleClass().add("nav-bar-button");
        glassesToggle.getStyleClass().add("toggle-switch");
        bottlesToggle.getStyleClass().add("toggle-switch");
        cancelLogButton.getStyleClass().add("nav-bar-button");
        finishLogButton.getStyleClass().add("nav-bar-button");
        datePicker.getStyleClass().add("date-picker");
        amountTextField.getStyleClass().add("sign-in-screen-text-field");
        logNoteTextArea.getStyleClass().add("sign-in-screen-text-field");
        deleteLogButton.getStyleClass().add("nav-bar-button");
    }

    @FXML
    void onCreatePersonalWineButtonClicked() {
        FXWrapper.getInstance().loadPersonalWinePopup(wine -> {
            newLog.setWine(wine);
            setSelected();
        });
    }
}
