package seng202.team3.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

/**
 * Class starts the javaFX application window
 *
 * @author seng202 teaching team
 */
public class MainWindow extends Application {

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(MainWindow.class);

    /**
     * Opens the gui with the fxml content specified in resources/fxml/main.fxml
     * Adds the stylesheets to the scene
     * @param primaryStage The current fxml stage, handled by javaFX Application class
     * @throws IOException if there is an issue loading fxml file
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/super_pane.fxml"));
        Parent root = baseLoader.load();

        primaryStage.setTitle("Winedy");
        Scene scene = new Scene(root, 1200, 800);

        String[] cssPaths = new String[]{
                "/css/date_combo_box.css",
                "/css/help_screen_contents_button.css",
                "/css/home_screen_button.css",
                "/css/home_screen_text.css",
                "/css/individual_wine_view_scroll_pane.css",
                "/css/nav_bar_button.css",
                "/css/nav_bar_rectangle.css",
                "/css/nav_bar_winedy_rectangle.css",
                "/css/red_wine_button.css",
                "/css/red_wine_rectangle.css",
                "/css/white_wine_rectangle.css",
                "/css/white_wine_scroll_pane.css",
                "/css/red_wine_scroll_pane.css",
                "/css/profile_tab_pane.css",
                "/css/add_to_list_button.css",
                "/css/like_button.css"
        };
        try {
            for (String cssFilePath : cssPaths) {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(cssFilePath)).toExternalForm());
            }
        } catch (NullPointerException e) {
            log.error("check for error in css paths", e);
        }
            primaryStage.setScene(scene);
            primaryStage.show();

    }

    /**
     * Launches the FXML application, this must be called from another class (in this cass App.java) otherwise JavaFX
     * errors out and does not run
     * @param args command line arguments
     */
    public static void main(String [] args) {
        launch(args);
    }

}
