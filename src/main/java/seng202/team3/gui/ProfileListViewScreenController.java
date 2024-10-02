package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;

/**
 * Controller for the profile_list_view_screen.fxml window
 * Not implemented in deliverable 2 (coming in deliverable 3)
 * Will display the contents of a user's list
 *
 * @author Krishna Sridhar (nsr36)
 */
public class ProfileListViewScreenController {

    @FXML
    private Button backButton;

    @FXML
    private Button removeAllButton;

    @FXML
    private Button renameButton;

    @FXML
    private Button saveChangesButton;

    @FXML
    private ListView<?> searchListView;

    @FXML
    private ComboBox<?> sortByComboBox;

    @FXML
    private AnchorPane wineDetailsAnchorPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField wineListNameTextField;

    @FXML
    private Label wineListNameLabel;

    @FXML
    private VBox listContentsVBox;
    @FXML
    private Button editListButton;
    @FXML
    private Button cancelChangesButton;


    private WineDrinkerManager wineDrinkerManager = WineDrinkerManager.getInstance();

    private WineListManager wineListManager = WineListManager.getInstance();

    private UserWineList listToDisplay;


    /**
     * method called when the back button is clicked
     */
//    @FXML
//    private void onGoBackButtonClicked() {}

    /**
     * method called when the remove all button is clicked
     */
//    @FXML
//    private void onRemoveAllButtonClicked() {}
    public void initialize() {
        wineListNameTextField.setText(listToDisplay.getWineListName());
        wineListNameLabel.setText(listToDisplay.getWineListName());
        wineListNameTextField.setVisible(false);

    }

    /**
     * Constructor for the profileListView controller
     * @param listToDisplay list to be displayed in the individual list view
     */
    public ProfileListViewScreenController(UserWineList listToDisplay) {
        this.listToDisplay = listToDisplay;
    }

    /**
     * Saves the name of the list when the list is renamed
     * TODO add valid list name logic and don't allow user to rename their favourites
     */
    @FXML
    public void onSaveChangesButtonClicked() {
        wineListManager.rename(listToDisplay, wineListNameTextField.getText());

        wineListNameTextField.setVisible(false);
        wineListNameLabel.setVisible(true);
        wineListNameLabel.setText(listToDisplay.getWineListName());

        saveChangesButton.setVisible(false);
        renameButton.setVisible(true);
        editListButton.setVisible(true);
        cancelChangesButton.setVisible(false);
    }

    /**
     * Enables the text field when the Wine Drinker clicks the rename button
     * Sets the required buttons to visible
     */
    @FXML
    void onRenameButtonClicked() {
        wineListNameLabel.setVisible(false);
        wineListNameTextField.setVisible(true);
        wineListNameTextField.setEditable(true);

        saveChangesButton.setVisible(true);
        renameButton.setVisible(false);
        editListButton.setVisible(false);
        cancelChangesButton.setVisible(true);
    }

    /**
     * Goes back to the list view page when the Wine Drinker clicks the back button
     * TODO notify user of unsaved changes
     */
    @FXML
    public void onBackButtonClicked() {
        System.out.println(listToDisplay.getWineListName());
        System.out.println(wineListNameTextField.getText());
        if (!listToDisplay.getWineListName().equals(wineListNameTextField.getText())) {
            System.out.println("Notify user of unsaved changes");
        } else {
            FXWrapper.getInstance().loadProfileActionScreen(rootAnchorPane, Screen.WINELISTSSCREEN);
        }
    }

    /**
     * Loads Pop up that asks the WineDrinker if they are sure they would like to cancel their changes
     * TODO allow user to confirm they would like to cancel their changes.
     */
    @FXML
    public void onCancelChangesButtonClicked() {
        wineListNameTextField.setText(listToDisplay.getWineListName());
        wineListNameTextField.setVisible(false);
        wineListNameLabel.setVisible(true);

        saveChangesButton.setVisible(false);
        renameButton.setVisible(true);
        editListButton.setVisible(true);
        cancelChangesButton.setVisible(false);
    }

}
