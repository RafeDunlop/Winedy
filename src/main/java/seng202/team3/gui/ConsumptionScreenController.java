package seng202.team3.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import seng202.team3.models.TimeRange;
import seng202.team3.models.WineLog;
import seng202.team3.services.LogManager;
import seng202.team3.services.WineManager;

import java.util.HashMap;
import java.util.List;

/**
 * controller for tracking_consumption_screen.fxml
 *
 * @author Rafe Dunlop (rdu46)
 */
public class ConsumptionScreenController {

    @FXML
    private BarChart<?, ?> consumptionChart;

    @FXML
    private ComboBox<TimeRange> timeRangeComboBox;

    @FXML
    private VBox logVBox;

    @FXML
    private Rectangle graphBannerRectangle;

    @FXML
    private Rectangle graphRectangle;

    @FXML
    private Rectangle logRectangle;

    @FXML
    private Rectangle logBannerRectangle;

    @FXML
    private Button logButton;

    /**
     * WineLog manager for services
     */
    private LogManager logManager;

    /**
     * selected TimeRange
     */
    private TimeRange timeRange;

    /**
     * the number of logs to display on each page of the ScrollPane
     */
    private final int logsPerPage = 10;

    /**
     * all the user's WineLogs
     */
    private List<WineLog> logs;

    /**
     * maps an index to a load for the pagination of the logs display
     */
    private HashMap<Integer, ScrollPane> pageMap;

    /**
     * initializes the controller, calling function to add style classes, setup variables, setup combo boxes and pagination
     */
    public void initialize() {

        addStyleClasses();
        this.logManager = LogManager.getInstance();
        logs = logManager.getAllLogs();
        timeRangeComboBox.getItems().addAll(TimeRange.getAll());
        timeRangeComboBox.setConverter(TimeRange.getStringConverter());
        timeRangeComboBox.setOnAction(event -> {
                    timeRange = timeRangeComboBox.getSelectionModel().getSelectedItem();
                    loadGraph();
        });
        timeRangeComboBox.getSelectionModel().select(0);

        int numberOfPages = (logs.size() % logsPerPage == 0) ? logs.size() / logsPerPage : (logs.size() / logsPerPage) + 1;

        Pagination pagination = new Pagination(numberOfPages, 0);
        if (numberOfPages < 2) {
        }

        pageMap = new HashMap<>();
        for (int i = 0; i < numberOfPages; i++) {
            pageMap.put(i, createPage(i));
        }
        pagination.setPageFactory(i -> pageMap.get(i));
        logVBox.getChildren().add(pagination);
    }

    /**
     * creates a ScrollPane containing up to 10 WineLog buttons or al that remain
     * implemented as a ScrollPane with a VBox nested inside
     * @param pageIndex the index, key of index -> ScrollPane map
     * @return the fully formed ScrollPane
     */
    private ScrollPane createPage(int pageIndex) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setPrefWidth(570);
        int start = pageIndex * logsPerPage;
        int end = Math.min(start + logsPerPage, logs.size());
        for (int i = start; i < end; i++) {
            WineLog log = logs.get(i);

            HBox buttonGraphic = new HBox();
            String buttonString = WineManager.getInstance().getWineById(log.getUniqueWineId()).getName() +
                    "\nGlasses: " +
                    log.getStandards(); //extract
            buttonGraphic.getChildren().add(new Text(logManager.getLogDateString(log) + "|"));
            buttonGraphic.getChildren().add(new Text(buttonString));

            Button button = new Button();

            button.setGraphic(buttonGraphic);
            button.setOnAction(event -> FXWrapper.getInstance().loadLogPopup(log));
            button.setPrefSize(500, 80);
            vbox.getChildren().add(button);
;        }
        ScrollPane page = new ScrollPane(vbox);
        page.setPrefSize(570, 445);
        page.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return page;
    }

    /**
     * loads the graph for the specified time range
     */
    private void loadGraph() {

    }

    /**
     * adds to all relevant fx nodes, their relevant style classes
     */
    private void addStyleClasses() {
        timeRangeComboBox.getStyleClass().add("fifteen-combo-box");
        logRectangle.getStyleClass().add("red-wine-rectangle");
        logBannerRectangle.getStyleClass().add("white-wine-rectangle");
        graphRectangle.getStyleClass().add("white-wine-rectangle");
        graphBannerRectangle.getStyleClass().add("white-wine-rectangle");
        logButton.getStyleClass().add("nav-bar-button");
    }

    /**
     * method called when the "Log a wine" button is clicked. launches a relevant popup
     */
    @FXML
    void onLogClicked() {

    }

}