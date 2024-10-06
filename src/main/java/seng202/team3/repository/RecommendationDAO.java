package seng202.team3.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.DrinkerPreferenceModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Recommendation DAO retrieves the users preference model for the recommendation algorithm
 *
 * @author Steven Leishman (sle159)
 */
public class RecommendationDAO {

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(WineDAO.class);
    /**
     * Database manager instance to manage database connections
     */
    private final DatabaseManager databaseManager;



    public RecommendationDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Retrieves the set of preferences from the database
     *
     * @param username string of record to retrieve
     * @return drinkerPreferenceModel filled with retrieved data
     */
    public DrinkerPreferenceModel getPreferenceModelByUsername(String username) {
        DrinkerPreferenceModel drinkerPrefModel = null;
        String sql = "SELECT * FROM drinkerPreferenceModel WHERE username=?";
        String columnNameSql = "select name from pragma_table_info('drinkerPreferenceModel')";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement psColName = conn.prepareStatement(columnNameSql)) {
            ps.setString(1, username);
            try (ResultSet resultSet = ps.executeQuery();
                ResultSet colNamesSet = psColName.executeQuery()) {

                ArrayList<String> colNames = new ArrayList<>();
                while (colNamesSet.next()){
                    if (colNamesSet.getString(1).compareTo( "username") != 0){
                        colNames.add(colNamesSet.getString(1));
                    }
                }
                if (resultSet.next()) {
                    drinkerPrefModel = new DrinkerPreferenceModel();
                    drinkerPrefModel.setUsername(resultSet.getString("username"));
                    ArrayList<Float> prefValues = new ArrayList<>();
                    prefValues.add(resultSet.getFloat(2));
                    for (int i = 3; i < 50; i++) {
                        prefValues.add(resultSet.getFloat(i));
                    }
                    drinkerPrefModel.setHashMapValues( colNames, prefValues);
                }
                return drinkerPrefModel;
            }
        } catch (SQLException e) {
            log.error(e);
        }
        return drinkerPrefModel;
    }


    /**
     * Retrieves the all distinct attributes from specified column in table
     * and adds those attributes as columns in the drinkerPreferenceModel table
     * with default value 5
     *
     * @param nameOfColumn string column to retrieve values from
     */
    public void addColumnsToPrefModelFromPopulatedTables(String nameOfColumn, String table) {
        String sql = "select distinct " + nameOfColumn + " from " + table;
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        String sql2 = "ALTER TABLE drinkerPreferenceModel ADD '" + resultSet.getString(1) + "' float Default 5";
                        PreparedStatement ps2 = conn.prepareStatement(sql2);
                        ps2.execute();
                    } catch (SQLException e) {
                        log.error(e);
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e);
        }
    }

    /**
     * Creates a new drinkerPreferenceModel tuple
     * by initialising the username and setting all values to defaults
     *
     * @param username string of username for primary key
     */
    public void createNewPreferenceModel (String username) {
        String sql = "INSERT INTO drinkerPreferenceModel (username) VALUES (?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1,username);
            ps.executeUpdate();
        } catch(SQLException e){
            log.error(e);
        }
    }

    /**
     * Updates individual preference of the currently logged-in user
     * with the given input
     * @param username string representation of current user's username
     * @param prefToUpdate string of preference to update
     * @param newPrefValue new float value of updated preference
     */
    public void updateIndividualPreferenceVal(String username, String prefToUpdate, float newPrefValue) {
        if (newPrefValue >= 0 && newPrefValue <= 10){
            String sql = "UPDATE drinkerPreferenceModel SET '" + prefToUpdate + "'=? where username = ?";
            try (Connection conn = databaseManager.connect();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setFloat(1,newPrefValue);
                ps.setString(2,username);
                ps.executeUpdate();
            } catch (SQLException e) {
                log.error(e);
            }
        } else {
            log.warn("Preference value already at max/min value - ignored");
        }
    }
}
