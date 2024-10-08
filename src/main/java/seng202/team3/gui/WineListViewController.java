package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.Wine;
import seng202.team3.models.WineList;

import java.util.List;

/**
 * Controller for the wine_list_view.fxml
 *
 * @author Hannah Botting (hbo51)
 */
public class WineListViewController {

    /**
     * Logger for robust error and info logging
     */
    private static final Logger log = LogManager.getLogger(FXWrapper.class);

    @FXML
    private ScrollPane wineScrollPane;

    @FXML
    private VBox wineVBox;

    /**
     * The Wine List to be displayed on the screen
     */
    private final List<Wine> wineToDisplay;

    /**
     * The AnchorPane where the wine details should be displayed
     */
    private final AnchorPane wineDetailsAnchorPane;

    /**
     * Constructor for the WineListViewController.
     * Sets the wine list to display to be the given wine list.
     *
     * @param wineListToDisplay the wine list to be assigned
     * @param wineDetailsAnchorPane the AnchorPane that the wine details should be inserted into on clicking
     */
    public WineListViewController(WineList wineListToDisplay, AnchorPane wineDetailsAnchorPane) {
        this.wineToDisplay = wineListToDisplay.getWineList();
        this.wineDetailsAnchorPane = wineDetailsAnchorPane;
    }

    /**
     * Called by JavaFX upon initialisation of the Wine List View Screen
     */
    public void initialize() {
        log.info("Wine list view loaded");
        GuiService.startButtonGeneration(wineToDisplay, wineVBox, wineDetailsAnchorPane, 3);
        wineScrollPane.getStyleClass().add("red-wine-scroll-pane");
    }

}
