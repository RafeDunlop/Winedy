package seng202.team3.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.DrinkerPreferenceModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public RecommendationDAO(){
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Retrieves the set of preferences from the database
     * @param username string of record to retrieve
     * @return
     */
    public DrinkerPreferenceModel getPreferenceModelByUsername(String username) {
        DrinkerPreferenceModel drinkerPrefModel = new DrinkerPreferenceModel();
        String sql = "SELECT * FROM drinkerPreferenceModel WHERE username=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,username);
            try (ResultSet resultSet = ps.executeQuery()){
                if (resultSet.next()) {
//                    string = "ALTER TABLE ADD " + resultSet.getString(1) + " float";
//                    resultSet.getFloat("france")
                    //TODO
                    //drinkerPrefModel.set
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
     * @param nameOfColumn string column to retrieve values from
     */
    public void addColumnsToPrefModelFromPopulatedTables(String nameOfColumn, String table){
        String sql = "select distinct " + nameOfColumn + " from " + table;
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()) {
                    try {
                        String sql2 = "ALTER TABLE drinkerPreferenceModel ADD \'" + resultSet.getString(1) + "\' float";
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
}
