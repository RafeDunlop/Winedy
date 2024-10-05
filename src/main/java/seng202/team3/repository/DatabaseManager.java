package seng202.team3.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.Wine;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.List;

/**
 * Singleton class responsible for interaction with SQLite database
 *
 * @author Yuvraj Singh Fagotra (Yfa50), Steven Leishman(sle159)
 */
public class DatabaseManager {

    /**
     * Database instance
     */
    private static DatabaseManager instance = null;

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(DatabaseManager.class);

    /**
     * Database url
     */
    private final String url;

    /**
     * Private constructor for singleton purposes
     * Creates database if it does not already exist in specified location
     */
    private DatabaseManager(String urlIn) {
        if (urlIn==null || urlIn.isEmpty()){
            this.url = getDatabasePath();
        } else {
            this.url = urlIn;
        }
        if(!checkDatabaseExists(url)){
            createDatabaseFile(url);
            log.info("Resetting database");
            resetDB();
            log.info("Populating database");
            try {
                populateWineTables("/csv/majestic_df_preprocessed.csv");
                initialisePreferenceModelTable();
            } catch (URISyntaxException | FileNotFoundException e) {
                log.error("Error populating database", e);
            }
        }
    }

    /**
     * initialise the preferencemodel table by dynamically setting column names
     * by attributes read from populated tables
     */
    private static void initialisePreferenceModelTable(){
        RecommendationDAO recommendationDAO = new RecommendationDAO();
        recommendationDAO.addColumnsToPrefModelFromPopulatedTables("fullness", "wineSuper");
        recommendationDAO.addColumnsToPrefModelFromPopulatedTables("colour", "wineSuper");
        recommendationDAO.addColumnsToPrefModelFromPopulatedTables("name", "grape");
    }


    /**
     * Singleton method to get current Instance if exists otherwise create it
     * @return the single instance DatabaseSingleton
     */
    public static DatabaseManager getInstance(){
        if (instance == null) {
            instance = new DatabaseManager(null);
        }
        return instance;
    }

    /**
     * getInstance method where a url can be passed into the function. This function can handle the case where the url is
     * null as well.
     * @return the single instance DatabaseSingleton for a database located at the given url
     */
    public static DatabaseManager getInstance(String url){
        if (instance == null) {
                instance = new DatabaseManager(url);
        }
        return instance;
    }

    /**
     *  WARNING Sets the current singleton instance to null
     */
    public static void REMOVE_INSTANCE() {
        instance = null;
    }

    /**
     * Connect to the database
     * @return database connection
     */
    public Connection connect(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            log.error("Error connecting to database", e);
        }
        return conn;
    }

    /**
     * Initialises the database using the sql script included in resources
     */
    private void resetDB() {
        try {
            InputStream in = getClass().getResourceAsStream("/sql/initialise_wine_database.sql");
            executeSQLScript(in);
        } catch (NullPointerException e) {
            log.error("Error loading database initialisation file", e);
        }
    }

    /**
     * Gets path to the database relative to the jar file
     * @return jdbc encoded url location of database
     */
    private String getDatabasePath() {
        String path = DatabaseManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File jarDir = new File(path);
        return "jdbc:sqlite:"+jarDir.getParentFile()+"/database.db";
    }

    /**
     * Check that a database exists in the expected location
     * @param url expected location to check for database
     * @return True if database exists else false
     */
    private boolean checkDatabaseExists(String url){
        File f = new File(url.substring(12));
        return f.exists();
    }

    /**
     * Creates a database file at the location specified by the url
     * @param url url to creat database at
     */
    private void createDatabaseFile(String url) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                String metaDriverLog = String.format("A new database has been created at %s. The driver name is %s", meta.getURL(), meta.getDriverName());
                log.info(metaDriverLog);
            }
        } catch (SQLException e) {
            log.error(String.format("Error creating new database file url:%s", url));
            log.error(e);
        }
    }

    /**
     * Reads and executes all statements within the sql file provided
     * Note that each statement must be separated by '--Split' this is not a desired limitation but allows for a much
     * wider range of statement types.
     * @param sqlFile input stream of file containing sql statements for execution (separated by --SPLIT)
     */
    private void executeSQLScript(InputStream sqlFile) {
        String s;
        StringBuffer sb = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(sqlFile))) {
            while((s=br.readLine()) != null) {
                sb.append(s);
            }

            String[] individualStatements = sb.toString().split("--Split");
            try (Connection conn = this.connect();
                 Statement statement = conn.createStatement()) {
                for (String singleStatement : individualStatements) {
                    statement.executeUpdate(singleStatement);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("Error could not find specified database initialisation file", e);
        } catch (IOException e) {
            log.error("Error working with database initialisation file", e);
        } catch (SQLException e) {
            log.error("Error executing sql statements in database initialisation file", e);
        }
    }

    /**
     * Populates the wine table in the database with the wine data from the file specified by the filePath.
     * @param filePath The path of the input file that contains the data
     */
    private void populateWineTables(String filePath) throws URISyntaxException, FileNotFoundException {
        if (!checkDatabaseExists(url)) {
            throw new FileNotFoundException();
        }
        InputStream inputStream = getClass().getResourceAsStream(filePath);
        List<Wine> wines = WineCSVImporter.readFromFile(inputStream);
        WineDAO wineDAO = new WineDAO(url);
        int i = 0;
        while (i < wines.size()) {
            if (i + 100 > wines.size()) {
                wineDAO.addBatch(wines.subList(i, wines.size()));
            } else {
                wineDAO.addBatch(wines.subList(i, i + 100));
            }
            i += 100;
        }
    }
}
