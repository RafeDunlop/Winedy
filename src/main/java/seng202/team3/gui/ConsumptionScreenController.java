package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Pair;
import seng202.team3.models.TimeRange;
import seng202.team3.models.WineLog;
import seng202.team3.services.LogManager;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import static seng202.team3.gui.GuiService.fullDisable;
import static seng202.team3.models.TimeRange.getStringRep;

/**
 * controller for tracking_consumption_screen.fxml
 *
 * @author Rafe Dunlop (rdu46)
 */
public class ConsumptionScreenController {

    @FXML
    private BarChart<String, Float> consumptionChart;

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

    @FXML
    private Label noDataLabel;

    @FXML
    private Label noLogsLabel;

    @FXML
    private Rectangle noLogsRectangle;

    @FXML
    private Rectangle noDataRectangle;

    /**
     * WineLog manager for services
     */
    private LogManager logManager;

    /**
     * the number of logs to display on each page of the ScrollPane
     */
    private final int logsPerPage = 10;

    /**
     * all the user's WineLogs corresponding to teh current date
     */
    private List<WineLog> logs;

    private TimeRange selectedTimeRange;


    /**
     * initializes the controller, calling function to add style classes, setup variables, setup combo boxes and pagination
     */
    public void initialize() {
        addStyleClasses();
        this.logManager = LogManager.getInstance();

        timeRangeComboBox.getItems().addAll(TimeRange.getAll());
        timeRangeComboBox.setConverter(TimeRange.getStringConverter());
        timeRangeComboBox.setOnAction(event -> loadLogData(timeRangeComboBox.getValue()));
        timeRangeComboBox.getSelectionModel().select(logManager.getPrevRange());
        loadLogData(logManager.getPrevRange());
    }

    private void loadLogData(TimeRange timeRange) {
        selectedTimeRange = timeRange;
        logManager.setTimeRange(timeRange);
        Pair<Date, Date> dateRange = TimeRange.getDateRange(timeRange);
        logs = logManager.getLogsInRange(dateRange.getKey(), dateRange.getValue());
        boolean empty = logs.isEmpty();
        fullDisable(noDataLabel, !empty);
        fullDisable(noDataRectangle, !empty);
        fullDisable(noLogsLabel, !empty);
        fullDisable(noLogsRectangle, !empty);
        fullDisable(consumptionChart, empty);
        fullDisable(logVBox, empty);
        if (!empty) {
            createPaginationLogs();
            loadGraph();
        }
    }

    private void createPaginationLogs() {
        logVBox.getChildren().clear();

        int numberOfPages = (logs.size() % logsPerPage == 0) ? logs.size() / logsPerPage : (logs.size() / logsPerPage) + 1;
        Pagination pagination = new Pagination(numberOfPages, 0);
        logVBox.getChildren().add(pagination);
        pagination.getStyleClass().add("wine-pagination");
        pagination.setPrefHeight(900);

        pagination.setPageFactory(pageIndex -> {
            VBox scrollContent = new VBox();
            scrollContent.setSpacing(10);
            int start = pageIndex * logsPerPage;
            int end = Math.min(start + logsPerPage, logs.size());
            logs.subList(start, end).forEach(log -> genButton(log, scrollContent));
            ScrollPane scrollPane = new ScrollPane(scrollContent);
            scrollPane.setFitToWidth(true);
            scrollPane.getStyleClass().add("red-wine-scroll-pane");
            return scrollPane;
        });
    }

    private void genButton(WineLog log, VBox scrollContent) {
        HBox buttonGraphic = new HBox();
        buttonGraphic.getChildren().add(genDateButtonText(log));
        buttonGraphic.getChildren().add(genLogLabel(log));
        buttonGraphic.setAlignment(Pos.CENTER);

        Button button = new Button();

        button.setGraphic(buttonGraphic);
        button.setOnAction(event -> {
            System.out.println(log);
            FXWrapper.getInstance().loadLogPopup(log, null, Screen.TRACKINGCONSUMPTIONSCREEN);
        });
        button.setPrefSize(560, 80);
        button.setMaxWidth(540);
        button.getStyleClass().add("nav-bar-button");
        scrollContent.getChildren().add(button);
    }

    private Node genLogLabel(WineLog toGen) {
        VBox vBox = new VBox();
        String[] logString = toGen.toString().split("\n");


        Text wineText = new Text(logString[0]);
        wineText.setFont(new Font("System", 20));
        wineText.setWrappingWidth(350);

        Text stdText = new Text(logString[1]);
        stdText.setFont(Font.font("System", FontWeight.BOLD, 18));

        vBox.getChildren().add(wineText);
        vBox.getChildren().add(stdText);
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.setPrefWidth(350);

        return vBox;
    }

    private Node genDateButtonText(WineLog toGen) {
        HBox hBox = new HBox();

        VBox vBox = new VBox();

        Label datelabel = new Label(logManager.getLogDateString(toGen));
        datelabel.setFont(new Font("System", 24));
        datelabel.setAlignment(Pos.CENTER_LEFT);

        Label timeLabel = new Label(logManager.getHourConverter().toString(toGen.getTime().toLocalTime().getHour()));
        timeLabel.setFont(new Font("System", 18));

        Rectangle vBar = new Rectangle(2, 70);
        vBar.getStyleClass().add("red-wine-rectangle");

        vBox.getChildren().add(datelabel);
        vBox.getChildren().add(timeLabel);
        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER_LEFT);

        hBox.getChildren().add(vBox);
        hBox.getChildren().add(vBar);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPrefWidth(200);

        return hBox;
    }

    /**
     * loads the graph for the specified time range
     */
    private void loadGraph() {
        consumptionChart.getData().clear();

        consumptionChart.getXAxis().setLabel(TimeRange.getLabel(selectedTimeRange));
        consumptionChart.getXAxis().setTickLabelFont(new Font("System", 20));
        consumptionChart.getYAxis().setTickLabelFont(new Font("System", 20));
        consumptionChart.getYAxis().setTickMarkVisible(false);
        consumptionChart.getYAxis().setTickLength(10);
        consumptionChart.setStyle("-fx-background-color: #EEEDC4;");
        consumptionChart.getYAxis().setLabel("Standard drinks");
        consumptionChart.setLegendVisible(false);


        XYChart.Series<String, Float> dataSeries = new XYChart.Series<>();
        for (Map.Entry<Integer, List<WineLog>> entry : selectedTimeRange.timePeriod.splitIntoPeriods(logs).entrySet()) {
            float standards = 0;
            for (WineLog log : entry.getValue()) {
                standards += log.getStandards();
            }
            dataSeries.getData().add(new XYChart.Data<>(getStringRep(entry.getKey(), selectedTimeRange), standards));
        }

        consumptionChart.getData().add(dataSeries);
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
        noDataRectangle.getStyleClass().add("white-red-wine-rectangle");
        noLogsRectangle.getStyleClass().add("white-wine-rectangle");
        consumptionChart.getStyleClass().add("bar-chart");
    }

    /**
     * method called when the "Log a wine" button is clicked. launches a relevant popup
     */
    @FXML
    void onLogClicked() {
        FXWrapper fxWrapper = FXWrapper.getInstance();
        fxWrapper.addPreviousScreen(() -> fxWrapper.loadProfileTabPane(2));
        FXWrapper.getInstance().loadLogPopup(null, null, Screen.TRACKINGCONSUMPTIONSCREEN);
    }

}