package seng202.team3.unittests.models;

import javafx.util.Pair;
import javafx.util.StringConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.models.TimeRange;
import seng202.team3.repository.DatabaseManager;

import java.io.File;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seng202.team3.models.TimePeriod.dayMillis;

public class TimeRangeTest {

    final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";

    public static void assertDatePairsEqual(Pair<Date, Date> pair1, Pair<Date, Date> pair2) {
        assertEquals(pair1.getKey().toString(), pair2.getKey().toString());
        assertEquals(pair1.getValue().toString(), pair2.getValue().toString());
    }

    @BeforeAll
    public static void deleteTestDB() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
    }

    @AfterEach
    public void cleanUp() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @Test
    public void getDateRangeThisWeekTest() {
        Pair<Date, Date> weekToCheck = TimeRange.getDateRange(TimeRange.THISWEEK);
        Date today = new Date(System.currentTimeMillis());
        Calendar cal = TimeRange.getResetCalendar();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date soWeek = new Date(cal.getTimeInMillis());
        Pair<Date, Date> thisWeek = new Pair<>(soWeek, today);
        assertDatePairsEqual(thisWeek, weekToCheck);
    }

    @Test
    public void getDateRangeLastWeekTest() {
        Pair<Date, Date> weekToCheck = TimeRange.getDateRange(TimeRange.LASTWEEK);
        Calendar cal = TimeRange.getResetCalendar();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date soWeek = new Date(cal.getTimeInMillis());
        Pair<Date, Date> lastWeek = new Pair<>(new Date(soWeek.getTime() - 7 * dayMillis), new Date(soWeek.getTime() - dayMillis));
        assertDatePairsEqual(lastWeek, weekToCheck);
    }

    @Test
    public void getDateRangeThisMonthTest() {
        Pair<Date, Date> monthToCheck = TimeRange.getDateRange(TimeRange.THISMONTH);
        Date today = new Date(System.currentTimeMillis());
        Calendar cal = TimeRange.getResetCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date soThisMonth = new Date(cal.getTimeInMillis());
        Pair<Date, Date> thisMonth = new Pair<>(soThisMonth, today);
        assertDatePairsEqual(thisMonth, monthToCheck);
    }

    @Test
    public void getDateRangeLastMonthTest() {
        Pair<Date, Date> monthToCheck = TimeRange.getDateRange(TimeRange.LASTMONTH);
        Calendar cal = TimeRange.getResetCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date eoLastMonth = new Date(cal.getTimeInMillis() - dayMillis);
        cal.add(Calendar.MONTH, -1);
        Date soLastMonth = new Date(cal.getTimeInMillis());
        Pair<Date, Date> lastMonth = new Pair<>(soLastMonth, eoLastMonth);
        assertDatePairsEqual(lastMonth, monthToCheck);
    }

    @Test
    public void getDateRangeThisYearTest() {
        Pair<Date, Date> yearToCheck = TimeRange.getDateRange(TimeRange.THISYEAR);
        Date today = new Date(System.currentTimeMillis());
        Calendar cal = TimeRange.getResetCalendar();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date soThisYear = new Date(cal.getTimeInMillis());
        Pair<Date, Date> thisYear = new Pair<>(soThisYear, today);
        assertDatePairsEqual(thisYear, yearToCheck);
    }

    @Test
    public void getDateRangeLastYearTest() {
        Pair<Date, Date> yearToCheck = TimeRange.getDateRange(TimeRange.LASTYEAR);
        Calendar cal = TimeRange.getResetCalendar();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date eoLastYear = new Date(cal.getTimeInMillis() - dayMillis);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date soLastYear = new Date(cal.getTimeInMillis());
        Pair<Date, Date> lastYear = new Pair<>(soLastYear, eoLastYear);
        assertEquals(lastYear, yearToCheck);
    }

    @Test
    public void getDateRangeAllTimeTest() {
        Pair<Date, Date> rangeToCheck = TimeRange.getDateRange(TimeRange.ALLTIME);
        Date today = new Date(System.currentTimeMillis());
        Pair<Date, Date> allTime = new Pair<>(new Date(0), today);
        assertDatePairsEqual(allTime, rangeToCheck);
    }

    @Test
    public void getLabelThisWeekTest() {
        String label = TimeRange.getLabel(TimeRange.THISWEEK);
        assertEquals("Days", label);
    }

    @Test
    public void getLabelLastWeekTest() {
        String label = TimeRange.getLabel(TimeRange.LASTWEEK);
        assertEquals("Days", label);
    }

    @Test
    public void getLabelThisMonthTest() {
        String label = TimeRange.getLabel(TimeRange.THISMONTH);
        assertEquals("Weeks", label);
    }

    @Test
    public void getLabelLastMonthTest() {
        String label = TimeRange.getLabel(TimeRange.LASTMONTH);
        assertEquals("Weeks", label);
    }

    @Test
    public void getLabelThisYearTest() {
        String label = TimeRange.getLabel(TimeRange.THISYEAR);
        assertEquals("Months", label);
    }

    @Test
    public void getLabelLastYearTest() {
        String label = TimeRange.getLabel(TimeRange.LASTYEAR);
        assertEquals("Months", label);
    }

    @Test
    public void getLabelAllTimeTest() {
        String label = TimeRange.getLabel(TimeRange.ALLTIME);
        assertEquals("Years", label);
    }

    @Test
    public void stringConverterToStringTest() {
        StringConverter<TimeRange> stringConverter = TimeRange.getStringConverter();
        String lastWeek = stringConverter.toString(TimeRange.LASTWEEK);
        assertEquals(TimeRange.LASTWEEK.strRep, lastWeek);
    }

    @Test
    public void stringConverterFromString() {
        StringConverter<TimeRange> stringConverter = TimeRange.getStringConverter();
        TimeRange thisMonth = stringConverter.fromString("This month");
        assertEquals(TimeRange.THISMONTH, thisMonth);
    }

    @Test
    public void getAllTest() {
        List<TimeRange> allTimeRangesToTest = TimeRange.getAll();
        assertEquals(7, allTimeRangesToTest.size());
    }

    @Test
    public void getResetCalendarTest() {
        Calendar cal = TimeRange.getResetCalendar();
        assertEquals(1728691200000L, cal.getTimeInMillis());
    }

    @Test
    public void getStringRepDaysTuesdayTest() {
        String tuesday = TimeRange.getStringRep(2, TimeRange.THISWEEK);
        assertEquals("Tue", tuesday);
    }

    @Test
    public void getStringRepWeeksWeek3Test() {
        String week3 = TimeRange.getStringRep(2, TimeRange.THISMONTH);
        assertEquals("Week 3", week3);
    }

    @Test
    public void getStringRepMonthsMarchTest() {
        String march = TimeRange.getStringRep(2, TimeRange.THISYEAR);
        assertEquals("March", march);
    }

    @Test
    public void getStringRepYears2024Test() {
        String twentytwentyfour = TimeRange.getStringRep(2024, TimeRange.ALLTIME);
        assertEquals("2024", twentytwentyfour);
    }
}
