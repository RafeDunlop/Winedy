package seng202.team3.unittests.services;

import javafx.util.Pair;
import javafx.util.StringConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.models.LogDiff;
import seng202.team3.models.WineDrinker;
import seng202.team3.models.WineLog;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.WineDAO;
import seng202.team3.repository.WineLogDAO;
import seng202.team3.services.LogManager;
import seng202.team3.services.WineDrinkerManager;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogManagerTest {

    private static final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";

    private final WineLog log0 = new WineLog(0, "Log 0",
            Date.valueOf(LocalDate.of(2024, 1, 1)),
            Time.valueOf(LocalTime.of(1, 1)), 1, false);
    private final WineLog log1 = new WineLog(0, "Log 1",
            Date.valueOf(LocalDate.of(2024, 1, 1)),
            Time.valueOf(LocalTime.of(2, 1)), 1, false);
    private final WineLog log2 = new WineLog(0, "Log 2",
            Date.valueOf(LocalDate.of(2024, 1, 3)),
            Time.valueOf(LocalTime.of(1, 1)), 1, false);
    private final WineLog log3 = new WineLog(0, "Log 3",
            Date.valueOf(LocalDate.of(2024, 1, 6)),
            Time.valueOf(LocalTime.of(1, 1)), 1, false);
    private final WineLog log4 = new WineLog(0, "Log 4",
            Date.valueOf(LocalDate.of(2024, 1, 9)),
            Time.valueOf(LocalTime.of(1, 1)), 1, false);
    private final WineLog log5 = new WineLog(0, "Log 5",
            Date.valueOf(LocalDate.of(2024, 1, 28)),
            Time.valueOf(LocalTime.of(1, 1)), 1, false);
    private final WineLog log6 = new WineLog(0, "Log 6",
            Date.valueOf(LocalDate.of(2024, 4, 1)),
            Time.valueOf(LocalTime.of(1, 1)), 1, false);
    private final WineLog log7 = new WineLog(0, "Log 7",
            Date.valueOf(LocalDate.of(2024, 12, 1)),
            Time.valueOf(LocalTime.of(1, 1)), 1, false);
    private final WineLog log8 = new WineLog(0, "Log 8",
            Date.valueOf(LocalDate.of(2023, 12, 1)),
            Time.valueOf(LocalTime.of(1, 1)), 1, false);
    private final WineLog log9 = new WineLog(0, "Log 9",
            Date.valueOf(LocalDate.of(2022, 12, 1)),
            Time.valueOf(LocalTime.of(1, 1)), 1, false);

    private static LogManager logManager;

    private static WineDrinkerManager wineDrinkerManager;

    private static WineLogDAO wineLogDAO;

    private static WineDAO wineDAO;

    @BeforeAll
    public static void deleteTestDB() {
        File file = new File(DATABASE_PATH);
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        LogManager.REMOVE_INSTANCE();
        WineDrinkerManager.REMOVE_INSTANCE();
        logManager = LogManager.getInstance(DATABASE_PATH);
        wineDrinkerManager = WineDrinkerManager.getInstance(DATABASE_PATH);
        wineDAO = new WineDAO(DATABASE_PATH);
        wineDrinkerManager.setCurrentUser(new WineDrinker("test1",
                "test1",
                "New Zealand",
                "Red",
                "Dry",
                "A Grape",
                15));
        wineLogDAO = new WineLogDAO(DATABASE_PATH);
        wineLogDAO.add(log0);
        wineLogDAO.add(log1);
        wineLogDAO.add(log2);
        wineLogDAO.add(log3);
        wineLogDAO.add(log4);
        wineLogDAO.add(log5);
        wineLogDAO.add(log6);
        wineLogDAO.add(log7);
        wineLogDAO.add(log8);
        wineLogDAO.add(log9);
    }
    @AfterEach
    public void cleanup(){
        File file = new File(DATABASE_PATH.substring(12));
        file.delete();
    }

    @Test
    public void getCurrentDateTest() {
        Date today = new Date(System.currentTimeMillis());
        Date dateToCheck = logManager.getCurrentDate();
        assertTrue(today.equals(dateToCheck));  // Intellij says this can be simplified to assertEquals, this is untrue as assertTrue does not call equals()
    }

    @Test
    public void getCurrentTimeTest() {
        Time timeToTest = logManager.getCurrentTime();
        long timeToTestMillis = timeToTest.getTime();
        long timeMillisNow = System.currentTimeMillis();
        assertTrue(timeMillisNow < (timeToTestMillis + 1000));
    }

    @Test
    public void getAllLogsTest() {
        List<WineLog> listToTest = logManager.getAllLogs();
        assertEquals(10, listToTest.size());
    }

    @Test
    public void getLogsInRangeTest() {
        List<WineLog> listToTest = logManager.getLogsInRange(Date.valueOf(LocalDate.of(2024, 1, 1)),
                Date.valueOf(LocalDate.of(2024, 4, 1)));
        assertEquals(7, listToTest.size());
    }

    @Test
    public void addLogIsGlassesTest() {
        LogDiff logDiff = new LogDiff();
        logDiff.setWine(wineDAO.getWineByID(1));
        logDiff.setAmt(1);
        logDiff.setNote("");
        logDiff.setDate(Date.valueOf(LocalDate.of(2024, 1, 1)));
        logDiff.setHour(12);
        logDiff.setIsBottles(false);
        logManager.addLog(logDiff);
        List<WineLog> logs = logManager.getAllLogs();
        WineLog expectedLog = new WineLog(1, "", Date.valueOf(LocalDate.of(2024, 1, 1)),
                Time.valueOf(LocalTime.of(12, 0, 0)), 1.4f, false);
        WineLog logToTest = logs.get(6);
        assertTrue(expectedLog.equals(logToTest));
    }

    @Test
    public void addLogIsBottlesTest() {
        LogDiff logDiff = new LogDiff();
        logDiff.setWine(wineDAO.getWineByID(1));
        logDiff.setAmt(1);
        logDiff.setNote("");
        logDiff.setDate(Date.valueOf(LocalDate.of(2024, 1, 1)));
        logDiff.setHour(12);
        logDiff.setIsBottles(true);
        logManager.addLog(logDiff);
        List<WineLog> logs = logManager.getAllLogs();
        WineLog expectedLog = new WineLog(1, "", Date.valueOf(LocalDate.of(2024, 1, 1)),
                Time.valueOf(LocalTime.of(12, 0, 0)), 8.4f, true);
        WineLog logToTest = logs.get(6);
        assertTrue(expectedLog.equals(logToTest));
    }

    @Test
    public void getAmtIsGlassesTest() {
        float testAmt = logManager.getAmt(wineDAO.getWineByID(2), 1.5f, false);
        assertEquals("1.07", String.format("%.2f", testAmt));
    }

    @Test
    public void getAmtIsBottlesTest() {
        float testAmt = logManager.getAmt(wineDAO.getWineByID(3), 1.5f, true);
        assertEquals("0.19", String.format("%.2f", testAmt));
    }

    @Test
    public void updateWineLogTest() {
        LogDiff logDiff = new LogDiff();
        logDiff.setWine(wineDAO.getWineByID(1));
        logDiff.setAmt(1);
        logDiff.setNote("");
        logDiff.setDate(Date.valueOf(LocalDate.of(2024, 1, 1)));
        logDiff.setHour(12);
        logDiff.setIsBottles(false);
        logManager.addLog(logDiff);
        List<WineLog> oldLogs = logManager.getAllLogs();
        WineLog oldLog = oldLogs.get(6);
        logDiff.setNote("Hey yall");
        logManager.update(oldLog, logDiff);
        List<WineLog> newLogs = logManager.getAllLogs();
        WineLog newLog = newLogs.get(6);
        assertEquals("Hey yall", newLog.getNote());
    }

    @Test
    public void updateWineLogReturnsUpDatedLogTest() {
        LogDiff logDiff = new LogDiff();
        logDiff.setWine(wineDAO.getWineByID(1));
        logDiff.setAmt(1);
        logDiff.setNote("");
        logDiff.setDate(Date.valueOf(LocalDate.of(2024, 1, 1)));
        logDiff.setHour(12);
        logDiff.setIsBottles(false);
        logManager.addLog(logDiff);
        List<WineLog> oldLogs = logManager.getAllLogs();
        WineLog oldLog = oldLogs.get(6);
        logDiff.setNote("Hey yall");
        WineLog newLog = logManager.update(oldLog, logDiff);
        assertEquals("Hey yall", newLog.getNote());
    }

    @Test
    public void getLogDateStringTest() {
        String logDateString = logManager.getLogDateString(log3);
        assertEquals("06/01/2024", logDateString);
    }

    @Test
    public void getDateStringTest() {
        Date toTest = Date.valueOf(LocalDate.of(2028, 7, 3));
        String testString = logManager.getDateString(toTest);
        assertEquals("the 3rd of July 2028", testString);
    }

    @Test
    public void validateAmountStringEmptyTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("");
        assertEquals("", validation.getValue());
    }

    @Test
    public void validateAmountBooleanEmptyTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("");
        assertFalse(validation.getKey());
    }

    @Test
    public void validateAmountStringTooLongTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("This string is definitely more than 21 characters long");
        assertEquals("your amount entry is too long", validation.getValue());
    }

    @Test
    public void validateAmountBooleanTooLongTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("This string is definitely more than 21 characters long");
        assertFalse(validation.getKey());
    }

    @Test
    public void validateAmountStringNegativeTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("-1");
        assertEquals("Amount must be positive", validation.getValue());
    }

    @Test
    public void validateAmountBooleanNegativeTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("-1");
        assertFalse(validation.getKey());
    }

    @Test
    public void validateAmountStringZeroTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("0");
        assertEquals("Amount cannot be 0", validation.getValue());
    }

    @Test
    public void validateAmountBooleanZeroTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("0");
        assertFalse(validation.getKey());
    }

    @Test
    public void validateAmountStringValidValueTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("23");
        assertEquals("errorDisplayLabel", validation.getValue());
    }

    @Test
    public void validateAmountBooleanValidValueTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("23");
        assertTrue(validation.getKey());
    }

    @Test
    public void validateAmountStringNANTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("Hello World!");
        assertEquals("Hello World! is not a valid number", validation.getValue());
    }

    @Test
    public void validateAmountBooleanNANTest() {
        Pair<Boolean, String> validation = logManager.validateAmount("Hello World!");
        assertFalse(validation.getKey());
    }

    @Test
    public void hourConverterToStringAMTest() {
        StringConverter<Integer> hourConverter = logManager.getHourConverter();
        String threeAM = hourConverter.toString(3);
        assertEquals("3AM", threeAM);
    }

    @Test
    public void hourConverterToStringPMTest() {
        StringConverter<Integer> hourConverter = logManager.getHourConverter();
        String fivePM = hourConverter.toString(17);
        assertEquals("5PM", fivePM);
    }

    @Test
    public void hourConverterToString12PMTest() {
        StringConverter<Integer> hourConverter = logManager.getHourConverter();
        String twelvePM = hourConverter.toString(12);
        assertEquals("12PM", twelvePM);
    }

    @Test
    public void hourConverterToString12AMTest() {
        StringConverter<Integer> hourConverter = logManager.getHourConverter();
        String twelveAM = hourConverter.toString(0);
        assertEquals("12AM", twelveAM);
    }

    @Test
    public void hourConverterFromStringAMTest() {
        StringConverter<Integer> hourConverter = logManager.getHourConverter();
        int sixAM = hourConverter.fromString("6AM");
        assertEquals(6, sixAM);
    }

    @Test
    public void hourConverterFromStringPMTest() {
        StringConverter<Integer> hourConverter = logManager.getHourConverter();
        int ninePM = hourConverter.fromString("9PM");
        assertEquals(21, ninePM);
    }

    @Test
    public void hourConverterFromString12PMTest() {
        StringConverter<Integer> hourConverter = logManager.getHourConverter();
        int twelvePM = hourConverter.fromString("12PM");
        assertEquals(12, twelvePM);
    }

    @Test
    public void hourConverterFromString12AMTest() {
        StringConverter<Integer> hourConverter = logManager.getHourConverter();
        int twelveAM = hourConverter.fromString("12AM");
        assertEquals(0, twelveAM);
    }
}
