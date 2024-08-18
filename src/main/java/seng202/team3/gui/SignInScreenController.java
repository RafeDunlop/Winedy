package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import seng202.team3.models.IllegalWineDrinkerException;

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
    private ComboBox<String> colourPreferenceComboBox;

    /*
    field for entering password
     */
    @FXML
    private PasswordField enterPasswordField;

    /*
    comboBox for selecting the fullness of your preferred wines
     */
    @FXML
    private ComboBox<String> fullnessPreferenceComboBox;

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
    private ComboBox<String> varietyPreferenceComboBox;

    @FXML
    private Label errorLabel;

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
    private void onCreateAccountButtonClicked() {
        try {
            String username = usernameTextField.getText(); //replace with method to check input against constraints
            String password = enterPasswordField.getText(); //""
            assertPasswordsMatch();
            String colour = getComboInput(colourPreferenceComboBox);
            String fullness = getComboInput(fullnessPreferenceComboBox);
            String variety = getComboInput(varietyPreferenceComboBox);
            int ABVLimit = (int) abvLimitSlider.getValue();

            FXWrapper.getInstance().loadScreen(Screen.PROFILESCREEN);

        } catch (IllegalWineDrinkerException e) {
            fullDisable(errorLabel, false);
            errorLabel.setText(e.getMessage());
        }
    }

    /*
     * method called when the login button is clicked
     *
     * determines whether inputted credentials reference a valid WineDrinker object, and if so, logs the user in.
     * Otherwise, prompts the user with the reason their login attempt failed (wrong password or no such username in DB)
     */
    @FXML
    void onLoginButtonClicked() {
        FXWrapper.getInstance().loadScreen(Screen.PROFILESCREEN);
    }

    /*
     * method called when the register/login Button is Clicked
     * not direct FXML button method because called upon initialization
     */
    private void toggleMode() {
        fullDisable(preferencesAnchorPane, !registerMode);
        toggleLabel.setText((registerMode) ? "Already have an account?" : "Don't have an account?");
        toggleSignInButton.setText((registerMode) ? "Sign in" : "Register");
        fullDisable(reEnterPasswordField, !registerMode);
        fullDisable(reEnterPasswordLabel, !registerMode);
        fullDisable(loginButton, registerMode);
        fullDisable(errorLabel, true);
        registerMode = !registerMode;
    }

    /*
     * helper function for toggleMode to disable and make invisible the component in one line
     * @param component Node object, fx component to disable
     * @param fullDisable whether to disable or enable the component
     */
    private void fullDisable(Node component, boolean fullDisable) {
        component.setDisable(fullDisable);
        component.setOpacity((fullDisable) ? 0 : 1);
    }

    /*
     * tries to acquire a combo box's selected item (currently String, may be changed)
     * otherwise throws a IllegalWineDrinker exception with the name of the combo box as the reason
     */
    private String getComboInput(ComboBox<String> comboBox) throws IllegalWineDrinkerException {
        try {
            return comboBox.getSelectionModel().getSelectedItem();
        } catch (NullPointerException e) {
            throw new IllegalWineDrinkerException("Please select a " + comboBox.getPromptText());
        }
    }

    /**
     * does nothing as long as the inputted passwords are matching
     * @throws IllegalWineDrinkerException thrown if the passwords do not match
     */
    private void assertPasswordsMatch() throws IllegalWineDrinkerException {
        if (!(enterPasswordField.getText().equals(reEnterPasswordField.getText()))) {
            throw new IllegalWineDrinkerException("Passwords do not match");
        }
    }

    /**
     * sets up combo-boxes, sets Button actions and sets the GUI to login mode
     * TODO: replace Strings of combobox with enum types
     * TODO: variety combobox is neither exhaustive nor can in be this long!
     */
    public void initialize() {
        toggleSignInButton.setOnAction(x -> toggleMode());
        toggleMode();
        colourPreferenceComboBox.getItems().addAll("Red", "White", "Rose");
        fullnessPreferenceComboBox.getItems().addAll("Off Dry", "Dry", "Light", "Medium", "Full");
        varietyPreferenceComboBox.getItems().addAll("Pinot Noir", "Chardonnay", "Sauvignon Blanc", "Cabernet Sauvignon",
                "Pinot Gris", "Malbec", "Shiraz", "Viognier", "Syrah", "Grenache", "Merlot", "Prosecco");
    }
}
