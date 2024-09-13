package seng202.team3.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;
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
     * the currently active screen; stored to not reload a page when clicked
     */
    private Screen selectedScreen;

    /*
     * Stores whether the nav bar is currently expanded
     */
    private boolean expanded = false;

    /*
     * PauseTransition used by the Navigation button to delay its on action.
     */
    PauseTransition hoverPause;

    /**
     * Method used by JavaFX when initialising the Nav Bar.
     */
    public void initialize() {
        FXWrapper instance = FXWrapper.getInstance();
        instance.setScreenPane(screenPane);

        homeButton.setOnAction(x -> onButtonClick(Screen.HOME));
        searchButton.setOnAction(x -> onButtonClick(Screen.SEARCH));
        profileButton.setOnAction(x -> onProfileButtonClicked());

        homeButton.getStyleClass().addAll("button", "nav-bar-button");
        searchButton.getStyleClass().addAll("button", "nav-bar-button");
        profileButton.getStyleClass().addAll("button", "nav-bar-button");
        reloadButton.getStyleClass().addAll("button", "nav-bar-button");
        helpButton.getStyleClass().addAll("button", "nav-bar-button");
        navigationButton.getStyleClass().addAll("button", "nav-bar-button");

        buttonHBox.setPrefSize(66, 66);
        buttonHBox.setMaxWidth(66);

        searchButton.setManaged(false);
        profileButton.setManaged(false);
        reloadButton.setManaged(false);
        helpButton.setManaged(false);

        hoverPause = new PauseTransition(Duration.seconds(0.5));
        hoverPause.setOnFinished(event -> {
            expandNavBar();
            expanded = true;
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

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), new KeyValue(buttonHBox.prefWidthProperty(), 350), new KeyValue(buttonHBox.maxWidthProperty(), 350));

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Animates the compression of the Nav Bar TODO: Make this work properly :(
     */
    private void closeNavBar() {
        searchButton.setManaged(false);
        profileButton.setManaged(false);
        reloadButton.setManaged(false);
        helpButton.setManaged(false);

        Timeline timeline = new Timeline();

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), new KeyValue(buttonHBox.prefWidthProperty(), 66),
                new KeyValue(searchButton.maxWidthProperty(), 0),
                new KeyValue(profileButton.maxWidthProperty(), 0),
                new KeyValue(reloadButton.maxWidthProperty(), 0),
                new KeyValue(helpButton.maxWidthProperty(), 0));

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Used by JavaFX as the onMouseEntered of navigationButton.
     */
    @FXML
    private void onNavigationMouseEntered() {
        if (!expanded) {
            hoverPause.playFromStart();
        } else {  // TODO: Fix compression of nav bar so it can be closed
            // closeNavBar();
            // expanded = false;
        }
    }

    /**
     * Used by JavaFX as the onMouseExited of navigationButton.
     */
    @FXML
    private void onNavigationMouseExited() {
        hoverPause.stop();
    }
}
