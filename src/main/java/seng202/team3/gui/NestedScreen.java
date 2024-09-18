package seng202.team3.gui;

/**
 * enum detailing necessary field for LoadNestedScreen method in FXWrapper. Used for loading nested screens
 *
 * @author Rafe Dunlop (rdu46)
 */
public enum NestedScreen {

    WINE("individual_wine_view.fxml");

    /**
     * the file name of the fxml file related to this screen without prefix i.e. "main.fxml"
     */
    public final String file;

    /**
     * enum constructor for NestedScreen.
     * @param fxmlFile the name of the fxml file "i.e. screen.fxml"
     */
    NestedScreen(String fxmlFile) {
        this.file = fxmlFile;
    }
}
