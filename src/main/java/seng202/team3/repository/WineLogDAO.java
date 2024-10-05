package seng202.team3.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.WineLog;
import seng202.team3.services.WineDrinkerManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DAO class for interfacing between the database and models with reference to WineLogs
 *
 * @author Rafe Dunlop (rdu46)
 */
public class WineLogDAO implements DAOInterface<WineLog> {

    /**
     * Database manager instance to manage database connections
     */
    private final DatabaseManager databaseManager;

    /**
     * Logger for robust error logging.
     * Called logger here to differentiate from a wine Log
     */
    private static final Logger logger = LogManager.getLogger(WineLogDAO.class);

    /**
     * the url corresponding to the database path used by this DAO (set by constructor, null for default)
     */
    private final String url;

    /**
     * Creates a new WineLogDAO object and gets a reference to the database singleton
     */
    public WineLogDAO() {
        databaseManager = DatabaseManager.getInstance();
        url = null;
    }

    /**
     * Creates a new WineLogDAO object and gets a reference to the database singleton for a database at the specified url.
     * Used for testing.
     */
    public WineLogDAO(String url) {
        databaseManager = DatabaseManager.getInstance(url);
        this.url = url;
    }


    /**
     * Gets all WineLog objects from the database which are associated with the logged-in user
     * ordered by recency
     * @return List of all the logged-in user's WineLogs
     */
    @Override
    public List<WineLog> getAll() {
        String sql = "SELECT wineID, logEntry, date, Time, quantity FROM log WHERE wineDrinker = ? ORDER BY date, time DESC";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getUsername());
            return getMulti(ps);
        } catch (SQLException e) {
            logger.error(e);
            return Collections.emptyList();
        }
    }

    /**
     * private method for common use between getAll and getInRange
     * @param ps the preloaded PreparedStatement
     * @return the processed results
     * @throws SQLException thrown if there is a problem with the query
     */
    private List<WineLog> getMulti(PreparedStatement ps) throws SQLException {
        ArrayList<WineLog> logs = new ArrayList<>();
        ps.setString(1, getUsername());
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            logs.add(getLogFromResultSet(resultSet));
        }
        return logs;
    }

    /**
     * Gets WineLog objects from the database which are associated with the logged-in user and fall within the specified range
     * the list is ordered by recency, and the range boundaries are inclusive
     * @param startDate the first Java.sql.Date in the range
     * @param endDate the last Java.sql.Date in the range
     * @return list of logs that fall in the range
     */
    public List<WineLog> getInRange(Date startDate, Date endDate) {
        String sql = "SELECT wineID, logEntry, date, Time, quantity FROM log WHERE wineDrinker = ? AND date >= ? AND date <= ? ORDER BY date, time DESC";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getUsername());
            ps.setDate(2, startDate);
            ps.setDate(3, endDate);
            return getMulti(ps);
        } catch (SQLException e) {
            logger.error(e);
            return Collections.emptyList();
        }
    }

    /**
     * Adds the specified WineLog to the database associated with the currently logged-in user
     * @param toAdd the WineLog to be added
     * @return 0 if add is successful, 1 if there was an exception (and the log was not added)
     */
    @Override
    public int add(WineLog toAdd) {
        String sql = "INSERT INTO log (wineDrinker, wineId, logEntry, date, time, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setAddLogParams(ps, toAdd);
            ps.executeUpdate();
            return 0;
        } catch (SQLException e) {
            logger.error(e);
            return 1;
        }
    }

    /**
     * Deletes the specified WineLog
     *
     * @param toDelete the Log to be deleted from the database
     * @return 0 if deletion executes as expected, 1 if no such Log was found or 2 if there was an error during query execution
     */
    @Override
    public int delete(WineLog toDelete) {
        String sql = "DELETE FROM log WHERE wineDrinker = ? AND wineId = ? AND date = ? AND time = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setDeleteParams(ps, toDelete);
            int deleted = ps.executeUpdate();
            return (deleted == 0) ? 1 : 0;
        } catch (SQLException e) {
            logger.error(e);
            return 2;
        }
    }

    /**
     * unused Interface version of the update method
     * @param toUpdate Object to be updated
     * @return 1 to indicate that the update failed
     */
    @Override
    public int update(WineLog toUpdate) {
        return 1;
    }

    /**
     * --this method is recommended to update a WineLog--
     * overloaded update method to update the log.
     * removes the specified WineLog from the database
     * creates a new WineLog with updates, adds this to the database and returns it.
     * does not use the sql update because some parameters that may like to be updated are in the primary key
     * all paramters except toUpdate may be null
     * @param toUpdate mandatory WineLog to be updated NOT in place -this parameter is removed from the database.
     * @param newLoggedID the foreign key to the new wine or null to use the previous one
     * @param newNote the new note (entire string) or null to use previous
     * @param newDate the new date of drinking (or null to use previous)
     * @param newTime the new time (of day) of drinking (or null to use previous)
     * @param newStandards the new number of standards (as a float) or null to use previous
     * @return the new, updated, in-database WineLog object
     */
    public WineLog update(WineLog toUpdate, Integer newLoggedID, String newNote, Date newDate, Time newTime, Float newStandards) {
        delete(toUpdate);
        WineLog updated = new WineLog(
                (newLoggedID == null) ? toUpdate.getUniqueWineId() : newLoggedID,
                (newNote == null) ? toUpdate.getNote() : newNote,
                (newDate == null) ? toUpdate.getDate() : newDate,
                (newTime == null) ? toUpdate.getTime() : newTime,
                (newStandards == null) ? toUpdate.getStandards() : newStandards);
        add(updated);
        return updated;
    }

    /**
     * private method to set the parameters for the add method
     * @param ps prepared statement into which to set the necessary fields
     * @param toAdd the Wine whose fields need to be added to the statement
     * @throws SQLException if there is an error when adding parameters
     */
    private void setAddLogParams(PreparedStatement ps, WineLog toAdd) throws SQLException {
        ps.setString(1, getUsername());
        ps.setInt(2, toAdd.getUniqueWineId());
        ps.setString(3, toAdd.getNote());
        ps.setDate(4, toAdd.getDate());
        ps.setTime(5, toAdd.getTime());
    }

    /**
     * private method that gets the attributes of a log from the specified result set and wraps them into a WineLog object
     * @param resultSet the result set from which to extract
     * @return the WineLog object corresponding to the results
     * @throws SQLException thrown if there is an error when extracting fields
     */
    private WineLog getLogFromResultSet(ResultSet resultSet) throws SQLException {
        int wineId = resultSet.getInt(1);
        String note = resultSet.getString(2);
        Date date = resultSet.getDate(3);
        Time time = resultSet.getTime(4);
        float quantity = resultSet.getFloat(5);
        return new WineLog(wineId, note, date, time, quantity);
    }

    /**
     * sets the parameters for the delete method
     * @param ps the prepared statement into which to set the necessary search fields
     * @param toDelete the WineLog to be deleted
     * @throws SQLException thrown if there is an error when setting a search field into the prepared statement
     */
    private void setDeleteParams(PreparedStatement ps, WineLog toDelete) throws SQLException {
        ps.setString(1, getUsername());
        ps.setInt(2, toDelete.getUniqueWineId());
        ps.setDate(3, toDelete.getDate());
        ps.setTime(4, toDelete.getTime());
    }

    /**
     * private method that gets the username of the logged-in user using the correct credentials
     * @return the String corresponding to the WineDrinker (current user) primary key
     */
    private String getUsername() {
        WineDrinkerManager wineDrinkerManager = (url == null) ? WineDrinkerManager.getInstance() : WineDrinkerManager.getInstance(url);
        return wineDrinkerManager.getCurrentUser().getUsername();
    }
}
