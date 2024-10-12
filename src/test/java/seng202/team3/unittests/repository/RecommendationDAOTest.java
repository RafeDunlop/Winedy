package seng202.team3.unittests.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.RecommendationDAO;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecommendationDAOTest {
    final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    private RecommendationDAO recommendationDAO;
    private String username = "username";
    private int expectedNumCols = 48;
    @BeforeAll
    public static void deleteTestDB() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
        recommendationDAO = new RecommendationDAO(DATABASE_PATH);
        recommendationDAO.createNewPreferenceModel(username);
    }

    @AfterEach
    public void cleanUp() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @Test
    public void testGetPreferenceModelByUsername(){
        assertEquals(expectedNumCols, recommendationDAO.getPreferenceModelByUsername(username).getNumColumns());
    }

    @Test
    public void testUpdateIndividualPreferenceValid(){
        recommendationDAO.updateIndividualPreferenceVal(username, "Red", (float) 8.5);
        assertEquals(8.5, recommendationDAO.getPreferenceModelByUsername(username).getPrefValByAttr("Red"));
    }

    @Test
    public void testUpdateIndividualPreferenceInvalidLow(){
        recommendationDAO.updateIndividualPreferenceVal(username, "Red", (float) -0.5);
        assertEquals(5.0, recommendationDAO.getPreferenceModelByUsername(username).getPrefValByAttr("Red"));
    }
    @Test
    public void testUpdateIndividualPreferenceInvalidHigh(){
        recommendationDAO.updateIndividualPreferenceVal(username, "Red", (float) 11);
        assertEquals(5.0, recommendationDAO.getPreferenceModelByUsername(username).getPrefValByAttr("Red"));
    }
}
