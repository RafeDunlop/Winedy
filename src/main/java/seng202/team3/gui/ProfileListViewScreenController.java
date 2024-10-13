package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.services.ProfileScreenService;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the profile_list_view_screen.fxml window
 * Displays the contents of the users lists
 *
 * @author Sophia Copley (sco207)
 */
public class ProfileListViewScreenController {


    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField wineListNameTextField;

    @FXML
    private Label wineListNameLabel;

    @FXML
    private VBox listContentsVBox;

    @FXML
    private ScrollPane listContentsScrollPane;

    @FXML
    private Button backButton;

    @FXML
    private Button renameButton;

    @FXML
    private Button saveListChangesButton;

    @FXML
    private Button editListButton;

    @FXML
    private Button cancelListChangesButton;

    @FXML
    private Button cancelDescChangesButton;

    @FXML
    private Button editDescriptionButton;

    @FXML
    private Button saveDescChangesButton;

    @FXML
    private Button cancelDeleteButton;

    @FXML
    private Button removeWinesButton;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label errorLabel;

    @FXML
    private Label descErrorLabel;

    @FXML
    private Rectangle winesRectangle;

    @FXML
    private Rectangle descriptionRectangle;

    @FXML
    private ScrollPane descriptionScrollPane;

    @FXML
    private Rectangle infoRectangle;

    @FXML
    private Label infoLabel;

    /**
     * Wine drinker manager to handle wine drinker related actions
     */
    private WineDrinkerManager wineDrinkerManager;

    /**
     * Wine list manager to handle wine list related actions
     */
    private WineListManager wineListManager;

    /**
     * List that is being displayed on the screen
     */
    private UserWineList listToDisplay;
    /**
     * List of selected wines in edit list mode
     */
    private List<Wine> selectedWines;

    /**
     * Profile screen service instance to deal with list name validation
     */
    private ProfileScreenService profileScreenService;

    /**
     * Character limit for a list name
     */
    private final int listNameCharLimit = 30;

    /**
     * Character limit for a list description
     */
    private final int descCharLimit = 500;

    /**
     * Map from page index to its child scrollpane for the wine buttons
     */
    private Map<Integer, ScrollPane> pageScrollPaneMap;

    /**
     * Boolean variable to declare whether the screen is in delete mode or not
     */
    private boolean deleteMode = false;

    private Image checked;
    private Image unChecked;
    private Image unCheckedHover;


    /**
     * Constructor for the profileListView controller
     * @param listToDisplay list to be displayed in the individual list view
     */
    public ProfileListViewScreenController(UserWineList listToDisplay) {

        this.listToDisplay = listToDisplay;
        this.wineDrinkerManager = wineDrinkerManager.getInstance();
        this.wineListManager = WineListManager.getInstance();
        this.profileScreenService = new ProfileScreenService();
    }

    /**
     * Initialises the list view screen where a user can view one of their lists in detail
     */
    public void initialize() {
        selectedWines = new ArrayList<>();
        wineListNameTextField.setText(listToDisplay.getWineListName());
        wineListNameLabel.setText(listToDisplay.getWineListName());
        wineListNameTextField.setVisible(false);

        descriptionTextArea.setText(listToDisplay.getDescription());
        descriptionTextArea.setVisible(false);

        if (listToDisplay.getWineListName().equals("Favourites")) {
            renameButton.setVisible(false);
            editDescriptionButton.setVisible(false);
        }
        setUpTextAreaListenersForErrorMessages();
        setUpListenersForDescValidation();

        addStyleSheets();

        checked = new Image("/images/checked.png");
        unChecked = new Image("/images/unchecked.png");
        unCheckedHover = new Image("/images/unchecked_hover.png");

        if (listToDisplay.getWineList().isEmpty()) {
            infoLabel.setVisible(true);
            infoLabel.setText(listToDisplay.getWineListName() + " currently contains 0 wines. Go to the search screen to browse wines to add to your list.");
            infoRectangle.setVisible(true);
        } else {
            infoLabel.setVisible(false);
            infoLabel.setVisible(false);
            this.pageScrollPaneMap = createPagination(listToDisplay.getWineList(), listContentsVBox, 4, 3, null);
            Pagination pagination = (Pagination) listContentsVBox.getChildren().get(0);
            pagination.getStyleClass().add("wine-list-pagination");
        }

        if (listToDisplay.getDescription().isEmpty()) {
            descriptionLabel.setText("You currently do not have a description for your wine list " + listToDisplay.getDescription() +
                    ". Click the edit description button to create a description!");
        } else {
            descriptionLabel.setText(listToDisplay.getDescription());
        }
    }

    /**
     * Saves the name of the list when the list is renamed
     */
    @FXML
    public void onSaveListChangesButtonClicked() {
        wineListManager.rename(listToDisplay, wineListNameTextField.getText());

        wineListNameTextField.setVisible(false);
        wineListNameLabel.setVisible(true);
        wineListNameLabel.setText(listToDisplay.getWineListName());

        saveListChangesButton.setVisible(false);
        renameButton.setVisible(true);
        editListButton.setVisible(true);
        cancelListChangesButton.setVisible(false);

        errorLabel.setVisible(false);
        descErrorLabel.setVisible(false);

        editDescriptionButton.setDisable(false);

    }

    /**
     * Enables the text field when the Wine Drinker clicks the rename button
     * Sets the required buttons to visible
     */
    @FXML
    public void onRenameButtonClicked() {

            wineListNameLabel.setVisible(false);
            wineListNameTextField.setVisible(true);
            wineListNameTextField.setEditable(true);

            saveListChangesButton.setVisible(true);
            saveListChangesButton.setDisable(true);
            renameButton.setVisible(false);
            editListButton.setVisible(false);
            cancelListChangesButton.setVisible(true);

            editDescriptionButton.setDisable(true);
    }

    /**
     * Goes back to the list view page when the Wine Drinker clicks the back button
     * TODO notify user of unsaved changes
     */
    @FXML
    public void onBackButtonClicked() {
        if (profileScreenService.isValidRenamedListName(listToDisplay.getWineListName(), wineListNameTextField.getText())) {
            if (profileScreenService.unsavedChanges(listToDisplay.getWineListName(), wineListNameTextField.getText())
                    || profileScreenService.unsavedChanges(listToDisplay.getDescription(), descriptionTextArea.getText())) {
                FXWrapper.getInstance().loadCancelChangesPopUp(listToDisplay, false, wineListNameTextField.getText(), descriptionTextArea.getText(), rootAnchorPane);
            } else {
                FXWrapper.getInstance().loadProfileActionScreen(rootAnchorPane, Screen.WINELISTSSCREEN);
            }
        } else {
            GuiService.shakeNode(errorLabel);
        }
    }

    /**
     * Loads Pop up that asks the WineDrinker if they are sure they would like to cancel their changes
     */
    @FXML
    public void onCancelListChangesButtonClicked() {
        if (profileScreenService.isValidRenamedListName(listToDisplay.getWineListName(), wineListNameTextField.getText()) &&
        profileScreenService.unsavedChanges(listToDisplay.getWineListName(), wineListNameTextField.getText())) {
            FXWrapper.getInstance().loadCancelChangesPopUp(listToDisplay, true, wineListNameTextField.getText(), descriptionTextArea.getText(), rootAnchorPane);
        } else {
            wineListNameTextField.setVisible(false);
            wineListNameLabel.setVisible(true);

            saveListChangesButton.setVisible(false);
            renameButton.setVisible(true);
            editListButton.setVisible(true);
            cancelListChangesButton.setVisible(false);

            errorLabel.setVisible(false);
            descErrorLabel.setVisible(false);

            editDescriptionButton.setDisable(false);


        }
    }

    /**
     * Allows user to edit the text area when they press the edit description button
     */
    @FXML
    public void onEditDescriptionButtonClicked() {

        descriptionScrollPane.setVisible(false);
        descriptionTextArea.setVisible(true);
        descriptionTextArea.setEditable(true);

        saveDescChangesButton.setVisible(true);
        editDescriptionButton.setVisible(false);
        cancelDescChangesButton.setVisible(true);

        renameButton.setDisable(true);
        editListButton.setDisable(true);

        saveDescChangesButton.setDisable(true);
    }

    /**
     * Saves the description of a list that has been edits
     */
    @FXML
    public void onSaveDescChangesButtonClicked() {
        listToDisplay.setDescription(descriptionTextArea.getText());
        wineListManager.update(listToDisplay);

        descriptionTextArea.setVisible(false);
        descriptionScrollPane.setVisible(true);

        saveDescChangesButton.setVisible(false);
        editDescriptionButton.setVisible(true);
        cancelDescChangesButton.setVisible(false);

        errorLabel.setVisible(false);
        errorLabel.setVisible(false);

        renameButton.setDisable(false);
        editListButton.setDisable(false);
    }

    /**
     * Loads Pop up that asks the WineDrinker if they are sure they would like to cancel their changes on their description
     */
    @FXML
    public void onCancelDescChangesButtonClicked() {
        if (profileScreenService.unsavedChanges(listToDisplay.getDescription(), descriptionTextArea.getText())) {
            FXWrapper.getInstance().loadCancelChangesPopUp(listToDisplay, true, wineListNameTextField.getText(), descriptionTextArea.getText(), rootAnchorPane);
        } else  {
            descriptionTextArea.setVisible(false);
            descriptionScrollPane.setVisible(true);

            saveDescChangesButton.setVisible(false);
            editDescriptionButton.setVisible(true);
            cancelDescChangesButton.setVisible(false);

            errorLabel.setVisible(false);
            errorLabel.setVisible(false);

            renameButton.setDisable(false);
            editListButton.setDisable(false);
        }
    }

    /**
     * Handles showing the error messages for the user based on the input into text fields
     */
    private void setUpTextAreaListenersForErrorMessages() {
        wineListNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!profileScreenService.isValidRenamedListName(listToDisplay.getWineListName(), newValue)) {
                errorLabel.setVisible(true);
                errorLabel.setText(profileScreenService.getCreateListErrorMessage(newValue));
                GuiService.shakeNode(errorLabel);
                saveListChangesButton.setDisable(true);
                saveListChangesButton.setOpacity(0.5);
            } else if (profileScreenService.reachedCharLimit(newValue, listNameCharLimit)) {
                wineListNameTextField.setText(oldValue);
                errorLabel.setVisible(true);
                errorLabel.setText("Character limit reached ("+ listNameCharLimit + " characters)");
                GuiService.shakeNode(errorLabel);
                saveListChangesButton.setDisable(false);
                saveListChangesButton.setOpacity(1);
            } else {
                errorLabel.setVisible(false);
                saveListChangesButton.setDisable(false);
                saveListChangesButton.setOpacity(1);
            }
        });
    }

    /**
     *  Controls labels that will show the user when they have reached the character limit for the description
     */
    private void setUpListenersForDescValidation() {
        descriptionTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (profileScreenService.unsavedChanges(listToDisplay.getDescription(), descriptionTextArea.getText())) {
                saveDescChangesButton.setDisable(false);
            }
            if (profileScreenService.reachedCharLimit(descriptionTextArea.getText(), descCharLimit)) {
                descriptionTextArea.setText(oldValue);
                descErrorLabel.setVisible(true);
                descErrorLabel.setText("Character limit reached ("+ descCharLimit + " characters)");
                GuiService.shakeNode(descErrorLabel);
            } else {
                descErrorLabel.setVisible(false);
            }
            if (descriptionTextArea.getText().isEmpty()) {
                descriptionLabel.setText("You currently do not have a description for your wine list " + listToDisplay.getDescription() +
                        ". Click the edit description button to create a description!");
            } else {
                descriptionLabel.setText(descriptionTextArea.getText());
            }
        });
    }

    /**
     * Toggles the checkboxes associated with the wines for deletion
     *
     * @param pageVBox the page content so that the individual wine buttons can be accessed
     * @param pageIndex index of the page to toggle the checkboxes
     */
    private void toggleCheckBoxes(VBox pageVBox, int pageIndex) {
        for (int i = 0; i < pageVBox.getChildren().size(); i++) {
            HBox hbox = (HBox) pageVBox.getChildren().get(i);
            int j = 0;
            for (Node child : hbox.getChildren()) {
                Button button = (Button) child;
                StackPane stackPane = (StackPane) button.getGraphic();
                HBox buttonHBox = (HBox) stackPane.getChildren().get(0);
                ImageView imageView = (ImageView) buttonHBox.getChildren().get(1);
                imageView.setVisible(deleteMode);
                imageView.setImage(unChecked);
                int finalI = i;
                int finalJ = j;
                if (deleteMode) {
                    Wine wine = listToDisplay.getWineList().get(12 * pageIndex + finalI * 3 + finalJ);
                    button.setOnAction(e -> {
                        deleteModeButtonAction(imageView, wine);
                        setRemoveWinesButtonLabel();
                    });

                    imageView.setOnMouseEntered(e -> {
                        if (!selectedWines.contains(wine)) {
                            imageView.setImage(unCheckedHover);
                        }
                    });

                    imageView.setOnMouseExited(e -> {
                        if (selectedWines.contains(wine)) {
                            imageView.setImage(checked);
                        } else {
                            imageView.setImage(unChecked);
                        }
                    });
                    button.getStyleClass().add("wine-button-disabled");
                } else {
                    button.setOnAction(event -> FXWrapper.getInstance().loadIndividualWineViewPopup(listToDisplay.getWineList().get(12 * pageIndex + finalI * 3 + finalJ)));
                    button.getStyleClass().add("nav-bar-button");
                }
                j++;
            }
        }
    }

    /**
     * Updates the remove wine button labels to show how many wines are selected for removal
     */
    private void setRemoveWinesButtonLabel() {
        if (selectedWines.size() > 1) {
            removeWinesButton.setText("Remove " + selectedWines.size() + " wines");
            removeWinesButton.setDisable(false);
        } else if (selectedWines.size() == 1) {
            removeWinesButton.setText("Remove " + selectedWines.size() + " wine");
            removeWinesButton.setDisable(false);
        } else {
            removeWinesButton.setText("Remove wines");
            removeWinesButton.setDisable(true);
        }
    }

    /**
     * Action of the wine buttons when delete mode is on
     *
     * @param imageView the check boxes to be set to visible in delete move
     * @param wine the wine corresponding to the button
     */
    private void deleteModeButtonAction(ImageView imageView, Wine wine) {
        if (selectedWines.contains(wine)) {
            selectedWines.remove(wine);
            imageView.setImage(unChecked);
        } else {
            selectedWines.add(wine);
            imageView.setImage(checked);
        }
    }

    /**
     * Toggles delete mode on and off
     */
    private void toggleDeleteMode() {
        deleteMode = !deleteMode;
        selectedWines.clear();
        for (int i = 0; i < pageScrollPaneMap.size(); i++) { //index starting at
            ScrollPane pageScrollPane = pageScrollPaneMap.get(i);
            VBox pageVBox = (VBox) pageScrollPane.getContent();
            toggleCheckBoxes(pageVBox, i);
        }

        editListButton.setVisible(!deleteMode);
        if (!listToDisplay.getWineListName().equals("Favourites")) {
            renameButton.setVisible(!deleteMode);
        }
        cancelDeleteButton.setVisible(deleteMode);
        removeWinesButton.setVisible(deleteMode);

        editDescriptionButton.setDisable(deleteMode);
    }

    /**
     * Toggles delete mode to be on when the edit button is clicked
     */
    @FXML
    public void onEditListButtonClicked() {
        toggleDeleteMode();
    }

    /**
     * Styles the buttons to be consistent with all other buttons in the UI
     */
    private void addStyleSheets() {
        backButton.getStyleClass().add("nav-bar-button");
        renameButton.getStyleClass().add("nav-bar-button");
        saveListChangesButton.getStyleClass().add("nav-bar-button");
        editListButton.getStyleClass().add("nav-bar-button");
        cancelListChangesButton.getStyleClass().add("nav-bar-button");
        cancelDescChangesButton.getStyleClass().add("nav-bar-button");
        editDescriptionButton.getStyleClass().add("nav-bar-button");
        saveDescChangesButton.getStyleClass().add("nav-bar-button");
        removeWinesButton.getStyleClass().add("nav-bar-button");
        cancelDeleteButton.getStyleClass().add("nav-bar-button");

        winesRectangle.getStyleClass().add("red-wine-rectangle");
        descriptionRectangle.getStyleClass().add("white-wine-rectangle");
        descriptionScrollPane.getStyleClass().add("white-wine-scroll-pane");
        descriptionLabel.setStyle("-fx-background-color: transparent");
        listContentsVBox.setStyle("-fx-background-color: transparent");

        wineListNameTextField.getStyleClass().add("sign-in-screen-text-field");
        descriptionTextArea.getStyleClass().add("description-text-area");

        errorLabel.setStyle("-fx-text-fill: -fx-dark-red-wine-colour;");
        descErrorLabel.setStyle("-fx-text-fill: -fx-dark-red-wine-colour;");

        infoRectangle.getStyleClass().add("white-red-wine-rectangle");
    }

    /**
     * Goes back to the regular state of the app when the user clicks cancel delete
     */
    @FXML
    public void onCancelDeleteButtonClicked() {
        toggleDeleteMode();
    }

    /**
     * Loads the pop up that asks the user to confirm the removal of wines from a list
     */
    @FXML
    public void onRemoveWinesButtonClicked() {
        FXWrapper.getInstance().loadDeletingWinesPopUp(selectedWines, listToDisplay, rootAnchorPane);
    }

    /**
     * Creates paginated display of wine buttons for search screen and profile screens
     *
     * @param winesToDisplay list of wines to display in the pagination
     * @param rootVBox vbox to insert pagination into
     * @param rowsPerPage number of rows of wines per page
     * @param winesPerRow number of wines per row
     * @param wineDetailsAnchorPane wine details anchor pane if applicable for the onAction of the wine button
     * @return pagination so it can be styled as needed per screen
     */
    public Map<Integer, ScrollPane> createPagination(List<Wine> winesToDisplay, VBox rootVBox, int rowsPerPage, int winesPerRow, AnchorPane wineDetailsAnchorPane) {
        int winesPerPage = 12;
        int numberOfPages = (int) Math.ceil((double) winesToDisplay.size() / winesPerPage);
        Map<Integer, ScrollPane> pageScrollPaneMap = new HashMap<>();

        Pagination pagination = new Pagination(numberOfPages, 0);
        rootVBox.getChildren().add(pagination);
        pagination.getStyleClass().add("wine-pagination");

        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            GuiService.createPageContentsScrollPane(pageScrollPaneMap, pageIndex, rowsPerPage, winesPerRow, winesToDisplay, wineDetailsAnchorPane);
        }

        pagination.setPageFactory(pageScrollPaneMap::get);
        return pageScrollPaneMap;
    }

}
