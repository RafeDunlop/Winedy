package seng202.team3.unittests.services;

import org.junit.jupiter.api.*;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.services.WineDrinkerManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the WineDrinkerManager service class
 * @author Krishna Sridhar (nsr36)
 */

public class WineDrinkerManagerTest {
    private static final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    private WineDrinkerManager wineDrinkerManager;
    private final String username = "TestUser1";
    private final String password = "password";

    private final WineDrinker testWineDrinker = new WineDrinker(username, password, null, null, null,null,0);

    @BeforeAll
    public static void deleteTestDB() {
        File file = new File(DATABASE_PATH);
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        WineDrinkerManager.REMOVE_INSTANCE();
        wineDrinkerManager = WineDrinkerManager.getInstance(DATABASE_PATH);
    }

    @AfterEach
    public void cleanup() {
        File file = new File(DATABASE_PATH.substring(12));
        file.delete();
    }

    @Test
    public void testRegisterCurrentUser() {
        wineDrinkerManager.setCurrentUser(testWineDrinker);
        wineDrinkerManager.registerWineDrinker();
        assertNotNull(wineDrinkerManager.getWineDrinker(username));
    }

    @Test
    public void testLoginCurrentUser() {
        wineDrinkerManager.setCurrentUser(testWineDrinker);
        wineDrinkerManager.registerWineDrinker();
        wineDrinkerManager.loginCurrentUser(username, password);
        WineDrinker wineDrinker = wineDrinkerManager.getWineDrinker(username);
        assertEquals(username, wineDrinker.getUsername());
    }
}
