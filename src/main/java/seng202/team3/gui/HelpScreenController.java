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
    private Button usefulWineInformationButton;

    @FXML
    private Button colourButton;

    @FXML
    private Button varietyButton;

    @FXML
    private Button fullnessButton;

    @FXML
    private Button abvButton;

    @FXML
    private Button howToUseWinedyButton;

    @FXML
    private Button createAnAccountButton;

    @FXML
    private Button signinButton;

    @FXML
    private Button searchingButton;

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
    private Label usefulWineInformationTitleLabel;

    @FXML
    private Label usefulWineInformationContentLabel;

    @FXML
    private Label colourTitleLabel;

    @FXML
    private Label colourContentLabel;

    @FXML
    private Label varietyTitleLabel;

    @FXML
    private Label varietyContentLabel;

    @FXML
    private Label fullnessTitleLabel;

    @FXML
    private Label fullnessContentLabel;

    @FXML
    private Label abvTitleLabel;

    @FXML
    private Label abvContentLabel;

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

    @FXML
    private Label searchingTitleLabel;

    @FXML
    private Label searchingContentLabel;

    /**
     * Method used by JavaFX when initialising the Help Screen.
     */
    @FXML
    public void initialize() {
        try {
            contentsRectangle.getStyleClass().add("white-wine-rectangle");
            informationRectangle.getStyleClass().add("red-wine-rectangle");

            addContentsButtonsStyleClass();

            contentsScrollPane.getStyleClass().add("white-wine-scroll-pane");
            informationScrollPane.getStyleClass().add("red-wine-scroll-pane");
        } catch (NullPointerException e) {
            log.warn("Error loading CSS style classes. Did you misspell their names?");
        }

        setContentsLabelsText();

        setupContentsButtonOnActions();
    }

    /**
     * Adds the help-screen-contents-button style class to all the contents buttons
     *
     * @throws NullPointerException if the style class could not be added, likely due to a typo, this is thrown to the
     * initialise method that calls it
     */
    private void addContentsButtonsStyleClass() throws NullPointerException {
        aboutWinedyButton.getStyleClass().add("help-screen-contents-button");
        usefulWineInformationButton.getStyleClass().add("help-screen-contents-button");
        colourButton.getStyleClass().add("help-screen-contents-button");
        varietyButton.getStyleClass().add("help-screen-contents-button");
        fullnessButton.getStyleClass().add("help-screen-contents-button");
        abvButton.getStyleClass().add("help-screen-contents-button");
        howToUseWinedyButton.getStyleClass().add("help-screen-contents-button");
        createAnAccountButton.getStyleClass().add("help-screen-contents-button");
        signinButton.getStyleClass().add("help-screen-contents-button");
        searchingButton.getStyleClass().add("help-screen-contents-button");
    }

    /**
     * Sets the text of all the content buttons using getContentFromFile in GuiService
     */
    private void setContentsLabelsText() {
        try {
            aboutWinedyContentLabel.setText(GuiService.getContentFromFile("/text/about_winedy.txt"));
            usefulWineInformationContentLabel.setText(GuiService.getContentFromFile("/text/useful_wine_information.txt"));
            colourContentLabel.setText(GuiService.getContentFromFile("/text/colour.txt"));
            varietyContentLabel.setText(GuiService.getContentFromFile("/text/variety.txt"));
            fullnessContentLabel.setText(GuiService.getContentFromFile("/text/fullness.txt"));
            abvContentLabel.setText(GuiService.getContentFromFile("/text/abv.txt"));
            createAnAccountContentLabel.setText(GuiService.getContentFromFile("/text/create_an_account.txt"));
            signinContentLabel.setText(GuiService.getContentFromFile("/text/sign_in.txt"));
            searchingContentLabel.setText(GuiService.getContentFromFile("/text/searching.txt"));
            log.info("Help Screen loaded.");
        } catch (NullPointerException e) {
            log.error("Error loading txt files. Did you misspell their path", e);
        }
    }

    /**
     * Calls setContentButtonOnAction for all the content buttons on the page
     */
    private void setupContentsButtonOnActions() {
        setContentsButtonOnAction(aboutWinedyButton, aboutWinedyTitleLabel);
        setContentsButtonOnAction(usefulWineInformationButton, usefulWineInformationTitleLabel);
        setContentsButtonOnAction(colourButton, colourTitleLabel);
        setContentsButtonOnAction(varietyButton, varietyTitleLabel);
        setContentsButtonOnAction(fullnessButton, fullnessTitleLabel);
        setContentsButtonOnAction(abvButton, abvTitleLabel);
        setContentsButtonOnAction(howToUseWinedyButton, howToUseWinedyTitleLabel);
        setContentsButtonOnAction(createAnAccountButton, createAnAccountTitleLabel);
        setContentsButtonOnAction(signinButton, signinTitleLabel);
        setContentsButtonOnAction(searchingButton, searchingTitleLabel);
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
            double labelPosition = contentsTitleLabel.getBoundsInParent().getMinY();
            double height = informationVBox.getBoundsInParent().getHeight();
            double viewportHeight = contentsScrollPane.getViewportBounds().getHeight();
            double scrollPosition = labelPosition / (height - viewportHeight);
            informationScrollPane.setVvalue(scrollPosition);
        });
    }
}
