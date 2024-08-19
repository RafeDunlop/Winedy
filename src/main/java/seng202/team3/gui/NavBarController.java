package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;


/**
controller for the navigation bar (nav_bar.fxml)
 which is the container for all screens except for the landing screen
 (currently only contains features for demonstration)
 @author Rafe Dunlop (rdu46)
 */
public class NavBarController {

    /*
     * the container for all screens featuring the navigation bar
     */
    @FXML
    private AnchorPane screenPane;

    /*
     * button clicked to go to the Home screen
     */
    @FXML
    private Button homeButton;

    /*
     * Button clicked to go to the Search screen
     */
    @FXML
    private Button searchButton;

    /*
     * Button clicked to go to the Profile screen
     */
    @FXML
    private Button profileButton;

    /*
     * the currently active screen; stored to not reload a page when clicked
     */
    private Screen selectedScreen;

    public void initialize() {
        FXWrapper instance = FXWrapper.getInstance();
        instance.setScreenPane(screenPane);

        homeButton.setOnAction(x -> onButtonClick(Screen.HOME));
        searchButton.setOnAction(x -> onButtonClick(Screen.SEARCH));
        profileButton.setOnAction(x -> onProfileButtonClicked());

        homeButton.getStyleClass().addAll("button", "small-red-wine-button");
        searchButton.getStyleClass().addAll("button", "small-red-wine-button");
        profileButton.getStyleClass().addAll("button", "small-red-wine-button");
    }

    /*
     * reloads the selected screen by simply calling FXWrapper.loadScreen()
     */
    @FXML
    private void onReloadClicked() {
        if (selectedScreen != null) {
            FXWrapper.getInstance().loadScreen(selectedScreen);
        }
    }

    /**
     * method to load the correct profile screen (depending on whether the user is logged in)
     */
    private void onProfileButtonClicked() {
        //determine is user is logged in, if so, load the profile screen, otherwise
        onButtonClick(Screen.SIGNINSCREEN);
    }

    /*
     * calls FXWrapper.loadScreen() with the parameter as long as that is not the selected screen
     * @param screen the screen corresponding to the specific button, i.e. HOME
     */
    private void onButtonClick(Screen screen) {
        if (selectedScreen != screen) {
            FXWrapper.getInstance().loadScreen(screen);
            selectedScreen = screen;
        }
    }
}
