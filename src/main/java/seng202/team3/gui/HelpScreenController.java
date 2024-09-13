package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Used by JavaFX as the controller for help_screen.fxml
 * @author Hannah Botting (hbo51)
 */
public class HelpScreenController {

    /*
     * Logger for logging!
     */
    private static final Logger log = LogManager.getLogger(HelpScreenController.class);

    /*
     * The rectangle behind the table of contents.
     */
    @FXML
    Rectangle contentsRectangle;

    /*
     * The rectangle behind the help information.
     */
    @FXML
    Rectangle informationRectangle;

    /*
     * Button within the table of contents that links to the 'About Winedy' section.
     */
    @FXML
    Button aboutWinedyButton;

    /*
     * Button within the table of contents that links to the 'How to use Winedy' section.
     */
    @FXML
    Button howToUseWinedyButton;

    /*
     * Button within the table of contents that links to the 'Sign-in' section.
     */
    @FXML
    Button signinButton;

    /*
     * Button within the table of contents that links to the 'Create an Account' section.
     */
    @FXML
    Button createAnAccountButton;

    /*
     * ScrollPane that contains the VBox that contains the table of contents.
     */
    @FXML
    ScrollPane contentsScrollPane;

    /*
     * ScrollPane that contains the VBox that contains the help information.
     */
    @FXML
    ScrollPane informationScrollPane;

    /*
     * VBox that contains the help information.
     */
    @FXML
    VBox informationVBox;

    /**
     * Method used by JavaFX when initialising the Help Screen.
     */
    @FXML
    public void initialize() {
        log.info("Help Screen loaded.");
        try {
            contentsRectangle.getStyleClass().add("white-wine-rectangle");
            informationRectangle.getStyleClass().add("red-wine-rectangle");

            aboutWinedyButton.getStyleClass().add("help-screen-contents-button");
            howToUseWinedyButton.getStyleClass().add("help-screen-contents-button");
            createAnAccountButton.getStyleClass().add("help-screen-contents-button");
            signinButton.getStyleClass().add("help-screen-contents-button");

            contentsScrollPane.getStyleClass().add("white-wine-scroll-pane");
            informationScrollPane.getStyleClass().add("red-wine-scroll-pane");
        } catch (NullPointerException e) {
            log.warn("Error loading CSS style classes. Did you misspell their names?");
        }
    }
}
