package seng202.team3.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.ProfileScreenService;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

/**
 * Controller for the profile_screen.fxml window
 * @author Steven Leishman (sle159)
 */

public class ProfileScreenController {
    private WineDrinkerManager wineDrinkerManager;
    private ProfileScreenService profileScreenService;

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
     * Changes textfield to an editable box and saves the username
     *
     */
    @FXML
    void onEditUsernameButtonClicked() {
        usernameTextField.setEditable(true);
        editUsernameButton.setText("Save New Username");
        editUsernameButton.setOnAction(e->{saveUsernameInfo();});
        usernameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                    saveUsernameInfo();
                }
            }
        });
    }

    private void saveUsernameInfo(){
        usernameTextField.setEditable(false);
        editUsernameButton.setText("Edit Username");
        editUsernameButton.setOnAction(e->{onEditUsernameButtonClicked();});
    }

    @FXML
    void onNewListButtonClicked() {

    }

    @FXML
    void onRemoveListButtonClicked() {

    }
    @FXML
    void onLogoutButtonClicked(){
        wineDrinkerManager.setCurrentUser(null);
        //TODO Save data?
        FXWrapper.getInstance().loadScreen(Screen.SIGNINSCREEN);
    }

    @FXML
    void onSavePreferencesButtonClicked(){
        String colour = colourPreferenceComboBox.valueProperty().getValue();
        String fullness = fullnessPreferenceComboBox.valueProperty().getValue();
        String grapeVariety =  varietyPreferenceComboBox.valueProperty().getValue();
        double abvLimit = abvLimitSlider.getValue();
        profileScreenService.savePreferences(colour, fullness, grapeVariety, abvLimit);
    }

    public void initialize() {
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
}
