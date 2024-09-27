package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import seng202.team3.services.WineDrinkerManager;

public class ProfileTabPaneController {
    @FXML
    private Tab winedyProfileTab;

    @FXML
    private AnchorPane winedyProfileAnchorPane;

    @FXML
    private Tab wineListTab;

    @FXML
    private AnchorPane wineListAnchorPane;

    @FXML
    private Tab consumptionTrackingTab;

    @FXML
    private AnchorPane consumptionTrackingAnchorPane;

    @FXML
    private Label helloUserLabel;

    public void initialize() {
        helloUserLabel.setText("Welcome, " + WineDrinkerManager.getInstance().getCurrentUser().getUsername() + "!");
        FXWrapper.getInstance().loadProfileActionScreen(winedyProfileAnchorPane, Screen.PROFILESCREEN);
        FXWrapper.getInstance().loadProfileActionScreen(wineListAnchorPane, Screen.WINELISTSSCREEN);
        FXWrapper.getInstance().loadProfileActionScreen(consumptionTrackingAnchorPane, Screen.TRACKINGCONSUMPTIONSCREEN);
    }
}
