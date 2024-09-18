package seng202.team3.unittests.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;
import seng202.team3.repository.WineDAO;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the WineDAO service class
 * @author Krishna Sridhar
 */

public class WineDAOTest {
    private final int CSV_LENGTH = 473;
    private final int HIGHEST_ID = 782;
    private final WineDAO wineDAO = new WineDAO("jdbc:sqlite:./src/test/resources/test_database.db");

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

    @AfterAll
    public static void cleanUp() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }
    @Test
    void testAddUniqueWine() {
        int insertId = wineDAO.add(WINE_1);
        assertEquals(HIGHEST_ID + 1, insertId);
        wineDAO.delete(insertId);
    }

    @Test
    void testAddNonUniqueWine() {
        int insertId = wineDAO.add(WINE_1);
        assertDoesNotThrow(() -> wineDAO.add(WINE_1));
        wineDAO.delete(insertId);
    }

    @Test
    void testGetAll() {
        int id1 = wineDAO.add(WINE_1);
        int id2 = wineDAO.add(WINE_2);
        List<Wine> allWines = wineDAO.getAll();
        assertEquals(CSV_LENGTH + 2, allWines.size());
        wineDAO.delete(id1);
        wineDAO.delete(id2);
    }

    @Test
    void testGetExistingWineByID() {
        int insertId = wineDAO.add(WINE_1);
        Wine retrievedWine = wineDAO.getWineByID(insertId);
        assertEquals(WINE_1.getLongDescription(), retrievedWine.getLongDescription());
        wineDAO.delete(insertId);
    }

    @Test
    void testGetNonExistingWineByID() {
        Wine retrievedWine = wineDAO.getWineByID(HIGHEST_ID + 1);
        assertNull(retrievedWine);
    }

    @Test
    void testDeleteExistingWine() {
        int insertId = wineDAO.add(WINE_2);
        wineDAO.delete(insertId);
        assertNull(wineDAO.getWineByID(insertId));
    }

    @Test
    void testDeleteNonExistingWine() {
        assertDoesNotThrow(() -> wineDAO.delete(WINE_2.getUniqueWineID()));
    }

    @Test
    void testSearchWines() {
        int insertId = wineDAO.add(WINE_2);
        List<String> keywords = Arrays.asList("Waihopai");
        SearchWineList searchWineList = wineDAO.searchWines(keywords, 2018, 2018, 0.0f, 20.0f, "New Zealand", "White", "DRY", "Sauvignon Blanc");
        assertEquals("The Ned Waihopai River Sauvignon Blanc 2018 Marlborough", searchWineList.getWineList().getFirst().getName());
        wineDAO.delete(insertId);
    }

    @Test
    void testSearchWinesNoKeywordsAndFilters() {
        List<String> keywords = new ArrayList<>();
        SearchWineList searchWineList = wineDAO.searchWines(keywords, null, null, 0.0f, 220.0f, null, null, null, null);
        assertEquals(CSV_LENGTH, searchWineList.getWineList().size());
    }
}
