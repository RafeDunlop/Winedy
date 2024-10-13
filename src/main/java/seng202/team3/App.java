package seng202.team3;

import seng202.team3.gui.MainWindow;
import seng202.team3.repository.DatabaseManager;

/**
 * Default entry point class
 * @author seng202 teaching team
 */
public class App {

    /**
     * Create an instance of DatabaseManger if not already existing and then run the app using MainWindow
     * @param args program arguments from command line
     */
    public static void main(String[] args) {
        DatabaseManager.getInstance();
        MainWindow.main(args);
    }
}
