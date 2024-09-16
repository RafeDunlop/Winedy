package seng202.team3.unittests.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;
import seng202.team3.services.DatabaseManager;
import seng202.team3.services.WineDAO;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the WineDAO service class
 * @author Krishna Sridhar
 */

public class WineDAOTest {
    private final int CSV_LENGTH = 473;
    private final int HIGHEST_ID = 782;
    private WineDAO wineDAO = new WineDAO();
    private DatabaseManager databaseManager;
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

    @BeforeEach
    public void setup() {
        String databasePath = "./build/classes/java/database.db";
        File databaseFile = new File(databasePath);
        if (databaseFile.exists()) {
            if (databaseFile.delete()) {
                System.out.println("Existing database file deleted successfully.");
            } else {
                System.out.println("Failed to delete the existing database file.");
            }
        }
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.getInstance();
    }

    @AfterEach
    public void removeInstance() {
        DatabaseManager.REMOVE_INSTANCE();
    }

    @Test
    void testAdd() {
        int insertId = wineDAO.add(WINE_1);
        assertEquals(HIGHEST_ID + 1, insertId);
    }

    @Test
    void testGetAll() {
        wineDAO.add(WINE_1);
        wineDAO.add(WINE_2);
        List<Wine> allWines = wineDAO.getAll();
        assertEquals(CSV_LENGTH + 2, allWines.size());
    }

    @Test
    void testGetWineByID() {
        int insertId = wineDAO.add(WINE_1);
        Wine retrievedWine = wineDAO.getWineByID(insertId);
        assertEquals(WINE_1.getLongDescription(), retrievedWine.getLongDescription());
    }

    @Test
    void testDelete() {
        int insertId = wineDAO.add(WINE_2);
        wineDAO.delete(insertId);
        assertNull(wineDAO.getWineByID(insertId));
    }

    @Test
    void testSearchWines() {
        List<String> keywords = Arrays.asList("Waihopai");
        SearchWineList searchWineList = wineDAO.searchWines(keywords, 2018, 2018, 0.0f, 20.0f, "New Zealand", "White", "DRY", "Sauvignon Blanc");
        assertEquals("The Ned Waihopai River Sauvignon Blanc 2018 Marlborough", searchWineList.getWineList().getFirst().getName());
    }

}
