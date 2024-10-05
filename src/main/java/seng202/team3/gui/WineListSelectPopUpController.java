package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;
import seng202.team3.services.WineListSelectService;

public class WineListSelectPopUpController {

    private WineDrinkerManager wineDrinkerManager;
    private WineListManager wineListManager;
    private WineListSelectService wineListSelectService;
    private Wine wineToAdd;
    @FXML
    private VBox wineListsVBox;
    @FXML
    private Label addWineStatusLabel;
    @FXML
    private Button exitButton;
    @FXML
    private Button createListButton;
    @FXML
    private StackPane overlayPane;
    @FXML
    private AnchorPane popUpAnchorPane;

    public  WineListSelectPopUpController(Wine wineToAdd) {
        this.wineToAdd = wineToAdd;
    }

    public void initialize() {

        wineDrinkerManager = WineDrinkerManager.getInstance();
        wineListManager = WineListManager.getInstance();
        wineListSelectService = new WineListSelectService();

        GuiService.setUpPopUp(overlayPane, popUpAnchorPane);

        for (UserWineList wineList: wineListSelectService.getWineLists()) {
            Button wineListButton = new Button(wineList.getWineListName());
            wineListsVBox.getChildren().add(wineListButton);
        }
    }

    /**
     * Loads the create list pop up when the create list button is clicked
     */
    @FXML
    public void onCreateListButtonClicked() {
        FXWrapper.getInstance().loadCreateListPopUp();

    }

}
