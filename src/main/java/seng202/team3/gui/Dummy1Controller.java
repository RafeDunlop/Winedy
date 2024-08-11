package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Dummy1Controller {

    @FXML
    private TextField text;

    public void initialize() {
        text.setText("Dummy1!");
    }
}
