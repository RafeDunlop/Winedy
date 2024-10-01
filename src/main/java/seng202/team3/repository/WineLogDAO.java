package seng202.team3.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.models.WineLog;
import seng202.team3.services.WineDrinkerManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private final String url;

    /**
     * Creates a new WineDAO object and gets a reference to the database singleton
     */
    public WineLogDAO() {
        databaseManager = DatabaseManager.getInstance();
        url = null;
    }

    /**
     * Creates a new WineDAO object and gets a reference to the database singleton for a database at the specified url.
     * Used for testing.
     */
    public WineLogDAO(String url) {
        databaseManager = DatabaseManager.getInstance(url);
        this.url = url;
    }


    /**
     * Gets all of T from the database
     *
     * @return List of all objects type T from the database
     */
    @Override
    public List<WineLog> getAll() {
        ArrayList<WineLog> logs = new ArrayList<>();
        String sql = "SELECT wineID, logEntry, date, Time, quantity FROM  log WHERE wineDrinker = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getUsername());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                logs.add(getLogFromResultSet(resultSet));
            }
            return logs;
        } catch (SQLException e) {
            logger.error(e);
            return Collections.emptyList();
        }
    }


    private String getUsername() {
        WineDrinkerManager wineDrinkerManager = (url == null) ? WineDrinkerManager.getInstance() : WineDrinkerManager.getInstance(url);
        return wineDrinkerManager.getCurrentUser().getUsername();
    }

    /**
     * Adds a single object of type T to database
     *
     * @param toAdd object of type T to add
     * @return object insert id if inserted correctly
     * @throws WineDrinkerAlreadyExistsException if method is called with a WineDrinker with a username that already exists
     */
    @Override
    public int add(WineLog toAdd) throws WineDrinkerAlreadyExistsException {
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
     * Updates an object in the database
     *
     * @param toUpdate Object that needs to be updated (this object must be able to identify itself and its previous self)
     * @return
     */
    @Override
    public int update(WineLog toUpdate) {
        return 0;
    }

    private void setAddLogParams(PreparedStatement ps, WineLog toAdd) throws SQLException {
        ps.setString(1, getUsername());
        ps.setInt(2, toAdd.getUniqueWineId());
        ps.setString(3, toAdd.getNote());
        ps.setDate(4, toAdd.getDate());
        ps.setTime(5, toAdd.getTime());
    }

    private WineLog getLogFromResultSet(ResultSet resultSet) throws SQLException {
        int wineId = resultSet.getInt(1);
        String note = resultSet.getString(2);
        Date date = resultSet.getDate(3);
        Time time = resultSet.getTime(4);
        float quantity = resultSet.getFloat(5);
        return new WineLog(wineId, note, date, time, quantity);
    }

    private void setDeleteParams(PreparedStatement ps, WineLog toDelete) throws SQLException {
        ps.setString(1, getUsername());
        ps.setInt(2, toDelete.getUniqueWineId());
        ps.setDate(3, toDelete.getDate());
        ps.setTime(4, toDelete.getTime());
    }
}
