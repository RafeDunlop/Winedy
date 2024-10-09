package seng202.team3.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;
import seng202.team3.services.WineDrinkerManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

/**
 * Wine DAO class that handles all wine related actions to the database
 *
 * @author Sophia Copley (sco207)
 */
public class WineDAO implements DAOInterface<Wine> {

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(WineDAO.class);

    /**
     * Database manager instance to manage database connections
     */
    private final DatabaseManager databaseManager;

    /**
     * Boolean to determine whether an AND is needed in the setUpSearchQuery statement
     */
    private boolean hasOne = false;

    /**
     * Creates a new WineDAO object and gets a reference to the database singleton
     */
    public WineDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Creates a new WineDAO object and gets a reference to the database singleton for a database at the specified url.
     * Used for testing.
     *
     * @param url the url that the test database is located at
     */
    public WineDAO(String url) {
        databaseManager = DatabaseManager.getInstance(url);
    }

    /**
     * Gets all wines in the database and converts them into wine objects
     *
     * @return a list of all sales
     */
    @Override
    public List<Wine> getAll() {
        List<Wine> wines = new ArrayList<>();
        String sqlWine = "SELECT * FROM wineSuper JOIN wine on wineSuper.id = wine.id ORDER BY name DESC";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psWine = conn.prepareStatement(sqlWine);) {
            ResultSet resultSet = psWine.executeQuery();
                Wine newWine;
                int id;
                while (resultSet.next()) {
                    id = resultSet.getInt("id");
                    String[] grapeList = getGrapesByID(id);
                    String[] awardList = getGrapesByID(id);
                    newWine = getWineFromResultSet(resultSet, grapeList, awardList);
                    wines.add(newWine);
                }

                try {
                    PersonalWineDAO personalWineDAO = new PersonalWineDAO();
                    wines.addAll(personalWineDAO.getAll());
                } catch (NullPointerException e)  {
                    log.info("no logged in user");
                }

                return wines;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list of Strings representing grapes associated with a wine ID
     *
     * @param wineId ID of the wine to get the grapes from
     * @return an array of the grapes in the specified wine
     */
    protected String[] getGrapesByID(int wineId) {
        String sqlGrape = "SELECT * FROM grape WHERE wineId = ?";
        return getMultivaluedAttribute(wineId, sqlGrape);
    }

    /**
     * Gets a list of Strings representing awards associated with a wine ID
     *
     * @param wineId ID of the wine to get the awards from
     * @return an array of the awards won by the specified wine
     */
    protected String[] getAwardsByID(int wineId) {
        String sqlAward = "SELECT * FROM award WHERE wineId = ?";
        return getMultivaluedAttribute(wineId, sqlAward);
    }

    /**
     * Gets a list of Strings representing the multivariable attribute associated with a wine ID
     *
     * @param wineId ID of the wine to get the multivariable attributes from
     * @param sql statement in the form of  "SELECT * FROM {table name} award WHERE wineId = ?"
     * @return a list corresponding to the desired multivalued attribute
     */
    @Nullable
    protected String[] getMultivaluedAttribute(int wineId, String sql) {
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
        String sql = "SELECT * FROM wineSuper WHERE id=?";
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
     *
     * @param toAdd wine to add
     * @return the insertId of the action
     */
    @Override
    public int add(Wine toAdd) {
        String sqlWineSuper = "INSERT OR IGNORE INTO wineSuper (id, name, country, colour, style, fullness, longDescription, pricePerBottle, alcoholByVolume, volumeInML, year) values (?,?,?,?,?,?,?,?,?,?,?);";
        String sqlGrape = "INSERT INTO grape (wineId, name) VALUES (?, ?)";
        String sqlAward = "INSERT INTO award (wineId, name) VALUES (?, ?)";
        String sqlWine = "INSERT INTO wine (id) VALUES (?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psWineSuper = conn.prepareStatement(sqlWineSuper);
             PreparedStatement psGrape = conn.prepareStatement(sqlGrape);
             PreparedStatement psAward = conn.prepareStatement(sqlAward);
             PreparedStatement psWine = conn.prepareStatement(sqlWine)) {
            setWineSuperParams(psWineSuper, toAdd);
            for (String grape : toAdd.getGrapes()) {
                setGrapeParams(psGrape, toAdd.getUniqueWineID(), grape);
            }
            for (String award : toAdd.getAwards()) {
                setAwardParams(psAward, toAdd.getUniqueWineID(), award);
            }
            psWine.setInt(1, toAdd.getUniqueWineID());
            psWine.executeUpdate();
            psWineSuper.executeUpdate();
            ResultSet resultSet = psWineSuper.getGeneratedKeys();
            return (resultSet.next()) ? resultSet.getInt(1) : -1;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return -1;
        }
    }

    /**
     * Adds a batch of wines to the database
     * This is done much quicker than individually
     *
     * @param toAdd a list of wines to add to the database
     */
    public void addBatch (List < Wine > toAdd) {
        String sqlWineSuper = "INSERT OR IGNORE INTO wineSuper (id, name, country, colour, style, fullness, longDescription, pricePerBottle, alcoholByVolume, volumeInML, year) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        String sqlGrape = "INSERT INTO grape (wineId, name) VALUES (?, ?)";
        String sqlAward = "INSERT INTO award (wineId, name) VALUES (?, ?)";
        String sqlWine = "INSERT INTO wine (id) VALUES (?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psWineSuper = conn.prepareStatement(sqlWineSuper);
             PreparedStatement psGrape = conn.prepareStatement(sqlGrape);
             PreparedStatement psAward = conn.prepareStatement(sqlAward);
             PreparedStatement psWine = conn.prepareStatement(sqlWine)) {
            conn.setAutoCommit(false);
            for (Wine wine : toAdd) {
                setWineSuperParams(psWineSuper, wine);
                for (String grape : wine.getGrapes()) {
                    setGrapeParams(psGrape, wine.getUniqueWineID(), grape);
                    psGrape.addBatch();
                }
                for (String award : wine.getAwards()) {
                    setAwardParams(psAward, wine.getUniqueWineID(), award);
                    psAward.addBatch();
                }
                psWine.setInt(1, wine.getUniqueWineID());
                psWine.addBatch();
                psWineSuper.addBatch();
            }
            psWineSuper.executeBatch();
            psGrape.executeBatch();
            psAward.executeBatch();
            psWine.executeBatch();
            ResultSet resultSet = psWineSuper.getGeneratedKeys();
            while (resultSet.next()){
                log.info(resultSet.getLong(1));
            }
            conn.commit();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Updates the note a Wine Drinker has left on a wine in the database
     *
     * @param toSet the wine whose note is to be updated
     * @param note the new note to be set in the database
     * @return an integer representing the success code of the method
     */
    public int updateNote(Wine toSet, String note) {
        String sql = "UPDATE writesNoteAbout SET note = ? WHERE wineDrinker = ? AND wineId = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, note);
            ps.setString(2, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            ps.setInt(3, toSet.getUniqueWineID());
            ps.executeUpdate();
            return 0;
        } catch (SQLException | NullPointerException e) {
            log.error(e);
            return 1;
        }
    }

    /**
     * Adds a note written by a Wine Drinker to a wine in the database
     *
     * @param toSet the wine that the note was written about
     * @param note the note written about the wine
     * @return an integer representing the success code of the method
     */
    public int addNote(Wine toSet, String note) {
        String sql = "INSERT INTO writesNoteAbout (wineDrinker, wineId, note) VALUES (?, ?, ?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            ps.setInt(2, toSet.getUniqueWineID());
            ps.setString(3, note);
            ps.executeUpdate();
            return 0;
        } catch (SQLException | NullPointerException e) {
            log.error(e);
            return 1;
        }
    }

    /**
     * Gets and returns the note that the current user had written about the given wine from the database.
     *
     * @param hasNote the wine whose note is to be retrieved from the database
     * @return an integer representing the success code of the method
     */
    public String getNote(Wine hasNote) {
        String sql = "SELECT * FROM writesNoteAbout WHERE wineDrinker = ? AND wineId = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            ps.setInt(2, hasNote.getUniqueWineID());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("note");
            }
            return "";
        } catch (SQLException | NullPointerException e) {
            log.error(e);
            return "There was a problem getting this Wine's note";
        }
    }

    /**
     * Loads the Wine's single valued attributes into the prepared statement
     *
     * @param ps Prepared Statement to be executed by a caller function
     * @param wine The wine object to be loaded into the statement
     * @throws SQLException If the loading encounters a problem, this is thrown up to the add or addBatch method that calls it
     */
    protected void setWineSuperParams(PreparedStatement ps, Wine wine) throws SQLException {
        ps.setInt(1,wine.getUniqueWineID());
        ps.setString(2, wine.getName());
        ps.setString(3, wine.getCountry());
        ps.setString(4, wine.getColour());
        ps.setString(5, wine.getStyle());
        ps.setString(6, wine.getFullness());
        ps.setString(7, wine.getLongDescription());
        ps.setFloat(8, wine.getPricePerBottle());
        ps.setFloat(9, wine.getAlcoholByVolume());
        ps.setFloat(10, wine.getVolumeInMl());
        ps.setInt(11, wine.getYear());
    }

    /**
     * Sets the parameters for the grape prepared statements
     *
     * @param ps prepared sql statement for the parameters to be added to
     * @param wineID Wine ID parameter for SQL statement
     * @param grape the grape type parameter  for SQL statement
     * @throws SQLException If an SQL Exception occurs, this is thrown up to the add or addBatch method that calls it
     */
    protected void setGrapeParams(PreparedStatement ps, int wineID, String grape) throws SQLException {
        ps.setInt(1, wineID);
        ps.setString(2, grape);
    }

    /**
     * Sets the parameters for the grape prepared statements
     *
     * @param ps prepared sql statement for the parameters to be added to
     * @param wineID Wine ID parameter for SQL statement
     * @param name the name of the parameter  for SQL statement
     * @throws SQLException If an SQL Exception occurs, this is thrown up to the add or addBatch method that calls it
     */
    protected void setAwardParams(PreparedStatement ps, int wineID, String name) throws SQLException {
        ps.setInt(1, wineID);
        ps.setString(2, name);
    }

    /**
     * Creates a wine object from a result set
     *
     * @param resultSet result set to create wine object from
     * @param grapes list of grapes to be added to the wine object
     * @param awards list of awards to be added to the wine object
     * @return the Wine object created from the result set
     * @throws SQLException If an SQL Exception occurs, this is thrown up to the method that calls it
     */
    protected Wine getWineFromResultSet(ResultSet resultSet, String[] grapes, String[] awards) throws SQLException {
        return new Wine( resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("country"),
                resultSet.getString("colour"),
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
     * Deletes a Wine from database by id
     *
     * @param toDelete Wine object to be deleted
     * @return either: -1 if there is an error, 0 if no tuple is deleted or the number of tuples deleted (1)
     */
    @Override
    public int delete (Wine toDelete) {
        String sql = "DELETE FROM wineSuper WHERE id=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, toDelete.getUniqueWineID());
            ps.executeUpdate();
            return 0;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return 1;
        }
    }

    /**
     * Updates a Wine in database
     *
     * @param toUpdate sale that needs to be updated (this object must be able to identify itself and its previous self)
     */
    @Override
    public int update (Wine toUpdate) {
        return 1;
    }

    /**
     * Helper function to keep track of whether an "AND" is needed in the filters part of the sql search query
     */
    private String addAnd() {
        if (!hasOne) {
            hasOne = true;
            return " ";
        }
        return "AND ";
    }

    /**
     * Helper function to create the keywords part of the sql search query
     *
     * @param sql the sql search query as a StringBuilder object to build on
     * @param keywords list of keywords from the search bar of the search screen
     */
    private void addKeywords(StringBuilder sql, List<String> keywords) {
        if (keywords != null) {
            sql.append("(");
            for (int i = 0; i < keywords.size(); i++) {
                if (!hasOne) {
                    hasOne = true;
                }
                sql.append("(LOWER(wineSuper.name) LIKE ? OR LOWER(style) LIKE ? OR LOWER(longDescription) LIKE ?)");
                if (i < keywords.size() - 1) {
                    sql.append(" OR ");
                }
            }
            sql.append(") ");
        }
    }

    /**
     * Helper function to create the filters part of the sql search query
     *
     * @param sql the sql search query as a StringBuilder object to build on
     * @param condition filter condition in sql formatting
     * @param parameter value of the respective filter for the condition
     */
    private void addFilter(StringBuilder sql, String condition, Object parameter) {
        if (parameter != null) {
            sql.append(addAnd()).append(condition).append(" ");
        }
    }

    /**
     * Sets up the SQL query string for a wine search based on the existence of the provided parameters
     *
     * @param keywords list of keywords that have been collected from the search bar on the app
     * @param minYear the earliest year a wine can be from, specified by the wine drinker
     * @param maxYear the latest year a wine can be from
     * @param minPrice the minimum price of a wine in the search
     * @param maxPrice the maximum price of a wine in the search
     * @param country the specified country the wine should be from
     * @param colour the specified colour of wine between red, white and rose
     * @param fullness the specified dryness of the wine
     * @param grapeName the colour of grape that the wine is made of
     * @return a string that is the SQL query for the search method
     */
    protected String setUpSearchQuery(List<String> keywords, Integer minYear, Integer maxYear, Float minPrice, Float maxPrice, String country, String colour, String fullness, String grapeName) {
        hasOne = false;
        StringBuilder sql = new StringBuilder("SELECT * FROM wineSuper ");
        if (grapeName != null) {
            sql.append("JOIN grape ON grape.wineId = wineSuper.id ");
        }
        sql.append("JOIN wine ON wine.id = wineSuper.id WHERE ");
        addKeywords(sql, keywords);
        addFilter(sql, "year >= ?", minYear);
        addFilter(sql, "year <= ?", maxYear);
        addFilter(sql, "pricePerBottle >= ?", minPrice);
        addFilter(sql, "pricePerBottle <= ?", maxPrice);
        addFilter(sql, "country=?", country);
        addFilter(sql, "colour=?", colour);
        addFilter(sql, "fullness=?", fullness);
        addFilter(sql, "grape.name=?", grapeName);
        return sql.toString();
    }

    /**
     * Adds the required parameters to a PreparedStatement for the search method
     *
     * @param ps prepared statement to add parameters to
     * @param keywords list of keywords that have been collected from the search bar on the app
     * @param minYear the earliest year a wine can be from, specified by the wine drinker
     * @param maxYear the latest year a wine can be from
     * @param minPrice the minimum price of a wine in the search
     * @param maxPrice the maximum price of a wine in the search
     * @param country the specified country the wine should be from
     * @param colour the specified colour of wine between red, white and rose
     * @param fullness the specified dryness of the wine
     * @param grapeName the colour of grape that the wine is made of
     * @throws SQLException If an SQL Exception occurs, this is thrown up to the method that calls it
     */
    protected void setUpSearchPreparedStatement(PreparedStatement ps, List<String> keywords, Integer minYear, Integer maxYear, Float minPrice, Float maxPrice, String country, String colour, String fullness, String grapeName) throws SQLException {
        int i = 0;
        if (keywords != null) {
            for (; i < keywords.size(); i++) {
                ps.setString(3 * i + 1, "%" + keywords.get(i) + "%");
                ps.setString(3 * i + 2, "%" + keywords.get(i) + "%");
                ps.setString(3* i + 3, "%" + keywords.get(i) + "%");
            }
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
        if (colour != null) {
            ps.setString(i, colour);
            i++;
        }
        if (fullness != null) {
            ps.setString(i, fullness);
            i++;
        }
        if (grapeName != null) {
            ps.setString(i, grapeName);
        }
    }

    /**
     * Searches database for wines based on keywords from the search bar and a number of filters
     *
     * @param keywords keywords that have been collected from the search bar on the app
     * @param minYear the earliest year a wine can be from, specified by the wine drinker
     * @param maxYear the latest year a wine can be from
     * @param minPrice the minimum price of a wine in the search
     * @param maxPrice the maximum price of a wine in the search
     * @param country the specified country the wine should be from
     * @param colour the specified colour of wine between red, white and rose
     * @param fullness the specified dryness of the wine
     * @param grapeName the colour of grape that the wine is made of
     * @return a SearchWineList object containing the search results of a wine search
     */
    public SearchWineList searchWines(List<String> keywords, Integer minYear, Integer maxYear, Float minPrice, Float maxPrice, String country, String colour, String fullness, String grapeName) {
        String sql = setUpSearchQuery(keywords, minYear, maxYear, minPrice, maxPrice, country, colour, fullness, grapeName);
        SearchWineList searchResults = new SearchWineList();
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setUpSearchPreparedStatement(ps, keywords, minYear, maxYear, minPrice, maxPrice, country, colour, fullness, grapeName);
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