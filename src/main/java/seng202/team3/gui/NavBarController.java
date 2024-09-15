package seng202.team3.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import seng202.team3.guiservice.NavBarService;
import seng202.team3.WineDrinkerManager;


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
     * Button clicked to open the full navigation bar
     */
    @FXML
    private Button navigationButton;

    /*
     * Button clicked to go to the Help screen
     */
    @FXML
    private Button helpButton;

    /*
     * Button clicked to reload the screen
     */
    @FXML
    private Button reloadButton;

    /*
     * HBox that contains all the hidden navigation buttons
     */
    @FXML
    private HBox buttonHBox;

    /*
     * Rectangle located behind the Nav Bar buttons
     */
    @FXML
    private Rectangle navBarRectangle;

    /*
     * The currently active screen; stored to not reload a page when clicked
     */
    private Screen selectedScreen;

    /*
     * Stores whether the nav bar is currently expanded
     */
    private boolean expanded = false;

    /*
     * PauseTransition used by the navigation button to delay its on mouse entered action.
     */
    PauseTransition expansionHoverPause;

    /*
     * ImageView used as the graphic of the search button, used by expandNavBar() and closeNavBar() to animate the Nav Bar.
     */
    ImageView searchButtonImageView;

    /*
     * ImageView used as the graphic of the profile button, used by expandNavBar() and closeNavBar() to animate the Nav Bar.
     */
    ImageView profileButtonImageView;

    /*
     * ImageView used as the graphic of the reload button, used by expandNavBar() and closeNavBar() to animate the Nav Bar.
     */
    ImageView reloadButtonImageView;

    /*
     * ImageView used as the graphic of the help button, used by expandNavBar() and closeNavBar() to animate the Nav Bar.
     */
    ImageView helpButtonImageView;

    /**
     * Method used by JavaFX when initialising the Nav Bar.
     */
    public void initialize() {
        FXWrapper instance = FXWrapper.getInstance();
        instance.setScreenPane(screenPane);

        navBarRectangle.getStyleClass().add("nav-bar-rectangle");

        homeButton.setOnAction(x -> onButtonClick(Screen.HOME));
        searchButton.setOnAction(x -> onButtonClick(Screen.SEARCH));
        profileButton.setOnAction(x -> onProfileButtonClicked());

        NavBarService.setUpButton(homeButton, "/images/nav_bar_home_button.png", "nav-bar-button", false);
        searchButtonImageView = NavBarService.setUpButton(searchButton, "/images/home_screen_search_button.png", "nav-bar-button", true);
        profileButtonImageView = NavBarService.setUpButton(profileButton, "/images/home_screen_profile_button.png", "nav-bar-button", true);
        reloadButtonImageView = NavBarService.setUpButton(reloadButton, "/images/nav_bar_reload_button.png", "nav-bar-button", true);
        helpButtonImageView = NavBarService.setUpButton(helpButton, "/images/home_screen_help_button.png", "nav-bar-button", true);
        NavBarService.setUpButton(navigationButton, "/images/nav_bar_navigate_button.png", "nav-bar-button", false);

        buttonHBox.setPrefSize(66, 66); // Prevents a little glitch in the animation where the HBox expands for a split second
        buttonHBox.setMaxWidth(66);
        buttonHBox.setSpacing(0);

        expansionHoverPause = new PauseTransition(Duration.seconds(0.5));
        expansionHoverPause.setOnFinished(event -> {
            if (!expanded) {
                expandNavBar();
                expanded = true;
            } else {
                closeNavBar();
                expanded = false;
            }
        });
    }

    /**
     * Reloads the selected screen by simply calling FXWrapper.loadScreen()
     */
    @FXML
    private void onReloadClicked() {
        if (selectedScreen != null) {
            FXWrapper.getInstance().loadScreen(selectedScreen);
        }
    }

    /**
     * Method to load the correct profile screen (depending on whether the user is logged in)
     */
    private void onProfileButtonClicked() {
        if (WineDrinkerManager.getInstance().getCurrentUser() == null) {
            onButtonClick(Screen.SIGNINSCREEN);
        } else {
            onButtonClick(Screen.PROFILESCREEN);
        }
    }

    /**
     * Calls FXWrapper.loadScreen() with the parameter as long as that is not the selected screen
     * @param screen the screen corresponding to the specific button, i.e. HOME
     */
    private void onButtonClick(Screen screen) {
        if (selectedScreen != screen) {
            FXWrapper.getInstance().loadScreen(screen);
            selectedScreen = screen;
        }
    }

    /**
     * Animates the expansion of the Nav Bar using a Timeline and KeyFrame.
     */
    private void expandNavBar() {
        searchButton.setManaged(true);
        profileButton.setManaged(true);
        reloadButton.setManaged(true);
        helpButton.setManaged(true);

        searchButton.setMaxWidth(Region.USE_COMPUTED_SIZE);
        profileButton.setMaxWidth(Region.USE_COMPUTED_SIZE);
        reloadButton.setMaxWidth(Region.USE_COMPUTED_SIZE);
        helpButton.setMaxWidth(Region.USE_COMPUTED_SIZE);

        Timeline timeline = new Timeline();

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),
                new KeyValue(buttonHBox.prefWidthProperty(), 350),
                new KeyValue(buttonHBox.maxWidthProperty(), 350),
                new KeyValue(buttonHBox.spacingProperty(), 5),

                new KeyValue(searchButtonImageView.fitHeightProperty(), 50),
                new KeyValue(searchButtonImageView.opacityProperty(), 1),

                new KeyValue(profileButtonImageView.fitHeightProperty(), 50),
                new KeyValue(profileButtonImageView.opacityProperty(), 1),

                new KeyValue(reloadButtonImageView.fitHeightProperty(), 50),
                new KeyValue(reloadButtonImageView.opacityProperty(), 1),

                new KeyValue(helpButtonImageView.fitHeightProperty(), 50),
                new KeyValue(helpButtonImageView.opacityProperty(), 1)
        );

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Animates the compression of the Nav Bar
     */
    private void closeNavBar() {
        Timeline timeline = new Timeline();

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),
                new KeyValue(searchButtonImageView.fitHeightProperty(), 1),
                new KeyValue(searchButtonImageView.opacityProperty(), 0),

                new KeyValue(profileButtonImageView.fitHeightProperty(), 1),
                new KeyValue(profileButtonImageView.opacityProperty(), 0),

                new KeyValue(reloadButtonImageView.fitHeightProperty(), 1),
                new KeyValue(reloadButtonImageView.opacityProperty(), 0),

                new KeyValue(helpButtonImageView.fitHeightProperty(), 1),
                new KeyValue(helpButtonImageView.opacityProperty(), 0),

                new KeyValue(buttonHBox.prefWidthProperty(), 66),
                new KeyValue(buttonHBox.maxWidthProperty(), 66),
                new KeyValue(buttonHBox.spacingProperty(), 0)
        );

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Used by JavaFX as the onMouseEntered of navigationButton.
     */
    @FXML
    private void onNavigationMouseEntered() {
        expansionHoverPause.playFromStart();
    }

    /**
     * Used by JavaFX as the onMouseExited of navigationButton.
     */
    @FXML
    private void onNavigationMouseExited() {
        expansionHoverPause.stop();
    }
}
