package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team3.models.FavouritesWineList;
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
        setUpVBox(wineListsVBox);

        for (UserWineList wineList: wineListSelectService.getWineLists()) {
            if (!wineList.getWineListName().equals(FavouritesWineList.getFavouritesName())) {
                Button wineListButton = new Button(wineList.getWineListName());
                wineListButton.setOnAction(event -> onWineListButtonClicked(wineList));
                wineListButton.setPrefSize(215, 55);
                VBox.setVgrow(wineListButton, Priority.ALWAYS);
                wineListsVBox.getChildren().add(wineListButton);
            }
        }
    }

    /**
     * Loads the create list pop up when the create list button is clicked
     */
    @FXML
    public void onCreateListButtonClicked() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        FXWrapper.getInstance().addPreviousScreen(() -> FXWrapper.getInstance().loadAddWineToListPopUp(wineToAdd));
        FXWrapper.getInstance().loadCreateListPopUp();
    }

    @FXML
    public void onWineListButtonClicked(UserWineList wineList) {
        wineListSelectService.addWineToList(wineToAdd, wineList);
    }

    private void setUpVBox(VBox vBox) {
        vBox.setPadding(new Insets(5,5,5,5));
    }

}
