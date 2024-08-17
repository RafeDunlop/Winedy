package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * controller for sign_in_screen.fxml. Handles logging in, registering and setting initial preferences
 *
 * @author Rafe Dunlop (rdu46)
 */
public class SignInScreenController {

    /*
     *slider to select maximum Alcohol By Volume user's would like to see appear
     */
    @FXML
    private Slider abvLimitSlider;

     /*
      *combobox for selecting red/white
      */
    @FXML
    private ComboBox<?> colourPreferenceComboBox;

    /*
    button for creating account. Hidden when in login mode
     */
    @FXML
    private Button createAccountButton;

    /*
    field for entering password
     */
    @FXML
    private PasswordField enterPasswordField;

    /*
    comboBox for selecting the fullness of your preferred wines
     */
    @FXML
    private ComboBox<?> fullnessPreferenceComboBox;

    /*
    button clicked upon logging in
     */
    @FXML
    private Button loginButton;

    /*
    label for explaining register/login (toggle) button
     */
    @FXML
    private Label toggleLabel;

    /*
    anchorPane for all preference-related FX components (to disable/enable in tandem)
     */
    @FXML
    private AnchorPane preferencesAnchorPane;

    /*
    TextField for re-entering password in register mode
     */
    @FXML
    private PasswordField reEnterPasswordField;

    /*
    Label for explaining reEnterPasswordField. Stored to disable in login mode
     */
    @FXML
    private Label reEnterPasswordLabel;

    /*
    Button for switching between register and login mode
     */
    @FXML
    private Button toggleSignInButton;

    /*
    TextField for enterring user's username
     */
    @FXML
    private TextField usernameTextField;

    /*
    ComboBox for variety selection
     */
    @FXML
    private ComboBox<?> varietyPreferenceComboBox;

    /*
    state variable (state design pattern) to decide if the UI is in register mode (true) or login mode (false)
     */
    private boolean registerMode = false;

    /*
     * method called when the createAccountButton is clicked
     *
     * determines whether the user has inputted a valid WineDrinker and if so, creates, stores it and logs it in.
     * Otherwise, prompts user with what input is invalid
     */
    @FXML
    private void onCreateAccountButtonClicked(ActionEvent event) {

    }

    /*
     * method called when the login button is clicked
     *
     * determines whether inputted credentials reference a valid WineDrinker object, and if so, logs the user in.
     * Otherwise, prompts the user with the reason their login attempt failed (wrong password or no such username in DB)
     */
    @FXML
    void onLoginButtonClicked(ActionEvent event) {

    }

    /*
     * method called when the register/login Button is Clicked
     */
    @FXML
    void onToggleSignInButtonClicked(ActionEvent event) {

    }

    /**
     * sets up combo-boxes, sets Button actions and sets the GUI to login mode
     */
    public void initialize() {

    }

}
