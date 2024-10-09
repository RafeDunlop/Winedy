package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.Wine;

public class MiniIndividualWineViewController {

    /**
     * Logger for robust error logging and debugging
     */
    private static final Logger log = LogManager.getLogger(IndividualWineViewController.class);

    @FXML
    private Label wineNameLabel;

    @FXML
    private Label wineFullnessLabel;

    @FXML
    private Label winePriceLabel;


    @FXML
    private Rectangle rectangle;

    @FXML
    private Button viewInDetailButton;

    @FXML
    private Label wineStyleLabel;
    @FXML
    private Label wineColourLabel;

    /**
     * The Wine object whose details are displayed on the screen
     */
    private final Wine wineToDisplay;

    /**
     * Constructor to pass the Wine to be displayed into the controller
     *
     * @param wineToDisplay Wine whose details are to be displayed
     */
    public MiniIndividualWineViewController(Wine wineToDisplay) {
        this.wineToDisplay = wineToDisplay;
    }

    /**
     * Initializes the fxml file and sets the relevant styles.
     * Displays all non-null attributes which are to be displayed to their outlined labels and makes invisible any
     * that are null along with their indicator labels.
     */
    @FXML
    public void initialize() {
        rectangle.getStyleClass().add("white-wine-rectangle");
        viewInDetailButton.getStyleClass().add("nav-bar-button");
        wineNameLabel.setText(wineToDisplay.getName());
        wineFullnessLabel.setText("Fullness: " + wineToDisplay.getFullness());
        winePriceLabel.setText("Price: $" + wineToDisplay.getPricePerBottle());
        wineStyleLabel.setText("Style: " + wineToDisplay.getStyle());
        wineColourLabel.setText("Colour: " + wineToDisplay.getColour());
        log.info("Individual wine view loaded successfully");
    }

    /**
     * Used by JavaFX as the OnAction of the View In Detail Button.
     */
    @FXML
    public void onViewInDetailButtonClicked() {
        FXWrapper.getInstance().loadIndividualWineViewPopup(wineToDisplay);
    }

}
