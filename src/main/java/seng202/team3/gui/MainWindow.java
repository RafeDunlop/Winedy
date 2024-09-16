package seng202.team3.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            Path cssPath = Paths.get(Objects.requireNonNull(getClass().getResource("/css")).toURI());

            Files.list(cssPath).forEach(path -> {
                if (path.toString().endsWith(".css")) { // This should always be true but prevents errors from occurring if someone adds a non css file
                    String cssFilePath = "/css/" + path.getFileName().toString();
                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(cssFilePath)).toExternalForm());
                }
            });
        } catch (NullPointerException e) {
            log.error("Error loading CSS style sheets. Did you misspell the path?", e);
        } catch (URISyntaxException e) {
            log.error("A file could not be parsed as a URI reference.", e);
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
