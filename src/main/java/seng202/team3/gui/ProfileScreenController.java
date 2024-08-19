package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

/**
 * Controller for the profile_screen.fxml window
 * @author
 */

public class ProfileScreenController {
    @FXML
    private Slider abvLimitSlider;

    @FXML
    private ComboBox<?> colourPreferenceComboBox;

    @FXML
    private Button editUsernameButton;

    @FXML
    private ComboBox<?> fullnessPreferenceComboBox;

    @FXML
    private Button newListButton;

    @FXML
    private Button removeListButton;

    @FXML
    private ListView<?> searchListView;

    @FXML
    private TextField usernameTextField;

    @FXML
    private ComboBox<?> varietyPreferenceComboBox;

    @FXML
    void onEditUsernameButtonClicked(ActionEvent event) {

    }

    @FXML
    void onNewListButtonClicked(ActionEvent event) {

    }

    @FXML
    void onRemoveListButtonClicked(ActionEvent event) {

    }
}
