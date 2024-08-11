package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * controller for dummy1.fxml, not for release
 * @author Rafe Dunlop (rdu46)
 */
public class Dummy1Controller {

    /**
     * TextField to write to, demonstrating that the controller
     * can operate without wresting control from navbar
     */
    @FXML
    private TextField text;

    /**
     * initializes the controller, setting a default value to the TextField
     */
    public void initialize() {
        text.setText("Dummy1!");
    }
}
