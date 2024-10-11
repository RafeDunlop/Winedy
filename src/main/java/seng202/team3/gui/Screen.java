package seng202.team3.gui;

/**
 * enum detailing necessary fields for LoadScreen method in FXWrapper
 *
 * @author Rafe Dunlop (rdu46), Krishna Sridhar (nsr36)
 */
public enum Screen {
    /**
     * The home screen
     */
    HOME("home_screen.fxml", false),

    /**
     * The navigation bar. Embeds the search screen, sign-in screen, profile screen, and help screen into it.
     */
    NAVBAR("nav_bar.fxml", false),

    /**
     * The search screen.
     */
    SEARCH("search_screen.fxml", true),

    /**
     * The sign-in screen. Is embedded into the navigation bar.
     */
    SIGNINSCREEN("sign_in_screen.fxml", true),

    /**
     * The help screen. Is embedded into the navigation bar.
     */
    HELPSCREEN("help_screen.fxml", true),

    /**
     * The profile tab pane. Is embedded into the profile screen. Embeds the wine list screen, profile list view screen,
     * and tracking consumption screen into its tabs.
     */
    PROFILETABPANE("profile_tab_pane.fxml", true),

    /**
     * The profile screen. Embeds the profile tab pane into it.
     */
    PROFILESCREEN("profile_screen.fxml", false),

    /**
     * The wine lists screen. Embedded into the profile tab pane, in the same tab as profile list view screen. Shows all
     * the user's wine lists. Opens the 'create a list' and 'delete a list' popups.
     */
    WINELISTSSCREEN("profile_wine_lists_screen.fxml", false),

    /**
     * The tracking consumption screen.
     */
    TRACKINGCONSUMPTIONSCREEN("tracking_consumption_screen.fxml", false),

    /**
     * The profile list view screen. Embedded into the profile tab pane, in the same tab as the wine lists view screen.
     * Shows the wines in a certain list. Opens the cancel changes popup.
     */
    PROFILELISTVIEWSCREEN("profile_list_view_screen.fxml", false),

    /**
     * The 'create a list' popup. Opened on top of and by the wine lists screen.
     */
    CREATELISTPOPUP("create_list_pop_up.fxml", false),

    /**
     * The 'delete a list' popup. Opened on top of and by the wine lists screen.
     */
    DELETELISTSPOPUP("deleting_lists_pop_up.fxml", false),

    /**
     * The cancel changes popup. Opened on top of and by the profile list view screen.
     */
    CANCELCHANGESPOPUP("cancel_changes_pop_up.fxml", false),

    /**
     * The add log popup. Opened on top of and by the tracking consumption screen.
     */
    ADDLOGPOPUP("add_log_popup.fxml", false),

    /**
     * The individual wine view popup. Displays the information of a specific wine in detail. Opened on top of and by
     * the individual wine view in the search screen.
     */
    INDIVIDUALWINEVIEWPOPUP("individual_wine_view_popup.fxml", false),

    /**
     * The wine list select popup. Opened on top of and by the individual wine view in the search screen when the add
     * to list button is selected.
     */
    WINELISTSELECTPOPUP("wine_list_select.fxml", false);

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
     *
     * @param fxmlFile filename of the fxml file of the current screen
     * @param hasNavBar boolean variable stating whether the screen will have the navigation bar
     */
    Screen(String fxmlFile, boolean hasNavBar) {
        this.file = fxmlFile;
        this.hasNavBar = hasNavBar;
    }
}
