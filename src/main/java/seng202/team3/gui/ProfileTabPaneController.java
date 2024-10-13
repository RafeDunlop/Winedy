package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import seng202.team3.models.SearchWineList;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Controller for profile_tab_pane.fxml file
 * Controls the tab pane which contains all user actions
 *
 * @author Sophia Copley (sco207)
 */
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
     * Index of tab that the tab pane should open to
     */
    private int startTabIndex;

    /**
     * Constructor for the profile tab pane controller
     * @param startTabIndex Index of tab that the tab pane should open to
     */
    public ProfileTabPaneController(int startTabIndex) {
        this.startTabIndex = startTabIndex;
    }

    /**
     * Initialises the tab pane that holds all the functionality that logged-in users can do
     */
    public void initialize() {
        profileTabPane.getStyleClass().add("profile-tab-pane");
        logoutButton.getStyleClass().add("nav-bar-button");
        helloUserLabel.setText("Welcome, " + WineDrinkerManager.getInstance().getCurrentUser().getUsername() + "!");
        profileTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) ->reloadTabContent(newTab));
        FXWrapper.getInstance().loadProfileActionScreen(winedyProfileAnchorPane, Screen.PROFILESCREEN);
        FXWrapper.getInstance().loadProfileActionScreen(wineListAnchorPane, Screen.WINELISTSSCREEN);
        FXWrapper.getInstance().loadProfileActionScreen(consumptionTrackingAnchorPane, Screen.TRACKINGCONSUMPTIONSCREEN);
        profileTabPane.getSelectionModel().select(startTabIndex);
    }

    /**
     * Removes the current logged-in user and launches the sign-in screen
     */
    @FXML
    public void onLogoutButtonClicked() throws FileNotFoundException, URISyntaxException {
        WineDrinkerManager.getInstance().setCurrentUser(null);
        FXWrapper.getInstance().loadScreen(Screen.SIGNINSCREEN);
        DatabaseManager.getInstance().resetWineTable();
        SearchWineList searchReset = new SearchWineList(null, null, null, null, null, null, null, null, null);
        WineListManager.getInstance().setLastSearched(searchReset);
    }

    /**
     * Reloads tab content when you switch pages
     */
    public void reloadTabContent(Tab tab) {
        switch(tab.getText()) {
            case ("Winedy Profile "):
                FXWrapper.getInstance().loadProfileActionScreen(winedyProfileAnchorPane, Screen.PROFILESCREEN);
                break;
            case ("My Wine Lists "):
                FXWrapper.getInstance().loadProfileActionScreen(wineListAnchorPane, Screen.WINELISTSSCREEN);
                break;
            case ("Consumption Tracking"):
                FXWrapper.getInstance().loadProfileActionScreen(consumptionTrackingAnchorPane, Screen.TRACKINGCONSUMPTIONSCREEN);
                break;
        }
    }
}
