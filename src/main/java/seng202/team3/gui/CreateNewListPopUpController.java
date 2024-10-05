package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import seng202.team3.services.ProfileScreenService;
import seng202.team3.services.WineListManager;


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

    /**
     * Instance of profile screen service
     */
    private ProfileScreenService profileScreenService;

    /**
     * Instance of wine list manager so that lists can be added to the database
     */
    private WineListManager wineListManager;
    private ProfileTabPaneController profileTabPaneController;

    /**
     * Initialises the create new list pop up that will be displayed on the home screen
     * Sets the action that when you click out of the popup it closes and sets the overlay to dim the behind functionality
     */
    public void initialize() {
        this.profileScreenService = new ProfileScreenService();
        this.wineListManager = WineListManager.getInstance();
        GuiService.setUpPopUp(overlayPane,popUpAnchorPane);
    }

    /**
     * Sets the on action for clicking the create list button
     */
    @FXML
    public void onCreateListButtonClicked() {
        if (profileScreenService.isValidListName(listNameTextField.getText())) {
            wineListManager.newList(listNameTextField.getText(), "hello");
            FXWrapper.getInstance().removePopUp(overlayPane);
            FXWrapper.getInstance().loadPreviousScreen();
        }
    }

}
