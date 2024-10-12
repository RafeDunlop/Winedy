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
import seng202.team3.services.PersonalWinePopupService;
import seng202.team3.services.SearchService;
import seng202.team3.services.WineManager;

import java.util.List;

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

    Screen toReturnTo;

    final float DEFAULTPRICE = 0f;
    final int DEFAULTABV = 0;
    final int DEFAULTVOLUME = 750;
    final int DEFAULTYEAR = 0;

    public PersonalWinePopupController(Screen toReturnTo) {
        this.toReturnTo = toReturnTo;
    }

    public void initialize() {
        GuiService.setUpPopUp(overlayPane, popUpAnchorPane, () ->
            FXWrapper.getInstance().loadLogChangesPopup(() -> FXWrapper.getInstance().removePopUp(overlayPane))
        );
        addStyleClasses();
        setFormatters();
        setSearchableComboboxes();
    }


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

    private void setFormatters() {
        countryTextField.setTextFormatter(PersonalWinePopupService.getAlphabeticalFormatter());
        pricePerBottleTextField.setTextFormatter(PersonalWinePopupService.getFloatFormatter());
        abvTextField.setTextFormatter(PersonalWinePopupService.getFloatFormatter());
        volumeTextField.setTextFormatter(PersonalWinePopupService.getFloatFormatter());
        yearTextField.setTextFormatter(PersonalWinePopupService.getIntegerFormatter());
    }

    private void closethis() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        if (toReturnTo == Screen.ADDLOGPOPUP) {
            FXWrapper.getInstance().loadLogPopup(null, null, Screen.TRACKINGCONSUMPTIONSCREEN);
        } else {
            FXWrapper.getInstance().loadScreen(toReturnTo);
        }
    }

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
            service.validatePersonalWineColour();
            PersonalWineDAO personalWineDAO = new PersonalWineDAO();
            personalWineDAO.add(personalWine);
            closethis();
        }
    }

    private Wine createPersonalWine() {
        int uniqueWineID = WineManager.getInstance().getAllWines().getLast().getUniqueWineID() + 1;
        String[] grape = {grapeComboBox.getSelectionModel().getSelectedItem()};
        return new Wine(uniqueWineID, nameTextField.getText(), countryTextField.getText(), colourComboBox != null ? colourComboBox.getSelectionModel().getSelectedItem() : null,
                styleComboBox != null ? styleComboBox.getSelectionModel().getSelectedItem() : null, grapeComboBox != null ? grape : null,
                fullnessComboBox != null ? fullnessComboBox.getSelectionModel().getSelectedItem() : null, descriptionTextArea.getText(),
                pricePerBottleTextField.getText().isEmpty() ? DEFAULTPRICE : parseFloat(pricePerBottleTextField.getText()), null,
                abvTextField.getText().isEmpty() ? DEFAULTABV : parseFloat(abvTextField.getText()),
                volumeTextField.getText().isEmpty() ? DEFAULTVOLUME : parseFloat(volumeTextField.getText()),
                yearTextField.getText().isEmpty() ? DEFAULTYEAR : parseInt(yearTextField.getText()));
    }

    @FXML
    void cancelPersonalWine() {
        closethis();
    }

    private void addStyleClasses() {
        popUpAnchorPane.getStyleClass().add("titled-pane");
        cancelPersonalWineButton.getStyleClass().add("nav-bar-button");
        addPersonalWineButton.getStyleClass().add("nav-bar-button");
    }

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

    private void styleError(Label label, TextField textField, String message) {
        label.setStyle("-fx-text-fill: -fx-dark-red-wine-colour;");
        textField.setText("");
        textField.setStyle("-fx-background-color: -fx-dark-red-wine-colour; -fx-text-fill: white; -fx-prompt-text-fill: white");
        textField.promptTextProperty().set(message);
    }
}
