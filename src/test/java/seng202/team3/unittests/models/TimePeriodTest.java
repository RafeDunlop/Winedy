package seng202.team3.unittests.models;

import com.password4j.Hash;
import org.junit.jupiter.api.Test;
import seng202.team3.models.TimePeriod;
import seng202.team3.models.WineLog;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimePeriodTest {

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


    @Test
    public void splitIntoPeriodsDaysTest() {
        List<WineLog> logsInWeek = List.of(log2, log1, log0);
        HashMap<Integer, List<WineLog>> logsInDays = TimePeriod.DAYS.splitIntoPeriods(logsInWeek);
        assertEquals(2, logsInDays.size());
        assertEquals(2, logsInDays.get(1).size());
    }

    @Test
    public void splitIntoPeriodsWeeksTest() {
        List<WineLog> logsInMonth = List.of(log5, log4, log3, log2, log1, log0);
        HashMap<Integer, List<WineLog>> logsInWeeks = TimePeriod.WEEKS.splitIntoPeriods(logsInMonth);
        assertEquals(3, logsInWeeks.size());
        assertEquals(4, logsInWeeks.get(0).size());
    }

    @Test
    public void splitIntoPeriodsMonthsTest() {
        List<WineLog> logsInYear = List.of(log7, log6, log5, log4, log3, log2, log1, log0);
        HashMap<Integer, List<WineLog>> logsInMonths = TimePeriod.MONTHS.splitIntoPeriods(logsInYear);
        assertEquals(3, logsInMonths.size());
        assertEquals(6, logsInMonths.get(0).size());
    }

    @Test
    public void splitIntoPeriodsYearsTest() {
        List<WineLog> allLogs = List.of(log9, log8, log7, log6, log5, log4, log3, log2, log1, log0);
        HashMap<Integer, List<WineLog>> logsInYear = TimePeriod.YEARS.splitIntoPeriods(allLogs);
        assertEquals(3, logsInYear.size());
        assertEquals(8, logsInYear.get(2024).size());
    }
}
