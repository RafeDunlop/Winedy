package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng202.team3.models.Wine;

/**
 * Controller for the profile_list_view_screen.fxml window
 * Not implemented in deliverable 2 (coming in deliverable 3)
 * Will display the contents of a user's list
 *
 * @author Krishna Sridhar (nsr36)
 */
public class ProfileListViewScreenController {

    @FXML
    private Button goBackButton;

    @FXML
    private Button removeAllButton;

    @FXML
    private Button renameButton;

    @FXML
    private ListView<?> searchListView;

    @FXML
    private ComboBox<?> sortByComboBox;

    @FXML
    private AnchorPane wineDetailsAnchorPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField wineListNameTextField;

    @FXML
    private Label wineListNameLabel;

    @FXML
    private VBox listContentsVBox;

    /**
     * method called when the back button is clicked
     */
//    @FXML
//    private void onGoBackButtonClicked() {}

    /**
     * method called when the remove all button is clicked
     */
//    @FXML
//    private void onRemoveAllButtonClicked() {}

    /**
     * method called when the rename button is clicked
     */
//    @FXML
//    void onRenameButtonClicked() {}

    public void initialize() {


    }
}
