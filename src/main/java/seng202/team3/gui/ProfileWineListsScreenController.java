package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import seng202.team3.services.WineListManager;
import seng202.team3.models.UserWineList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the profile_wine_lists_screen.fxml file
 * the screen where a users can view all of their wine lists
 *
 * @author Sophia Copley (sco207)
 */
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
    private final int listsPerPage = 4;

    /**
     * WineList manager singleton instance to handle wine list related actions
     */
    private WineListManager wineListManager;

    /**
     * List of the users current wines
     */
    private List<UserWineList> wineLists;

    /**
     * List of wines lists to delete
     */
    private final List<UserWineList> listsToDelete = new ArrayList<>();

    /**
     * Boolean variable to declare whether the screen is in delete mode or not
     */
    private boolean deleteMode = false;

    private final Map<Integer, VBox> pageVBoxMap = new HashMap<>();


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

        for (int i = 0; i < numberOfPages; i++) {
            VBox pageContent = createPage(i);
            pageContent.setPadding(new Insets(20, 20, 0, 40));
            pageVBoxMap.put(i, pageContent);
        }

        pagination.setPageFactory(pageIndex -> pageVBoxMap.get(pageIndex));

        rootVBox.getChildren().add(pagination);

        styleButtons();

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

            int index = i;

            HBox hbox = new HBox();

            Button button = new Button();
            setUpWineListButton(button, index);
            hbox.getChildren().add(button);

            CheckBox checkBox = new CheckBox();
            setUpCheckBox(checkBox, index);
            hbox.getChildren().add(checkBox);


            wineListVBox.getChildren().add(hbox);
            System.out.println();
        }

        return wineListVBox;
    }

    /**
     * Toggles the checkboxes for a single page of wine lists
     */
    private void toggleCheckBoxes(VBox pageVBox) {
        for (int i = 0; i < pageVBox.getChildren().size(); i++) {
            HBox hbox = (HBox) pageVBox.getChildren().get(i);
            Button button = (Button) hbox.getChildren().get(0);
            HBox buttonHBox = (HBox) button.getGraphic();
            Label label = (Label) buttonHBox.getChildren().get(1);
            if (!label.getText().equals("Favourites")) {
                CheckBox checkBox = (CheckBox) hbox.getChildren().get(1);
                checkBox.setVisible(deleteMode);
            }
        }

    }

    /**
     * Toggles screen between delete mode and regular mode
     */
    private void toggleDeleteMode() {
        deleteMode = !deleteMode;

        for (int i = 0; i < numberOfPages; i++) { //index starting at
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

    /**
     * Turns on delete mode when the delete button is clicked
     */
    @FXML
    public void onDeleteListsButtonClicked() {
        toggleDeleteMode();
    }

    /**
     * Turns delete mode off when the cancel button is clicked
     */
    @FXML
    public void onCancelButtonClicked() {
        toggleDeleteMode();
    }

    /**
     * Loads the delete list pop up when you click the delete button to ask
     * the user to confirm they would like to delete the selected lists
     */
    @FXML
    public void onDeleteButtonClicked() {
        FXWrapper.getInstance().loadDeleteListPopUp(listsToDelete);
    }

    /**
     * Sets up the checkboxes that go with each list so that they can be deleted.
     * Check box is disabled for the favourites list
     * @param checkBox
     * @param index
     */
    public void setUpCheckBox(CheckBox checkBox, int index) {
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                listsToDelete.add(wineLists.get(index));
            } else {
                listsToDelete.remove(wineLists.get(index));
            }

            if (listsToDelete.size() == 0) {
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
        checkBox.setPadding(new Insets(40, 20, 20,20));

    }

    /**
     * Sets up and styles the wine list button
     * @param button the button to set up
     * @param index index of the wine list that it represents
     */
    private void setUpWineListButton(Button button, int index) {
        button.setOnAction(event -> FXWrapper.getInstance().loadIndividualListView(wineListsAnchorPane, wineLists.get(index)));
        button.setPrefSize(800, 100);
        button.getStyleClass().add("nav-bar-button");
        Image image = new Image("/images/wine_box_image.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);

        Label nameLabel = new Label(wineLists.get(index).getWineListName());
        styleListNameLabel(nameLabel);

        Rectangle line = new Rectangle(3, 80);
        line.setArcHeight(10);
        line.setArcWidth(10);
        line.setStyle("-fx-fill: black");

        Label numberLabel = new Label(wineLists.get(index).getWineList().size() + " Wines");
        numberLabel.setStyle("-fx-font-size: 25");

        HBox hbox = new HBox( 30, imageView, nameLabel, line, numberLabel);
        nameLabel.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER_LEFT);
        button.setGraphic(hbox);
    }

    /**
     * Styles the buttons to be consistent with all other buttons in the UI
     */
    private void styleButtons() {
        createListButton.getStyleClass().add("nav-bar-button");
        deleteListsButton.getStyleClass().add("nav-bar-button");
        cancelButton.getStyleClass().add("nav-bar-button");
        deleteButton.getStyleClass().add("nav-bar-button");
    }

    /**
     * Styles the label for the name of the list which will go on to the button
     * that takes you to that list
     * @param nameLabel name label to style
     */
    private void styleListNameLabel(Label nameLabel) {
        nameLabel.setStyle("-fx-font-size: 25");
        nameLabel.setMinWidth(430);
        nameLabel.setMaxWidth(430);
        nameLabel.setPadding(new Insets(10, 10, 10, 10));
        nameLabel.setWrapText(true);
    }
}
