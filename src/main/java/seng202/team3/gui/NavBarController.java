package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
     * pane upon which the application is built, may need to be moved to a different class.
     * landing screen has been bypassed in this branch for simplicity
     */
    @FXML
    private Pane superPane;

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

    /**
     * Dummy Button
     */
    @FXML
    Button dummy1;


    public void initialize() {
        FXWrapper instance = FXWrapper.getInstance();
        instance.setScreenPane(screenPane);
        instance.setSuperPane(superPane);
        dummy1.getStyleClass().add("test-button");
    }

    @FXML
    private void dummy1Clicked() throws IOException {
        FXWrapper.getInstance().launchDummy1();
        dummyText.setText("dummy1clicked");
    }

    @FXML
    private void dummy2Clicked() throws IOException {
        FXWrapper.getInstance().launchDummy2();
        dummyText.setText("dummy2clicked");
    }
}
