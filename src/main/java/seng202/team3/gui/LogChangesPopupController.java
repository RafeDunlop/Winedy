package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class LogChangesPopupController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button discardButton;

    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    private final Runnable onDiscard;

    public LogChangesPopupController(Runnable onDiscard) {
        this.onDiscard = onDiscard;
    }

    public void initialize() {
        cancelButton.setOnAction(event -> FXWrapper.getInstance().removePopUp(overlayPane));
        discardButton.setOnAction(event -> {
            onDiscard.run();
            FXWrapper.getInstance().removePopUp(overlayPane);
        });
        GuiService.setUpPopUp(overlayPane, popUpAnchorPane, null);
        addStyleClasses();
    }

    private void addStyleClasses() {
        cancelButton.getStyleClass().add("nav-bar-button");
        discardButton.getStyleClass().add("nav-bar-button");
    }

}
