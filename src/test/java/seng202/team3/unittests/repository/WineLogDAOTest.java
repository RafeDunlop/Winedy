package seng202.team3.unittests.repository;

import org.junit.jupiter.api.*;
import seng202.team3.models.WineDrinker;
import seng202.team3.models.WineLog;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.WineLogDAO;
import seng202.team3.services.WineDrinkerManager;

import java.io.File;
import java.sql.Time;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for WineLogDAO
 *
 * @author Sophia Copley (sco207)
 */
public class WineLogDAOTest {
    final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    private WineLogDAO wineLogDAO;
    private final String username = "TestUser1";
    private final String password = "TestUserPassword";

    private final WineDrinker testWineDrinker = new WineDrinker(username, password, null, null, null,null,0);

    private WineDrinkerManager wineDrinkerManager;

    private final WineLog WINE_LOG_1 = new WineLog(
            10,
            "Wine I drank at a BYO at Shanghai Dumplings for my friend's birthday",
            new Date(2004, 30, 12),
            new Time(1900),
            1,
            true);

    private final WineLog WINE_LOG_2 = new WineLog(
            53,
            "Drinks at the bar after work. Really sweet tasting, maybe a little too sweet for my " +
                    "liking. I ate it with steak and fries which was a good pairing though. I really enjoyed" +
                    "the light orange notes, it was nice and wintery.",
            new Date(2022, 8, 20),
            new Time(1900),
            2.5F,
            false);
    private final WineLog WINE_LOG_3 = new WineLog(
            67,
            "Husband bought this wine and I thought I wouldn't like it but it was actually good! Turns out " +
                    "he made a winedy account for me and bought one of the wines it recommended for me! What a great " +
                    "app!",
            new Date(2024, 10, 12),
            new Time(2200),
            0.5F,
            true);

    private final WineLog WINE_LOG_4 = new WineLog(
            230,
            "This was definitely a new one for me! I have never tried a Chardonnay before but I have " +
                    "to say it might me my new favourite. Had a few too many glasses though!",
            new Date(2021, 3, 9),
            new Time(700),
            6,
            false);

    private final WineLog WINE_LOG_5 = new WineLog(
            69,
            "Pres with the girls!",
            new Date(2004, 12, 30),
            new Time(1800),
            1,
            true);
    @BeforeAll
    public static void deleteTestDB() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
        wineLogDAO = new WineLogDAO(DATABASE_PATH);
        this.wineDrinkerManager = WineDrinkerManager.getInstance(DATABASE_PATH);
        wineDrinkerManager.setCurrentUser(testWineDrinker);
        wineDrinkerManager.registerWineDrinker();
        wineDrinkerManager.loginCurrentUser(username, password);
        wineLogDAO.add(WINE_LOG_1);
        wineLogDAO.add(WINE_LOG_2);
    }

    @AfterEach
    public void cleanUp() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @Test
    public void testGetAll() {
        wineLogDAO.add(WINE_LOG_4);
        wineLogDAO.add(WINE_LOG_5);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals(WINE_LOG_2.getUniqueWineId(), allLogs.getFirst().getUniqueWineId());
        assertEquals(WINE_LOG_2.getDate(), allLogs.getFirst().getDate());
        assertEquals(WINE_LOG_2.getTime(), allLogs.getFirst().getTime());
        assertEquals(WINE_LOG_5.getUniqueWineId(), allLogs.getLast().getUniqueWineId());
        assertEquals(WINE_LOG_5.getDate(), allLogs.getLast().getDate());
        assertEquals(WINE_LOG_5.getTime(), allLogs.getLast().getTime());
        Assertions.assertEquals(4, allLogs.size());
    }

    @Test
    public void testGetInRange() {
        wineLogDAO.add(WINE_LOG_3);
        wineLogDAO.add(WINE_LOG_4);
        List<WineLog> wineLogsInRange = wineLogDAO.getInRange(new Date(2021, 1, 1), new Date(2024, 3, 16));
        Assertions.assertEquals(2, wineLogsInRange.size());
    }

    @Test
    public void testAdd() {
        wineLogDAO.add(WINE_LOG_3);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals(3, allLogs.size());
    }

    @Test
    public void testAddDuplicateLog() {
        int added = wineLogDAO.add(WINE_LOG_1);
        assertEquals(1, added);
    }
    @Test
    public void testDeleteExistingLog() {
        int deleted = wineLogDAO.delete(WINE_LOG_1);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals(0, deleted);
        assertEquals(1, allLogs.size());
    }

    @Test
    public void testDeleteNotExistingLog() {
        int deleted = wineLogDAO.delete(WINE_LOG_3);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals(1, deleted);
    }

    @Test
    public void testUpdateWineId() {
        wineLogDAO.update(WINE_LOG_1, 2, null, null, null, null, false);
        wineLogDAO.delete(WINE_LOG_2);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals(2, allLogs.getFirst().getUniqueWineId());
    }

    @Test
    public void testUpdateNote() {
        wineLogDAO.update(WINE_LOG_1, null, "Hello World", null, null, null, false);
        wineLogDAO.delete(WINE_LOG_2);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals("Hello World", allLogs.getFirst().getNote());
    }

    @Test
    public void testUpdateDate() {
        wineLogDAO.update(WINE_LOG_1, null, null, new Date(2024, 5, 12), null, null, false);
        wineLogDAO.delete(WINE_LOG_2);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals(new Date(2024, 5, 12), allLogs.getFirst().getDate());
    }

    @Test
    public void testUpdateTime() {
        wineLogDAO.update(WINE_LOG_1, null, null, null, new Time(300), null, false);
        wineLogDAO.delete(WINE_LOG_2);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals(new Time(300), allLogs.getFirst().getTime());
    }

    @Test
    public void testUpdateStandards() {
        wineLogDAO.update(WINE_LOG_1, null, null, null, null, 4.5F, false);
        wineLogDAO.delete(WINE_LOG_2);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals(4.5F, allLogs.getFirst().getStandards(), 0.01);
    }

    @Test
    public void testGetAllAfterUpdate() {
        wineLogDAO.add(WINE_LOG_3);
        wineLogDAO.add(WINE_LOG_4);
        wineLogDAO.add(WINE_LOG_5);
        wineLogDAO.update(WINE_LOG_3, null, null, new Date(2003, 1, 1), null, null, false);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals(WINE_LOG_3.getUniqueWineId(), allLogs.getLast().getUniqueWineId());
        assertEquals(new Date(2003, 1, 1), allLogs.getLast().getDate());
        assertEquals(WINE_LOG_3.getTime(), allLogs.getLast().getTime());
    }

    @Test
    public void testUpdateMultipleFields() {
        wineLogDAO.update(WINE_LOG_1, 2, "Hello world", new Date(2024, 5, 12), new Time(1600), 7F, true);
        wineLogDAO.delete(WINE_LOG_2);
        List<WineLog> allLogs = wineLogDAO.getAll();
        assertEquals(2, allLogs.getFirst().getUniqueWineId());
        assertEquals("Hello world", allLogs.getFirst().getNote());
        assertEquals(new Date(2024, 5, 12), allLogs.getFirst().getDate());
        assertEquals(new Time(1600), allLogs.getFirst().getTime());
        assertEquals(7F, allLogs.getFirst().getStandards(), 0.01);
        assertEquals(true, allLogs.getFirst().getIsBottles());
    }

}
