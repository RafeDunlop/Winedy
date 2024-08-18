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
    PROFILESCREEN("profile_screen.fxml", true),
    PROFILELISTVIEWSCREEN("profile_list_view_screen.fxml", true),

    //TODO: get rid of these!
    DUMMY1("dummy1.fxml", true),
    DUMMY2("dummy2.fxml", true);

    /**
     * the file name of the fxml file related to this screen without prefix i.e. "main.fxml"
     */
    public final String file;

    /**
     * describes whether the screen should be loaded into navbar,
     * or if navbar should be cleared away
     */
    public final boolean hasNavBar;

    Screen(String fxmlFile, boolean hasNavBar) {
        this.file = fxmlFile;
        this.hasNavBar = hasNavBar;
    }
}
