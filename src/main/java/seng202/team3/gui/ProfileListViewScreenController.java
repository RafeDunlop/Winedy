package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import seng202.team3.models.FavouritesWineList;
import seng202.team3.models.UserWineList;
import seng202.team3.services.ProfileScreenService;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;

/**
 * Controller for the profile_list_view_screen.fxml window
 * Displays the contents of the users lists
 *
 * @author Sophia Copley (sco207)
 */
public class ProfileListViewScreenController {


    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField wineListNameTextField;

    @FXML
    private Label wineListNameLabel;

    @FXML
    private VBox listContentsVBox;

    @FXML
    private ScrollPane listContentsScrollPane;

    @FXML
    private Button backButton;

    @FXML
    private Button renameButton;

    @FXML
    private Button saveListChangesButton;

    @FXML
    private Button editListButton;

    @FXML
    private Button cancelListChangesButton;

    @FXML
    private Button cancelDescChangesButton;

    @FXML
    private Button editDescriptionButton;

    @FXML
    private Button saveDescChangesButton;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label errorLabel;

    @FXML
    private Label descErrorLabel;

    @FXML
    private Rectangle winesRectangle;

    @FXML
    private Rectangle descriptionRectangle;

    @FXML
    private ScrollPane descriptionScrollPane;

    /**
     * Wine drinker manager to handle wine drinker related actions
     */
    private WineDrinkerManager wineDrinkerManager;

    /**
     * Wine list manager to handle wine list related actions
     */
    private WineListManager wineListManager;

    /**
     * List that is being displayed on the screen
     */
    private UserWineList listToDisplay;

    /**
     * Profile screen service instance to deal with list name validation
     */
    private ProfileScreenService profileScreenService;

    /**
     * Character limit for a list name
     */
    private final int listNameCharLimit = 30;

    /**
     * Character limit for a list description
     */
    private final int descCharLimit = 500;

    /**
     * Constructor for the profileListView controller
     * @param listToDisplay list to be displayed in the individual list view
     */
    public ProfileListViewScreenController(UserWineList listToDisplay) {

        this.listToDisplay = listToDisplay;
        this.wineDrinkerManager = wineDrinkerManager.getInstance();
        this.wineListManager = WineListManager.getInstance();
        this.profileScreenService = new ProfileScreenService();
    }

    /**
     * Initialises the list view screen where a user can view one of their lists in detail
     */
    public void initialize() {
        wineListNameTextField.setText(listToDisplay.getWineListName());
        wineListNameLabel.setText(listToDisplay.getWineListName());
        wineListNameTextField.setVisible(false);

        descriptionTextArea.setText(listToDisplay.getDescription());
        if (listToDisplay.getDescription().isEmpty()) {
            descriptionLabel.setText("You currently do not have a description for your wine list" + listToDisplay.getDescription() +
                    ". Click the edit description button to create a description!");
        } else {
            descriptionLabel.setText(listToDisplay.getDescription());
        }
        descriptionTextArea.setVisible(false);

        if (listToDisplay.getWineListName().equals("Favourites")) {
            renameButton.setVisible(false);
            editDescriptionButton.setVisible(false);
        }
        setUpTextAreaListenersForErrorMessages();

        styleButtons();
        winesRectangle.getStyleClass().add("red-wine-rectangle");
        descriptionRectangle.getStyleClass().add("white-wine-rectangle");
        descriptionScrollPane.getStyleClass().add("white-wine-scroll-pane");
        listContentsScrollPane.getStyleClass().add("red-wine-scroll-pane");
        descriptionLabel.setStyle("-fx-background-color: transparent");
        listContentsVBox.setStyle("-fx-background-color: transparent");

    }


    /**
     * Saves the name of the list when the list is renamed
     */
    @FXML
    public void onSaveListChangesButtonClicked() {
        wineListManager.rename(listToDisplay, wineListNameTextField.getText());

        wineListNameTextField.setVisible(false);
        wineListNameLabel.setVisible(true);
        wineListNameLabel.setText(listToDisplay.getWineListName());

        saveListChangesButton.setVisible(false);
        renameButton.setVisible(true);
        editListButton.setVisible(true);
        cancelListChangesButton.setVisible(false);

        errorLabel.setVisible(false);
        descErrorLabel.setVisible(false);

    }

    /**
     * Enables the text field when the Wine Drinker clicks the rename button
     * Sets the required buttons to visible
     */
    @FXML
    public void onRenameButtonClicked() {
        wineListNameLabel.setVisible(false);
        wineListNameTextField.setVisible(true);
        wineListNameTextField.setEditable(true);

        saveListChangesButton.setVisible(true);
        renameButton.setVisible(false);
        editListButton.setVisible(false);
        cancelListChangesButton.setVisible(true);

    }

    /**
     * Goes back to the list view page when the Wine Drinker clicks the back button
     * TODO notify user of unsaved changes
     */
    @FXML
    public void onBackButtonClicked() {
        if (profileScreenService.isValidRenamedListName(listToDisplay.getWineListName(), wineListNameTextField.getText())) {
            if (profileScreenService.unsavedChanges(listToDisplay.getWineListName(), wineListNameTextField.getText())
                    || profileScreenService.unsavedChanges(listToDisplay.getDescription(), descriptionTextArea.getText())) {
                FXWrapper.getInstance().loadCancelChangesPopUp(listToDisplay, false, wineListNameTextField.getText(), descriptionTextArea.getText(), rootAnchorPane);
            } else {
                FXWrapper.getInstance().loadProfileActionScreen(rootAnchorPane, Screen.WINELISTSSCREEN);
            }
        }
    }

    /**
     * Loads Pop up that asks the WineDrinker if they are sure they would like to cancel their changes
     */
    @FXML
    public void onCancelListChangesButtonClicked() {
        FXWrapper.getInstance().loadCancelChangesPopUp(listToDisplay, true, wineListNameTextField.getText(), descriptionTextArea.getText(), rootAnchorPane);
    }

    /**
     * Allows user to edit the text area when they press the edit description button
     */
    @FXML
    public void onEditDescriptionButtonClicked() {
        descriptionScrollPane.setVisible(false);
        descriptionTextArea.setVisible(true);
        descriptionTextArea.setEditable(true);

        saveDescChangesButton.setVisible(true);
        editDescriptionButton.setVisible(false);
        cancelDescChangesButton.setVisible(true);
    }

    /**
     * Saves the description of a list that has been edits
     */
    @FXML
    public void onSaveDescChangesButtonClicked() {
        listToDisplay.setDescription(descriptionTextArea.getText());
        wineListManager.update(listToDisplay);

        descriptionTextArea.setVisible(false);
        descriptionScrollPane.setVisible(true);
        descriptionLabel.setText(descriptionTextArea.getText());

        saveDescChangesButton.setVisible(false);
        editDescriptionButton.setVisible(true);
        cancelDescChangesButton.setVisible(false);

        errorLabel.setVisible(false);
        errorLabel.setVisible(false);
    }

    /**
     * Loads Pop up that asks the WineDrinker if they are sure they would like to cancel their changes on their description
     */
    @FXML
    public void onCancelDescChangesButtonClicked() {
        FXWrapper.getInstance().loadCancelChangesPopUp(listToDisplay, true, wineListNameTextField.getText(), descriptionTextArea.getText(), rootAnchorPane);
    }

    /**
     * Handles showing the error messages for the user based on the input into text fields
     */
    private void setUpTextAreaListenersForErrorMessages() {
        wineListNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!profileScreenService.isValidRenamedListName(listToDisplay.getWineListName(), newValue)) {
                descErrorLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText(profileScreenService.getCreateListErrorMessage(newValue));
                saveListChangesButton.setDisable(true);
                saveListChangesButton.setOpacity(0.5);
            } else if (profileScreenService.reachedCharLimit(newValue, listNameCharLimit)) {
                wineListNameTextField.setText(oldValue);
                descErrorLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText("You have reached the character limit for a list name (" + listNameCharLimit + " characters)");
                saveListChangesButton.setDisable(false);
                saveListChangesButton.setOpacity(1);
            } else {
                errorLabel.setVisible(false);
                saveListChangesButton.setDisable(false);
                saveListChangesButton.setOpacity(1);
            }
        });
    }

    /**
     *  Controls labels that will show the user when they have reached the character limit for the description
     */
    private void setUpListenersForDescValidation() {
        descriptionTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (profileScreenService.reachedCharLimit(descriptionTextArea.getText(), descCharLimit)) {
                descriptionTextArea.setText(oldValue);
                errorLabel.setVisible(false);
                descErrorLabel.setVisible(true);
                descErrorLabel.setText("You have reached the character limit for a list description (" + descCharLimit + " characters)");
            } else {
                descErrorLabel.setVisible(false);
            }
        });
    }

    /**
     * Styles the buttons to be consistent with all other buttons in the UI
     */
    private void styleButtons() {
        backButton.getStyleClass().add("nav-bar-button");
        renameButton.getStyleClass().add("nav-bar-button");
        saveListChangesButton.getStyleClass().add("nav-bar-button");
        editListButton.getStyleClass().add("nav-bar-button");
        cancelListChangesButton.getStyleClass().add("nav-bar-button");
        cancelDescChangesButton.getStyleClass().add("nav-bar-button");
        editDescriptionButton.getStyleClass().add("nav-bar-button");
        saveDescChangesButton.getStyleClass().add("nav-bar-button");
    }
}
