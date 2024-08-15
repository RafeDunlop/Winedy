package seng202.team3.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.IIOException;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * Singleton class that handles initialization and connection to the SQLite database file
 * @author Yuvraj Singh Fagotra
 */
public class DatabaseManager {

    private static DatabaseManager database = null;
    private final String url;
    private static final Logger log = LogManager.getLogger(DatabaseManager.class);

    /**
     * Constructor that is only used internally to make sure exactly one instance of the database exists at a time
     */
    private DatabaseManager() {

        this.url = getPath();
        if (!checkDatabaseExists()) {
            initializeDatabase();
        }

    }

    /**
     *
     * @return
     */
    public static DatabaseManager getInstance() {

        if (database == null) {
            database = new DatabaseManager();
        }
        return database;
    }

    public Connection connect() {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * Attempts to load the database file at specified path
     * @return True if database exists and False otherwise
     */
    private boolean checkDatabaseExists() {
        File f = new File(url.substring(12)); //The call to substring gets rid of "jdbc:sqlite:" from path
        return f.exists();
    }

    private static String getPath() {
        String path = DatabaseManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        //path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File jarDir = new File(path);
        return "jdbc:sqlite:" + jarDir.getParentFile() + "/Winedy.sqlite";
    }

    private static void initializeDatabase() {

        Connection conn = database.connect();
//        InputStream script = DatabaseManager.class.getResourceAsStream("/sql/initialise_wine_database.sql");
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader());

    }

    public static void main(String[] args) {

        InputStream script = DatabaseManager.class.getResourceAsStream("/sql/initialise_wine_database.sql");
        StringBuilder scriptLines = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(script));

        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                scriptLines.append(line);
            }

            String[] scriptList = scriptLines.toString().split("--Split");

            Connection conn = DatabaseManager.getInstance().connect();
            Statement statement = conn.createStatement();
            for (String scriptLine: scriptList) {
                statement.execute(scriptLine);
            }
        

        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}
