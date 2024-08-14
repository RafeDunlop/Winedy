package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
controller for the navigation bar (nav_bar.fxml)
 which is the container for all screens except for the landing screen
 (currently only contains features for demonstration)
 @author Rafe Dunlop (rdu46), Krishna Sridhar (nsr36)
 */
public class NavBarController {

    /**
     * just a dummy text field for verifying that this structure works graphically
     */
    @FXML
    TextField dummyText;

    private WinedyAppEnvironment winedyAppEnvironment;

    public NavBarController(WinedyAppEnvironment x) {
        winedyAppEnvironment = x;
    }

    @FXML
    private void dummy1Clicked() {
        winedyAppEnvironment.launchDummy1();
        dummyText.setText("dummy1clicked");
    }

    @FXML
    private void dummy2Clicked() {
        winedyAppEnvironment.launchDummy2();
        dummyText.setText("dummy2clicked");
    }
}
