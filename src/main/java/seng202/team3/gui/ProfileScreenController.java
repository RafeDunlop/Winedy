package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import seng202.team3.models.Wine;
import seng202.team3.services.RecommendationManager;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.ProfileScreenService;

import java.util.ArrayList;
import java.util.List;

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

    //*******************************************************MOCK Recommendation
    @FXML
    private Button mockRecommendButton;
    @FXML
    private Label mockRecWineLabel;
    @FXML
    private Button mockSwipeLeft;
    @FXML
    private Button mockSwipeRight;
    private int recommendedWineIndex = 0;
    private List<Wine> recommendedWines = new ArrayList<>();
    private List<Float> wineMatchPercentages = new ArrayList<>();

    //******************************************************MOCK ENDS
    @FXML
    private Button savePreferencesButton;

    @FXML
    private ListView<?> searchListView;

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
        /**
         * The current instance of WineDrinkerManager
         */
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
    private void onMockButtonClicked() {
        recommendedWineIndex = 0;
        recommendedWines = new ArrayList<>();
        wineMatchPercentages = new ArrayList<>();
        RecommendationManager.getInstance().recommendWines(recommendedWines, wineMatchPercentages);
        recommendNextWineToUser();

    }

    private void recommendNextWineToUser(){
        mockRecWineLabel.setText(recommendedWines.get(recommendedWineIndex).getLongDescription()
             + "\n This wine matches your preferences " + wineMatchPercentages.get(recommendedWineIndex) + "%");
    }
    @FXML
    void onMockSwipeLeftClicked(){
        RecommendationManager.getInstance().
            updatePreferenceModelAfterUserSelection(recommendedWines.get(recommendedWineIndex), false);
        recommendedWineIndex++;
        if (recommendedWineIndex < 5) {
            recommendNextWineToUser();
        } else {
            //TODO EXIT recommendation
            System.out.println("Need to exit now");
        }
    }
    @FXML
    void onMockSwipeRightClicked(){
        RecommendationManager.getInstance().
                updatePreferenceModelAfterUserSelection(recommendedWines.get(recommendedWineIndex), true);
        recommendedWineIndex++;
        if (recommendedWineIndex < 5) {
            recommendNextWineToUser();
        } else {
            //TODO EXIT recommendation
            System.out.println("Need to exit now");
        }
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
