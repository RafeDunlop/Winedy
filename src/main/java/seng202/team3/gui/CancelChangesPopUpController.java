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

    private AnchorPane toNest;

    private WineListManager wineListManager;

    private boolean cancelButtonClicked;

    private UserWineList currentList;
    private String name;

    private String description;






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

        GuiService.setUpPopUp(overlayPane, popUpAnchorPane);

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
