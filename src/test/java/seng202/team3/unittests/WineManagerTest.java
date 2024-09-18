package seng202.team3.unittests;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.models.SearchWineList;
import seng202.team3.repository.WineDAO;
import seng202.team3.services.WineManager;
import seng202.team3.models.Wine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WineManagerTest {
    private WineManager wineManager = WineManager.getInstance();
    private final int HIGHEST_ID = 782;
    private static final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    private final int NUMBER_OF_WINES = 473;

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
    @BeforeAll
    public static void setup() {
        WineManager.getInstance().setWineDAO(new WineDAO(DATABASE_PATH));
    }

    @Test
    public void testAddWine() {
        int insertID = wineManager.addWine(WINE_1);
        assertEquals(HIGHEST_ID + 1, insertID);
        wineManager.deleteWine(WINE_1);
    }
    @Test
    public void testGetWineByID() {
        wineManager.addWine(WINE_1);
        Wine wine = wineManager.getWineById(WINE_1.getUniqueWineID());
        assertEquals(WINE_1.getName(), wine.getName());
        assertEquals(WINE_1.getLongDescription(), wine.getLongDescription());
        wineManager.deleteWine(WINE_1);
    }

    @Test
    public void testDeleteWine() {
        int insertID = wineManager.addWine(WINE_1);
        wineManager.deleteWine(WINE_1);
        assertNull(wineManager.getWineById(insertID));
    }
     @Test
    public void testGetAllWines() {
        List<Wine> allWines = wineManager.getAllWines();
        assertEquals(allWines.size(), NUMBER_OF_WINES);
    }
    @Test
    public void searchWines() {
        SearchWineList searchedWines = wineManager.searchWines("fruity", 2008, 2018, null, (float) 100, "New Zealand", "White", null, "Sauvignon Blanc");
        assertEquals(22, searchedWines.getWineList().size());

    }

}
