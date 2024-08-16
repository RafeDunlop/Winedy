package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contains all methods for loading JavaFX classes
 * @author Rafe Dunlop (rdu46), Krishna Sridhar (nsr36)
 */
public class FXWrapper {

    /**
     * container below navigation bar for screens featuring the navigation bar
     */
    @FXML
    private Pane screenPane;

    /**
     * higher level container for all GUI in the application including navigation bar
     */
    @FXML
    private Pane superPane;

    private Stage stage;

    /**
     * Initialising function
     * @param stage
     */
    public void init(Stage stage) {
        this.stage = stage;
        new WinedyAppEnvironment(this::launchHomeScreen,
                                 this::launchNavBar,
                                 this::launchDummy1,
                                 this::launchDummy2,
                                 this::launchSignInScreen,
                                 this::clearScreen,
                                 this::clearSuper);
    }

    /**
     * Method to launch the home screen using its fxml
     * @param winedyAppEnvironment instance
     */
    public void launchHomeScreen(WinedyAppEnvironment winedyAppEnvironment) {
        try {
            FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/homeScreen.fxml"));
            baseLoader.setControllerFactory(param -> new HomeScreenController(winedyAppEnvironment));
            Parent root = baseLoader.load();
            superPane.getChildren().add(root);
            stage.setTitle("Winedy Home");
        } catch (IOException e) {
            e.printStackTrace(); //TODO: replace with error logging
        }
    }

    /**
     * Method to launch the navigation bar using its fxml
     * @param winedyAppEnvironment instance
     */
    public void launchNavBar(WinedyAppEnvironment winedyAppEnvironment) {
        try {
            FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/nav_bar.fxml"));
            baseLoader.setControllerFactory(param -> new NavBarController(winedyAppEnvironment));
            Parent root = baseLoader.load();
            superPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace(); //TODO: replace with error logging
        }
    }

    /**
     * loads dummy1.fxml into the screen below navbar
     * not for release
     */
    public void launchDummy1(WinedyAppEnvironment winedyAppEnvironment) {
        try {
            FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/dummy1.fxml"));
            baseLoader.setControllerFactory(param -> new Dummy1Controller(winedyAppEnvironment));
            Parent root = baseLoader.load();
            screenPane.getChildren().add(root);
            stage.setTitle("Dummy1");
            System.out.println("Launched Dummy1");
        } catch (IOException e) {
            e.printStackTrace(); //TODO: replace with error logging
        }
    }

    /**
     * loads dummy2.fxml into the screen below navbar
     * not for release
     */
    public void launchDummy2(WinedyAppEnvironment winedyAppEnvironment) {
        try {
            FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/dummy2.fxml"));
            baseLoader.setControllerFactory(param -> new Dummy2Controller(winedyAppEnvironment));
            Parent root = baseLoader.load();
            screenPane.getChildren().add(root);
            stage.setTitle("Dummy2");
            System.out.println("Launched Dummy2");
        } catch (IOException e) {
            e.printStackTrace(); //TODO: replace with error logging
        }
    }

    /**
     * loads signInScreen.fxml into the screen below navbar
     */
    public void launchSignInScreen(WinedyAppEnvironment winedyAppEnvironment) {
        try {
            FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/signInScreen.fxml"));
            baseLoader.setControllerFactory(param -> new SignInScreenController(winedyAppEnvironment));
            Parent root = baseLoader.load();
            screenPane.getChildren().add(root);
            stage.setTitle("Winedy Profile Sign In");
        } catch (IOException e) {
            e.printStackTrace(); //TODO: replace with error logging
        }
    }

    /**
     * Removes all FXML components below the navBar level
     * called when switching screens
     * TODO: more robust use of exceptions
     */
    public void clearScreen() {
        screenPane.getChildren().removeAll(screenPane.getChildren());
    }

    /**
     * Removes all FXML components in the navBar level
     * called when switching screens
     * TODO: more robust use of exceptions
     */
    public void clearSuper() {
        superPane.getChildren().removeAll(superPane.getChildren());
    }

}
