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
                Wine wine = new Wine( resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("country"),
                        resultSet.getInt("year"),
                        resultSet.getString("shortDesription"),
                        resultSet.getString("longDescription"),
                        resultSet.getString("awards"),
                        resultSet.getFloat("pricePerBottle"),
                        resultSet.getFloat("alcoholByVolume"),
                        resultSet.getFloat("volumeInML"));
                wines.add(wine);

            }
            return wines;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }

    }

    /**
     * Gets an individual sale from database by id
     *
     * @param id id of sale to get
     * @return Wine wine from database that matches id
     */
    @Override
    public Wine getOne(int id) {
        Wine newWine = null;
        String sql = "SELECT * FROM wine WHERE id=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    newWine = new Wine( resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("type"),
                            resultSet.getString("country"),
                            resultSet.getInt("year"),
                            resultSet.getString("shortDesription"),
                            resultSet.getString("longDescription"),
                            resultSet.getString("awards"),
                            resultSet.getFloat("pricePerBottle"),
                            resultSet.getFloat("alcoholByVolume"),
                            resultSet.getFloat("volumeInML"));
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
    public int add (Wine toAdd){
        String sql = "INSERT INTO wine (id, name, type, country, year, shortDescription, longDescription, awards, pricePerBottle, alcoholByVolume, volumeInML) values (?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,toAdd.getUniqueWineID());
            ps.setString(2, toAdd.getName());
            ps.setString(3, toAdd.getType());
            ps.setString(4, toAdd.getCountry());
            ps.setInt(5, toAdd.getYear());
            ps.setString(6, toAdd.getShortDescription());
            ps.setString(7, toAdd.getLongDescription());
            ps.setString(8, toAdd.getAwards());
            ps.setFloat(9, toAdd.getPricePerBottle());
            ps.setFloat(10, toAdd.getAlcoholByVolume());
            ps.setFloat(11, toAdd.getVolumeInMl());
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
        String sql = "INSERT OR IGNORE INTO wine (id, name, type, country, year, shortDescription, longDescription, awards, pricePerBottle, alcoholByVolume, volumeInML) values (?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Wine wine : toAdd) {
                ps.setInt(1,wine.getUniqueWineID());
                ps.setString(2, wine.getName());
                ps.setString(3, wine.getType());
                ps.setString(4, wine.getCountry());
                ps.setInt(5, wine.getYear());
                ps.setString(6, wine.getShortDescription());
                ps.setString(7, wine.getLongDescription());
                ps.setString(8, wine.getAwards());
                ps.setFloat(9, wine.getPricePerBottle());
                ps.setFloat(10, wine.getAlcoholByVolume());
                ps.setFloat(11, wine.getVolumeInMl());
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