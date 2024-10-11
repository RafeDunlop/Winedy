package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Image checked;
    private Image unChecked;
    private Image unCheckedHover;
    private Image checkedHover;

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

        checked = new Image("/images/checked.png");
        unChecked = new Image("/images/unchecked.png");
        checkedHover = new Image("/images/checked_hover.png");
        unCheckedHover = new Image("/images/unchecked_hover.png");

        exitButton.setOnAction(e -> onExitClicked());
        exitButton.getStyleClass().add("nav-bar-button");

        GuiService.setUpPopUp(overlayPane, popUpAnchorPane);
        FXWrapper.getInstance().addPreviousScreen(() -> FXWrapper.getInstance().loadScreen(Screen.SEARCH));
        setUpVBox(wineListsVBox);
        setUpButtons();
        setStyleClasses();
    }

    /**
     * sets action for the exit button
     */
    @FXML
    public void onExitClicked() {
        FXWrapper.getInstance().removePopUp(overlayPane);
        FXWrapper.getInstance().loadPreviousScreen();
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
     * Used by JavaFX as the onAction of the wine list button. Updates the wine list by toggling whether the wine is in
     * the list or not. Sets the status label to show whether the wine was added or removed from the list
     *
     * @param wineList the wine list to be updated
     */
    @FXML
    public void onWineListButtonClicked(UserWineList wineList) {
        wineListSelectService.updateWineList(wineToAdd, wineList);
    }

    private void setUpVBox(VBox vBox) {
        vBox.setPadding(new Insets(5,5,5,5));
        vBox.setSpacing(5);
        vBox.getStyleClass().add("wine-list-vbox");
    }

    private void setStyleClasses() {
        titleLabel.getStyleClass().add("status-label");
        gridPane.setStyle("-fx-background-color: transparent");
        scrollPane.getStyleClass().add("wine-list-scroll-pane");
        createListButton.getStyleClass().add("wine-list-button");
    }

    private void setUpButtons() {

        for (UserWineList wineList: wineListSelectService.getWineLists()) {
            if (!wineList.getWineListName().equals(FavouritesWineList.getFavouritesName())) {
                Button wineListButton = new Button();
                HBox graphic = new HBox();
                graphic.setStyle("-fx-alignment: center");

                Label listName = new Label(wineList.getWineListName());
                listName.setStyle("-fx-pref-width: infinity; -fx-max-width: 470");

                ImageView imageView = new ImageView();
                setUpImageView(imageView);
                updateImageView(imageView, wineList);

                graphic.getChildren().addAll(listName, imageView);
                wineListButton.setGraphic(graphic);

                wineListButton.setOnAction(e -> {
                    onWineListButtonClicked(wineList);
                    if (wineList.getWineList().contains(wineToAdd)) {
                        imageView.setImage(checkedHover);
                    } else {
                        imageView.setImage(unCheckedHover);
                    }
                });

                wineListButton.setOnMouseEntered(e -> {
                    if (wineList.getWineList().contains(wineToAdd)) {
                        imageView.setImage(checkedHover);
                    } else {
                        imageView.setImage(unCheckedHover);
                    }
                });
                wineListButton.setOnMouseExited(e -> updateImageView(imageView, wineList));
                wineListButton.getStyleClass().add("wine-list-button");
                wineListsVBox.getChildren().add(wineListButton);
            }
        }
    }

    private void setUpImageView(ImageView imageView) {
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
    }

    private void updateImageView(ImageView imageView, UserWineList wineList) {
        if (wineList.getWineList().contains(wineToAdd)) {
            imageView.setImage(checked);
        } else {
            imageView.setImage(unChecked);
        }
    }

}
