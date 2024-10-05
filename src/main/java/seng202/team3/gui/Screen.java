package seng202.team3.gui;

/**
enum detailing necessary fields for LoadScreen method in FXWrapper

 @author Rafe Dunlop (rdu46), Krishna Sridhar (nsr36)
 */
public enum Screen {
    HOME("home_screen.fxml", false),
    NAVBAR("nav_bar.fxml", false),
    SEARCH("search_screen.fxml", true),
    SIGNINSCREEN("sign_in_screen.fxml", true),
    HELPSCREEN("help_screen.fxml", true),
    PROFILETABPANE("profile_tab_pane.fxml", true),
    PROFILESCREEN("profile_screen.fxml", false),
    WINELISTSSCREEN("profile_wine_lists_screen.fxml", false),
    TRACKINGCONSUMPTIONSCREEN("tracking_consumption_screen.fxml", false),
    PROFILELISTVIEWSCREEN("profile_list_view_screen.fxml", false),
    CREATELISTPOPUP("create_list_pop_up.fxml", false),
    DELETELISTSPOPUP("deleting_lists_pop_up.fxml", false);

    /**
     * The file name of the fxml file related to this screen without prefix i.e. "main.fxml"
     */
    public final String file;

    /**
     * Describes whether the screen should be loaded into navbar,
     * or if navbar should be cleared away
     */
    public final boolean hasNavBar;

    /**
     * Constructor for the screen enum
     * @param fxmlFile filename of the fxml file of the current screen
     * @param hasNavBar boolean variable stating whether the screen will have the navigation bar
     */
    Screen(String fxmlFile, boolean hasNavBar) {
        this.file = fxmlFile;
        this.hasNavBar = hasNavBar;
    }
}
