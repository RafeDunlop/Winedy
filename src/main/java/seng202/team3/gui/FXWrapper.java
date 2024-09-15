package seng202.team3.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     *
     */
    private static final Logger log = LogManager.getLogger(FXWrapper.class);

    /**
     * higher level container for all GUI in the application
     */
    private Pane superPane;

    /**
     * instance variable, stores the only copy of this class that may exist.
     */
    private static FXWrapper instance;

    /**
     * private default constructor to prevent instantiation outside this class
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
     * loads specified screen passed via enum
     * @param screen Enum which contains fxml path and
     */
    protected void loadScreen(Screen screen) {
        try {
            FXMLLoader screenLoader = new FXMLLoader(getClass().getResource("/fxml/" + screen.file));
            Parent root = screenLoader.load();
            clearPane(superPane);
            if (screen.hasNavBar) {
                loadScreen(Screen.NAVBAR);
                screenPane.getChildren().add(root);
            } else {
                superPane.getChildren().add(root);
            }
        } catch (IOException e) {
            log.error(e);
        }
    }

    public void loadNestedScreen(Pane toNest, Screen toLoad) {
        try {
           FXMLLoader screenLoader = new FXMLLoader(getClass().getResource("/fxml/" + toLoad.file));
           Parent leaf = screenLoader.load();
           clearPane(toNest);
           toNest.getChildren().add(leaf);
        } catch (IOException e) {
            log.error(e);
        }
    }

    /**
     * Removes all FXML components, including the navBar
     * @throws NullPointerException thrown if superPane is not set yet via setSuperPane
     */
    private void clearPane(Pane toClear) throws NullPointerException {
        toClear.getChildren().removeAll(toClear.getChildren());
    }
}
