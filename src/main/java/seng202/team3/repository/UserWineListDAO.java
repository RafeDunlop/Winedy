package seng202.team3.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import seng202.team3.models.FavouritesWineList;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.services.WineDrinkerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * UserWineList DAO class that handles all UserWineList related actions to the database
 *
 * @author Hannah Botting (hbo51)
 */
public class UserWineListDAO implements DAOInterface<UserWineList> {

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(UserWineListDAO.class);

    /**
     * Database manager instance to manage database connections.
     * This is required for testing using the test database
     */
    private final DatabaseManager databaseManager;

    private final String url;

    /**
     * Creates a new UserWineListDAO object and gets a reference to the database singleton
     */
    public UserWineListDAO() {
        databaseManager = DatabaseManager.getInstance();
        url = null;
    }

    /**
     * Creates a new UserWineListDAO object and gets a reference to the database singleton for a database at the specified url.
     * Used for testing.
     */
    public UserWineListDAO(String url) {
        databaseManager = DatabaseManager.getInstance(url);
        this.url = url;
    }

    /**
     * Retrieves and returns all the wine lists of the current logged-in Wine Drinker
     *
     * @return A list of all the current logged-in Wine Drinker's wine lists
     */
    @Override
    public List<UserWineList> getAll() {
        ArrayList<UserWineList> userWineLists = new ArrayList<>();
        String sql = "SELECT * FROM wineList WHERE wineList.username = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psList = conn.prepareStatement(sql)) {
            psList.setString(1, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            ResultSet resultSet = psList.executeQuery();
            while (resultSet.next()) {
                String description = resultSet.getString("description");
                String name = resultSet.getString("name");
                Integer sortKey = resultSet.getInt("sortKey");
                UserWineList userWineList = new UserWineList(name, description, sortKey);
                getContainedWines(userWineList);
                userWineLists.add(userWineList);
            }
            return userWineLists;
        } catch (SQLException | NullPointerException e) {
            log.error(e);
            return Collections.emptyList();
        }
    }

    /**
     * Adds a new UserWineList to the database
     *
     * @param toAdd object of type UserWineList to add to the database  
     * @return an int representing the success of the INSERT statements
     */
    @Override
    public int add(UserWineList toAdd) {
        String sqlList = "INSERT INTO wineList (name, username, description, sortKey) VALUES (?, ?, ?, ?)";
        String sqlContains = "INSERT INTO contains (wineID, listName, wineDrinker) VALUES (?, ?, ?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psList = conn.prepareStatement(sqlList);
             PreparedStatement psContains = conn.prepareStatement(sqlContains)) {
            setAddListParams(psList, toAdd);
            psList.executeUpdate();
            for (Wine wine : toAdd.getWineList()) {
                setContainsParams(psContains, wine, toAdd.getWineListName());
                psContains.executeUpdate();
            }
            return 0;
        } catch (SQLException | NullPointerException e) {
            log.error(e);
            return 1;
        }
    }

    /**
     * Deletes the given UserWineList object
     *
     * @param toDelete UserWineList Object to be deleted
     * @return either: -1 if there is an error, 0 if no tuple is deleted or the number of tuples deleted (1)
     */
    @Override
    public int delete(UserWineList toDelete) {
        String sqlDelete = "DELETE FROM wineList WHERE name = ? AND username = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
            psDelete.setString(1, toDelete.getWineListName());
            psDelete.setString(2, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            return psDelete.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            log.error(e);
            return -1;
        }
    }

    /**
     * Update the given UserWineList object. Changes the value of the description and sort key in the database.
     * Resolves discrepancies between specified UserWineList's wine list and that stored in the contains table with insert and delete statements
     *
     * @param toUpdate Object that needs to be updated (this object must be able to identify itself and its previous self)
     * @return 0 if update succeeds without exceptions, otherwise 1
     */
    @Override
    public int update(UserWineList toUpdate) {
        List<Wine> newList = toUpdate.getWineList();
        getContainedWines(toUpdate);
        HashSet<Wine> oldSet = new HashSet<>(toUpdate.getWineList());
        String sqlList = "UPDATE wineList SET description = ?, sortKey = ? WHERE name = ? AND username = ?";
        String sqlContainsAdd = "INSERT OR IGNORE INTO contains (wineId, listName, wineDrinker) VALUES (?, ?, ?)";
        String sqlContainsDel = "DELETE FROM contains WHERE wineId = ? AND listName = ? AND wineDrinker = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psList = conn.prepareStatement(sqlList);
             PreparedStatement psContainsAdd = conn.prepareStatement(sqlContainsAdd);
             PreparedStatement psContainsDel = conn.prepareStatement(sqlContainsDel)) {
            setUpdateListParams(psList, toUpdate);
            containsUpdateSetup(toUpdate.getWineListName(), newList, oldSet, psContainsAdd, psContainsDel);
            psList.executeUpdate();
            psContainsAdd.executeBatch();
            psContainsDel.executeBatch();
            return 0;
        } catch (SQLException | NullPointerException e) {
            log.error(e);
            return 1;
        }
    }

    /**
     * iterates through wines in the database and the list to be updated and makes the database match the list
     *
     * @param listKey String the name of the list
     * @param newList list of wines in the new list to be added
     * @param oldSet set of wines in the database for the wine list being updated prior to update
     * @param psAdd Prepared statement for addition of new wines
     * @param psDel Prepared statement for deletion of wines no longer in the wine list
     * @throws SQLException thrown if there is an exception when adding batches
     */
    private void containsUpdateSetup(String listKey, List<Wine> newList, HashSet<Wine> oldSet, PreparedStatement psAdd, PreparedStatement psDel) throws SQLException {
        for (Wine wine : newList) {
            if (!oldSet.contains(wine)) {
                setContainsParams(psAdd, wine, listKey);
                psAdd.addBatch();
            } else {
                oldSet.remove(wine);
            }
        }

        for (Wine wine : oldSet) {
            setContainsParams(psDel, wine, listKey);
            psDel.addBatch();
        }
    }

    /**
     * gets the minimum sort key in the database associated with the logged-in user and not their favourites list
     * @return the minimum sort key
     */
    public int getMinSortKey() {
        String sql = "SELECT MIN(sortKey) FROM wineList WHERE wineList.name <> ? AND wineList.username = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, FavouritesWineList.getFavouritesName());
            ps.setString(2, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            ResultSet min = ps.executeQuery();
            if (min.next()) {
                return min.getInt(1);
            }
            return 0;
        } catch (SQLException | NullPointerException e) {
            log.error(e);
            return 1;
        }
    }

    /**
     * Renames the given UserWineList in the database by first deleting it, setting the Model Object's name to the new
     * name, and then re adding it to the database.
     *
     * @param toRename The UserWineList object to be renamed
     * @param newName The new name of the UserWineList to be updated as a String
     */
    public void rename(UserWineList toRename, String newName) {
        delete(toRename);
        toRename.setWineListName(newName);
        add(toRename);
    }

    /**
     * Sets the parameters of the given Prepared Statement to have the given UserWineList's name, and description.
     * Also sets it to have the current logged in WineDrinker's username.
     *
     * @param ps The Prepared Statement to be set
     * @param toAdd The UserWineList being added to the database
     * @throws SQLException If an SQLException occurs, this is thrown up to the add method that calls it to be logged
     */
    private void setAddListParams(PreparedStatement ps, UserWineList toAdd) throws SQLException, NullPointerException {
        ps.setString(1, toAdd.getWineListName());
        ps.setString(2, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
         ps.setString(3, toAdd.getDescription());
        ps.setInt(4, -1 * toAdd.getSortKey());
    }

    /**
     * Sets the parameters of the given Prepared Statement to have the given UserWineList's description, and name.
     * Also sets it to have the current logged in WineDrinker's username.
     *
     * @param ps The Prepared Statement to be set
     * @param toUpdate The UserWineList being updated in the database
     * @throws SQLException If an SQLException occurs, this is thrown up to the update method that calls it to be logged
     */
    private void setUpdateListParams(PreparedStatement ps, UserWineList toUpdate) throws SQLException, NullPointerException {
        ps.setString(1, toUpdate.getDescription());
        ps.setInt(2, -1 * toUpdate.getSortKey());
        ps.setString(3, toUpdate.getWineListName());
        ps.setString(4, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
    }

    /**
     * Gets all the wines contained in the given UserWineList from the database and adds them to its stored list
     * Called by the getAll method when retrieving a wine list from the database
     *
     * @param userWineList The UserWineList to have wines added to
     */
    private void getContainedWines(UserWineList userWineList) throws NullPointerException {
        String sqlContains = "SELECT * FROM contains JOIN wineSuper ON contains.wineId = wineSuper.id WHERE contains.wineDrinker = ? AND contains.listName = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psContains = conn.prepareStatement(sqlContains)) {
            psContains.setString(1, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            psContains.setString(2, userWineList.getWineListName());
            ResultSet resultSet = psContains.executeQuery();
            WineDAO wineDAO;
            if (url == null) {
                wineDAO = new WineDAO();
            } else {
                wineDAO = new WineDAO(url);
            }
            Wine containedWine;
            ArrayList<Wine> toAdd = new ArrayList<>();
            while (resultSet.next()) {
                String[] grapeList = wineDAO.getGrapesByID(resultSet.getInt("wineId"));
                String[] awardList = wineDAO.getAwardsByID(resultSet.getInt("wineId"));
                containedWine = wineDAO.getWineFromResultSet(resultSet, grapeList, awardList);
                toAdd.add(containedWine);
            }
            userWineList.setWineList(toAdd);
        } catch (SQLException e) {
            log.error(e);
        }
    }

    /**
     * Sets the parameters of the given Prepared Statement to have the given Wine's unique id, the given UserWineList's
     * name, and the currently logged in WineDrinker's username.
     *
     * @param ps The Prepared Statement to be set
     * @param wine The Wine being added into the contains table
     * @param wineListName The UserWineList being added into the contains table
     * @throws SQLException If an SQLException occurs, this is thrown up to the add or update method that calls it to be logged
     */
    private void setContainsParams(PreparedStatement ps, Wine wine, String wineListName) throws SQLException, NullPointerException {
        ps.setInt(1, wine.getUniqueWineID());
        ps.setString(2, wineListName);
        ps.setString(3, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
    }
}
