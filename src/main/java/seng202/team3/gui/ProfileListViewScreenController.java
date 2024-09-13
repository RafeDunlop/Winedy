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
 * @author
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

    @FXML
    void onGoBackButtonClicked(ActionEvent event) {

    }

    @FXML
    void onRemoveAllButtonClicked(ActionEvent event) {

    }

    @FXML
    void onRenameButtonClicked(ActionEvent event) {

    }
}
