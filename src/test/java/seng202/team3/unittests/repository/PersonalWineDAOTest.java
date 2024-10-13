package seng202.team3.unittests.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.PersonalWineDAO;
import seng202.team3.repository.WineDAO;
import seng202.team3.services.WineDrinkerManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for PersonalWineDAO
 * @author Krishna Sridhar (nsr36)
 */

public class PersonalWineDAOTest {
    final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    WineDAO wineDAO;
    PersonalWineDAO personalWineDAO;
    WineDrinkerManager wineDrinkerManager;
    private final int HIGHEST_ID = 782;
    private final Wine WINE_1 = new Wine(
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
    private final Wine WINE_2 = new Wine(
            HIGHEST_ID + 2,
            "The Ned Waihopai River Sauvignon Blanc 2018 Marlborough",
            "New Zealand",
            "White",
            "Fruity",
            new String[]{"Sauvignon Blanc"},
            "DRY",
            "It's our best-ever-selling white for good reason.",
            (float) 10.99,
            new String[]{"IWC 2019 - Commended Award, IWC 2018 - Bronze Award", "Decanter 2018 - Silver Award"},
            (float) 13,
            75,
            2018);
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
        wineDrinkerManager = WineDrinkerManager.getInstance(DATABASE_PATH);
        wineDAO = new WineDAO(DATABASE_PATH);
        personalWineDAO = new PersonalWineDAO(DATABASE_PATH);
        wineDrinkerManager.setCurrentUser(testWineDrinker);
        wineDrinkerManager.registerWineDrinker();
        wineDrinkerManager.loginCurrentUser(username, password);
    }

    @Test
    void testGetAllPersonalWinesWhenEmpty() {
        assertEquals(0, personalWineDAO.getAll().size());
        assertTrue(personalWineDAO.getAll().isEmpty());
    }

    @Test
    void testGetAllPersonalWines() throws WineDrinkerAlreadyExistsException {
        personalWineDAO.add(WINE_1);
        personalWineDAO.add(WINE_2);
        assertEquals(2, personalWineDAO.getAll().size());
        personalWineDAO.delete(WINE_1);
        personalWineDAO.delete(WINE_2);
    }

    @Test
    void testAddNewWineToPersonalWineTable() throws WineDrinkerAlreadyExistsException {
        personalWineDAO.add(WINE_1);
        assertEquals(1, personalWineDAO.getAll().size());
        personalWineDAO.delete(WINE_1);
    }

    @Test
    void testAddExistingWineToPersonalWineTable() throws WineDrinkerAlreadyExistsException {
        personalWineDAO.add(WINE_1);
        assertEquals(0, personalWineDAO.add(WINE_1));
        personalWineDAO.delete(WINE_1);
    }

    @Test
    void testDeleteExistingPersonalWine() throws WineDrinkerAlreadyExistsException {
        personalWineDAO.add(WINE_1);
        personalWineDAO.add(WINE_2);
        personalWineDAO.delete(WINE_2);
        assertEquals(1, personalWineDAO.getAll().size());
        personalWineDAO.delete(WINE_1);
    }

    @Test
    void testDeleteNonExistingPersonalWine() {
        assertEquals(0, personalWineDAO.delete(WINE_1));
    }

    @Test
    public void testUpdateExistingPersonalWine() throws WineDrinkerAlreadyExistsException {
        WINE_2.setUniqueWineID(HIGHEST_ID+4);
        personalWineDAO.add(WINE_2);
        WINE_2.setCountry("France");
        personalWineDAO.update(WINE_2);
        assertEquals("France", wineDAO.getWineByID(WINE_2.getUniqueWineID()).getCountry());
        personalWineDAO.delete(WINE_2);
    }

    @Test
    public void testUpdateNonExistingNonPersonalWine() {
        assertEquals(0, personalWineDAO.update(WINE_2));
    }
}
