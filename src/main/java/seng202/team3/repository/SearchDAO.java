package seng202.team3.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Search DAO class that handles all wine related actions to the database
 *
 * @author Yuvraj Fagotra (yfa50)
 */
public class SearchDAO {

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(WineDAO.class);

    /**
     * Database manager instance to manage database connections
     */
    private final DatabaseManager databaseManager;

    /**
     * Creates a new WineDAO object and gets a reference to the database singleton
     */
    public SearchDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Creates a new WineDAO object and gets a reference to the database singleton for a database at the specified url.
     * Used for testing.
     *
     * @param url the relative url where the test database is located
     */
    public SearchDAO(String url) {
        databaseManager = DatabaseManager.getInstance(url);
    }

    /**
     * Gets all distinct values that occur in a given attribute (column) in the database
     *
     * @param attribute the attribute whose values are being selected from the database
     * @param tableName the name of the table whose attribute values are being selected
     * @return a list of the distinct attribute values retrieved from the database
     */
    public ArrayList<String> getWineAttributeValues(String attribute, String tableName) {

        ArrayList<String> values = new ArrayList<>();

        String sql = "SELECT DISTINCT " + attribute + " FROM " + tableName;
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet resultset = ps.executeQuery();

            while (resultset.next()) {
                String result = resultset.getString(attribute);
                if (result != null && !result.isEmpty()) {
                    values.add(resultset.getString(attribute));
                }
            }

        } catch (SQLException sqlException) {
            log.error("Error verifying attribute in database", sqlException);
        }

        return values;
    }

    /**
     * Returns true if the attribute is a valid column in the given table, false if not.
     *
     * @param attribute the attribute to be validated
     * @param tableName the table to check that attribute is in
     * @return the truth value of the attribute being a valid column in the table
     */
    public boolean isValidAttribute(String attribute, String tableName) {

        String sql = "SELECT name FROM pragma_table_info(?) WHERE name = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, attribute);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();

        } catch (SQLException sqlException) {
            log.error("Error verifying attribute in database", sqlException);
        }

        return false;
    }
}
