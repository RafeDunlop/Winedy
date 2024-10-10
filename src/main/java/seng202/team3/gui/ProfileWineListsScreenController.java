package seng202.team3.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import java.util.List;

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

    @FXML
    private Rectangle backgroundRectangle;

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
     * Observable Boolean Property that stores delete mode status
     */
    private final BooleanProperty deleteMode = new SimpleBooleanProperty(false);

    //Images for different states of the checkbox
    private Image checked;
    private Image unChecked;
    private Image unCheckedHover;
    private Image checkedHover;


    /**
     * Initialises the screen under the My Wine Lists tab which displays a paginated list
     * of wine lists.
     */
    public void initialize() {

        this.wineListManager = WineListManager.getInstance();
        wineLists = wineListManager.getAllUserWineLists();

        //initialise boolean property
        deleteMode.setValue(false);

        //create pagination and set page factory
        numberOfPages = (int) Math.ceil((double) wineLists.size() / listsPerPage);
        pagination = new Pagination(numberOfPages, 0);
        pagination.setPageFactory(this::createPage);

        rootVBox.getChildren().add(pagination);
        setStyleSheets();

        checked = new Image("/images/checked.png");
        unChecked = new Image("/images/unchecked.png");
        checkedHover = new Image("/images/checked_hover.png");
        unCheckedHover = new Image("/images/unchecked_hover.png");
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
     * Turns delete mode off when the cancel button is clicked and resets the listsToDelete
     */
    @FXML
    public void onCancelButtonClicked() {
        toggleDeleteMode();
        listsToDelete.clear();
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
     * Page factory that creates a VBox containing up to listsPerPage buttons. Each button represents a user wine list.
     * This is called every time the pagination navigates to a new page. This is because the page content is dynamic and
     * needs to be updated based on the current state (delete mode). Currently, pages are not cached.
     *
     * @param pageIndex index of the page to fill
     * @return Vbox containing the wine lists for a given page index
     */
    private VBox createPage(int pageIndex) {

        //The style centers the VBox
        VBox wineListVBox = new VBox(10);
        wineListVBox.setStyle("-fx-min-height: 440");
        wineListVBox.setPadding(new Insets(20, 0, 0, 40));

        //Determine start and end index for wine lists on the current page
        int start = pageIndex * listsPerPage;
        int end = Math.min(start + listsPerPage, wineLists.size());

        //Create buttons
        for (int i = start; i < end; i++) {
            Button button = new Button();
            setUpWineListButton(button, i);
            wineListVBox.getChildren().add(button);
        }

        //Add a listener for delete mode so the VBox can be updated whenever delete mode is toggled
        deleteMode.addListener((observable, oldValue, newValue) -> {
            //Only update buttons on current page
            if (pageIndex == pagination.getCurrentPageIndex()) {
                toggleCheckBoxes(wineListVBox);
            }
        });

        //Initial call to set up buttons when the page is created
        toggleCheckBoxes(wineListVBox);

        return wineListVBox;
    }

    /**
     * Toggles the buttons on the given page based on delete mode status.
     * If delete mode is active, set button action to add the winelist to listsToDelete. Also sets checkboxes visible.
     * If delete mode is inactive, set button action to load the wine list view and disables checkboxes.
     *
     * @param pageVBox The VBox that is the content of the current page
     */
    private void toggleCheckBoxes(VBox pageVBox) {

        //Go through all the buttons on current page
        for (int i = 0; i < pageVBox.getChildren().size(); i++) {

            Button button = (Button) pageVBox.getChildren().get(i);
            HBox buttonHBox = (HBox) button.getGraphic();
            Label label = (Label) buttonHBox.getChildren().get(1);

            //Ignore favourites list
            if (!label.getText().equals("Favourites")) {

                //update checkbox image
                ImageView checkBox = (ImageView) buttonHBox.getChildren().get(4);
                checkBox.setVisible(deleteMode.getValue());

                UserWineList wineList = wineLists.get(pagination.getCurrentPageIndex() * listsPerPage + i);
                int finalI = i;

                //If in delete mode
                if (deleteMode.getValue()) {

                    //Set button to add or remove the wine list to be deleted
                    button.setOnAction(e-> {
                        if (listsToDelete.contains(wineList)) {
                            listsToDelete.remove(wineList);
                            checkBox.setImage(unChecked);
                        } else {
                            listsToDelete.add(wineList);
                            checkBox.setImage(checked);
                        }
                        updateDeleteButton();
                    });

                    //Set up the checkbox image to respond to mouse hover
                    button.setOnMouseEntered(e -> {
                        if (listsToDelete.contains(wineList)) {
                            checkBox.setImage(checkedHover);
                        } else {
                            checkBox.setImage(unCheckedHover);
                        }
                    });
                    button.setOnMouseExited(e -> {
                        if (listsToDelete.contains(wineList)) {
                            checkBox.setImage(checked);
                        } else {
                            checkBox.setImage(unChecked);
                        }
                    });
                } else {
                    //Set button to load wine list view
                    button.setOnAction(event -> FXWrapper.getInstance().loadIndividualListView(wineListsAnchorPane, wineLists.get(finalI)));
                }
            }
        }
    }

    /**
     * Toggles screen between delete mode and regular mode. Updates the delete, delete list, create list and cancel buttons
     * to match current state (delete mode)
     */
    private void toggleDeleteMode() {
        deleteMode.setValue(!deleteMode.getValue());
        createListButton.setVisible(!deleteMode.getValue());
        deleteListsButton.setVisible(!deleteMode.getValue());
        cancelButton.setVisible(deleteMode.getValue());
        deleteButton.setVisible(deleteMode.getValue());
    }

    /**
     * Updates delete button to show how many lists are currently selected to be deleted (in delete mode).
     */
    public void updateDeleteButton() {

            if (listsToDelete.isEmpty()) {
                deleteButton.setText("Delete");
                deleteButton.setDisable(true);
                deleteButton.setOpacity(0.5);
            } else if (listsToDelete.size() == 1){
                deleteButton.setText("Delete " + listsToDelete.size() + " list");
            } else {
                deleteButton.setText("Delete " + listsToDelete.size() + " lists");
            }

        deleteButton.setDisable(listsToDelete.isEmpty());
        deleteButton.setOpacity(1);
    }

    /**
     * Sets up and styles the wine list button
     * @param button the button to set up
     * @param index index of the wine list that it represents
     */
    private void setUpWineListButton(Button button, int index) {
        button.setOnAction(event -> FXWrapper.getInstance().loadIndividualListView(wineListsAnchorPane, wineLists.get(index)));
        button.setPrefSize(750, 100);
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

        ImageView checkbox = new ImageView();
        GuiService.setUpImageView(checkbox);

        //Set initial checkbox based on whether the wineList is in the list of wines to be deleted
        checkbox.setImage(listsToDelete.contains(wineLists.get(index))? checked : unChecked);

        HBox hbox = new HBox(30, imageView, nameLabel, line, numberLabel, checkbox);
        nameLabel.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(checkbox, new Insets(0, 0, 0, 25));
        button.setGraphic(hbox);
    }

    /**
     * Styles the buttons to be consistent with all other buttons in the UI
     */
    private void setStyleSheets() {
        createListButton.getStyleClass().add("nav-bar-button");
        deleteListsButton.getStyleClass().add("nav-bar-button");
        cancelButton.getStyleClass().add("nav-bar-button");
        deleteButton.getStyleClass().add("nav-bar-button");

        backgroundRectangle.getStyleClass().add("red-wine-rectangle");

        pagination.getStyleClass().add("wine-pagination");
        //pagination.getStyleClass().add("list-pagination");
    }

    /**
     * Styles the label for the name of the list which will go on to the button
     * that takes you to that list
     * @param nameLabel name label to style
     */
    private void styleListNameLabel(Label nameLabel) {
        nameLabel.setStyle("-fx-font-size: 20");
        nameLabel.setMinWidth(350);
        nameLabel.setMaxWidth(350);
        nameLabel.setPadding(new Insets(10, 10, 10,10));
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);
    }
}
