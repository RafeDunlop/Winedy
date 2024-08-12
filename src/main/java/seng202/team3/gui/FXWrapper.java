package seng202.team3.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Contains all methods for loading JavaFX classes
 * @author Rafe Dunlop (rdu46)
 */
public class FXWrapper {

    /**
     * container below navigation bar for screens featuring the navigation bar
     */
    private AnchorPane screenPane;

    /**
     * higher level container for all GUI in the application
     */
    private Pane superPane;

    /**
     * instance variable, stores the only copy of this class that may exist.
     */
    private static FXWrapper instance;

    /**
     * private (almost) default constructor to prevent instantiation outside this class
     */
    private FXWrapper() {
        screenPane = null;
        superPane = null;
    }

    /**
     * sets the container for screens below the navigation bar.
     * Used each time the navigation bar is re-initialised.
     * @param screenPane container in which to load screens within the navigation bar
     */
    protected void setScreenPane(AnchorPane screenPane) {
        this.screenPane = screenPane;
    }

    /**
     * sets the top level container
     * @param superPane the top level container into which navbar is nested
     */
    protected void setSuperPane(Pane superPane) {
        this.superPane = superPane;
    }

    /**
     * provides the singleton instance of SuperWrapper so that it is available to any GUI Controller class
     * @return the SuperWrapper instance which can be used to call non-static methods
     */
    protected static FXWrapper getInstance() {
        if (instance == null) {
            instance = new FXWrapper();
        }
        return instance;
    }

    /**
     * loads dummy1.fxml into the screen below navbar
     * not for release
     * @throws IOException thrown if the FXMLLoader.load() method encounters a problem
     */
    protected void launchDummy1() throws IOException {
        clearScreen();
        FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/dummy1.fxml"));
        Parent root = baseLoader.load();
        screenPane.getChildren().add(root);

    }

    /**
     * loads dummy2.fxml into the screen below navbar
     * not for release
     * @throws IOException thrown if the FXMLLoader.load() method encounters a problem
     */
    protected void launchDummy2() throws IOException {
        clearScreen();
        FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/dummy2.fxml"));
        Parent root = baseLoader.load();
        screenPane.getChildren().add(root);
    }

    /**
     * Removes all FXML components, including the navBar
     * @throws NullPointerException thrown if superPane is not set yet via setSuperPane
     */
    protected void clearNavBar() throws NullPointerException {
        superPane.getChildren().removeAll(superPane.getChildren());
    }

    /**
     * Removes all FXML components below the navBar level
     * called when switching screens
     * @throws NullPointerException thrown if screenPane is not set yet via setScreenPane
     */
    protected void clearScreen() throws NullPointerException {
        screenPane.getChildren().removeAll(screenPane.getChildren());
    }
}
