package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.SignInScreenService;
import seng202.team3.exceptions.IllegalWineDrinkerException;

/**
 * controller for sign_in_screen.fxml. Handles logging in, registering and setting initial preferences
 *
 * @author Rafe Dunlop (rdu46), Steven Leishman (sle159)
 */
public class SignInScreenController {

    /**
     * Logger for logging successful screen loading and other important information
     */
    private static final Logger log = LogManager.getLogger(SignInScreenController.class);

    @FXML
    private Slider abvLimitSlider;

    @FXML
    private ComboBox<String> colourPreferenceComboBox;

    @FXML
    private PasswordField enterPasswordField;

    @FXML
    private ComboBox<String> fullnessPreferenceComboBox;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private Label toggleLabel;

    @FXML
    private Label signInTitleLabel;

    @FXML
    private AnchorPane preferencesAnchorPane;

    @FXML
    private PasswordField reEnterPasswordField;

    @FXML
    private Label reEnterPasswordLabel;

    @FXML
    private Button toggleSignInButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private ComboBox<String> varietyPreferenceComboBox;

    @FXML
    private Label errorLabel;

    @FXML
    private Rectangle preferencesRectangle;

    @FXML
    private Rectangle signInRectangle;

    @FXML
    private Rectangle colourPreferenceRectangle;

    @FXML
    private Rectangle varietyPreferenceRectangle;

    @FXML
    private Rectangle fullnessPreferenceRectangle;

    @FXML
    private Rectangle helpTextRectangle;

    @FXML
    private Rectangle preferencesTextRectangle;

    @FXML
    private Rectangle abvLimitRectangle;

    @FXML
    private Rectangle wineImageRectangle;

    @FXML
    private ImageView wineImageView;

    /**
     * An instance of a WineDrinkerManager class that manages database interactions of Wine Drinker objects
     */
    private static WineDrinkerManager wineDrinkerManager = WineDrinkerManager.getInstance();

    /**
     * state variable (state design pattern) to decide if the UI is in register mode (true) or login mode (false)
     */
    private boolean registerMode = false;

    /**
     * sets up combo-boxes, sets Button actions and sets the GUI to login mode
     * TODO: replace Strings of combobox with enum types
     * TODO: variety combobox is neither exhaustive nor can in be this long!
     */
    public void initialize() {
        toggleSignInButton.setOnAction(x -> toggleMode());
        toggleMode();
        colourPreferenceComboBox.getItems().addAll(null, "Red", "White", "Rose");
        fullnessPreferenceComboBox.getItems().addAll(null, "Off Dry", "Dry", "Light", "Medium", "Full");
        varietyPreferenceComboBox.getItems().addAll(null, "Pinot Noir", "Chardonnay", "Sauvignon Blanc", "Cabernet Sauvignon",
                "Pinot Gris", "Malbec", "Shiraz", "Viognier", "Syrah", "Grenache", "Merlot", "Prosecco");

        addStyleClasses();

        try {
            wineImageView.setImage(new Image("/images/signin_screen_wine.gif"));
        } catch (Exception e) {
            log.warn("Error loading wine gif", e);
        }

        log.info("Sign in screen initialised");
    }

    /**
     * Method called when the createAccountButton is clicked.
     * Determines whether the user has inputted a valid WineDrinker and if so, creates, stores it and logs it in.
     * Otherwise, prompts user with what input is invalid
     */
    @FXML
    private void onCreateAccountButtonClicked() {
        try {
            String username = usernameTextField.getText(); //replace with method to check input against constraints
            String password = enterPasswordField.getText();
            String secondPassword = reEnterPasswordField.getText();
            String colour = getComboInput(colourPreferenceComboBox);
            String fullness = getComboInput(fullnessPreferenceComboBox);
            String variety = getComboInput(varietyPreferenceComboBox);
            int ABVLimit = (int) abvLimitSlider.getValue();

            SignInScreenService.validateAndRegisterUser(username, password, secondPassword, null, colour, fullness, variety, ABVLimit);
            FXWrapper.getInstance().loadProfileTabPane(0);

        } catch (IllegalWineDrinkerException e) {
            fullDisable(errorLabel, false);
            errorLabel.setText(e.getMessage());
            errorLabel.setStyle("-fx-text-fill: -fx-dark-red-wine-colour; -fx-font-size: 20");
            errorLabel.setWrapText(true);
        }
    }

    /**
     * Method called when the login button is clicked.
     * Determines whether inputted credentials reference a valid WineDrinker object, and if so, logs the user in.
     * Otherwise, prompts the user with the reason their login attempt failed (wrong password or no such username in DB)
     */
    @FXML
    void onLoginButtonClicked() {
        try {
            String username = usernameTextField.getText();
            String password = enterPasswordField.getText();
            SignInScreenService.validateAndLoginUser(username, password);
            FXWrapper.getInstance().loadProfileTabPane(0);
        } catch (IllegalWineDrinkerException e) {
            fullDisable(errorLabel, false);
            errorLabel.setText(e.getMessage());
            errorLabel.setStyle("-fx-text-fill: -fx-dark-red-wine-colour; -fx-font-size: 20;");
            errorLabel.setWrapText(true);
        }
    }

    /**
     * Method called when the register/login Button is Clicked.
     * Not a direct FXML button method because called upon initialization
     */
    private void toggleMode() {
        fullDisable(preferencesAnchorPane, !registerMode);
        toggleLabel.setText((registerMode) ? "Already have an account?" : "Don't have an account?");
        toggleSignInButton.setText((registerMode) ? "Sign in" : "Register");
        signInTitleLabel.setText((registerMode) ? "Create An Account" : "Login To Account");
        errorLabel.setLayoutY((registerMode) ? 400 : 325);
        fullDisable(reEnterPasswordField, !registerMode);
        fullDisable(reEnterPasswordLabel, !registerMode);
        fullDisable(loginButton, registerMode);
        fullDisable(createAccountButton, !registerMode);
        fullDisable(errorLabel, true);
        fullDisable(wineImageView, registerMode);
        fullDisable(wineImageRectangle, registerMode);
        registerMode = !registerMode;
    }

    /**
     * Helper function for toggleMode to disable and make invisible the component in one line.

     * @param component Node object, fx component to disable
     * @param fullDisable whether to disable or enable the component
     */
    private void fullDisable(Node component, boolean fullDisable) {
        component.setDisable(fullDisable);
        component.setOpacity((fullDisable) ? 0 : 1);
    }

    /**
     * Tries to acquire a combo box's selected item (currently String, may be changed).
     * Otherwise throws a IllegalWineDrinker exception with the name of the combo box as the reason
     */
    private String getComboInput(ComboBox<String> comboBox) throws IllegalWineDrinkerException {
        try {
            return comboBox.getSelectionModel().getSelectedItem();
        } catch (NullPointerException e) {
            throw new IllegalWineDrinkerException("Please select a " + comboBox.getPromptText());
        }
    }

    /**
     * Adds style classes to the Rectangles, Buttons, ComboBoxes, TextField, and PasswordFields
     */
    private void addStyleClasses() {
        signInRectangle.getStyleClass().add("white-wine-rectangle");
        preferencesRectangle.getStyleClass().add("red-wine-rectangle");
        colourPreferenceRectangle.getStyleClass().add("white-red-wine-rectangle");
        varietyPreferenceRectangle.getStyleClass().add("white-red-wine-rectangle");
        fullnessPreferenceRectangle.getStyleClass().add("white-red-wine-rectangle");
        abvLimitRectangle.getStyleClass().add("white-red-wine-rectangle");
        helpTextRectangle.getStyleClass().add("white-red-wine-rectangle");
        preferencesTextRectangle.getStyleClass().add("white-red-wine-rectangle");
        wineImageRectangle.getStyleClass().add("white-red-wine-rectangle");

        createAccountButton.getStyleClass().add("nav-bar-button");
        loginButton.getStyleClass().add("nav-bar-button");
        toggleSignInButton.getStyleClass().add("nav-bar-button");

        colourPreferenceComboBox.getStyleClass().add("fifteen-combo-box");
        varietyPreferenceComboBox.getStyleClass().add("fifteen-combo-box");
        fullnessPreferenceComboBox.getStyleClass().add("fifteen-combo-box");

        usernameTextField.getStyleClass().add("sign-in-screen-text-field");
        enterPasswordField.getStyleClass().add("sign-in-screen-password-field");
        reEnterPasswordField.getStyleClass().add("sign-in-screen-password-field");
    }
}
