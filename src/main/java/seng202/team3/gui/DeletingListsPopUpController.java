package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import seng202.team3.models.UserWineList;
import seng202.team3.services.WineListManager;

import java.util.List;

public class DeletingListsPopUpController {
    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popUpAnchorPane;

    @FXML
    private Label areYouSureLabel;

    @FXML
    private VBox listNamesVBox;

    private WineListManager wineListManager;

    private List<UserWineList> listsToDelete;


    public DeletingListsPopUpController(List<UserWineList> listsToDelete) {
        this.listsToDelete = listsToDelete;
    }

    /**
     * initialises pop-up that asks user to confirm they would like to delete lists
     * TODO create a style sheet for text of the wine names
     */
    public void initialize() {
        this.wineListManager = WineListManager.getInstance();

        GuiService.setUpPopUp(overlayPane,popUpAnchorPane);


        areYouSureLabel.setText("Are you sure you would like to delete " + listsToDelete.size() + " lists?");

        int rows = (listsToDelete.size() % 2 == 0)? listsToDelete.size() / 2 : listsToDelete.size() / 2 + 1;
        for (int i = 0; i < rows ; i++) {
            Label bullet1 = new Label("- " + listsToDelete.get(2 * i).getWineListName() + " (" + listsToDelete.get(2*i).getWineList().size() +" wines)");
            bullet1.setPrefWidth(220);
            bullet1.setStyle("-fx-font-size: 16");
            if (2 * i + 1 < listsToDelete.size()) {
                Label bullet2 = new Label("- " + listsToDelete.get(2 * i + 1).getWineListName() + " (" + listsToDelete.get(2 * i + 1).getWineList().size() + " wines)");
                bullet2.setPrefWidth(220);
                bullet2.setStyle("-fx-font-size: 16");
                TextFlow bulletFlow = new TextFlow(bullet1, bullet2);
                listNamesVBox.getChildren().add(bulletFlow);
            } else {
                TextFlow bulletFlow = new TextFlow(bullet1);
                listNamesVBox.getChildren().add(bulletFlow);
            }
        }
    }

    /**
     * removes the pop-up and gets off of delete mode when you click the cancel button
     */
    @FXML
    public void onCancelButtonClicked() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        FXWrapper.getInstance().loadProfileTabPane(1);
    }

    @FXML
    public void onYesButtonClicked() {
        for (UserWineList list : listsToDelete) {
            wineListManager.remove(list);
        }
        FXWrapper.getInstance().removePopUp(overlayPane);
        FXWrapper.getInstance().loadProfileTabPane(1);
    }
}
