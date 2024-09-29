package seng202.team3.unittests.repository;

import org.junit.jupiter.api.*;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.UserWineListDAO;
import seng202.team3.repository.WineDAO;
import seng202.team3.services.WineDrinkerManager;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserWineListDAOTest {
    String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";

    private UserWineListDAO userWineListDAO;
    
    private final int HIGHEST_ID = 782;


    private final Wine testWine1 = new Wine(
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

    private final Wine testWine2 = new Wine(
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
        WineDAO wineDAO = new WineDAO(DATABASE_PATH);
        userWineListDAO = new UserWineListDAO(DATABASE_PATH);
        wineDAO.add(testWine1);
        wineDAO.add(testWine2);
        WineDrinkerManager.getInstance(DATABASE_PATH).setCurrentUser(new WineDrinker("test1", "test1", "New Zealand", "Rose", "DRY", "Chardonnay", 25));
        WineDrinkerManager.getInstance(DATABASE_PATH).registerWineDrinker();

    }

    @AfterEach
    public void cleanUp() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @Test
    public void testAlreadyAdded() {
        UserWineList testWineList = new UserWineList("Name", "Description", null);
        userWineListDAO.add(testWineList);
        assertEquals(1, userWineListDAO.add(testWineList));
    }

    @Test
    public void testAddNewList() {
        UserWineList testWineList = new UserWineList("Name", "Description", null);
        userWineListDAO.add(testWineList);
        List<UserWineList> retrievedWineList = userWineListDAO.getAll();
        assertEquals(testWineList.getDescription(), retrievedWineList.getFirst().getDescription());
    }

    @Test
    public void testGetAllNullUser() {
        UserWineList testWineList = new UserWineList("Name", "Description", null);
        userWineListDAO.add(testWineList);
        WineDrinkerManager.getInstance(DATABASE_PATH).setCurrentUser(null);
        assertEquals(0, userWineListDAO.getAll().size());
    }

    @Test
    public void testGetAllContainsWines() {
        UserWineList testWineList = new UserWineList("Name", "Description", null);
        testWineList.addWineToList(testWine1);
        testWineList.addWineToList(testWine2);
        userWineListDAO.add(testWineList);
        List<UserWineList> retrievedWineList = userWineListDAO.getAll();
        assertEquals(testWine1.getUniqueWineID(), retrievedWineList.getFirst().getWineList().getFirst().getUniqueWineID());
        assertEquals(testWine2.getUniqueWineID(), retrievedWineList.getFirst().getWineList().get(1).getUniqueWineID());
    }

    @Test
    public void testGetAllTwoLists() {
        UserWineList testWineList = new UserWineList("Name", "Description", null);
        UserWineList testWineList1 = new UserWineList("Name1", "Description1", null);
        userWineListDAO.add(testWineList);
        userWineListDAO.add(testWineList1);
        List<UserWineList> retrievedWineList = userWineListDAO.getAll();
        assertEquals(2, retrievedWineList.size());
    }

    @Test
    public void testUpdate() { //test for case where update fails is untestable (update never fails in an intended manner)
        UserWineList testWineList = new UserWineList("Name", "Description", null);
        userWineListDAO.add(testWineList);
        int currentNumber = userWineListDAO.getAll().getFirst().getWineList().size();
        testWineList.addWineToList(testWine1);
        userWineListDAO.update(testWineList);
        int newNumber = userWineListDAO.getAll().getFirst().getWineList().size();
        assertEquals(1, newNumber - currentNumber);
    }

    @Test
    public void testDelete() { //test for case where deletion fails is untestable (deletion never fails in an intended manner)
        UserWineList testWineList = new UserWineList("Name", "Description", null);
        userWineListDAO.add(testWineList);
        int currentNumber = userWineListDAO.getAll().size();
        userWineListDAO.delete(testWineList);
        int newNumber = userWineListDAO.getAll().size();
        assertEquals(-1, newNumber - currentNumber);
    }

    @Test
    public void testDeleteNotAdded() {
        UserWineList testWineList = new UserWineList("Name", "Description", null);
        //not added
        assertEquals(0, userWineListDAO.delete(testWineList));
    }

    @Test
    public void testRename() {
        UserWineList testWineList = new UserWineList("Name", "Description", null);
        userWineListDAO.add(testWineList);
        userWineListDAO.rename(testWineList, "newName");
        assertEquals("newName", userWineListDAO.getAll().getFirst().getWineListName());
    }


}
