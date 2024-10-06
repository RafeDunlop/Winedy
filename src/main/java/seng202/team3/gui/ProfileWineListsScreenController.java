package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng202.team3.services.WineListManager;
import seng202.team3.models.UserWineList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileWineListsScreenController {
    @FXML
    private VBox rootVBox;

    @FXML
    private AnchorPane wineListsAnchorPane;

    @FXML
    private Button createListButton;

    @FXML
    private Button deleteListsButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    /**
     * Pagination to be generated and contain the user's wine lists
     */
    private Pagination pagination;

    /**
     * Number of pages in the pagination
     */
    private int numberOfPages;

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
     * List of wines lists to delete
     */
    private List<UserWineList> listsToDelete = new ArrayList<>();

    /**
     * Boolean variable to declare whether the screen is in delete mode or not
     */
    private boolean deleteMode = false;

    private Map<Integer, VBox> pageVBoxMap = new HashMap<>();


    /**
     * Initialises the screen under the My Wine Lists tab which displayed a paginated list
     * of wine lists.
     */
    public void initialize() {
        this.wineListManager = WineListManager.getInstance();
        wineLists = wineListManager.getAllUserWineLists();

        numberOfPages = wineLists.size() / listsPerPage;
        if (wineLists.size() % 4 != 0) { //Add an extra page for the lists where required
            numberOfPages += 1;
        }

        pagination = new Pagination(numberOfPages, 0); // 2 = total items / items per page
        if (numberOfPages <= 1) {
            //set style so that the pagination controls do not show
        }

        for (int i = 0; i < numberOfPages; i++) {
            VBox pageContent = createPage(i);
            pageContent.setPadding(new Insets(20, 20, 0, 40));
            pageVBoxMap.put(i, pageContent);
        }

        pagination.setPageFactory(pageIndex -> pageVBoxMap.get(pageIndex));

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
            int finalI = i;
            Button button = new Button(wineLists.get(i).getWineListName());
            button.setOnAction(event -> FXWrapper.getInstance().loadIndividualListView(wineListsAnchorPane, wineLists.get(finalI)));
            CheckBox checkBox = new CheckBox();
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    listsToDelete.add(wineLists.get(finalI));
                } else {
                    listsToDelete.remove(wineLists.get(finalI));
                }

                if (listsToDelete.isEmpty()) {
                    deleteButton.setText("Delete");
                    deleteButton.setDisable(true);
                    deleteButton.setOpacity(0.5);
                } else if (listsToDelete.size() == 1){
                    deleteButton.setText("Delete " + listsToDelete.size() + " list");
                    deleteButton.setDisable(false);
                    deleteButton.setOpacity(1);
                }
                else {
                    deleteButton.setText("Delete " + listsToDelete.size() + " lists");
                    deleteButton.setDisable(false);
                    deleteButton.setOpacity(1);
                }
            });
            checkBox.setVisible(false);
            button.setPrefSize(800, 100);
            HBox hbox = new HBox(button, checkBox);
            checkBox.setPadding(new Insets(40, 20, 20,20));
            wineListVBox.getChildren().add(hbox);
        }

        return wineListVBox;
    }

    /**
     * Toggles the checkboxes for a single page of wine lists
     */
    private void toggleCheckBoxes(VBox pageVBox) {
        for (int i = 0; i < pageVBox.getChildren().size(); i++) {
            HBox hbox = (HBox) pageVBox.getChildren().get(i);
            CheckBox checkBox = (CheckBox) hbox.getChildren().get(1);
            checkBox.setVisible(deleteMode);
        }

    }

    private void toggleDeleteMode() {
        deleteMode = !deleteMode;

        for (int i = 0; i < numberOfPages; i++) {
            VBox pageVBox = pageVBoxMap.get(i);
            toggleCheckBoxes(pageVBox);
        }
        createListButton.setVisible(!deleteMode);
        deleteListsButton.setVisible(!deleteMode);
        cancelButton.setVisible(deleteMode);
        deleteButton.setVisible(deleteMode);

    }

    /**
     * Loads the createList pop up when the create list button is clicked. Also adds the profile screen to the stack so
     * it can be reloaded to update the wine lists view.
     */
    @FXML
    public void onCreateListButtonClicked() {
        FXWrapper.getInstance().addPreviousScreen(() -> FXWrapper.getInstance().loadProfileTabPane(1));
        FXWrapper.getInstance().loadCreateListPopUp();
    }

    @FXML
    public void onDeleteListsButtonClicked() {
        toggleDeleteMode();
    }

    @FXML
    public void onCancelButtonClicked() {
        toggleDeleteMode();
    }

    @FXML
    public void onDeleteButtonClicked() {
        FXWrapper.getInstance().loadDeleteListPopUp(listsToDelete);
    }

}
