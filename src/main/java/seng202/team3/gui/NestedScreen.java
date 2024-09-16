package seng202.team3.gui;

/**
 *enum detailing necessary field for LoadNestedScreen method in FXWrapper. Used for loading nested screens
 *
 *@author Rafe Dunlop (rdu46), Krishna Sridhar (nsr36)
 */
public enum NestedScreen {
    WINE("individual_wine_view.fxml");

    /**
     * the file name of the fxml file related to this screen without prefix i.e. "main.fxml"
     */
    public final String file;

    NestedScreen(String fxmlFile) {
        this.file = fxmlFile;
    }
}
