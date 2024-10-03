package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import seng202.team3.repository.UserWineListDAO;
import seng202.team3.services.WineListManager;
import seng202.team3.models.UserWineList;

import java.util.List;

import static java.lang.Math.min;

public class ProfileWineListsScreenController {
    @FXML
    private VBox rootVBox;

    @FXML
    private AnchorPane wineListsAnchorPane;

    @FXML
    private Button createListButton;


    /**
     * Number of lists to be displayed on each page
     */
    private int listsPerPage = 4;
    /**
     * WineList manager singleton instance
     */
    private WineListManager wineListManager;

    /**
     * List of the users current wines
     */
    private List<UserWineList> wineLists;

    /**
     * Initialises the screen under the My Wine Lists tab which displayed a paginated list
     * of wine lists.
     */
    public void initialize() {
        this.wineListManager = WineListManager.getInstance();
        wineLists = wineListManager.getAllUserWineLists();
        int numberOfPages = wineLists.size() / listsPerPage;
        if (wineLists.size() % 4 != 0) { //Add an extra page for the lists where required
            numberOfPages += 1;
        }
        Pagination pagination = new Pagination(numberOfPages, 0); // 2 = total items / items per page
        if (numberOfPages <= 1) {
            //set style so that the pagination controls do not show
        }
        pagination.setPageFactory(pageIndex -> {
            VBox pageContent = createPage(pageIndex);
            pageContent.setPadding(new Insets(20, 20, 0, 40));
            return pageContent;
        });

        rootVBox.getChildren().add(pagination);

    }

    /**
     * Creates the VBox to be entered into each page of the paginated list of wineLists
     * @param pageIndex index of the page to fill
     * @return Vbox containing the wine lists for that page
     */

    private VBox createPage(int pageIndex) {
        VBox wineListVBox = new VBox(10);
        wineListVBox.setPadding(new Insets(10, 10, 10, 10));
        wineListVBox.setPrefSize(872, 475);
        int start = pageIndex * listsPerPage;
        int end = Math.min(start + listsPerPage, wineLists.size());
        for (int i = start; i < end; i++) {
            Button button = new Button(wineLists.get(i).getWineListName());
            int finalI = i;
            button.setOnAction(event -> FXWrapper.getInstance().loadIndividualListView(wineListsAnchorPane, wineLists.get(finalI)));
            button.setPrefSize(842, 100);
            wineListVBox.getChildren().add(button);
        }

        return wineListVBox;
    }

    /**
     * Loads the create list pop up when the create list button is clicked
     */
    public void onCreateListButtonClicked() {
        FXWrapper.getInstance().loadPopUp(Screen.CREATELISTPOPUP);

    }
}
