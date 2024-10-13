package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.SearchableComboBox;
import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.models.Wine;
import seng202.team3.models.WineAttribute;
import seng202.team3.repository.PersonalWineDAO;
import seng202.team3.repository.Table;
import seng202.team3.repository.WineDAO;
import seng202.team3.services.PersonalWinePopupService;
import seng202.team3.services.SearchService;
import seng202.team3.services.WineManager;

import java.util.List;
import java.util.function.Consumer;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

/**
 * Controller class for the add_personal_wine_popup.fxml
 * @author Krishna Sridhar (nsr36)
 */
public class PersonalWinePopupController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label abvLabel;

    @FXML
    private Label volumeLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private TextField abvTextField;

    @FXML
    private Button addPersonalWineButton;

    @FXML
    private Button cancelPersonalWineButton;

    @FXML
    private SearchableComboBox<String> colourComboBox;

    @FXML
    private TextField countryTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private SearchableComboBox<String> fullnessComboBox;

    @FXML
    private SearchableComboBox<String> grapeComboBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    @FXML
    private TextField pricePerBottleTextField;

    @FXML
    private SearchableComboBox<String> styleComboBox;

    @FXML
    private TextField volumeTextField;

    @FXML
    private TextField yearTextField;
    
    private Consumer<Wine> onWineCreated;

    final float DEFAULTPRICE = 0f;
    final int DEFAULTABV = 14;
    final int DEFAULTVOLUME = 750;
    final int DEFAULTYEAR = 0;

    /**
     * Constructor for the PersonalWinePopupController
     * @param onWineCreated consumer to carry personal wine
     */
    public PersonalWinePopupController(Consumer<Wine> onWineCreated) {
        this.onWineCreated = onWineCreated;
    }

    /**
     * Initialise popup, add styling, text field formatters and searchable comboboxes
     */
    public void initialize() {
        GuiService.setUpPopUp(overlayPane, popUpAnchorPane, () ->
            FXWrapper.getInstance().loadLogChangesPopup(() -> FXWrapper.getInstance().removePopUp(overlayPane))
        );
        addStyleClasses();
        setFormatters();
        setSearchableComboboxes();
    }

    /**
     * Populate the searchable comboboxes from the database using the SearchService
     */
    private void setSearchableComboboxes() {
        List<String> colours = SearchService.getAttributeValues(WineAttribute.COLOUR, Table.WINESUPER);
        colours.removeFirst();
        colourComboBox.getItems().addAll(colours);
        List<String> styles = SearchService.getAttributeValues(WineAttribute.STYLE, Table.WINESUPER);
        styles.removeFirst();
        styleComboBox.getItems().addAll(styles);
        List<String> grapes = SearchService.getAttributeValues(WineAttribute.VARIETY, Table.GRAPE);
        grapes.removeFirst();
        grapeComboBox.getItems().addAll(grapes);
        List<String> fullness = SearchService.getAttributeValues(WineAttribute.FULLNESS, Table.WINESUPER);
        fullness.removeFirst();
        fullnessComboBox.getItems().addAll(fullness);
    }

    /**
     * Set formatters for all the text fields depending on what type entry is required
     */
    private void setFormatters() {
        countryTextField.setTextFormatter(PersonalWinePopupService.getAlphabeticalFormatter());
        pricePerBottleTextField.setTextFormatter(PersonalWinePopupService.getFloatFormatter());
        abvTextField.setTextFormatter(PersonalWinePopupService.getFloatFormatter());
        volumeTextField.setTextFormatter(PersonalWinePopupService.getFloatFormatter());
        yearTextField.setTextFormatter(PersonalWinePopupService.getIntegerFormatter());
    }

    /**
     * Close personal wine popup
     * @param personalWine final personal wine after validation
     */
    private void closethis(Wine personalWine) {
        FXWrapper.getInstance().removePopUp(overlayPane);
        if (personalWine != null) {
            onWineCreated.accept(personalWine);
        }
    }

    /**
     * Validate personal wine entry using PersonalWinePopupService to check text fields
     * Add personal wine to database if valid
     * @throws WineDrinkerAlreadyExistsException exception thrown by the add method in PersonalWineDAO
     */
    @FXML
    void addPersonalWine() throws WineDrinkerAlreadyExistsException {
        Wine personalWine = createPersonalWine();
        PersonalWinePopupService service = new PersonalWinePopupService(personalWine);
        resetStyleErrors();
        if (!service.validatePersonalWineName()) {
            styleError(nameLabel, nameTextField, "Please enter the wine name");
        }
        else if (!service.validatePersonalWineABV()) {
            styleError(abvLabel, abvTextField, "Please enter a valid ABV");
        }
        else if (!service.validatePersonalWineVolume()) {
            styleError(volumeLabel, volumeTextField, "Please enter a valid volume");
        }
        else if (!service.validatePersonalWineYear()) {
            styleError(yearLabel, yearTextField, "Please enter a valid year");
        }
        else {
            service.validatePersonalWineColourStyleFullness();
            PersonalWineDAO personalWineDAO = new PersonalWineDAO();
            personalWineDAO.add(service.getPersonalWine());
            closethis(personalWine);
        }
    }

    /**
     * Construct a wine object using the fxml fields
     * @return personal wine represented as a wine object
     */
    private Wine createPersonalWine() {
        WineDAO wineDAO = new WineDAO();
        int uniqueWineID = wineDAO.getLastID() + 1;
        String[] grape = {grapeComboBox.getSelectionModel().getSelectedItem()};
        return new Wine(uniqueWineID, nameTextField.getText(), countryTextField.getText(), colourComboBox != null ? colourComboBox.getSelectionModel().getSelectedItem() : null,
                styleComboBox != null ? styleComboBox.getSelectionModel().getSelectedItem() : null, grapeComboBox != null ? grape : null,
                fullnessComboBox != null ? fullnessComboBox.getSelectionModel().getSelectedItem() : null, descriptionTextArea.getText(),
                pricePerBottleTextField.getText().isEmpty() ? DEFAULTPRICE : parseFloat(pricePerBottleTextField.getText()), null,
                abvTextField.getText().isEmpty() ? DEFAULTABV : parseFloat(abvTextField.getText()),
                volumeTextField.getText().isEmpty() ? DEFAULTVOLUME : parseFloat(volumeTextField.getText()),
                yearTextField.getText().isEmpty() ? DEFAULTYEAR : parseInt(yearTextField.getText()));
    }

    /**
     * Cancel a personal wine entry
     */
    @FXML
    void cancelPersonalWine() {
        closethis(null);
    }

    /**
     * Get style classes needed for styling the personal wine popup
     */
    private void addStyleClasses() {
        popUpAnchorPane.getStyleClass().add("titled-pane");
        cancelPersonalWineButton.getStyleClass().add("nav-bar-button");
        addPersonalWineButton.getStyleClass().add("nav-bar-button");
        nameTextField.getStyleClass().add("sign-in-screen-text-field");
        countryTextField.getStyleClass().add("sign-in-screen-text-field");
        pricePerBottleTextField.getStyleClass().add("sign-in-screen-text-field");
        abvTextField.getStyleClass().add("sign-in-screen-text-field");
        volumeTextField.getStyleClass().add("sign-in-screen-text-field");
        yearTextField.getStyleClass().add("sign-in-screen-text-field");
        descriptionTextArea.getStyleClass().add("description-text-area");
        colourComboBox.getStyleClass().add("fifteen-combo-box");
        fullnessComboBox.getStyleClass().add("fifteen-combo-box");
        grapeComboBox.getStyleClass().add("fifteen-combo-box");
        styleComboBox.getStyleClass().add("fifteen-combo-box");

    }

    /**
     * Reset the style of all text field so only the latest error is indicated
     */
    private void resetStyleErrors() {
        nameLabel.setStyle("");
        nameTextField.setStyle("");
        abvLabel.setStyle("");
        abvTextField.setStyle("");
        volumeLabel.setStyle("");
        volumeTextField.setStyle("");
        yearLabel.setStyle("");
        yearTextField.setStyle("");
    }

    /**
     * Styling for an error in a text field entry
     * @param label the label of the attribute with an error
     * @param textField the text field of the attribute with an error
     * @param message the message to display for the attribute with an error
     */
    private void styleError(Label label, TextField textField, String message) {
        label.setStyle("-fx-text-fill: -fx-dark-red-wine-colour;");
        textField.setText("");
        textField.setStyle("-fx-background-color: -fx-dark-red-wine-colour; -fx-text-fill: white; -fx-prompt-text-fill: white");
        textField.promptTextProperty().set(message);
    }
}
