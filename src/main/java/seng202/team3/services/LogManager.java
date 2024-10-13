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
import java.util.function.Consumer;
import java.util.stream.IntStream;

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
     * the density of alcohol (gmL^-1)
     */
    public final static float RHO_ALCOHOL = 0.8f;

    /**
     * the default presumed volume of a bottle of wine as a float (mL)
     */
    public final static float DEFAULT_WINE_VOLUME = 750.0f;

    /**
     * the default presumed alcohol by volume of a wine typically 12-14 but assume high for safety (%)
     */
    public final static float DEFAULT_WINE_ABV = 14.0f;

    /**
     * ratio, the definition of a standard drink in New Zealand (g(STD)^-1)
     */
    public final static float GRAMS_ALCOHOL_PER_NZ_STAN_DRINK = 10.0f;

    /**
     * the approximate number of standard drinks in a glass of wine,(Te Whatu Ora)
     * not calculated seperately becuase of inevitable innaccuracy in logging.
     */
    public final static float STANDARDS_PER_GLASS = 1.3f;

    /**
     * the last TimeRange used to categorize logs
     */
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
     * gets all the logs associated with the logged-in user ordered by recency which correspond to the specified range (inclusive)
     * @param startDate the first date that logs should be fetched
     * @param endDate the last date that logs should be fetched
     * @return the List of WineLogs that were created in the given range
     */
    public List<WineLog> getLogsInRange(Date startDate, Date endDate) {
        return wineLogDAO.getInRange(startDate, endDate);
    }

    /**
     * gets all the logs
     */
    public List<WineLog> getAllLogs() {
        return wineLogDAO.getAll();
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

    /**
     * gets the number of standard drinks from a specific wine, amount and logging type
     * using constants defined above
     * @param wine the wine to get relevant data from
     * @param amtHad the amount consumed as a number of glasses or bottles
     * @param isBottles whether amount corresponds to bottles (or glasses)
     * @return float, the number of standards this corresponds to
     */
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

    /**
     * reverses opperation of getStandards
     * @param wine the wine whose attributes are used
     * @param standards the number of standards which to be decoded
     * @param isBottles whether the number of standards was calculated via bottles (or glasses)
     * @return float, the amount the standards refers back to
     */
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

    /**
     * helper function to get the actual alcohol quantity of alcohol in a bottle, using defaults where necessary
     * @param wine the wine to get the alcohol content of
     * @return float, the number of grams of alcohol in one bottle of the specified wine
     */
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

    /**
     * gets a string to display the timing of the specified WineLog
     * @param wineLog WineLog whose timing is to be displayed
     * @return String representation of the wineLo;s timing
     */
    public String getLogDateString(WineLog wineLog) {
        LocalDate date = wineLog.getDate().toLocalDate();
        return ((date.getDayOfMonth() < 10) ? "0" + date.getDayOfMonth(): date.getDayOfMonth()) +
                "/" +
                ((date.getMonthValue() < 10) ? "0" + date.getMonthValue(): date.getMonthValue()) +
                "/" +
                date.getYear();
    }

    /**
     * gets a string to display a date readably
     * @param date Java.sql.Date object to be converted
     * @return readable String rep of the specified Java.sql.Date object
     */
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

    /**
     * gets the correct ordinal suffix to append to the day number
     * @param day the day of month to get the translation of
     * @return String suffix to append to the day number
     */
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

    /**
     * validates the amount entry
     * @param toValidate String to be parsed into an amount float
     * @return a pair, the first member of which is a boolean, true if the entry is valid (and van be parsed).
     * The second entry is an error to display if the first entry is false
     */
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

    /**
     * gets a string converter integer <--> String to show readable values for hours
     * @return a StringConverter: integer <--> String to show readable values for hours
     */
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

    /**
     * gets the prompt text to show the user what to enter and the units
     * @param isBottles whether the unit is bottles (or glasses)
     * @return String, prompt showing the prompt text
     */
    public String getAmtPromptText(boolean isBottles) {
        return "Enter how many " +
                ((isBottles) ? "bottles" : "glasses") +
                " you had.";
    }

    /**
     * sets all the valid hours via a consumer
     * @param logDiff the LogDiff whose fields are used for validation
     * @param comboSetFunc the Consumer used to accept the valid hours of the day
     */
    public void setValidHours(LogDiff logDiff, Consumer<Integer> comboSetFunc) {
        List<Integer> hours = IntStream.range(0, 24).boxed().toList();
        boolean isToday = logDiff.getDate().toLocalDate().equals(getCurrentDate().toLocalDate());
        for (int hour : hours) {
            if (!isToday || hour <= logDiff.getHour()) {
                comboSetFunc.accept(hour);
            }
        }
    }

    /**
     * sets the TimeRange that was selected so the next time the screen is loaded it uses this TimeRange.
     * Does not have database permanence
     * @param toSet the TimeRange to set
     */
    public void setTimeRange(TimeRange toSet) {
        prevRange = toSet;
    }

}
