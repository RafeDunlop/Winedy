package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.SearchableComboBox;
import org.w3c.dom.Text;
import seng202.team3.models.Wine;
import seng202.team3.models.WineLog;
import seng202.team3.services.LogManager;
import seng202.team3.services.PersonalWinePopupService;

import java.util.List;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

/**
 * Controller class for the add_personal_wine_popup.fxml
 * @author Krishna Sridhar (nsr36)
 */
public class PersonalWinePopupController {
    @FXML
    private TextField abvTextField;

    @FXML
    private Button addPersonalWineButton;

    @FXML
    private Button cancelPersonalWineButton;

    @FXML
    private TextField colourTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField fullnessTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    @FXML
    private TextField pricePerBottleTextField;

    @FXML
    private TextField styleTextField;

    @FXML
    private SearchableComboBox<Wine> grapeComboBox;

    @FXML
    private TextField volumeTextField;

    @FXML
    private TextField yearTextField;

    Screen toReturnTo;

    public PersonalWinePopupController(Screen toReturnTo) {
        this.toReturnTo = toReturnTo;
    }

    public void initialize() {
        GuiService.setUpPopUp(overlayPane, popUpAnchorPane, () -> {
            FXWrapper.getInstance().loadLogChangesPopup(() -> FXWrapper.getInstance().removePopUp(overlayPane));
        });
        addStyleClasses();
    }

    private void closeThis() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        if (toReturnTo == Screen.ADDLOGPOPUP) {
            FXWrapper.getInstance().loadLogPopup(null, null, Screen.TRACKINGCONSUMPTIONSCREEN);
        } else {
            FXWrapper.getInstance().loadScreen(toReturnTo);
        }
    }

    @FXML
    void addPersonalWine() {
        Wine personalWine = new Wine(0, nameTextField.getText(), countryTextField.getText(), colourTextField.getText(), styleTextField.getText(),
                (String[]) grapeComboBox.getItems().toArray(), fullnessTextField.getText(), descriptionTextArea.getText(), parseFloat(pricePerBottleTextField.getText()),
                null, parseInt(abvTextField.getText()), parseInt(volumeTextField.getText()), parseInt(yearTextField.getText()));
        PersonalWinePopupService service = new PersonalWinePopupService();
        if (!service.validatePersonalWineName(personalWine)) {

        }
    }

    @FXML
    void cancelPersonalWine() {
        closeThis();
    }

    private void addStyleClasses() {
        popUpAnchorPane.getStyleClass().add("titled-pane");
        cancelPersonalWineButton.getStyleClass().add("nav-bar-button");
        addPersonalWineButton.getStyleClass().add("nav-bar-button");
    }
}
