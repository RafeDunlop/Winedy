package seng202.team3.services;

import javafx.util.Pair;
import javafx.util.StringConverter;
import seng202.team3.models.LogDiff;
import seng202.team3.models.TimeRange;
import seng202.team3.models.Wine;
import seng202.team3.models.WineLog;
import seng202.team3.repository.WineLogDAO;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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

    public final static float RHO_ALCOHOL = 0.8f;

    public final static float DEFAULT_WINE_VOLUME = 750.0f;

    public final static float DEFAULT_WINE_ABV = 14.0f; //typically 12-14 but assume high for safety

    public final static float GRAMS_ALCOHOL_PER_NZ_STAN_DRINK = 10.0f;

    public final static float STANDARDS_PER_GLASS = 1.4f;

    private TimeRange prevRange = TimeRange.THISWEEK;

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
        wineLogDAO = new WineLogDAO();
    }

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

    public TimeRange getPrevRange() {
        return prevRange;
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
        return wineLogDAO.getInRange(startDate, endDate);
    }

    /**
     * adds a log with the specified parameters to the database
     * all parameters except note are mandatory
     * note that a glass is defined as 1.4 standards. This is because a "glass of wine" is slightly ill-defined.
     * A user logging port, for instance, should log a port glass of wine as one glass. The inflexibility of this
     * assumption is negligible in comparison to the human error with the logging itself
     * @param logDiff LogDiff object which wraps relevant args (name, Wine, hour, amount, isBottles)
     */
    public void addLog(LogDiff logDiff) {
        int loggedID = logDiff.getWine().getUniqueWineID();
        Time time = Time.valueOf(LocalTime.of(logDiff.getHour(), 0, 0));
        float standards = getStandards(logDiff.getWine(), logDiff.getAmt(), logDiff.getIsBottles());
        String note = (logDiff.getNote().isEmpty()) ? null : logDiff.getNote();
        WineLog toLog = new WineLog(loggedID, note, logDiff.getDate(), time, standards, logDiff.getIsBottles());
        wineLogDAO.add(toLog);
    }

    private float getStandards(Wine wine, float amtHad, boolean isBottles) {
        float standards;
        if (isBottles) {
            float gramsAlcoholPerBottle = getGramsAlcoholPerBottle(wine);
            standards = (amtHad * gramsAlcoholPerBottle) / GRAMS_ALCOHOL_PER_NZ_STAN_DRINK;
        } else { //glasses
            standards = amtHad * STANDARDS_PER_GLASS;
        }
        return standards;
    }

    public float getAmt(Wine wine, float standards, boolean isBottles) {
        float amt;
        if (isBottles) {
            float gramsAlcoholPerBottle = getGramsAlcoholPerBottle(wine);
            float gramsAlcohol = (standards * GRAMS_ALCOHOL_PER_NZ_STAN_DRINK);
            amt = gramsAlcohol / gramsAlcoholPerBottle;
        } else { //glasses
            amt = standards / STANDARDS_PER_GLASS;
        }
        return amt;
    }

    private float getGramsAlcoholPerBottle(Wine wine) {
        float mlsWine = (wine.getVolumeInMl() != 0) ? wine.getVolumeInMl() : DEFAULT_WINE_VOLUME;
        float percentageABV = (wine.getAlcoholByVolume() != 0) ? wine.getAlcoholByVolume() : DEFAULT_WINE_ABV;
        float mlsAlcoholPerBottle = (percentageABV * mlsWine) / 100; //convert percentage to decimal
        return mlsAlcoholPerBottle * RHO_ALCOHOL;
    }

    /**
     * updates the specified log
     * all parameters are optional except the Log to be updated (toUpdate)
     * this method is NOT in place, the returned value should be used
     * @param toUpdate the WineLog object to be updated
     * @param changes LogDiff object which holds the relevant updates
     * @return the updated WineLog object (different object to parameter)
     */
    public WineLog update(WineLog toUpdate, LogDiff changes) {
        return wineLogDAO.update(
                toUpdate,
                changes.getWine().getUniqueWineID(),
                changes.getNote(),
                changes.getDate(),
                Time.valueOf(LocalTime.of(changes.getHour(), 0, 0)),
                getStandards(changes.getWine(), changes.getAmt(), changes.getIsBottles()),
                changes.getIsBottles());
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
        return ((date.getDayOfMonth() < 10) ? "0" + date.getDayOfMonth(): date.getDayOfMonth()) +
                "/" +
                ((date.getMonthValue() < 10) ? "0" + date.getMonthValue(): date.getMonthValue()) +
                "/" +
                date.getYear();
    }

    public String getDateString(Date date) {
        LocalDate lDate = date.toLocalDate();
        int dayOM = lDate.getDayOfMonth();
        String monthString = lDate.getMonth().toString().toLowerCase();
        return String.format("the %d%s of %s %d",
                dayOM,
                getDaySuffix(dayOM),
                monthString.substring(0, 1).toUpperCase() + monthString.substring(1),
                lDate.getYear());
    }

    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }

        return switch (day % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    public Pair<Boolean, String> validateAmount(String toValidate) {
        int maxLength = 21;
        if (toValidate.isEmpty()) {
            return new Pair<>(false, "");
        } else if (toValidate.length() > maxLength) {
            return new Pair<>(false, "your amount entry is too long");
        }
        try {
            float value = Float.parseFloat(toValidate);
            if (value < 0) {
                return new Pair<>(false, "Amount must be positive");
            } else if (value == 0) {
                return new Pair<>(false, "Amount cannot be 0");
            } else if (value > 10){
                return new Pair<>(false, "Amount cannot be more than 10");
            } else {
                return new Pair<>(true,"errorDisplayLabel");
            }
        } catch (NumberFormatException e) {
            return new Pair<>(false, String.format("%s is not a valid number", toValidate));
        }
    }


    public StringConverter<Integer> getHourConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Integer integer) {
                if (integer == 0) {
                    return "12AM";
                }

                if (integer / 12 == 1) {
                    return (integer != 12) ? integer % 12 + "PM" : "12PM";
                }

                return integer + "AM";
            }

            @Override
            public Integer fromString(String s) {
                if (s.endsWith("PM")) {
                    s = s.replace("PM", "");
                    return (!s.equals("12")) ? Integer.parseInt(s) + 12 : 12;
                }

                if (s.equals("12AM")) {
                    return 0;
                }

                return Integer.parseInt(s.replace("AM", ""));
            }
        };
    }

    public String getAmtPromptText(boolean isBottles) {
        return "Enter how many " +
                ((isBottles) ? "bottles" : "glasses") +
                "you had.";
    }

    public void setTimeRange(TimeRange toSet) {
        prevRange = toSet;
    }
}
