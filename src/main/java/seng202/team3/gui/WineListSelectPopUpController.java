package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
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
    private Label titleLabel;
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
    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;

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
                wineListButton.getStyleClass().add("wine-list-button");
                wineListsVBox.getChildren().add(wineListButton);
            }
        }

        setStyleClasses();
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
        wineListSelectService.updateWineList(wineToAdd, wineList);
        if (wineListSelectService.wineInList(wineToAdd, wineList)) {
            addWineStatusLabel.setText("Wine was added to " + wineList.getWineListName());
        } else {
            addWineStatusLabel.setText("Wine was removed from " + wineList.getWineListName());
        }
    }

    private void setUpVBox(VBox vBox) {
        vBox.setPadding(new Insets(5,5,5,5));
        vBox.setSpacing(5);
        vBox.getStyleClass().add("wine-list-vbox");
    }

    private void setStyleClasses() {
        titleLabel.getStyleClass().add("status-label");
        addWineStatusLabel.getStyleClass().add("status-label");
        //popUpAnchorPane.getStyleClass().add("red-wine-pane");
        gridPane.setStyle("-fx-background-color: transparent");
        scrollPane.getStyleClass().add("wine-list-scroll-pane");
        createListButton.getStyleClass().add("wine-list-button");
    }

}
