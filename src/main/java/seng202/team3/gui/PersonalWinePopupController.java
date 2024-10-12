package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

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
    private TextField volumeTextField;

    @FXML
    void submitPersonalWine() {

    }

    @FXML
    void cancelPersonalWine() {

    }
}
