package seng202.team3.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all methods for loading JavaFX classes
 *
 * @author Rafe Dunlop (rdu46)
 */
public class FXWrapper {

    /**
     * container below navigation bar for screens featuring the navigation bar
     */
    private AnchorPane screenPane;

    /**
     *Logger for robust error logging
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

    private List<Runnable> previousScreen;

    /**
     * private default constructor to prevent instantiation outside this class
     */
    private FXWrapper() {
        screenPane = null;
        superPane = null;
        previousScreen = new ArrayList<>();
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
    public static FXWrapper getInstance() {
        if (instance == null) {
            instance = new FXWrapper();
        }
        return instance;
    }

    /**
     * loads specified screen passed via enum
     * @param screen Enum which contains fxml path and
     */
    public void loadScreen(Screen screen) {
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

    /**
     * method for loading screens which are nested within other screens
     * this method is unused for deliverable two but will be used in later releases
     * @param toNest the Pane object that the screen is loaded into
     * @param toLoad a member of the NestedScreen enum which specifies teh screen to be loaded
     */
    public void loadProfileActionScreen(Pane toNest, Screen toLoad) {
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
     * loads the screen onto which the wine details are displayed
     * @param toNest the Pane to load the wine details onto
     * @param wineToDisplay the wine to be passed to the constructor so that its information can be displayed
     */
    public void loadIndividualWineView(Pane toNest, Wine wineToDisplay) {
        try {
            FXMLLoader individualWineViewLoader = new FXMLLoader(getClass().getResource("/fxml/individual_wine_view.fxml"));
            individualWineViewLoader.setControllerFactory(param -> new IndividualWineViewController(wineToDisplay));
            Parent leaf = individualWineViewLoader.load();
            clearPane(toNest);
            toNest.getChildren().add(leaf);
        } catch (IOException e) {
            log.error(e);
        }
    }

    /**
     * Loads the profile tab pane so it opens to a specified tab
     * @param index tab index to be opened. It will between 0, 1 and 2.
     */
    public void loadProfileTabPane(int index) {
        try {
            FXMLLoader profileTabPaneLoader = new FXMLLoader(getClass().getResource("/fxml/" + Screen.PROFILETABPANE.file));
            profileTabPaneLoader.setControllerFactory(param -> new ProfileTabPaneController(index));
            Parent leaf = profileTabPaneLoader.load();
            clearPane(screenPane);
            screenPane.getChildren().add(leaf);
        } catch (IOException e) {
            log.error(e);
        }
    }
    /**
     * Loads the view of a list where you can see the wines etc
     * @param toNest Pane to nest the new screen into
     * @param listToDisplay wine list to display
     */
    public void loadIndividualListView(Pane toNest, UserWineList listToDisplay) {
        try {
            FXMLLoader individualListViewLoader = new FXMLLoader(getClass().getResource("/fxml/profile_list_view_screen.fxml"));
            individualListViewLoader.setControllerFactory(param -> new ProfileListViewScreenController(listToDisplay));
            Parent leaf = individualListViewLoader.load();
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
    public void clearPane(Pane toClear) throws NullPointerException {
        toClear.getChildren().removeAll(toClear.getChildren());
    }

    /**
     * Loads the create list pop up screen which disables and dims background functionality
     * and allows users to create new lists
     */
    public void loadCreateListPopUp() {
        try {
            FXMLLoader popUpLoader = new FXMLLoader(getClass().getResource("/fxml/" + Screen.CREATELISTPOPUP.file));
            StackPane popUpRoot = popUpLoader.load();
            superPane.getChildren().add(popUpRoot);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public void loadDeleteListPopUp(List<UserWineList> wineLists) {
        try {
            FXMLLoader popUpLoader = new FXMLLoader(getClass().getResource("/fxml/" + Screen.DELETELISTSPOPUP.file));
            popUpLoader.setControllerFactory(param -> new DeletingListsPopUpController(wineLists));
            StackPane popUpRoot = popUpLoader.load();
            superPane.getChildren().add(popUpRoot);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public void loadAddWineToListPopUp(Wine wine) {
        try {
            FXMLLoader popUpLoader = new FXMLLoader(getClass().getResource("/fxml/" + Screen.WINELISTSELECTPOPUP.file));
            popUpLoader.setControllerFactory(param -> new WineListSelectPopUpController(wine));
            StackPane popUpRoot = popUpLoader.load();
            superPane.getChildren().add(popUpRoot);
        } catch (IOException e) {
            log.error(e);
        }
    }

    /**
     * Removes pop up from screen. Any updates made on the pop-up will require the screen below to be reloaded
     * after this method is called
     * @param overlayPane Parent pane of the pop-up
     */
    public void removePopUp(StackPane overlayPane) {
        superPane.getChildren().remove(overlayPane);
    }

    /**
     * set the previous screen
     * @param screen
     */
    public void addPreviousScreen(Runnable screen) {
        previousScreen.add(screen);
    }
    public void loadPreviousScreen() {
        if (previousScreen != null) {
            previousScreen.removeLast().run();
        }
    }
}
