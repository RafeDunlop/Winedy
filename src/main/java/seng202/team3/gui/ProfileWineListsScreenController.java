package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static java.lang.Math.min;

public class ProfileWineListsScreenController {
    @FXML
    private VBox rootVBox;
    private int totalLists= 11;
    private int listsPerPage = 4;




    public void initialize() {
        Pagination pagination = new Pagination(totalLists / listsPerPage + 1, 0); // 2 = total items / items per page
        //pagination.setPadding(new Insets(800, 0, 0, 0));
        pagination.setPageFactory(pageIndex -> {
            VBox pageContent = createPage(pageIndex);
            return pageContent;
        });

        rootVBox.getChildren().add(pagination);

    }

    private VBox createPage(int pageIndex) {
        VBox wineListVBox = new VBox(10);
        wineListVBox.setPadding(new Insets(10, 10, 10, 10));
        wineListVBox.setPrefSize(872, 475);
        int start = pageIndex * listsPerPage;
        int end = Math.min(start + listsPerPage, totalLists);

        for (int i = start; i < end; i++) {
            Button button = new Button("List " + i);
            button.setPrefSize(842, 100);
            wineListVBox.getChildren().add(button);
        }

        return wineListVBox;
    }
}
