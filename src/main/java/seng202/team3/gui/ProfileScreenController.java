package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import seng202.team3.models.Wine;
import seng202.team3.models.WineAttribute;
import seng202.team3.repository.Table;
import seng202.team3.services.RecommendationManager;
import seng202.team3.services.SearchService;
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
    private ComboBox<String> fullnessPreferenceComboBox;

    @FXML
    private ComboBox<String> varietyPreferenceComboBox;

    @FXML
    private Rectangle profilePreferenceRectangle;

    @FXML
    private Rectangle profileColourPreferenceRectangle;

    @FXML
    private Rectangle profileVarietyPreferenceRectangle;

    @FXML
    private Rectangle profileFullnessPreferenceRectangle;

    @FXML
    private Rectangle profilePreferenceTextRectangle;

    @FXML
    private Rectangle profileAbvLimitRectangle;
    @FXML
    private Button savePreferencesButton;

    //******************************************************* Recommendation
    @FXML
    private Button beginRecommendationButton;
    @FXML
    private Label wineMatchLabel;
    @FXML
    private Button swipeRecommendationLeftButton;
    @FXML
    private Button swipeRecommedationRightButton;
    @FXML
    private Button beginNewRecommendationButton;
    @FXML
    private AnchorPane recommendStep1Pane;
    @FXML
    private AnchorPane recommendStep2Pane;
    @FXML
    private AnchorPane recommendStep3Pane;
    @FXML
    private VBox recommendedWinesVBox;
    @FXML
    private AnchorPane wineViewPane;
    @FXML
    private Rectangle recommendationRectangle;
    private int recommendedWineIndex = 0;
    private List<Wine> recommendedWines = new ArrayList<>();
    private ArrayList<Wine> userSelectedRecommendWines = new ArrayList<>();
    private List<Float> wineMatchPercentages = new ArrayList<>();

    //****************************************************** ENDS

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
        colourPreferenceComboBox.getItems().addAll(SearchService.getAttributeValues(WineAttribute.COLOUR, Table.WINESUPER));
        colourPreferenceComboBox.getItems().removeFirst();
        colourPreferenceComboBox.getSelectionModel().select(wineDrinkerManager.getCurrentUser().getColourPreference());
        fullnessPreferenceComboBox.getItems().addAll(SearchService.getAttributeValues(WineAttribute.FULLNESS, Table.WINESUPER));
        fullnessPreferenceComboBox.getItems().removeFirst();
        fullnessPreferenceComboBox.getSelectionModel().select(wineDrinkerManager.getCurrentUser().getFullnessPreference());
        varietyPreferenceComboBox.getItems().addAll(SearchService.getAttributeValues(WineAttribute.VARIETY, Table.GRAPE));
        varietyPreferenceComboBox.getItems().removeFirst();
        varietyPreferenceComboBox.getSelectionModel().select(wineDrinkerManager.getCurrentUser().getGrapePreference());
        abvLimitSlider.setValue(wineDrinkerManager.getCurrentUser().getAbvLimit());
        GuiService.turnOnPane(recommendStep1Pane);
        addStyleClasses();

    }

    /**
     * Called when the recommendation button is clicked
     * Shows and hides the correct panes and begins the
     * recommendation
     */
    @FXML
    private void onBeginRecommendationClicked() {
        GuiService.turnOffPane(recommendStep1Pane);
        GuiService.turnOffPane(recommendStep3Pane);
        GuiService.turnOnPane(recommendStep2Pane);

        recommendedWineIndex = 0;
        recommendedWines = new ArrayList<>();
        wineMatchPercentages = new ArrayList<>();
        userSelectedRecommendWines = new ArrayList<>();
        RecommendationManager.getInstance().recommendWines(recommendedWines, wineMatchPercentages);
        recommendNextWineToUser();

    }

    /**
     * helper function to show wine info
     * TODO will show individual wine view rather than text
     */
    private void recommendNextWineToUser(){
        FXWrapper.getInstance().loadMiniIndividualWineView(wineViewPane,recommendedWines.get(recommendedWineIndex));
        wineMatchLabel.setText("This wine matches your preferences " + wineMatchPercentages.get(recommendedWineIndex) + "%");
    }

    /**
     * Called by FXML swipe left
     * moves the recommendation along 1 point and updates preference model accordingly
     */
    @FXML
    void onSwipeLeftClicked(){
        RecommendationManager.getInstance().
            updatePreferenceModelAfterUserSelection(recommendedWines.get(recommendedWineIndex), false);
        recommendedWineIndex++;
        if (recommendedWineIndex < 5) {
            recommendNextWineToUser();
        } else {
            endRecommendationReturnWines();
        }
    }

    /**
     * Called by FXML swipe left
     * moves the recommendation along 1 point and updates preference model accordingly
     */
    @FXML
    void onSwipeRightClicked(){
        RecommendationManager.getInstance().
                updatePreferenceModelAfterUserSelection(recommendedWines.get(recommendedWineIndex), true);
        userSelectedRecommendWines.add(recommendedWines.get(recommendedWineIndex));
        recommendedWineIndex++;
        if (recommendedWineIndex < 5) {
            recommendNextWineToUser();
        } else {
            endRecommendationReturnWines();

        }
    }

    /**
     * Ends the recommendation and shows the result set of buttons that the user can
     * interact with
     */
    private void endRecommendationReturnWines(){
        GuiService.turnOnPane(recommendStep3Pane);
        GuiService.turnOffPane(recommendStep2Pane);
        recommendedWinesVBox.getChildren().clear();
        GuiService.startButtonGeneration(userSelectedRecommendWines, recommendedWinesVBox, null, 3);
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

    /**
     * Adds the style classes to the elements on the main profile tab
     */
    private void addStyleClasses() {
        profilePreferenceRectangle.getStyleClass().add("red-wine-rectangle");
        profileColourPreferenceRectangle.getStyleClass().add("white-red-wine-rectangle");
        profileVarietyPreferenceRectangle.getStyleClass().add("white-red-wine-rectangle");
        profileFullnessPreferenceRectangle.getStyleClass().add("white-red-wine-rectangle");
        profileAbvLimitRectangle.getStyleClass().add("white-red-wine-rectangle");
        profilePreferenceTextRectangle.getStyleClass().add("white-red-wine-rectangle");
        recommendationRectangle.getStyleClass().add("white-wine-rectangle");

        colourPreferenceComboBox.getStyleClass().add("fifteen-combo-box");
        varietyPreferenceComboBox.getStyleClass().add("fifteen-combo-box");
        fullnessPreferenceComboBox.getStyleClass().add("fifteen-combo-box");

        beginRecommendationButton.getStyleClass().add("nav-bar-button");
        beginNewRecommendationButton.getStyleClass().add("nav-bar-button");
        swipeRecommedationRightButton.getStyleClass().add("nav-bar-button");
        swipeRecommendationLeftButton.getStyleClass().add("nav-bar-button");
        savePreferencesButton.getStyleClass().add("nav-bar-button");
    }
}
