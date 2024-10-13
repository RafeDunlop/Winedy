package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.scene.control.ScrollPane;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.services.WineListManager;

import java.util.List;

/**
 * Controller for deleting_wines_pop_up.fxml file
 * Pops up when the user tries to delete a wine/wines
 *
 * @author Sophia Copley (sco207)
 */

public class DeletingWinesPopUpController {
    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    @FXML
    private Label areYouSureLabel;

    @FXML
    private VBox wineNamesVBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button yesButton;

    @FXML
    private ScrollPane wineNamesScrollPane;

    /**
     * Wine list manager to handle list related actions
     */
    private WineListManager wineListManager;

    /**
     * Selected wines to delete
     */
    private List<Wine> winesToDelete;

    /**
     * AnchorPane to reload the list view screen if the usr decides to delete the wines
     */
    private AnchorPane toNest;

    /**
     * List to delete from
     */
    private UserWineList listToDeleteFrom;



    /**
     * Deleting lists pop up controller
     * @param winesToDelete selected lists to delete
     */
    public DeletingWinesPopUpController(List<Wine> winesToDelete, UserWineList listToDeleteFrom, AnchorPane toNest) {
        this.winesToDelete = winesToDelete;
        this.listToDeleteFrom = listToDeleteFrom;
        this.toNest = toNest;
    }

    /**
     * initialises pop-up that asks user to confirm they would like to delete lists
     */
    public void initialize() {
        this.wineListManager = WineListManager.getInstance();

        GuiService.setUpPopUp(overlayPane,popUpAnchorPane, null);

        if (winesToDelete.size() > 1) {
            areYouSureLabel.setText("Are you sure you would like to delete " + winesToDelete.size() + " wines?");
        } else {
            areYouSureLabel.setText("Are you sure you would like to delete " + winesToDelete.size() + " wine?");
        }

        addStyleSheets();

        int rows = (winesToDelete.size() % 2 == 0)? winesToDelete.size() / 2 : winesToDelete.size() / 2 + 1;
        for (int i = 0; i < rows ; i++) {
            Label bullet1 = new Label("- " + winesToDelete.get(2 * i).getName());
            bullet1.setPrefWidth(190);
            bullet1.setStyle("-fx-font-size: 16");
            bullet1.setWrapText(true);
            TextFlow bulletFlow = new TextFlow();
            bulletFlow.getChildren().add(bullet1);
            if (2 * i + 1 < winesToDelete.size()) {
                Label bullet2 = new Label("- " + winesToDelete.get(2 * i + 1).getName());
                bullet2.setPrefWidth(190);
                bullet2.setStyle("-fx-font-size: 16");
                bullet2.setWrapText(true);
                bulletFlow.getChildren().add(bullet2);
            }
            wineNamesVBox.getChildren().add(bulletFlow);
        }

    }

    /**
     * removes the pop-up and gets off of delete mode when you click the cancel button
     */
    @FXML
    public void onCancelButtonClicked() {
        FXWrapper.getInstance().removePopUp(overlayPane);
    }

    /**
     * removes the pop-up and deletes the wine list
     */
    @FXML
    public void onYesButtonClicked() {
        if (listToDeleteFrom.getWineListName().equals("Favourites")) {
            for (Wine wine  : winesToDelete) {
                wineListManager.getFavourites().removeWineFromList(wine);
                listToDeleteFrom.removeWineFromList(wine);
            }
            wineListManager.update(wineListManager.getFavourites());
        } else {
            for (Wine wine : winesToDelete) {
                listToDeleteFrom.removeWineFromList(wine);
            }
        }
        wineListManager.update(listToDeleteFrom);

        FXWrapper.getInstance().removePopUp(overlayPane);
        FXWrapper.getInstance().loadIndividualListView(toNest, listToDeleteFrom);
    }

    /**
     * Styles the buttons to be consistent with all other buttons in the UI
     */
    private void addStyleSheets() {
        yesButton.getStyleClass().add("nav-bar-button");
        cancelButton.getStyleClass().add("nav-bar-button");
        wineNamesScrollPane.getStyleClass().add("white-wine-scroll-pane");
    }
}
