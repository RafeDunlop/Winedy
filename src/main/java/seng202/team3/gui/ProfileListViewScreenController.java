package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
    private TextField wineListNameTextField;

    /**
     * method called when the back button is clicked
     */
    @FXML
    private void onGoBackButtonClicked() {}

    /**
     * method called when the remove all button is clicked
     */
    @FXML
    private void onRemoveAllButtonClicked() {}

    /**
     * method called when the rename button is clicked
     */
    @FXML
    void onRenameButtonClicked() {}
}
