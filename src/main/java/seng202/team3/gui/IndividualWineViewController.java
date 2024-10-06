package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.Wine;
import seng202.team3.services.IndividualWineViewService;
import seng202.team3.services.WineDrinkerManager;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for individual_wine_view.fxml
 * to be nested onto other panes to display wine details
 *
 * @author Sophia Copley (sco207)
 */
public class IndividualWineViewController {

    /**
     * Logger for robust error logging and debugging
     */
    private static final Logger log = LogManager.getLogger(IndividualWineViewController.class);

    private IndividualWineViewService individualWineService;

    @FXML
    private Label wineNameLabel;

    @FXML
    private Label wineCountryLabel;

    @FXML
    private Label countryLabel;

    @FXML
    private Label wineStyleLabel;

    @FXML
    private Label styleLabel;

    @FXML
    private Label fullnessLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label ABVLabel;

    @FXML
    private Label volumeLabel;

    @FXML
    private Label notLoggedInLabel;

    @FXML
    private Rectangle rectangle;

    @FXML
    private Button likeButton;

    @FXML
    private Button addToListButton;

    @FXML
    private Button viewInDetailButton;


    /**
     * the Wine object whose details are displayed on the screen
     */
    private final Wine wineToDisplay;

    /**
     * Constructor to pass the Wine to be displayed into the controller
     * @param wineToDisplay Wine whose details are to be displayed
     */
    public IndividualWineViewController(Wine wineToDisplay) {
        this.wineToDisplay = wineToDisplay;
    }

    /**
     * initializes the fxml file and sets the relevant styles.
     * displays all non-null attributes which are to be displayed to their outlined labels and makes invisible any
     * that are null along with their indicator labels
     */
    @FXML
    public void initialize() {

        individualWineService = new IndividualWineViewService();
        WineDrinkerManager wineDrinkerManager = WineDrinkerManager.getInstance();

        rectangle.getStyleClass().add("white-wine-rectangle");
        likeButton.getStyleClass().add("like-button");
        addToListButton.getStyleClass().add("add-to-list-button");
        viewInDetailButton.getStyleClass().add("nav-bar-button");
        wineNameLabel.setText(wineToDisplay.getName());
        fullnessLabel.setText(wineToDisplay.getFullness());
        priceLabel.setText("$" + wineToDisplay.getPricePerBottle());
        ABVLabel.setText(wineToDisplay.getAlcoholByVolume() + "%");
        volumeLabel.setText(wineToDisplay.getVolumeInMl() + "mL");
        notLoggedInLabel.setStyle("-fx-text-fill: -fx-dark-red-wine-colour");

        if (!wineToDisplay.getStyle().isEmpty()) {
            wineStyleLabel.setVisible(true);
            wineStyleLabel.setText(wineToDisplay.getStyle());
            styleLabel.setVisible(true);
        }
        if (!wineToDisplay.getCountry().isEmpty()) {
            wineCountryLabel.setVisible(true);
            wineCountryLabel.setText(wineToDisplay.getCountry());
            countryLabel.setVisible(true);
        }

        if (wineDrinkerManager.getCurrentUser() == null) {
            likeButton.setDisable(true);
            likeButton.setOpacity(0.5);
            addToListButton.setDisable(true);
            addToListButton.setOpacity(0.5);
            notLoggedInLabel.setVisible(true);
        }  else {
           if (individualWineService.inFavourites(wineToDisplay)) {
               likeButton.setStyle(individualWineService.inFavourites(wineToDisplay)? "-fx-background-color: -fx-dark-red-wine-colour" : "");
           }
        }

        log.info("Individual wine view loaded successfully");
    }

    @FXML
    public void onLikeButtonClicked() {
        individualWineService.updateFavourites(wineToDisplay);
        likeButton.setStyle(individualWineService.inFavourites(wineToDisplay)? "-fx-background-color: -fx-dark-red-wine-colour" : "");
    }

    @FXML
    public void onAddButtonClicked() {

    }

    @FXML
    public void onViewInDetailButtonClicked() {

    }

}
