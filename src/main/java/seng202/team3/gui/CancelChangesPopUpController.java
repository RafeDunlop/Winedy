package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import seng202.team3.models.UserWineList;
import seng202.team3.services.WineListManager;

import java.util.List;

/**
 * Controller for the cancel_changes_pop_up.fxml file
 * Handles the pop-up that appears when a user press a cancel button
 * on their changes or exits the page with unsaved changes
 *
 * @author Sophia Copley (sco207)
 */
public class CancelChangesPopUpController {
    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    @FXML
    private Label areYouSureLabel;

    @FXML
    private Button saveChangesButton;

    @FXML
    private Button cancelChangesButton;

    @FXML
    private Button exitButton;

    /**
     * AnchorPane to nest the next screen into if the pop-up is used in the case
     * where the user is trying to exit the page and there are unsaved changes
     */
    private AnchorPane toNest;

    /**
     * Wine list manager to handle all wine line related actions
     */
    private WineListManager wineListManager;

    /**
     * Boolean to check if the cancel button has been clicked.
     * This determines which version of the cancel changes pop up it should show,
     * the cancel changes version or the unsaved changes version
     */
    private final boolean cancelButtonClicked;

    /**
     * The list the user is currently viewing
     */
    private final UserWineList currentList;

    /**
     * The name of the list from the text field (may have unsaved changes)
     */
    private final String name;

    /**
     * Description of current list from the text field (may have unsaved changes
     */
    private final String description;

    /**
     * Constructor for cancel changes pop up
     * @param currentList the list the user is currently viewing
     * @param cancelButtonClicked if true the pop-up will handle the user trying to cancel their changes
     *                            if false this means the user has tried to exit the page with unsaved changes
     *                            and the version of the pop-up will be changed for this
     * @param name name from text field that may have been updated
     * @param description Description of list from text area that may have been updated
     * @param toNest pane to nest the next screen
     */

    public CancelChangesPopUpController(UserWineList currentList, boolean cancelButtonClicked, String name, String description, AnchorPane toNest) {
        this.cancelButtonClicked = cancelButtonClicked;
        this.currentList = currentList;
        this.name = name;
        this.description = description;
        this.toNest = toNest;
    }

    /**
     * initialises pop-up that asks user to confirm if they would like to cancel their changes
     */
    public void initialize() {
        this.wineListManager = WineListManager.getInstance();

        GuiService.setUpPopUp(overlayPane, popUpAnchorPane, null);

        if (cancelButtonClicked) {
            areYouSureLabel.setText("Are you sure you would like to discard your changes?");
        } else {
            areYouSureLabel.setText("You have unsaved changes! Would you like to save them or discard them?");
        }

        styleButtons();
    }

    /**
     * removes the pop-up and allows user to return to their original page.
     */
    @FXML
    public void onCancelChangesButtonClicked() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        if (cancelButtonClicked) {
            FXWrapper.getInstance().loadIndividualListView(toNest, currentList);
        } else {

            FXWrapper.getInstance().loadProfileTabPane(1);
        }
    }

    /**
     * removes the popup and deletes the wine list
     */
    @FXML
    public void onSaveChangesButtonClicked() {
        currentList.setDescription(description);
        wineListManager.update(currentList);
        wineListManager.rename(currentList, name);
        FXWrapper.getInstance().removePopUp(overlayPane);
        if (cancelButtonClicked) {
            FXWrapper.getInstance().loadIndividualListView(toNest, currentList);
        } else {
            FXWrapper.getInstance().loadProfileTabPane(1);
        }
    }

    /**
     * Exits out of the pop-up when clicked.
     */
    @FXML
    public void onExitButtonClicked() {
        FXWrapper.getInstance().removePopUp(overlayPane);
    }

    /**
     * Styles the buttons to be consistent with all other buttons in the UI
     */
    private void styleButtons() {
        saveChangesButton.getStyleClass().add("nav-bar-button");
        cancelChangesButton.getStyleClass().add("nav-bar-button");
        exitButton.getStyleClass().add("nav-bar-button");
    }
}
