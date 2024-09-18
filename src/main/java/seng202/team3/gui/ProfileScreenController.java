package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.ProfileScreenService;

/**
 * Controller for the profile_screen.fxml window
 *
 * @author Steven Leishman (sle159)
 */
public class ProfileScreenController {

    @FXML
    private Slider abvLimitSlider;

    @FXML
    private ComboBox<String> colourPreferenceComboBox;

    @FXML
    private Button editUsernameButton;

    @FXML
    private ComboBox<String> fullnessPreferenceComboBox;

    @FXML
    private Button newListButton;

    @FXML
    private Button savePreferencesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button removeListButton;

    @FXML
    private ListView<?> searchListView;

    @FXML
    private TextField usernameTextField;

    @FXML
    private ComboBox<String> varietyPreferenceComboBox;
    /**
     * The current instance of WineDrinkerManager
     */
    private WineDrinkerManager wineDrinkerManager;
    /**
     * Instance of the ProfileScreenServiceClass, used for data validation
     */
    private ProfileScreenService profileScreenService;

    /**
     * Method called by JavaFX when initialising the profile screen.
     */
    public void initialize() {
        editUsernameButton.setDisable(true);  // Functionality for deliverable three
        editUsernameButton.setOpacity(0);  // This is not currently fully implemented

        wineDrinkerManager = WineDrinkerManager.getInstance();
        profileScreenService = new ProfileScreenService();
        usernameTextField.setText(wineDrinkerManager.getCurrentUser().getUsername());
        colourPreferenceComboBox.getItems().addAll("Red", "White", "Rose");
        colourPreferenceComboBox.getSelectionModel().select(wineDrinkerManager.getCurrentUser().getColourPreference());
        fullnessPreferenceComboBox.getItems().addAll("Off Dry", "Dry", "Light", "Medium", "Full");
        fullnessPreferenceComboBox.getSelectionModel().select(wineDrinkerManager.getCurrentUser().getFullnessPreference());
        varietyPreferenceComboBox.getItems().addAll("Pinot Noir", "Chardonnay", "Sauvignon Blanc", "Cabernet Sauvignon",
                "Pinot Gris", "Malbec", "Shiraz", "Viognier", "Syrah", "Grenache", "Merlot", "Prosecco");
        varietyPreferenceComboBox.getSelectionModel().select(wineDrinkerManager.getCurrentUser().getGrapePreference());
        abvLimitSlider.setValue(wineDrinkerManager.getCurrentUser().getAbvLimit());
    }

    @FXML
    void onNewListButtonClicked() {

    }

    @FXML
    void onRemoveListButtonClicked() {

    }

    /**
     * Removes the current logged-in user and launches the sign-in screen
     */
    @FXML
    void onLogoutButtonClicked(){
        wineDrinkerManager.setCurrentUser(null);
        //TODO Save data?
        FXWrapper.getInstance().loadScreen(Screen.SIGNINSCREEN);
    }

    /**
     * Saves the user preferences from the screen into the database
     */
    @FXML
    void onSavePreferencesButtonClicked(){
        String colour = colourPreferenceComboBox.valueProperty().getValue();
        String fullness = fullnessPreferenceComboBox.valueProperty().getValue();
        String grapeVariety =  varietyPreferenceComboBox.valueProperty().getValue();
        double abvLimit = abvLimitSlider.getValue();
        profileScreenService.savePreferences(colour, fullness, grapeVariety, abvLimit);
    }
}
