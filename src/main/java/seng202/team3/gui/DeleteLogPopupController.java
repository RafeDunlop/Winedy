package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class DeleteLogPopupController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    private Runnable onDeleteClicked;

    public DeleteLogPopupController(Runnable onDeleteClicked) {
        this.onDeleteClicked = onDeleteClicked;
    }

    public void initialize() {
        GuiService.setUpPopUp(overlayPane, popUpAnchorPane, null);
        deleteButton.getStyleClass().add("nav-bar-button");
        cancelButton.getStyleClass().add("nav-bar-button");
        cancelButton.setOnAction(event -> FXWrapper.getInstance().removePopUp(overlayPane));
        deleteButton.setOnAction(event -> {
            FXWrapper.getInstance().removePopUp(overlayPane);
            onDeleteClicked.run();
        });
    }


}
