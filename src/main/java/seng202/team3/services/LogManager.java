package seng202.team3.services;

import seng202.team3.models.WineLog;
import seng202.team3.repository.WineLogDAO;

import java.time.LocalDateTime;
import java.util.List;

public class LogManager {

    /**
     * instance used by this class so that the correct database is used (test vs actual)
     */
    private final WineLogDAO wineLogDAO;

    /**
     * Singleton instance of WineListManager
     */
    private static LogManager instance;

    /**
     * private constructor with specified database path
     * @param url database path
     */
    private LogManager(String url) {
        wineLogDAO = new WineLogDAO(url);
    }

    /**
     * private constructor with default database path
     */
    private LogManager() {
        wineLogDAO = new WineLogDAO();    }

    /**
     * gets the singleton instance of WineListManager
     * if none is set, it is set up with the default database path
     * @return unique WineListManager
     */
    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    /**
     * gets the singleton instance of WineListManager
     * if none is set, it is set up with the specified database path
     *
     * @param url database path with which to set up the instance (if the instance has not been instantiated)
     * @return unique WineListManager
     */
    public static LogManager getInstance(String url) {
        if (instance == null) {
            instance = new LogManager(url);
        }
        return instance;
    }

    /**
     *  WARNING Sets the current singleton instance to null
     */
    public static void REMOVE_INSTANCE() {
        instance = null;
    }

    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public List<WineLog> getAllLogs() {
        return wineLogDAO.getAll();
    }

    public void addLog(WineLog toLog) {
        wineLogDAO.add(toLog);
    }

    public void deleteLog(WineLog toDelete) {
        wineLogDAO.delete(toDelete);
    }
}
