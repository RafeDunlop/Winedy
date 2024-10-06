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
    private ComboBox<String> fullnessPreferenceComboBox;

    @FXML
    private ComboBox<String> varietyPreferenceComboBox;
    /**
     * Instance of the ProfileScreenServiceClass, used for data validation
     */
    private ProfileScreenService profileScreenService;

    /**
     * Method called by JavaFX when initialising the profile screen.
     */
    public void initialize() {
        WineDrinkerManager wineDrinkerManager = WineDrinkerManager.getInstance();
        profileScreenService = new ProfileScreenService();
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
