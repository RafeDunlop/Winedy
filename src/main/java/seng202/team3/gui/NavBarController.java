package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


import java.io.IOException;

/**
controller for the navigation bar (nav_bar.fxml)
 which is the container for all screens except for the landing screen
 (currently only contains features for demonstration)
 @author Rafe Dunlop (rdu46)
 */
public class NavBarController {

    /**
     * the container for all screens featuring the navigation bar
     */
    @FXML
    private AnchorPane screenPane;

    /**
     * just a dummy text field for verifying that this structure works graphically
     */
    @FXML
    TextField dummyText;


    public void initialize() {
        FXWrapper instance = FXWrapper.getInstance();
        instance.setScreenPane(screenPane);
    }

    @FXML
    private void dummy1Clicked() throws IOException {
        FXWrapper.getInstance().loadScreen(Screen.DUMMY1);
        dummyText.setText("dummy1clicked");
    }

    @FXML
    private void dummy2Clicked() throws IOException {
        FXWrapper.getInstance().loadScreen(Screen.DUMMY2);
        dummyText.setText("dummy2clicked");
    }
}
