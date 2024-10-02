package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import seng202.team3.services.WineDrinkerManager;

public class ProfileTabPaneController {
    @FXML
    private TabPane profileTabPane;
    @FXML
    private Tab winedyProfileTab;

    @FXML
    private AnchorPane winedyProfileAnchorPane;

    @FXML
    private Tab wineListTab;

    @FXML
    private AnchorPane wineListAnchorPane;

    @FXML
    private Tab consumptionTrackingTab;

    @FXML
    private AnchorPane consumptionTrackingAnchorPane;

    @FXML
    private Label helloUserLabel;

    @FXML
    private Button logoutButton;

    /**
     * Initialises the tab pane that holds all the functionality that logged-in users can do
     */
    public void initialize() {
        profileTabPane.getStyleClass().add("profile-tab-pane");
        helloUserLabel.setText("Welcome, " + WineDrinkerManager.getInstance().getCurrentUser().getUsername() + "!");
        FXWrapper.getInstance().loadProfileActionScreen(winedyProfileAnchorPane, Screen.PROFILESCREEN);
        FXWrapper.getInstance().loadProfileActionScreen(wineListAnchorPane, Screen.WINELISTSSCREEN);
        FXWrapper.getInstance().loadProfileActionScreen(consumptionTrackingAnchorPane, Screen.TRACKINGCONSUMPTIONSCREEN);
    }

    /**
     * Removes the current logged-in user and launches the sign-in screen
     */
    @FXML
    public void onLogoutButtonClicked(){
        WineDrinkerManager.getInstance().setCurrentUser(null);
        //TODO Save data?
        FXWrapper.getInstance().loadScreen(Screen.SIGNINSCREEN);
    }


}
