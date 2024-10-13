package seng202.team3.unittests.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.services.WineDrinkerManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class UserWineListTest {

    private final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";

    private UserWineList userWineList;

    private final Wine wine1 = new Wine(
            1,
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

    private final Wine wine2 = new Wine(
            0,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            0,
            null,
            0,
            0,
            0);

    @BeforeAll
    public static void deleteTestDB() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
        userWineList = new UserWineList("", "", 0);
        WineDrinkerManager.getInstance(DATABASE_PATH).setCurrentUser(new WineDrinker("test1",
                "test1", "New Zealand",
                "Red",
                "Dry",
                "Nero d'Avola",
                13));
    }

    @AfterEach
    public void cleanup() {
        File file = new File(DATABASE_PATH.substring(12));
        file.delete();
    }

    @Test
    public void addWineToListTest() {
        userWineList.addWineToList(wine1);
        assertEquals(wine1, userWineList.getWineList().getFirst());
    }

    @Test
    public void addTwoWinesToListTest() {
        userWineList.addWineToList(wine2);
        userWineList.addWineToList(wine1);
        assertEquals(wine1, userWineList.getWineList().get(1));
    }


    @Test
    public void removeWineFromListReturnsTrueTest() {
        userWineList.addWineToList(wine1);
        assertTrue(userWineList.removeWineFromList(wine1));
    }

    @Test
    public void removeWineFromListTest() {
        userWineList.addWineToList(wine1);
        userWineList.removeWineFromList(wine1);
        assertEquals(0, userWineList.getWineList().size());
    }

    @Test
    public void removeNonExistentWineFromListTest() {
        assertFalse(userWineList.removeWineFromList(wine1));
    }
}
