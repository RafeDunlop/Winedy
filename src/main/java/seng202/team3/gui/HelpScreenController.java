package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import seng202.team3.guiservice.HelpScreenService;
import seng202.team3.guiservice.HomeScreenService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /*
     * Label that contains the text of the About Winedy section
     */
    @FXML
    Label aboutWinedyLabel;

    /*
     * Label that contains the text of the Create an Account section
     */
    @FXML
    Label createAnAccountLabel;

    /*
     * Label that contains the text of the Sign-in section
     */
    @FXML
    Label signinLabel;

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

        try {
            aboutWinedyLabel.setText(HelpScreenService.getContentFromFile("/text/about_winedy.txt"));
            createAnAccountLabel.setText(HelpScreenService.getContentFromFile("/text/create_an_account.txt"));
            signinLabel.setText(HelpScreenService.getContentFromFile("/text/sign_in.txt"));
        } catch (Exception e) {
            log.error("Error loading txt files. Did you misspell their path", e);
        }
    }
}
