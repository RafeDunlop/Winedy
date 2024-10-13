package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.fxml.LoadException;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.Wine;
import seng202.team3.models.WineAttribute;
import seng202.team3.repository.SearchDAO;
import seng202.team3.repository.Table;
import seng202.team3.repository.WineDAO;
import seng202.team3.services.RecommendationManager;
import seng202.team3.services.SearchService;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.ProfileScreenService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Controller for the profile_screen.fxml window
 *
 * @author Steven Leishman (sle159)
 */
public class ProfileScreenController {

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(ProfileScreenService.class);

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
    private Rectangle noSelectedWinesRectangle;

    @FXML
    private Button savePreferencesButton;

    @FXML
    private Button beginRecommendationButton;

    @FXML
    private Label wineMatchLabel;

    @FXML
    private Label noSelectedWinesLabel;

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

    /**
     * Wines that have been recommended to the Wine Drinker
     */
    private List<Wine> recommendedWines = new ArrayList<>();

    /**
     * Recommended wines that have been selected by the Wine Drinker
     */
    private ArrayList<Wine> userSelectedRecommendWines = new ArrayList<>();

    /**
     * List of all the wine match percentages
     */
    private List<Float> wineMatchPercentages = new ArrayList<>();

    /**
     * Instance of the ProfileScreenServiceClass, used for data validation
     */
    private ProfileScreenService profileScreenService;

    /**
     * The selected colour preference to be saved
     */
    private String selectedColour;

    /**
     * The selected variety preference to be saved
     */
    private String selectedVariety;

    /**
     * The selected fullness preference to be saved
     */
    private String selectedFullness;

    /**
     * Method called by JavaFX when initialising the profile screen.
     */
    public void initialize() {
        WineDrinkerManager wineDrinkerManager = WineDrinkerManager.getInstance();
        profileScreenService = new ProfileScreenService();

        setUpPreferenceComboBoxes();

        abvLimitSlider.setValue(wineDrinkerManager.getCurrentUser().getAbvLimit());
        abvLimitSlider.setMax(ProfileScreenService.getBoundaryAttributeValue(WineAttribute.ABV, Table.WINESUPER, "max"));
        abvLimitSlider.setMin(ProfileScreenService.getBoundaryAttributeValue(WineAttribute.ABV, Table.WINESUPER, "min"));

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

        noSelectedWinesRectangle.setVisible(false);
        noSelectedWinesLabel.setVisible(false);

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
        if (recommendedWineIndex < 4) {
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
        if (recommendedWineIndex < 4) {
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
        if (userSelectedRecommendWines.isEmpty()) {
            noSelectedWinesLabel.setVisible(true);
            noSelectedWinesRectangle.setVisible(true);
        }
        Consumer<Wine> onAction = wine -> FXWrapper.getInstance().loadIndividualWineViewPopupWithButtons(wine);
        GuiService.startButtonGeneration(userSelectedRecommendWines, recommendedWinesVBox,
                onAction, 2);
    }

    /**
     * Saves the user preferences from the screen into the database
     */
    @FXML
    void onSavePreferencesButtonClicked(){
        double abvLimit = abvLimitSlider.getValue();
        selectedColour = (Objects.equals(selectedColour, "All")) ? null : selectedColour;
        selectedVariety = (Objects.equals(selectedVariety, "All")) ? null : selectedVariety;
        selectedFullness = (Objects.equals(selectedFullness, "All")) ? null : selectedFullness;
        profileScreenService.savePreferences(selectedColour, selectedFullness, selectedVariety, abvLimit);
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
        noSelectedWinesRectangle.getStyleClass().add("white-white-wine-rectangle");

        colourPreferenceComboBox.getStyleClass().add("fifteen-combo-box");
        varietyPreferenceComboBox.getStyleClass().add("fifteen-combo-box");
        fullnessPreferenceComboBox.getStyleClass().add("fifteen-combo-box");

        beginRecommendationButton.getStyleClass().add("nav-bar-button");
        beginNewRecommendationButton.getStyleClass().add("nav-bar-button");
        swipeRecommedationRightButton.getStyleClass().add("nav-bar-button");
        swipeRecommendationLeftButton.getStyleClass().add("nav-bar-button");
        savePreferencesButton.getStyleClass().add("nav-bar-button");
    }

    /**
     * Sets up all the preference combo boxes to have the correct on action, initial selected value, and possible
     * selection values
     */
    private void setUpPreferenceComboBoxes() {
        selectedColour = WineDrinkerManager.getInstance().getCurrentUser().getColourPreference();
        colourPreferenceComboBox.getItems().addAll(ProfileScreenService.getAttributeValues(WineAttribute.COLOUR, Table.WINESUPER));
        colourPreferenceComboBox.setValue((selectedColour == null) ? "All" : selectedColour);

        selectedFullness = WineDrinkerManager.getInstance().getCurrentUser().getFullnessPreference();
        fullnessPreferenceComboBox.getItems().addAll(ProfileScreenService.getAttributeValues(WineAttribute.FULLNESS, Table.WINESUPER));
        fullnessPreferenceComboBox.setValue((selectedFullness == null) ? "All" : selectedFullness);

        selectedVariety = WineDrinkerManager.getInstance().getCurrentUser().getGrapePreference();
        varietyPreferenceComboBox.getItems().addAll(ProfileScreenService.getAttributeValues(WineAttribute.VARIETY, Table.GRAPE));
        varietyPreferenceComboBox.setValue((selectedVariety == null) ? "All" : selectedVariety);

        colourPreferenceComboBox.setOnAction(select -> selectedColour = ((colourPreferenceComboBox.getSelectionModel()
                .getSelectedItem().isEmpty()) ? null : colourPreferenceComboBox.getSelectionModel().getSelectedItem()));
        fullnessPreferenceComboBox.setOnAction(select -> selectedFullness = (fullnessPreferenceComboBox.getSelectionModel()
                .getSelectedItem().isEmpty()) ? null : fullnessPreferenceComboBox.getSelectionModel().getSelectedItem());
        varietyPreferenceComboBox.setOnAction(select -> selectedVariety = (varietyPreferenceComboBox.getSelectionModel()
                .getSelectedItem().isEmpty()) ? null : varietyPreferenceComboBox.getSelectionModel().getSelectedItem());
    }
}
