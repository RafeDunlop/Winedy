package seng202.team3.services;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

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
                    newWine = new Wine( resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("type"),
                            resultSet.getString("country"),
                            resultSet.getInt("year"),
                            resultSet.getString("shortDescription"),
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
    public int add(Wine toAdd){
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

    /* Sets up the SQL query string for a wine search based on the existence of the provided parameters */
    private String setUpSearchQuery(List<String> keywords, Integer minYear, Integer maxYear, Float minPrice, Float maxPrice, String country, String type, String shortDescription, String grapeName) {
        String sql = "SELECT * FROM wine ";
        if (grapeName != null) {
            sql += "JOIN grape ON id = wineId ";
        }
        sql += "WHERE ";
        for (int i = 0; i < keywords.size(); i++) {
            if (i == keywords.size() - 1) {
                sql += "(name LIKE ? OR shortDescription LIKE ? OR longDescription LIKE ?) ";
            } else {
                sql += "(name LIKE ? OR shortDescription LIKE ? OR longDescription LIKE ?) OR ";
            }
        }

        if (minYear != null) {
            sql += "AND  year >= ? ";
        }
        if (maxYear != null) {
            sql += "AND  year <= ? ";
        }
        if (minPrice != null) {
            sql += "AND price >= ? ";
        }
        if (maxPrice != null) {
            sql += "AND price <= ? ";
        }
        if (country != null) {
            sql += "AND country=? ";
        }
        if (type != null) {
            sql += "AND type=? ";
        }
        if (shortDescription != null) {
            sql += "AND shortDescription=? ";
        }
        if (grapeName != null) {
            sql += "AND grape.name=? ";
        }
        return sql;
    }

    /* Adds the required parameters to a PreparedStatement */
    private void setUpSearchPreparedStatement(PreparedStatement ps, List<String> keywords, Integer minYear, Integer maxYear, Float minPrice, Float maxPrice, String country, String type, String shortDescription, String grapeName) throws SQLException {
        int i = 0;
        for (; i < keywords.size(); i++) {
            ps.setString(3 * i + 1, keywords.get(i));
            ps.setString(3 * i + 2, keywords.get(i));
            ps.setString(3 * i + 3, keywords.get(i));
        }
        i = 3 * i + 1;
        if (minYear != null) {
            ps.setInt(i, minYear);
            i++;
        }
        if (maxYear != null) {
            ps.setInt(i, maxYear);
            i++;
        }
        if (minPrice != null) {
            ps.setFloat(i, minPrice);
            i++;
        }
        if (maxPrice != null) {
            ps.setFloat(i, maxPrice);
            i++;
        }
        if (country != null) {
            ps.setString(i, country);
            i++;
        }
        if (type != null) {
            ps.setString(i, type);
            i++;
        }
        if (shortDescription != null) {
            ps.setString(i, shortDescription);
            i++;
        }
        if (grapeName != null) {
            ps.setString(i, grapeName);
        }
    }

    /**
     * Searches database for wines based on keywords from the search bar and a number of filters
     * @param keywords keywords that have been collected from the search bar on the app
     * @param minYear the earliest year a wine can be from, specified by the wine drinker
     * @param maxYear the latest year a wine can be from
     * @param minPrice the minimum price of a wine in the search
     * @param maxPrice the maximum price of a wine in the search
     * @param country the specified country the wine should be from
     * @param type the specified type of wine between red, white and rose
     * @param shortDescription the specified dryness of the wine
     * @param grapeName the type of grape that the wine is made of
     * @return a SearchWineList object containing the search results of a wine search
     */
    public SearchWineList searchWines(List<String> keywords, Integer minYear, Integer maxYear, Float minPrice, Float maxPrice, String country, String type, String shortDescription, String grapeName) {
        String sql = setUpSearchQuery(keywords, minYear, maxYear, minPrice, maxPrice, country, type, shortDescription, grapeName);
        SearchWineList searchResults = new SearchWineList();
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setUpSearchPreparedStatement(ps, keywords, minYear, maxYear, minPrice, maxPrice, country, type, shortDescription, grapeName);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                     Wine searchedWine = new Wine( resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("type"),
                            resultSet.getString("country"),
                            resultSet.getInt("year"),
                            resultSet.getString("shortDescription"),
                            resultSet.getString("longDescription"),
                            resultSet.getString("awards"),
                            resultSet.getFloat("pricePerBottle"),
                            resultSet.getFloat("alcoholByVolume"),
                            resultSet.getFloat("volumeInML"));
                     searchResults.addWineToList(searchedWine);
                }
                return searchResults;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }
}