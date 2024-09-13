package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import seng202.team3.WineDrinkerManager;
import seng202.team3.guiservice.HomeScreenService;

/**
 * Used by JavaFX as the controller for home_screen.fxml
 * @author Krishna Sridhar (nsr36)
 */
public class HomeScreenController {

    /*
     * Button that is clicked to open the wine search screen
     */
    @FXML
    private Button searchButton;

    /*
     * Button that is clicked to open the profile screen. First opens the sign in screen if not logged in.
     */
    @FXML
    private Button profileButton;

    /*
     * Button that is clicked to open the help screen.
     */
    @FXML
    private Button helpButton;

    /*
     * Rectangle located behind the Winedy title. Purely for style.
     */
    @FXML
    private Rectangle titleBackgroundRectangle;

    /*
     * Image view located on the titleBackgroundRectangle. Is initialized to contain the Image of the Winedy title.
     */
    @FXML
    ImageView winedyImageView;

    /*
     * Rectangle located behind the Buttons. Purely for style.
     */
    @FXML
    private Rectangle homeButtonsRectangle;

    /**
     * Used by JavaFX to initialise the Home Screen.
     * Initialises the three buttons and their styles.
     * Sets the styles of the titleBackgroundRectangle and homeButtonsRectangle.
     * Inserts an Image of the winedy_logo into winedyImageView.
     */
    @FXML
    public void initialize() {
        HomeScreenService.setUpButton(searchButton, "/images/home_screen_search_button.png", "home-screen-button");
        HomeScreenService.setUpButton(profileButton, "/images/home_screen_profile_button.png", "home-screen-button");
        HomeScreenService.setUpButton(helpButton, "/images/home_screen_help_button.png", "home-screen-button");
        titleBackgroundRectangle.getStyleClass().add("white-wine-rectangle");
        homeButtonsRectangle.getStyleClass().add("red-wine-rectangle");
        winedyImageView.setImage(new Image("/images/winedy_logo.png"));
    }

    /**
     * Used by JavaFX as the onAction of searchButton.
     * Launches the Nav Bar and the Search Screen.
     */
    @FXML
    public void goToSearch() {
        FXWrapper.getInstance().loadScreen(Screen.SEARCH);
    }

    /**
     * Used by JavaFX as the onAction of profileButton.
     * Launches the Nav Bar and the Sign In Screen.
     */
    @FXML
    public void goToProfile() {
        if (WineDrinkerManager.getInstance().getCurrentUser() == null) {
            FXWrapper.getInstance().loadScreen(Screen.SIGNINSCREEN);
        } else {
            FXWrapper.getInstance().loadScreen(Screen.PROFILESCREEN);
        }
    }

    /**
     * Used by JavaFX as the onAction of helpButton.
     * Launches the Nav Bar and the Help Screen.
     */
    @FXML
    public void goToHelp() {
        // FXWrapper.getInstance().loadScreen(Screen.HELPSCREEN); // TODO: Implement the Help Screen so this can be uncommented
    }
}
