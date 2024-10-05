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
import seng202.team3.services.WineListSelectService;

/**
 * Controller for the wine lists selection pop up that lets user add a wine to any of their lists
 * @author Yuvraj Fagotra (yfa50)
 */
public class WineListSelectPopUpController {

    private WineListSelectService wineListSelectService;
    private final Wine wineToAdd;
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

    /**
     * Constructs controller and sets the wine that this controller will handle
     * @param wineToAdd The wine to be added to the wine lists
     */
    public  WineListSelectPopUpController(Wine wineToAdd) {
        this.wineToAdd = wineToAdd;
    }

    /**
     * sets up the pop using GuiService and creates buttons for all wineLists for the current user
     */
    public void initialize() {

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
     * Loads the createList pop up when the create list button is clicked. Removes the wine list select pop up and adds
     * it to the stack of previous screens.
     */
    @FXML
    public void onCreateListButtonClicked() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        FXWrapper.getInstance().addPreviousScreen(() -> FXWrapper.getInstance().loadAddWineToListPopUp(wineToAdd));
        FXWrapper.getInstance().loadCreateListPopUp();
    }

    /**
     *
     * @param wineList
     */
    @FXML
    public void onWineListButtonClicked(UserWineList wineList) {
        wineListSelectService.addWineToList(wineToAdd, wineList);
    }

    private void setUpVBox(VBox vBox) {
        vBox.setPadding(new Insets(5,5,5,5));
    }

}
