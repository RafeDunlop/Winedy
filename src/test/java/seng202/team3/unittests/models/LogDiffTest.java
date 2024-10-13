package seng202.team3.unittests.models;

import org.junit.jupiter.api.*;
import seng202.team3.models.LogDiff;
import seng202.team3.models.Wine;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class LogDiffTest {
    LogDiff logDiff1;
    LogDiff logDiff2;
    private final int HIGHEST_ID = 782;
    private final Wine TESTWINE = new Wine(
            HIGHEST_ID + 1,
            "Nero Oro Appassimento 2018, Sicily",
            "Italy",
            "Red",
            "Big",
            new String[]{"Nero d'Avola"},
            "FULL",
            "Nero Oro is made by a winemaker who's scored a perfect 100 Parker Points.",
            (float) 9.99,
            new String[]{"IWC 2019 - Commended Award", "Decanter 2019 - Bronze Award"},
            (float) 14,
            75,
            2018);

    @BeforeEach
    public void setUp() {
        logDiff1 = new LogDiff();
        logDiff2 = new LogDiff();
        Date date = Date.valueOf("2024-10-13");
        String note = "This is a test note.";
        logDiff1.setDate(date).setHour(14).setWine(TESTWINE).setAmt(3.5f).setIsBottles(true).setNote(note);
    }

    @Test
    public void testEquals() {
        Date date = Date.valueOf("2024-10-13");
        String note = "This is a test note.";
        logDiff2.setDate(date).setHour(14).setWine(TESTWINE).setAmt(3.5f).setIsBottles(true).setNote(note);
        System.out.println(logDiff1);
        System.out.println(logDiff2);
        assertTrue(logDiff1.equals(logDiff2));
    }

    @Test
    public void testNotEquals() {
        Date date = Date.valueOf("2024-10-13");
        String note = "This is a different test note.";
        logDiff2.setDate(date).setHour(14).setWine(TESTWINE).setAmt(3.5f).setIsBottles(true).setNote(note);
        assertFalse(logDiff1.equals(logDiff2));
    }

    @Test
    public void testIsValid() {
        logDiff2.setWine(TESTWINE).setAmt(3.5f);
        assertTrue(logDiff2.isValid());
    }

    @Test
    public void testWineIsInvalid() {
        logDiff2.setWine(null).setAmt(0);
        assertFalse(logDiff2.isValid());
    }

    @Test
    public void testAmtIsInvalid() {
        logDiff2.setWine(TESTWINE).setAmt(0);
        assertFalse(logDiff2.isValid());
    }

    @Test
    public void testCopyConstructor() {
        LogDiff logDiff2 = new LogDiff(logDiff1);
        assertTrue(logDiff1.equals(logDiff2));
    }
}
