package seng202.team3.unittests.repository;

import org.junit.jupiter.api.*;
import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.WineDrinkerDAO;

import java.io.File;
/**
 * Unit tests for WineDrinkerDAO
 * @author Yuvraj Fagotra (yfa50)
 */
public class WineDrinkerDAOTest {
    String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    WineDrinkerDAO wineDrinkerDAO;
    private final String username = "TestUser1";
    private final String password = "TestUserPassword";

    private final WineDrinker testWineDrinker = new WineDrinker(username, password, null, null, null,null,0);

    @BeforeAll
    public static void deleteTestDB() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
        wineDrinkerDAO = new WineDrinkerDAO(DATABASE_PATH);

        try {
            wineDrinkerDAO.add(testWineDrinker);
        } catch (WineDrinkerAlreadyExistsException e) {
            Assertions.fail("Failed to register test user");
        }
    }

    @AfterEach
    public void cleanup() {
        File file = new File(DATABASE_PATH.substring(12));
        file.delete();
    }


    @Test
    public void testGetNonExistingUser() {
        Assertions.assertNull(wineDrinkerDAO.getWineDrinkerFromUsername("InvalidUser"));
    }

    @Test
    public void testRegisterExistingUser() {
        try {
            wineDrinkerDAO.add(testWineDrinker);
            Assertions.fail();
        } catch (WineDrinkerAlreadyExistsException e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    public void testUpdateUser() {
        String colour = "White";
        testWineDrinker.setColourPreference(colour);
        wineDrinkerDAO.update(testWineDrinker);
        Assertions.assertEquals(colour, wineDrinkerDAO.getWineDrinkerFromUsername(username).getColourPreference());
    }

}
