package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
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
    private Rectangle titleBackgroundRectangle;
    @FXML
    private Rectangle searchRectangle;
    @FXML
    private Rectangle profileRectangle;
    @FXML
    private Rectangle helpRectangle;
    @FXML
    ImageView winedyImageView;

    /**
     * Initialises the three buttons and their styles
     */
    @FXML
    public void initialize() {
        HomeScreenService.setUpButton(searchButton, "/images/home_screen_search_button.png", "home-screen-button");
        HomeScreenService.setUpButton(profileButton, "/images/home_screen_profile_button.png", "home-screen-button");
        HomeScreenService.setUpButton(helpButton, "/images/home_screen_help_button.png", "home-screen-button");
        titleBackgroundRectangle.getStyleClass().add("white-wine-rectangle");
        searchRectangle.getStyleClass().add("red-wine-rectangle");
        profileRectangle.getStyleClass().add("red-wine-rectangle");
        helpRectangle.getStyleClass().add("red-wine-rectangle");
        winedyImageView.setImage(new Image("/images/winedy_logo.png"));
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
