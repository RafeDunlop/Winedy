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
    private int listsPerPage = 4;
    private WineListManager wineListManager = WineListManager.getInstance();

    private List<UserWineList> wineLists;


    public void initialize() {
        wineLists = wineListManager.getAllUserWineLists();
        System.out.println(wineLists.size());
        int numberOfPages = wineLists.size() / listsPerPage + 1;
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
}
