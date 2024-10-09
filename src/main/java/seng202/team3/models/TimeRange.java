package seng202.team3.models;

import javafx.util.Pair;
import javafx.util.StringConverter;
import seng202.team3.services.LogManager;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public enum TimeRange {

    THISWEEK("This week"),
    LASTWEEK("Last week"),
    PASTTWOWEEKS("Past two weeks"),
    THISMONTH("This month"),
    LASTMONTH("Last month"),
    THISYEAR("This year"),
    LASTYEAR("Last year");

    public final String strRep;

    TimeRange(String strRep) {
        this.strRep = strRep;
    }

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

    public static List<TimeRange> getAll() {
        return List.of(THISWEEK, LASTWEEK, PASTTWOWEEKS, THISMONTH, LASTMONTH, THISYEAR, LASTYEAR);
    }

    public static Calendar getResetCalendar() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("NZDT"));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal;
    }

    private static Date getSOWeek() {
        Calendar cal = getResetCalendar();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        return new Date(cal.getTimeInMillis());
    }

    private static Date getSOMonth(int displacement) {
        Calendar cal = getResetCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, displacement);
        return new Date(cal.getTimeInMillis());
    }

    private static Date getSOYear(int displacement) {
        Calendar cal = getResetCalendar();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.add(Calendar.DAY_OF_YEAR, displacement);
        return new Date(cal.getTimeInMillis());
    }


}
