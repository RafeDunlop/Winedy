package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

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

    public void initialize() {
        FXWrapper.getInstance().loadProfileActionScreen(winedyProfileAnchorPane, Screen.PROFILESCREEN);
        FXWrapper.getInstance().loadProfileActionScreen(wineListAnchorPane, Screen.WINELISTSSCREEN);
        FXWrapper.getInstance().loadProfileActionScreen(consumptionTrackingAnchorPane, Screen.TRACKINGCONSUMPTIONSCREEN);
    }
}
