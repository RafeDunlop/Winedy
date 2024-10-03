package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.services.WineDrinkerManager;

/**
 * Used by JavaFX as the controller for home_screen.fxml
 *
 * @author Krishna Sridhar (nsr36)
 */
public class HomeScreenController {

    /**
     * Logger for logging successful screen loading
     */
    private static final Logger log = LogManager.getLogger(HomeScreenController.class);

    @FXML
    private Button searchButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button helpButton;

    @FXML
    private Rectangle titleBackgroundRectangle;

    @FXML
    ImageView winedyImageView;

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
        setUpHomeButton(searchButton, "/images/home_screen_search_button.png");
        setUpHomeButton(profileButton, "/images/home_screen_profile_button.png");
        setUpHomeButton(helpButton, "/images/home_screen_help_button.png");
        titleBackgroundRectangle.getStyleClass().add("white-wine-rectangle");
        homeButtonsRectangle.getStyleClass().add("red-wine-rectangle");
        winedyImageView.setImage(new Image("/images/winedy_logo.png"));

        log.info("HomeScreen initialized");
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
            FXWrapper.getInstance().loadProfileTabPane(0);
        }
    }

    /**
     * Used by JavaFX as the onAction of helpButton.
     * Launches the Nav Bar and the Help Screen.
     */
    @FXML
    public void goToHelp() {
        FXWrapper.getInstance().loadScreen(Screen.HELPSCREEN);
    }

    /**
     * Sets up the given button to have the home-screen-button style class and an image 100x100 pixels in size.
     * Used by the initialize method to set up the home screen buttons on launch.
     * @param button The button to be set up
     * @param imagePath The path the image is located at.
     */
    private void setUpHomeButton(Button button, String imagePath) {
        button.getStyleClass().add("home-screen-button");
        GuiService.addImageGraphicToButton(button, imagePath, 100, 100, false);
    }
}
