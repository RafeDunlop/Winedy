package seng202.team3.models;

import java.sql.Date;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

/**
 * An Enum containing values that represent different time periods. Defines methods useful for splitting Date objects
 * into each time period
 */
public enum TimePeriod {

    /**
     * Represents a singular day
     */
    DAYS,

    /**
     * Represents a week (7 days)
     */
    WEEKS,

    /**
     * Represents a calendar month
     */
    MONTHS,

    /**
     * Represents a calendar year
     */
    YEARS;

    /**
     * The number of milliseconds in a day
     */
    public final static long dayMillis = 24 * 60 * 60 * 1000;

    /**
     * Splits the given list of Timed objects T into its smaller groups (e.g. Month gets split into Weeks, Weeks into Days)
     * Returns a hash map that maps an integer to a list of Timed objects T where each list is a smaller group of T
     *
     * @param toSplit the list of Timed objects T to be split into its smaller groups
     * @return a hash map mapping an integer index to the smaller groups (lists) of T
     * @param <T> an object that extends the Timed class
     */
    public <T extends Timed> HashMap<Integer, List<T>> splitIntoPeriods(List<T> toSplit) {
        HashMap<Integer, List<T>> hashMap = new HashMap<>();
        toSplit.sort(Comparator.comparing(T::getDate));
        switch (this) {
            case DAYS:
                splitIntoDays(hashMap, toSplit);
                break;
            case WEEKS:
                splitIntoWeeks(hashMap, toSplit);
                break;
            case MONTHS:
                splitIntoMonths(hashMap, toSplit);
                break;
            case YEARS:
                splitIntoYears(hashMap, toSplit);
                break;
        }
        return hashMap;
    }

    /**
     * Splits the given list of Timed objects T into its year groups. Puts the groups of T into the given hashmap that
     * maps an integer to a list of T where each list represents a singular year.
     *
     * @param hashMap the hashmap that the year groups of T are to be put into
     * @param toSplit the list of T to be split into years
     * @param <T> an object type T which extends the Timed class
     */
    private <T extends Timed> void splitIntoYears(HashMap<Integer, List<T>> hashMap, List<T> toSplit) {
        for (T entry : toSplit) {
            hashMap.merge(
                    getYearKey(entry.getDate()),
                    new ArrayList<T>(List.of(entry)),
                    getMergeFunc()
            );
        }
    }

    /**
     * Splits the given list of Timed objects T into its month groups. Puts the groups of T into the given hashmap that
     * maps an integer to a list of T where each list represents a singular month.
     *
     * @param hashMap the hashmap that the year groups of T are to be put into
     * @param toSplit the list of T to be split into months
     * @param <T> an object type T which extends the Timed class
     */
    private <T extends Timed> void splitIntoMonths(HashMap<Integer, List<T>> hashMap, List<T> toSplit) {
        for (T entry : toSplit) {
            hashMap.merge(
                    getMonthKey(entry.getDate()),
                    new ArrayList<T>(List.of(entry)),
                    getMergeFunc()
            );
        }
    }

    /**
     * Splits the given list of Timed objects T into its week groups. Puts the groups of T into the given hashmap that
     * maps an integer to a list of T where each list represents a singular week.
     *
     * @param hashMap the hashmap that the year groups of T are to be put into
     * @param toSplit the list of T to be split into weeks
     * @param <T> an object type T which extends the Timed class
     */
    private <T extends Timed> void splitIntoWeeks(HashMap<Integer, List<T>> hashMap, List<T> toSplit) {
        int weekNum = toSplit.getFirst().getDate().toLocalDate().get(WeekFields.ISO.weekOfWeekBasedYear());
        for (T entry : toSplit) {
            hashMap.merge(
                    getWeekKey(entry.getDate(), weekNum),
                    new ArrayList<T>(List.of(entry)),
                    getMergeFunc()
            );
        }
    }

    /**
     * Returns the BiFunction used to merge Hashmaps when there is an existing key used by all splitting methods
     *
     * @return BiFunction to be passed to HashMap.merge()
     * @param <T> generic type which implements Timed or an extension thereof. Timed specifies the getTime() method
     *           which is use for comparators
     */
    private <T extends Timed> BiFunction<List<T>, List<T>, List<T>> getMergeFunc() {
        return (existingList, newList) -> {
            existingList.addAll(newList);
            return existingList;
        };
    }

    /**
     * Splits the given list of Timed objects T into its day groups. Puts the groups of T into the given hashmap that
     * maps an integer to a list of T where each list represents a singular day.
     *
     * @param hashMap the hashmap that the year groups of T are to be put into
     * @param toSplit the list of T to be split into days
     * @param <T> an object type T which extends the Timed class
     */
    private <T extends Timed> void splitIntoDays(HashMap<Integer, List<T>> hashMap, List<T> toSplit) {
        for (T entry : toSplit) {
            hashMap.merge(
                    getDayKey(entry.getDate()),
                    new ArrayList<>(List.of(entry)),
                    getMergeFunc()
            );
        }
    }

    /**
     * Retrieves which week of the month the given Date object is in
     *
     * @param date the date whose week of the month to return
     * @param startWeek the first week of the month the date is in
     * @return an integer representing which week of the month the Date is in
     */
    private int getWeekKey(Date date, int startWeek) {
        return date.toLocalDate().get(WeekFields.ISO.weekOfWeekBasedYear()) - startWeek;
    }

    /**
     * Retrieves the day of the week value of the given Date object
     *
     * @param date the date whose day of the week value to return
     * @return the value of the day of the week of the date
     */
    private int getDayKey(Date date) {
        return date.toLocalDate().getDayOfWeek().getValue();
    }

    /**
     * Retrieves the month value of the given date as an integer
     *
     * @param date the date whose month value is to be returned
     * @return the month value of the given date
     */
    private int getMonthKey(Date date) {
        return date.toLocalDate().getMonthValue();
    }

    /**
     * Retrieves the year value of the given date as an integer
     *
     * @param date the date whose year value is to be returned
     * @return the year value of the given date
     */
    private int getYearKey(Date date) {
        return date.toLocalDate().getYear();
    }
}
