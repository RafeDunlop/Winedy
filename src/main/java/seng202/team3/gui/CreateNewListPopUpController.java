package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import seng202.team3.services.ProfileScreenService;
import seng202.team3.services.WineListManager;

/**
 * Controller for the create_list_pop_up.fxml file
 * Allows the user to create a new list and give it a description
 *
 * @author Sophia Copley (sco207)
 */

public class CreateNewListPopUpController {

    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    @FXML
    private TextField listNameTextField;

    @FXML
    private Button exitButton;

    @FXML
    private Button createNewListButton;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label errorLabel;

    @FXML
    private Label descErrorLabel;

    /**
     * Instance of profile screen service
     */
    private ProfileScreenService profileScreenService;

    /**
     * Instance of wine list manager so that lists can be added to the database
     */
    private WineListManager wineListManager;

    /**
     * Character limit for a list name
     */
    private final int listNameCharLimit = 30;

    /**
     * Character limit for a description
     */
    private final int descCharLimit = 500;

    /**
     * Initialises the create new list pop up that will be displayed on the home screen
     * Sets the action that when you click out of the popup it closes and sets the overlay to dim the behind functionality
     */
    public void initialize() {
        this.profileScreenService = new ProfileScreenService();
        this.wineListManager = WineListManager.getInstance();
        GuiService.setUpPopUp(overlayPane,popUpAnchorPane);

        setUpTextAreaListenersForNameValidation();
        setUpListenersForDescValidation();

        styleButtons();
    }

    /**
     * Sets the on action for clicking the create list button
     */
    @FXML
    public void onCreateListButtonClicked() {
            wineListManager.newList(listNameTextField.getText(), descriptionTextArea.getText());
            FXWrapper.getInstance().removePopUp(overlayPane);
            FXWrapper.getInstance().loadProfileTabPane(1);
    }

    /**
     * sets action for the exit button
     */
    @FXML
    public void onExitClicked() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        FXWrapper.getInstance().loadProfileTabPane(1);
    }

    /**
     * Handles showing an error message to the user based on text field input
     */
    private void setUpTextAreaListenersForNameValidation() {
        listNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!profileScreenService.isValidNewListName(newValue)) {
                descErrorLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText(profileScreenService.getCreateListErrorMessage(newValue));
                createNewListButton.setDisable(true);
                createNewListButton.setOpacity(0.5);
            } else if (profileScreenService.reachedCharLimit(newValue, listNameCharLimit)) {
                listNameTextField.setText(oldValue);
                descErrorLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText("You have reached the character limit for a list name (" + listNameCharLimit + " characters)");
                createNewListButton.setDisable(false);
                createNewListButton.setOpacity(1);
            } else {
                errorLabel.setVisible(false);
                createNewListButton.setDisable(false);
                createNewListButton.setOpacity(1);
            }
        });
    }

    /**
     * Controls labels that will show the user when they have reached the character limit for the description
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
         exitButton.getStyleClass().add("nav-bar-button");
         createNewListButton.getStyleClass().add("nav-bar-button");
    }
}
