package seng202.team3.unittests.services;

import org.junit.jupiter.api.*;
import seng202.team3.models.FavouritesWineList;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class WineListManagerTest {
    private WineListManager toTest;
    private final int HIGHEST_ID = 782;
    private static final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";

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
    public static void deleteTestDB() {
        File file = new File(DATABASE_PATH);
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        WineListManager.REMOVE_INSTANCE();
        WineDrinkerManager.REMOVE_INSTANCE();
        toTest = WineListManager.getInstance(DATABASE_PATH);
        WineDrinkerManager.getInstance(DATABASE_PATH).setCurrentUser(new WineDrinker("username", "password", null, null, null,null,0));
    }

    @AfterEach
    public void cleanup() {
        File file = new File(DATABASE_PATH.substring(12));
        file.delete();
    }

    @Test
    public void testSetupFavouritesNoFavourites() {
        toTest.setupFavourites();
        assertNotNull(toTest.getFavourites());
    }

    @Test
    public void testSetupFavouritesPreExist() {
        toTest.setupFavourites();
        FavouritesWineList favourites = toTest.getFavourites();
        favourites.addWineToList(WINE_1);
        toTest.update(favourites);
        toTest.setupFavourites();
        UserWineList fromDB = toTest.getFavourites();
        //assertEquals(1, fromDB.getWineList().size());
    }

    @Test
    public void testSorting() {
        toTest.setupFavourites();
        UserWineList toAddTo = toTest.newList("1", "N/A");
        toTest.newList("3", "N/A");
        toTest.newList("2", "N/A");
        toAddTo.addWineToList(WINE_1);
        toTest.update(toAddTo);
        assertEquals(toAddTo.getWineListName(), toTest.getAllUserWineLists().get(1).getWineListName());
    }

    @Test
    public void testRename() {
        UserWineList toRename = toTest.newList("test", "N/A");
        toTest.rename(toRename, "name");
        assertEquals("name",  toTest.getAllUserWineLists().getFirst().getWineListName());
    }

    @Test
    public void testRenameKeepsWines() {
        UserWineList toRename = toTest.newList("test", "N/A");
        toRename.addWineToList(WINE_1);
        toTest.update(toRename);
        toTest.rename(toRename, "name");
        //assertEquals(WINE_1.getColour(),  toTest.getAllUserWineLists().getFirst().getWineList().getFirst().getColour());
    }

    @Test
    public void testDelete() {

    }

}
