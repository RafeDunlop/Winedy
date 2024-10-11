package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.Wine;
import seng202.team3.models.WineList;

import java.util.List;

/**TODO remove if not used (as well as the fxml file)
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
    private VBox rootVBox;

    /**
     * The Wine List to be displayed on the screen
     */
    private final List<Wine> winesToDisplay;

    /**
     * The AnchorPane where the wine details should be displayed
     */
    private final AnchorPane wineDetailsAnchorPane;

    /**
     * The number of rows of wines per page
     */
    private int rowsPerPage = 4;

    /**
     * The number of wines per row
     */
    private int winesPerRow = 3;

    /**
     * Height of scrollpane required for a particular screen
     */
    private int scrollPaneHeight;

    /**
     * Constructor for the WineListViewController.
     * Sets the wine list to display to be the given wine list.
     *
     * @param wineListToDisplay the wine list to be assigned
     * @param wineDetailsAnchorPane the AnchorPane that the wine details should be inserted into on clicking
     * @param scrollPaneHeight the height of the ScrollPane in the wine list view
     */
    public WineListViewController(WineList wineListToDisplay, AnchorPane wineDetailsAnchorPane, int scrollPaneHeight) {
        this.winesToDisplay = wineListToDisplay.getWineList();
        this.wineDetailsAnchorPane = wineDetailsAnchorPane;
        this.scrollPaneHeight = scrollPaneHeight;
    }

    /**
     * Called by JavaFX upon initialisation of the Wine List View Screen
     */
    public void initialize() {
        log.info("Wine list view loaded");
        //createPagination();
        wineScrollPane.getStyleClass().add("red-wine-scroll-pane");
    }

    /**
     * Creates paginated view to display wines
     */



}
