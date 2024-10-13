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

/**
 * complex controller which uses many service functions to handle Log alteration, updating, addition and deletion
 * hides illegal actions from the user strenuously and carefully styled
 *
 * @author Rafe Dunlop (rdu46)
 */
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

    /**
     * logManager singleton instance to use for services
     */
    private final LogManager logManager;

    /**
     * the WineLog to edit (null if not edit mode)
     */
    private final WineLog preExistingLog;

    /**
     * the Wine passed to log (null if none is pre-specified mode)
     */
    private final Wine preSelectedWine;

    /**
     * the button displayed in the selected section
     */
    private Button displayed;

    /**
     * boolean which keeps track of whether a log should be addable/savable
     */
    private boolean logValid;

    /**
     * boolean which keeps track of whether the amountTextField contains a valid parsable quantity
     */
    private boolean validAmount;

    /**
     * the LogDiff object corresponding to the state of the log upon popup start.
     * Set from the pre-existing log if relevant or with defaults
     */
    private LogDiff oldLog;

    /**
     * the LogDiff corresponding to the new changes, updated dynamically
     */
    private LogDiff newLog;

    /**
     * whether log should be updated or added to the database
     */
    private boolean editMode;

    /**
     * the screen from which this popup originated, passed on construction
     */
    private final Screen toReturnTo;

    /**
     * constructor which sets up final state variables
     * specify null to not use optional parameters
     * @param preExistingLog optional parameter if a log is being edited
     * @param preSelectedWine optional, the wine to select on startup.
     * @param toReturnTo Screen of origin
     */
    public LogPopupController(WineLog preExistingLog, Wine preSelectedWine, Screen toReturnTo) {
        this.preExistingLog = preExistingLog;
        this.preSelectedWine = preSelectedWine;
        this.logManager = LogManager.getInstance();
        this.toReturnTo = toReturnTo;
    }

    /**
     * sets up all components with helper functions and sets uo the popup
     * handles the state pattern implementation
     */
    public void initialize() {
        if (preExistingLog != null) {
            setLogParams();
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

        addStyleClasses();
        runValidationSequence();
    }

    /**
     * sets up the text area into which the log description is entered
     * validates dynamically
     */
    private void setupLogNoteTextArea() {
        logNoteTextArea.setWrapText(true);
        logNoteTextArea.setText(newLog.getNote());
        logNoteTextArea.textProperty().addListener((observable, oldNote, newNote) -> {
            newLog.setNote(newNote);
            runValidationSequence();
        });
    }

    /**
     * sets up the date picker preset to the current date if none is specified
     * opens when clicked anywhere (not just the tiny icon on the right)
     * sets a custom date cell factory to disable dates in the future
     * ensures that the hours displayed in the hours combo box are never in the future
     */
    private void setupDatePicker() {
        LocalDate currentDate = logManager.getCurrentDate().toLocalDate();
        datePicker.setEditable(false);
        datePicker.getEditor().setOnMouseClicked(event -> datePicker.show());
        datePicker.setValue(newLog.getDate().toLocalDate());
        datePicker.setOnAction(date -> {
            LocalDate selected = datePicker.getValue();
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

    /**
     * sets up the editing mode, creating the LogDiff object and specifies values to be set as component defaults
     */
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

    /**
     * sets up the default log diff object
     */
    private void setDefaultLogParams() {
        oldLog = new LogDiff().setAmt(0)
                .setIsBottles(false)
                .setDate(logManager.getCurrentDate())
                .setWine(null)
                .setHour(logManager.getCurrentTime().toLocalTime().getHour())
                .setNote("");
        newLog = new LogDiff(oldLog);
    }

    /**
     * sets up the key buttons, add log, cancel and delete log
     */
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
                    closeThis();
                }));
    }

    /**
     * closes the popup and returns to the correct screen
     */
    private void closeThis() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        if (toReturnTo == Screen.TRACKINGCONSUMPTIONSCREEN) {
            FXWrapper.getInstance().loadProfileTabPane(2);
        } else {
            FXWrapper.getInstance().loadScreen(toReturnTo);
        }
    }

    /**
     * sets up the text field to dynamically read from and validate the contents
     */
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

    /**
     * runs a series of services to ultimately decide whether to display an error message, and whether a log should
     * be savable or addable
     */
    private void runValidationSequence() {
        logValid = newLog.isValid();
        if (logValid) {
            fullDisable(qtyDisplayLabel, false);
            qtyDisplayLabel.setStyle("-fx-text-fill: Black; -fx-font-size: 15");
            qtyDisplayLabel.setWrapText(true);
            qtyDisplayLabel.setText(newLog.toString());
        }
        finishLogButton.setDisable((editMode) ? oldLog.equals(newLog) : !logValid);
    }

    /**
     * sets up the controlsfx toggle buttons so that only one may be on at a time, sets to adjust the prompt text
     */
    private void setupToggleButtons() {
        boolean isBottles = newLog.getIsBottles();
        glassesToggle.setSelected(!isBottles);
        glassesToggle.selectedProperty().addListener((observable, old, newVal) -> toggle(newVal));
        bottlesToggle.setSelected(isBottles);
        bottlesToggle.selectedProperty().addListener((observable, old, newVal) -> toggle(!newVal));
    }

    /**
     * toggles the switch states
     * @param toSet whether the final state is glasses --> toSet = !newLog.getIsBottles()
     */
    private void toggle(boolean toSet) {
        glassesToggle.setSelected(toSet);
        bottlesToggle.setSelected(!toSet);
        newLog.setIsBottles(!toSet);
        amountTextField.setPromptText(logManager.getAmtPromptText(!toSet));
        runValidationSequence();
    }

    /**
     * sets up the combo boxes for hour and wines
     * preloads the searchable combo box with all wines in the database
     * uses a custom cellFactory to ensure that text wraps lines
     * sets the String converter used for showing printable values from hour integers
     * dynamically updates newLog
     */
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

    /**
     * resets the combo box whenever the date changes,
     * reselecting the current hour
     * this can be used to select a time up to the end of the day (in the future) if the user is really keen
     */
    private void resetHoursComboBox() {
        hoursComboBox.getItems().clear();
        logManager.setValidHours(newLog, hour -> hoursComboBox.getItems().add(hour));
        hoursComboBox.getSelectionModel().select(newLog.getHour());
    }

    /**
     * sets the selected wine and sets uo the corresponding button
     */
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

    /**
     * adds all the  relevant style classes to the nodes
     */
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
        logNoteTextArea.getStyleClass().add("description-text-area");
        deleteLogButton.getStyleClass().add("nav-bar-button");
    }

    /**
     * loads the personal wine popup
     */
    @FXML
    void onCreatePersonalWineButtonClicked() {
        FXWrapper.getInstance().loadPersonalWinePopup(wine -> {
            newLog.setWine(wine);
            setSelected();
        });
    }
}
