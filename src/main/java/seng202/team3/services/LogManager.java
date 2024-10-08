package seng202.team3.services;

import seng202.team3.models.WineLog;
import seng202.team3.repository.WineLogDAO;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * singleton class for managing logged wines and helper functions for consumption tracking
 *
 * @author Rafe Dunlop (rdu46)
 */
public class LogManager {

    /**
     * instance used by this class so that the correct database is used (test vs actual)
     */
    private final WineLogDAO wineLogDAO;

    /**
     * Singleton instance of WineLogManager
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
     * gets the singleton instance of WineLogManager
     * if none is set, it is set up with the default database path
     * @return unique WineLogManager
     */
    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    /**
     * gets the singleton instance of WineLogManager
     * if none is set, it is set up with the specified database path
     *
     * @param url database path with which to set up the instance (if the instance has not been instantiated)
     * @return unique WineLogManager
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

    /**
     * gets the current date as a Java.sql.Date object
     *
     * @return current date
     */
    public Date getCurrentDate() {
        long millis = System.currentTimeMillis();
        return new Date(millis);
    }

    /**
     * gets the current time as a Java.sql.Time object
     *
     * @return current time
     */
    public Time getCurrentTime() {
        long millis = System.currentTimeMillis();
        return new Time(millis);
    }

    /**
     * gets all wine logs associated with the logged-in user ordered by their recency
     *
     * @return List of all user's wine logs
     */
    public List<WineLog> getAllLogs() {
        return wineLogDAO.getAll();
    }

    /**
     * gets all the logs associated with the logged-in user ordered by recency which correspond to the specified range (inclusive)
     * @param startDate the first date that logs should be fetched
     * @param endDate the last date that logs should be fetched
     * @return the List of WineLogs that were created in the given range
     */
    public List<WineLog> getLogsInRange(Date startDate, Date endDate) {
        return getLogsInRange(startDate, endDate);
    }

    /**
     * adds a log with the specified parameters to the database
     * all parameters except note are mandatory
     * @param loggedID foreign key to the wine in wineSuper which teh log is about
     * @param note the note associated with this log
     * @param date the date of the log as a Java.sql.date object
     * @param time the time of day teh log was created as a Java.sql.Time object
     * @param standards the number of NZ standard drinks the log corresponds to as a float
     * @return the added WineLog object
     */
    public WineLog addLog(int loggedID, String note, Date date, Time time, float standards) {
        WineLog toLog = new WineLog(loggedID, note, date, time, standards);
        wineLogDAO.add(toLog);
        return toLog;
    }

    /**
     * updates the specified log
     * all parameters are optional except the Log to be updated (toUpdate)
     * this method is NOT in place, the returned value should be used
     * @param toUpdate the WineLog object to be updated
     * @param newLoggedID foreign key to the wine in wineSuper which teh log is about
     * @param newNote the note associated with this log
     * @param newDate the date of the log as a Java.sql.date object
     * @param newTime the time of day teh log was created as a Java.sql.Time object
     * @param newStandards the number of NZ standard drinks the log corresponds to as a float
     * @return the updated WineLog object (different object to parameter)
     */
    public WineLog update(WineLog toUpdate, int newLoggedID, String newNote, Date newDate, Time newTime, float newStandards) {
        return wineLogDAO.update(toUpdate, newLoggedID, newNote, newDate, newTime, newStandards);
    }

    /**
     * deletes the specified WineLog from the database
     * @param toDelete The WineLog object to be deleted
     */
    public void deleteLog(WineLog toDelete) {
        wineLogDAO.delete(toDelete);
    }

    public String getLogDateString(WineLog wineLog) {
        LocalDate date = wineLog.getDate().toLocalDate();
        return date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear();
    }

}
