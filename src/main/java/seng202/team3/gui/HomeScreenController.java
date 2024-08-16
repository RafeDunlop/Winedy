package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the homeScreen.fxml window
 * @author Krishna Sridhar (nsr36)
 */

public class HomeScreenController {

    @FXML
    private Button searchButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button helpButton;
    private WinedyAppEnvironment winedyAppEnvironment;

    /**
     * HomeScreenController Constructor
     * Pass in the winedyAppEnvironment
     * @author nsr36
     * @param x WinedyAppEnvironment instance
     */
    public HomeScreenController(WinedyAppEnvironment x) {
        winedyAppEnvironment = x;
    }

    /**
     * When search button is pressed, launch the nav bar and search window
     */
    @FXML
    public void goToSearch() {
        winedyAppEnvironment.launchNavBar();
    }

    /**
     * When profile button is pressed, launch the nav bar and profile window
     */
    @FXML
    public void goToProfile() {
        winedyAppEnvironment.launchSignInScreen();
        winedyAppEnvironment.launchNavBar();
    }

    /**
     * When help button is pressed, launch the nav bar and help window
     */
    @FXML
    public void goToHelp() {
        // To implement
    }

}
