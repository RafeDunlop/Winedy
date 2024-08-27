package seng202.team3.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.App;

import java.io.IOException;
import java.util.Objects;

/**
 * Class starts the javaFX application window
 * @author seng202 teaching team
 */
public class MainWindow extends Application {
    private static final Logger log = LogManager.getLogger(MainWindow.class);

    /**
     * Opens the gui with the fxml content specified in resources/fxml/main.fxml
     * @param primaryStage The current fxml stage, handled by javaFX Application class
     * @throws IOException if there is an issue loading fxml file
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/super_pane.fxml"));
        Parent root = baseLoader.load();

        primaryStage.setTitle("Winedy");
        Scene scene = new Scene(root, 1200, 800);
        try {
            //TODO: write looping method to make this cleaner
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/red_wine_button.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/white_wine_rectangle.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/home_screen_button.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/red_wine_rectangle.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/home_screen_text.css")).toExternalForm());
        } catch (NullPointerException e) {
            log.error("Error loading CSS style sheets. Did you misspell the path?", e);
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
