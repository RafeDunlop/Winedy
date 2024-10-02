package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import seng202.team3.services.ProfileScreenService;
import seng202.team3.services.WineListManager;


public class CreateNewListPopUpController {


    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    @FXML
    private TextField listNameTextField;

    @FXML
    private Button exitButton;

    @FXML
    private Button createNewListButton;

    private ProfileScreenService profileScreenService;

    private WineListManager wineListManager;

    public void initialize() {
        this.profileScreenService = new ProfileScreenService();
        this.wineListManager = WineListManager.getInstance();
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5)");
        overlayPane.setOnMouseClicked(event -> {
            Bounds popUpBounds = popUpAnchorPane.localToScene(popUpAnchorPane.getLayoutBounds());
            if (!popUpBounds.contains(event.getSceneX(), event.getSceneY())) {
                FXWrapper.getInstance().removePopUp(overlayPane);
            }
        });
    }

    @FXML
    public void onCreateListButtonClicked() {
        System.out.println("Hello");
        if (profileScreenService.isValidListName(listNameTextField.getText())) {
            wineListManager.newList(listNameTextField.getText(), "hello");
            FXWrapper.getInstance().removePopUp(overlayPane);
        }
    }

}
