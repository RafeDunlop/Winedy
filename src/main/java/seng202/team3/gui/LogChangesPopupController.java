package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * controller for log_changes_popup - made general for a variety of possible unsaved changes
 *
 * @author Rafe Dunlop (rdu46)
 */
public class LogChangesPopupController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button discardButton;

    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    /**
     * runnable which handles removing any screens below this one
     * and reloading screens as necessary
     */
    private final Runnable onDiscard;

    /**
     * trivial constructor which accepts the runnable to be run upon discard
     * @param onDiscard Runnable to run if the changes are discarded
     */
    public LogChangesPopupController(Runnable onDiscard) {
        this.onDiscard = onDiscard;
    }

    /**
     * sets up the popup and buttons
     */
    public void initialize() {
        cancelButton.setOnAction(event -> FXWrapper.getInstance().removePopUp(overlayPane));
        discardButton.setOnAction(event -> {
            onDiscard.run();
            FXWrapper.getInstance().removePopUp(overlayPane);
        });
        GuiService.setUpPopUp(overlayPane, popUpAnchorPane, null);
        addStyleClasses();
    }

    /**
     * adds the relevant css style classes
     */
    private void addStyleClasses() {
        cancelButton.getStyleClass().add("nav-bar-button");
        discardButton.getStyleClass().add("nav-bar-button");
    }

}
