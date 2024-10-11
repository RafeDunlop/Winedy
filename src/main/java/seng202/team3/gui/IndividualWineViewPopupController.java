package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.Wine;

/**
 * Controller for individual_wine_view_popup.fxml
 *
 * @author Hannah Botting (hbo51)
 */
public class IndividualWineViewPopupController {

    /**
     * Logger for robust error and information logging
     */
    private static final Logger log = LogManager.getLogger(IndividualWineViewPopupController.class);

    @FXML
    private StackPane overlayPane;

    @FXML
    private AnchorPane popupAnchorPane;

    @FXML
    private Label nameTitleLabel;

    @FXML
    private Label countryTitleLabel;

    @FXML
    private Label styleTitleLabel;

    @FXML
    private Label yearTitleLabel;

    @FXML
    private Label priceContentsLabel;

    @FXML
    private Label abvContentsLabel;

    @FXML
    private Label fullnessContentsLabel;

    @FXML
    private Label volumeContentsLabel;

    @FXML
    private Label countryContentsLabel;

    @FXML
    private Label styleContentsLabel;

    @FXML
    private Label yearContentsLabel;

    @FXML
    private Label descriptionContentsLabel;

    @FXML
    private Label awardsContentsLabel;

    @FXML
    private ScrollPane awardsScrollPane;

    @FXML
    private ScrollPane descriptionScrollPane;

    @FXML
    private Button exitButton;

    @FXML
    private Rectangle wineViewRectangle;

    @FXML
    private ImageView wineViewImageView;

    /**
     * The wine whose details are to be displayed in the popup
     */
    private final Wine wineToDisplay;

    /**
     * Constructor for the IndividualWineViewPopupController. set the wine to be displayed to the given wine
     *
     * @param wineToDisplay the wine to be set to the wine to be displayed
     */
    public IndividualWineViewPopupController(Wine wineToDisplay) {
        this.wineToDisplay = wineToDisplay;
    }

    /**
     * Initialises the individual wine view popup. Sets all the label attributes to display the wine attributes.
     * Adds style classes to the exit button, and scroll panes.
     */
    public void initialize() {
        nameTitleLabel.setText(wineToDisplay.getName());
        priceContentsLabel.setText(String.format("$%.2f", wineToDisplay.getPricePerBottle()));
        abvContentsLabel.setText(String.format("%.1f%%", wineToDisplay.getAlcoholByVolume()));
        fullnessContentsLabel.setText(wineToDisplay.getFullness());
        volumeContentsLabel.setText(String.format("%.0fmL", wineToDisplay.getVolumeInMl()));

        if (!wineToDisplay.getCountry().isEmpty()) {
            countryContentsLabel.setText(wineToDisplay.getCountry());
        } else {
            countryContentsLabel.setVisible(false);
            countryTitleLabel.setVisible(false);
            countryContentsLabel.setManaged(false);
            countryTitleLabel.setManaged(false);
        }

        if (!wineToDisplay.getStyle().isEmpty() ) {
            styleContentsLabel.setText(wineToDisplay.getStyle());
        } else {
            styleContentsLabel.setVisible(false);
            styleTitleLabel.setVisible(false);
            styleContentsLabel.setManaged(false);
            styleTitleLabel.setManaged(false);
        }

        if (wineToDisplay.getYear() != 0) {
            yearContentsLabel.setText(String.format("%d", wineToDisplay.getYear()));
        } else {
            yearContentsLabel.setVisible(false);
            yearTitleLabel.setVisible(false);
            yearContentsLabel.setManaged(false);
            yearTitleLabel.setManaged(false);
        }

        if (!wineToDisplay.getLongDescription().isEmpty()) {
            descriptionContentsLabel.setText(wineToDisplay.getLongDescription());
        } else {
            descriptionContentsLabel.setText("This wine has no description");
        }

        for (String award : wineToDisplay.getAwards()) {
            if (award != null && !award.isEmpty()) {
                awardsContentsLabel.setText(awardsContentsLabel.getText() + award + '\n');
            }
        }

        if (awardsContentsLabel.getText().isEmpty()) {
            awardsContentsLabel.setText("This wine has no awards");
        }

        try {
            wineViewImageView.setImage(new Image("/images/" + wineToDisplay.getColour() + "_wine_image.png"));
        } catch (Exception e) {
            log.warn("Image file did not load correctly", e);
        }

        GuiService.setUpPopUp(overlayPane, popupAnchorPane);

        awardsScrollPane.getStyleClass().add("white-wine-scroll-pane");
        descriptionScrollPane.getStyleClass().add("white-wine-scroll-pane");
        exitButton.getStyleClass().add("nav-bar-button");
        wineViewRectangle.getStyleClass().add("white-white-wine-rectangle");
    }

    /**
     * used by JavaFX as the onAction for the exit button. calls the remove popup method in FXWrapper to close this popup
     */
    @FXML
    public void onExitButtonClicked() {
        FXWrapper.getInstance().removePopUp(overlayPane);
    }
}
