package seng202.team3.services;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.Wine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Wine DAO class that handles all wine related actions to the database
 */
public class WineDAO implements DAOInterface<Wine> {
    private final DatabaseManager databaseManager;
    private static final Logger log = LogManager.getLogger(WineDAO.class);

    /**
     * Creates a new WineDAO object and gets a reference to the database singleton
     */
    public WineDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Gets all wines in the database and converts them into wine objects
     *
     * @return a list of all sales
     */
    @Override
    public List<Wine> getAll() {
        List<Wine> wines = new ArrayList<>();
        String sqlQuery = "SELECT * FROM wine";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                Wine wine = getWineFromResultSet(resultSet);
                wines.add(wine);
            }
            return wines;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }

    }

    /**
     * Gets an individual wine from database by id
     *
     * @param id id of sale to get
     * @return Wine from database that matches id
     */

    public Wine getWineByID(int id) {
        Wine newWine = null;
        String sql = "SELECT * FROM wine WHERE id=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    newWine = getWineFromResultSet(resultSet);
                }
                return newWine;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Adds an individual wine to the database
     * @param toAdd wine to add
     * @return the insertId of the action
     */
    @Override
    public int add(Wine toAdd){
        String sql = "INSERT INTO wine (id, name, country, style, type, fullness, longDescription, pricePerBottle, alcoholByVolume, volumeInML, year) values (?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, toAdd);
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            int insertId = -1;
            if (resultSet.next()) {
                insertId = resultSet.getInt(1);
            }
            return insertId;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return -1;
        }
    }

    /**
     * Adds a batch of wines to the database
     * This is done much quicker than individually
     * @param toAdd a list of wines to add to the database
     */
    public void addBatch (List < Wine > toAdd) {
        String sql = "INSERT OR IGNORE INTO wine (id, name, country, style, type, fullness, longDescription, pricePerBottle, alcoholByVolume, volumeInML, year) values (?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Wine wine : toAdd) {
                setParams(ps, wine);
                ps.addBatch();
            }
            ps.executeBatch();
            ResultSet resultSet = ps.getGeneratedKeys();
            while (resultSet.next()){
                log.info(resultSet.getLong(1));
            }
            conn.commit();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * loads the Wine's single valued attributes into teh prepared statement
     * @param ps prepared statement to be executed by a caller function
     * @param wine the wine object to be loaded into the statement
     * @throws SQLException if the loading encounters a problem
     */
    private void setParams(PreparedStatement ps, Wine wine) throws SQLException {
        ps.setInt(1,wine.getUniqueWineID());
        ps.setString(2, wine.getName());
        ps.setString(3, wine.getCountry());
        ps.setString(4, wine.getType());
        ps.setString(5, wine.getStyle());
        ps.setString(6, wine.getFullness());
        ps.setString(7, wine.getLongDescription());
        ps.setFloat(8, wine.getPricePerBottle());
        ps.setFloat(9, wine.getAlcoholByVolume());
        ps.setFloat(10, wine.getVolumeInMl());
        ps.setInt(11, wine.getYear());
    }

    private Wine getWineFromResultSet(ResultSet resultSet) throws SQLException {
        return new Wine( resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("country"),
                resultSet.getString("type"),
                resultSet.getString("style"),
                null, //grapes
                resultSet.getString("fullness"),
                resultSet.getString("longDescription"),
                resultSet.getFloat("pricePerBottle"),
                null, //awards
                resultSet.getFloat("alcoholByVolume"),
                resultSet.getFloat("volumeInML"),
                resultSet.getInt("year"));
    }

    /**
     * Delete wine from database by id
     * @param id id of object to delete
     */
    @Override
    public void delete ( int id){
        String sql = "DELETE FROM wine WHERE id=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Updates wine in database
     * @param toUpdate sale that needs to be updated (this object must be able to identify itself and its previous self)
     */
    @Override
    public void update (Wine toUpdate){
        throw new NotImplementedException();
    }
}