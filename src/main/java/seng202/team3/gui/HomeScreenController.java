package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import seng202.team3.guiservice.HomeScreenService;

/**
 * Controller for the home_screen.fxml window
 * @author Krishna Sridhar (nsr36)
 */

public class HomeScreenController {

    @FXML
    private Button searchButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button helpButton;
    @FXML
    private Label winedyLabel;
    @FXML
    private Label sloganLabel;
    @FXML
    private Rectangle titleBackgroundRectangle;

    /**
     * Initialises the three buttons and their styles
     */
    @FXML
    public void initialize() {
        HomeScreenService.setUpButton(searchButton, "/images/home_screen_search_button.png", "red-wine-button");
        HomeScreenService.setUpButton(profileButton, "/images/home_screen_profile_button.png", "red-wine-button");
        HomeScreenService.setUpButton(helpButton, "/images/home_screen_help_button.png", "red-wine-button");
        titleBackgroundRectangle.getStyleClass().add("white-wine-rectangle");
    }

    /**
     * When search button is pressed, launch the nav bar and search window
     */
    @FXML
    public void goToSearch() {
        FXWrapper.getInstance().loadScreen(Screen.SEARCH);
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
        // ToDo implement
    }

}
