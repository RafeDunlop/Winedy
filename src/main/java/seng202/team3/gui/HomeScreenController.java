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

    /**
     * When search button is pressed, launch the nav bar and search window
     */
    @FXML
    public void goToSearch() {
        FXWrapper.getInstance().loadScreen(Screen.DUMMY1);
    }

    /**
     * When profile button is pressed, launch the nav bar and profile window
     */
    @FXML
    public void goToProfile() {
        FXWrapper.getInstance().loadScreen(Screen.SIGNINSCREEN);
    }

    /**
     * When help button is pressed, launch the nav bar and help window
     */
    @FXML
    public void goToHelp() {
        // To implement
    }

}
