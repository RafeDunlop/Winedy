package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Controller for the external pane that all screens are loaded to
 *
 * @author Rafe Dunlop (rdu46)
 */
public class SuperPaneController {

    /**
     * pane upon which the application is built, may need to be moved to a different class.
     * landing screen has been bypassed in this branch for simplicity
     */
    @FXML
    private Pane superPane;

    private static boolean instantiated = false;

    /**
     * sets the superPane in FXWrapper. Should only happen once.
     */
    public void initialize() {
        if (instantiated) {
            throw new IllegalStateException("superPane has already been instantiated in this program");
        }
        FXWrapper instance = FXWrapper.getInstance();
        instance.setSuperPane(superPane);
        instantiated = true;

        instance.loadScreen(Screen.HOME);
    }
}
