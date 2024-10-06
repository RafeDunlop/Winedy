package seng202.team3.unittests.repository;

import org.junit.jupiter.api.*;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.WineDAO;
import seng202.team3.services.WineDrinkerManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the WineDAO service class
 * @author Krishna Sridhar (nsr36)
 */

public class WineDAOTest {
    final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    private WineDAO wineDAO;
    private final int CSV_LENGTH = 473;
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

    @BeforeAll
    public static void deleteTestDB() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
        wineDAO = new WineDAO(DATABASE_PATH);
    }

    @AfterEach
    public void cleanUp() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }
    @Test
    public void testAddUniqueWine() {
        int insertId = wineDAO.add(WINE_1);
        assertEquals(HIGHEST_ID + 1, insertId);
    }

    @Test
    public void testAddNonUniqueWine() {
        assertDoesNotThrow(() -> wineDAO.add(WINE_1));
    }

    @Test
    public void testGetAll() {
        List<Wine> allWines = wineDAO.getAll();
        assertEquals(CSV_LENGTH, allWines.size());
    }

    @Test
    public void testGetExistingWineByID() {
        int insertId = wineDAO.add(WINE_1);
        Wine retrievedWine = wineDAO.getWineByID(insertId);
        assertEquals(WINE_1.getLongDescription(), retrievedWine.getLongDescription());
    }

    @Test
    public void testGetNonExistingWineByID() {
        Wine retrievedWine = wineDAO.getWineByID(HIGHEST_ID + 1);
        assertNull(retrievedWine);
    }

    @Test
    public void testDeleteExistingWine() {
        int insertId = wineDAO.add(WINE_2);
        wineDAO.delete(WINE_2);
        assertNull(wineDAO.getWineByID(insertId));
    }

    @Test
    public void testDeleteNonExistingWine() {
        assertDoesNotThrow(() -> wineDAO.delete(WINE_2));
    }

    @Test
    public void testAddNote() {
        WineDrinkerManager.getInstance().setCurrentUser(new WineDrinker("username", "password", null, null, null,null,0));
        wineDAO.add(WINE_1);
        assertEquals(0, wineDAO.addNote(WINE_1, "note"));
    }

    @Test
    public void testGetNote() {
        wineDAO.add(WINE_1);
        WineDrinkerManager.getInstance().setCurrentUser(new WineDrinker("username", "password", null, null, null,null,0));
        wineDAO.addNote(WINE_1, "note");
        assertEquals("note", wineDAO.getNote(WINE_1));
    }

    @Test
    public void testUpdateNote() {
        wineDAO.add(WINE_1);
        WineDrinkerManager.getInstance().setCurrentUser(new WineDrinker("username", "password", null, null, null,null,0));
        wineDAO.addNote(WINE_1, "note");
        wineDAO.updateNote(WINE_1, "note++");
        assertEquals("note++", wineDAO.getNote(WINE_1));
    }

    @Test
    void testSearchWinesWithKeywordsAndFilters() {
        List<String> keywords = List.of("Waihopai");
        SearchWineList searchWineList = wineDAO.searchWines(keywords, 2018, 2018, 0.0f, 20.0f, "New Zealand", "White", "DRY", "Sauvignon Blanc");
        assertEquals("The Ned Waihopai River Sauvignon Blanc 2018 Marlborough", searchWineList.getWineList().getFirst().getName());
    }

    @Test
    void testSearchWinesNoKeywordsAndFilters() {
        List<String> keywords = new ArrayList<>();
        SearchWineList searchWineList = wineDAO.searchWines(keywords, null, null, 0.0f, 220.0f, null, null, null, null);
        assertEquals(CSV_LENGTH, searchWineList.getWineList().size());
    }
}
