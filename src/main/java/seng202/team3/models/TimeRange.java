package seng202.team3.models;

import javafx.util.Pair;
import javafx.util.StringConverter;
import seng202.team3.services.LogManager;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public enum TimeRange {

    /**
     * Represents the current week, from Monday till the current day
     */
    THISWEEK("This week"),

    /**
     * Represents the previous week, from Monday to Sunday
     */
    LASTWEEK("Last week"),

    /**
     * Represents the past two weeks, from Monday to the second Sunday after
     */
    PASTTWOWEEKS("Past two weeks"),

    /**
     * Represents the current month, from the first of the month to the current day
     */
    THISMONTH("This month"),

    /**
     * Represents the entire previous month
     */
    LASTMONTH("Last month"),

    /**
     * Represents the current year, from the first of January to the current day
     */
    THISYEAR("This year"),

    /**
     * Represents the entire last year
     */
    LASTYEAR("Last year");

    /**
     * The String representation of the TimeRange value
     */
    public final String strRep;

    /**
     * Constructor for the TimeEnum values, sets the string representation of the value to be the given String
     *
     * @param strRep the textual representation of the time value, as a string
     */
    TimeRange(String strRep) {
        this.strRep = strRep;
    }

    /**
     * Returns the date range of the given TimeRange value
     *
     * @param range the TimeRange value of the requested date range
     * @return a pair object containing two Date objects, the beginning date, and end date of the range
     */
    public static Pair<Date, Date> getDateRange(TimeRange range) {
        Date now = LogManager.getInstance().getCurrentDate();
        Date soWeek = getSOWeek();
        Date soThisMonth = getSOMonth(0);
        Date soLastMonth = getSOMonth(-1);
        Date soThisYear = getSOYear(0);
        Date soLastYear = getSOYear(-1);
        long dayMillis = 24 * 60 * 60 * 1000;
        return switch (range) {
            case THISWEEK -> new Pair<>(soWeek, now);
            case LASTWEEK ->
                    new Pair<>(new Date(soWeek.getTime() - 8 * dayMillis), new Date(soWeek.getTime() - dayMillis));
            case PASTTWOWEEKS -> new Pair<>(new Date(soWeek.getTime() - 8 * dayMillis), now);
            case THISMONTH -> new Pair<>(soThisMonth, now);
            case LASTMONTH -> new Pair<>(soLastMonth, soThisMonth);
            case THISYEAR -> new Pair<>(soThisYear, now);
            case LASTYEAR -> new Pair<>(soLastYear, soThisYear);
        };
    }

    /**
     * Returns a StringConverter for converting the given TimeRange value to and from its string representation.
     * Defines/Overides two methods, toString which converts the TimeRange value to its string representation, and
     * fromString which converts a String into its respective TimeRange value, if there is no corresponding TimeRange
     * value, this is defaulted to THISWEEK.
     *
     *
     * @return a StringConverter for converting TimeRange objects to and from their string representations
     */
    public static StringConverter<TimeRange> getStringConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(TimeRange timeRange) {
                return timeRange.strRep;
            }

            @Override
            public TimeRange fromString(String s) {
                for (TimeRange range : getAll()) {
                    if (range.strRep.equals(s)) {
                        return range;
                    }
                }
                return THISWEEK;
            }
        };
    }

    /**
     * Returns a list containing all TimeRange values
     *
     * @return a list of all TimeRange values
     */
    public static List<TimeRange> getAll() {
        return List.of(THISWEEK, LASTWEEK, PASTTWOWEEKS, THISMONTH, LASTMONTH, THISYEAR, LASTYEAR);
    }

    /**
     * Resets the Calendar instance to the beginning of the current day in the New Zealand Standard Time Zone (midnight)
     *
     * @return the calendar instance that has been reset
     */
    private static Calendar getResetCalendar() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("NZDT"));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal;
    }

    /**
     * Returns a Date object representing the start of the current week in New Zealand Standard Time Zone.
     * This will be Monday at midnight
     *
     * @return the Date object representing the start of the week
     */
    private static Date getSOWeek() {
        Calendar cal = getResetCalendar();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        return new Date(cal.getTimeInMillis());
    }

    /**
     * Returns a Date object representing the start of the month in New Zealand Standard Time Zone.
     * This will be the first of the month at midnight displacement many months in the future
     *
     * @param displacement an integer representing how many months in the future (or past if negative) this Date should
     *                     be.
     * @return the Date object representing the start of the requested month
     */
    private static Date getSOMonth(int displacement) {
        Calendar cal = getResetCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, displacement);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * Returns a Date object representing the start of the year in New Zealand Standard Time Zone.
     * This will be the first of the January at midnight displacement many years in the future
     *
     * @param displacement an integer representing how many years in the future (or past if negative) this Date should
     *                     be.
     * @return the Date object representing the start of the requested year
     */
    private static Date getSOYear(int displacement) {
        Calendar cal = getResetCalendar();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.add(Calendar.DAY_OF_YEAR, displacement);
        return new Date(cal.getTimeInMillis());
    }
}
