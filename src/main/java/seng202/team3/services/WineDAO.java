package seng202.team3.services;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
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
        String sqlWine = "SELECT * FROM wine";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sqlWine)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                Wine newWine;
                int id;
                while (resultSet.next()) {
                    id = resultSet.getInt("id");
                    String[] grapeList = getGrapesByID(id);
                    String[] awardList = getGrapesByID(id);
                    newWine = getWineFromResultSet(resultSet, grapeList, awardList);
                    wines.add(newWine);

                }
                return wines;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }

    }

    private String[] getGrapesByID(int wineId) {
        String sqlGrape = "SELECT * FROM grape WHERE wineId = ?";
        return getMultivaluedAttribute(wineId, sqlGrape);
    }
    private String[] getAwardsByID(int wineId) {
        String sqlAward = "SELECT * FROM award WHERE wineId = ?";
        return getMultivaluedAttribute(wineId, sqlAward);
    }

    @Nullable
    private String[] getMultivaluedAttribute(int wineId, String sql) {
        String[] multivaluedAttributeList = new String[10];
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, wineId);
            try (ResultSet resultSet = ps.executeQuery()) {
                int i = 0;
                while (resultSet.next()) {
                    multivaluedAttributeList[i] = resultSet.getString("name");
                    i++;
                }
                return multivaluedAttributeList;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
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
                    String[] grapeList = getGrapesByID(resultSet.getInt("id"));
                    String[] awardList = getAwardsByID(resultSet.getInt("id"));
                    newWine = getWineFromResultSet(resultSet, grapeList, awardList);
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
        String sqlWine = "INSERT INTO wine (id, name, country, style, type, fullness, longDescription, pricePerBottle, alcoholByVolume, volumeInML, year) values (?,?,?,?,?,?,?,?,?,?,?);";
        String sqlGrape = "INSERT INTO grape (wineId, name) VALUES (?, ?)";
        String sqlAward = "INSERT INTO award (wineId, name) VALUES (?, ?)";
        try (Connection conn = databaseManager.connect();
            PreparedStatement psWine = conn.prepareStatement(sqlWine);
            PreparedStatement psGrape = conn.prepareStatement(sqlGrape);
            PreparedStatement psAward = conn.prepareStatement(sqlAward)) {
            setWineParams(psWine, toAdd);
            for (String grape : toAdd.getGrapes()) {
                setGrapeParams(psGrape, toAdd.getUniqueWineID(), grape);
            }
            for (String award : toAdd.getAwards()) {
                setAwardParams(psAward, toAdd.getUniqueWineID(), award);
            }
            psWine.executeUpdate();
            ResultSet resultSet = psWine.getGeneratedKeys();
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
        String sqlWine = "INSERT OR IGNORE INTO wine (id, name, country, style, type, fullness, longDescription, pricePerBottle, alcoholByVolume, volumeInML, year) values (?,?,?,?,?,?,?,?,?,?,?);";
        String sqlGrape = "INSERT INTO grape (wineId, name) VALUES (?, ?)";
        String sqlAward = "INSERT INTO award (wineId, name) VALUES (?, ?)";
        try (Connection conn = databaseManager.connect();
            PreparedStatement psWine = conn.prepareStatement(sqlWine);
            PreparedStatement psGrape = conn.prepareStatement(sqlGrape);
            PreparedStatement psAward = conn.prepareStatement(sqlAward)) {
            conn.setAutoCommit(false);
            for (Wine wine : toAdd) {
                setWineParams(psWine, wine);
                for (String grape : wine.getGrapes()) {
                    setGrapeParams(psGrape, wine.getUniqueWineID(), grape);
                    psGrape.addBatch();
                }
                for (String award : wine.getAwards()) {
                    setAwardParams(psAward, wine.getUniqueWineID(), award);
                    psAward.addBatch();
                }
                psWine.addBatch();
                psGrape.addBatch();
            }
            psWine.executeBatch();
            psGrape.executeBatch();
            psAward.executeBatch();
            ResultSet resultSet = psWine.getGeneratedKeys();
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
    private void setWineParams(PreparedStatement ps, Wine wine) throws SQLException {
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

    private void setGrapeParams(PreparedStatement ps, int wineID, String grape) throws SQLException {
        ps.setInt(1, wineID);
        ps.setString(2, grape);
    }

    private void setAwardParams(PreparedStatement ps, int wineID, String name) throws SQLException {
        ps.setInt(1, wineID);
        ps.setString(2, name);
    }


    private Wine getWineFromResultSet(ResultSet resultSet, String[] grapes, String[] awards) throws SQLException {
        return new Wine( resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("country"),
                resultSet.getString("type"),
                resultSet.getString("style"),
                grapes,
                resultSet.getString("fullness"),
                resultSet.getString("longDescription"),
                resultSet.getFloat("pricePerBottle"),
                awards,
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

    /* Sets up the SQL query string for a wine search based on the existence of the provided parameters */
    private String setUpSearchQuery(List<String> keywords, Integer minYear, Integer maxYear, Float minPrice, Float maxPrice, String country, String type, String shortDescription, String grapeName) {
        String sql = "SELECT * FROM wine ";
        if (grapeName != null) {
            sql += "JOIN grape ON id = wineId ";
        }
        sql += "WHERE ";
        for (int i = 0; i < keywords.size(); i++) {
            if (i == keywords.size() - 1) {
                sql += "(LOWER(name) LIKE ? OR LOWER(style) LIKE ? OR LOWER(longDescription) LIKE ?) ";
            } else {
                sql += "(LOWER(name) LIKE ? OR LOWER(style) LIKE ? LOWER(longDescription) LIKE ?) OR ";
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
                Wine searchedWine;
                while (resultSet.next()) {
                    String[] grapeList = getGrapesByID(resultSet.getInt("id"));
                    String[] awardList = getAwardsByID(resultSet.getInt("id"));
                    searchedWine = getWineFromResultSet(resultSet, grapeList, awardList);
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