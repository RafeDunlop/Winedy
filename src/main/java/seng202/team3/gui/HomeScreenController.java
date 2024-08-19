package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

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
     * Initialises the button styles to be red-wine-button
     */
    @FXML
    public void initialize() {
        searchButton.getStyleClass().addAll("button", "red-wine-button");
        profileButton.getStyleClass().addAll("button", "red-wine-button");
        helpButton.getStyleClass().addAll("button", "red-wine-button");
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
        // To implement
    }

}
