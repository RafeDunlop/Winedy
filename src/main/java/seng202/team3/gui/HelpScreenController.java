package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Used by JavaFX as the controller for help_screen.fxml
 *
 * @author Hannah Botting (hbo51)
 */
public class HelpScreenController {

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(HelpScreenController.class);

    @FXML
    private Rectangle contentsRectangle;

    @FXML
    private Rectangle informationRectangle;

    @FXML
    private Button aboutWinedyButton;

    @FXML
    private Button howToUseWinedyButton;

    @FXML
    private Button signinButton;

    @FXML
    private Button createAnAccountButton;

    @FXML
    private ScrollPane contentsScrollPane;

    @FXML
    private ScrollPane informationScrollPane;

    @FXML
    private VBox informationVBox;

    @FXML
    private Label aboutWinedyTitleLabel;

    @FXML
    private Label aboutWinedyContentLabel;

    @FXML
    private Label howToUseWinedyTitleLabel;

    @FXML
    private Label createAnAccountTitleLabel;

    @FXML
    private Label createAnAccountContentLabel;

    @FXML
    private Label signinTitleLabel;

    @FXML
    private Label signinContentLabel;

    /**
     * Method used by JavaFX when initialising the Help Screen.
     */
    @FXML
    public void initialize() {
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
            aboutWinedyContentLabel.setText(GuiService.getContentFromFile("/text/about_winedy.txt"));
            createAnAccountContentLabel.setText(GuiService.getContentFromFile("/text/create_an_account.txt"));
            signinContentLabel.setText(GuiService.getContentFromFile("/text/sign_in.txt"));
            log.info("Help Screen loaded.");
        } catch (Exception e) {
            log.error("Error loading txt files. Did you misspell their path", e);
        }

        setContentsButtonOnAction(aboutWinedyButton, aboutWinedyTitleLabel);
        setContentsButtonOnAction(howToUseWinedyButton, howToUseWinedyTitleLabel);
        setContentsButtonOnAction(createAnAccountButton, createAnAccountTitleLabel);
        setContentsButtonOnAction(signinButton, signinTitleLabel);
    }

    /**
     * Sets the given contents Button's onAction to change the scroll position of the informationScrollPane to be the
     * location of where the given contents title Label is
     *
     * @param contentsButton The Button for which the onAction is to be set
     * @param contentsTitleLabel The Label whose position in the ScrollPane the Button is to be linked to
     */
    private void setContentsButtonOnAction(Button contentsButton, Label contentsTitleLabel) {
        contentsButton.setOnAction(event -> {
            double y = contentsTitleLabel.getBoundsInParent().getMinY();
            double height = informationVBox.getBoundsInParent().getHeight();
            double viewportHeight = contentsScrollPane.getViewportBounds().getHeight();
            double scrollPosition = y / (height - viewportHeight);
            informationScrollPane.setVvalue(scrollPosition);
        });
    }
}
