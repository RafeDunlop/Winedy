package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import java.awt.*;

public class CreateNewListPopUpController {
    @FXML
    private StackPane overlayPane;


    public void initialize() {
        System.out.println("hello");
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5)");
    }
}
