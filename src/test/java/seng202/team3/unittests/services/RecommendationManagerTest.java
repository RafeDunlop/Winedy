package seng202.team3.unittests.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.models.DrinkerPreferenceModel;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.RecommendationDAO;
import seng202.team3.repository.WineDAO;
import seng202.team3.services.RecommendationManager;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RecommendationManagerTest {
    private static final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    private RecommendationManager recommendationManager = RecommendationManager.getInstance();
    private WineDrinker wineDrinker = new WineDrinker("username","password",
            null, "Red", "FULL", "Zinfandel", 12);

    private final Wine WINE_1 = new Wine(
            4,
            "Bread & Butter Chardonnay 2017, California",
            "USA",
            "White",
            "Rich",
            new String[]{"Chardonnay"},
            "OFF DRY",
            "Complex and layered, with distinctive notes of vanilla bean, almond husk and tropical fruit - it's reminiscent of a decadent crème brûlée! Californian Chardonnay at it's very best. This Chardonnay opens delicately with rich notes of vanilla bean and almond husk, which reminds us of a decadent crème brûlée. The creamy custard notes are balanced by a soft minerality and a hint of worn leather Any seafood dish that features butter or brown butter sauce... baked chicken, creamy pastas or soups, squash and winter vegetables... You're staying for dinner, right?",
            (float) 15.99,
            new String[]{"IWC 2019 - Commended Award, IWC 2018 - Bronze Award", "Decanter 2018 - Silver Award"},
            (float) 13.5,
            750,
            2017);
    private final Wine WINE_2 = new Wine(
            4,
            "Definition Zinfandel 2017, Lodi",
            "USA",
            "Red",
            "Big",
            new String[]{"Zinfandel"},
            "full",
            "The Definition range captures the quintessential qualities of the world's greatest wine styles. How do you capture the essence of Zinfandel? We went to the heart of Zin', Lodi, and a three-time 'US Wine Producer of the Year'. We sold 9,000 bottles in the first week. It's big, bold and bursting with ripe blackberries and sweet spices.",
            (float) 9.99,
            new String[]{"IWC 2019 - Commended Award, IWC 2018 - Bronze Award", "Decanter 2018 - Silver Award"},
            (float) 14,
            750,
            2017);
    private RecommendationDAO recommendationDAO;
    @BeforeAll
    public static void deleteTestDB() {
        File file = new File(DATABASE_PATH);
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        RecommendationManager.REMOVE_INSTANCE();
        recommendationManager = RecommendationManager.getInstance(DATABASE_PATH);
        recommendationDAO = new RecommendationDAO(DATABASE_PATH);
    }
    @AfterEach
    public void cleanup(){
        File file = new File(DATABASE_PATH.substring(12));
        file.delete();
    }

    @Test
    public void testGetUserPreferenceModel(){
        recommendationDAO.createNewPreferenceModel(wineDrinker.getUsername());
        DrinkerPreferenceModel prefModel = recommendationManager.getUserPreferenceModel(wineDrinker.getUsername());
        assertEquals(48, prefModel.getNumColumns());
    }

    @Test
    public void testInitialiseUserPreferenceNoPreexistingModel(){
        recommendationManager.initialiseUserPreferenceModel(wineDrinker);
        assertEquals(48, recommendationManager.getCurDrinkerPrefModel().getNumColumns());
    }

    @Test
    public void testInitialiseUserPreferenceWithPreexistingModel(){
        recommendationDAO.createNewPreferenceModel(wineDrinker.getUsername());
        recommendationManager.initialiseUserPreferenceModel(wineDrinker);
        assertEquals(48, recommendationManager.getCurDrinkerPrefModel().getNumColumns());
    }

    @Test
    public void testUpdateUserPreferenceWithUserSelectedPreferences(){
        recommendationDAO.createNewPreferenceModel(wineDrinker.getUsername());
        recommendationManager.updatePreferenceModelWithUserSelectedPreferences(wineDrinker);
        assertEquals(7, recommendationDAO.getPreferenceModelByUsername(wineDrinker.getUsername()).getPrefValByAttr("Red"));
    }
    @Test
    public void testUpdateUserPreferenceWithUserSelectedPreferencesNotSelected(){
        recommendationDAO.createNewPreferenceModel(wineDrinker.getUsername());
        wineDrinker.setColourPreference(null);
        recommendationManager.updatePreferenceModelWithUserSelectedPreferences(wineDrinker);
        assertNotEquals(7, recommendationDAO.getPreferenceModelByUsername(wineDrinker.getUsername()).getPrefValByAttr("Red"));
        wineDrinker.setColourPreference("Red");
    }

    @Test
    public void setupWineIndexList(){
        recommendationManager.setupWineIndexList();
        assertEquals(473, recommendationManager.getWineIndexes().size());

    }

    @Test
    public void testCalculateScoreUpperBound(){
        recommendationManager.initialiseUserPreferenceModel(wineDrinker);
        float score = recommendationManager.calculateWineScore(WINE_2);
        assertEquals(100, score, 0.01);
    }

    @Test
    public void testCalculateScoreMiddleBound(){
        recommendationManager.initialiseUserPreferenceModel(wineDrinker);
        float score = recommendationManager.calculateWineScore(WINE_1);
        assertEquals(76, score, 0.02);
    }
    @Test
    public void testSelectWinesWithIndexMatchPreference(){
        recommendationManager.setupWineIndexList();
        recommendationManager.initialiseUserPreferenceModel(wineDrinker);
        List<Wine> selectedWines = new ArrayList<>();
        List<Float> selectedWinePercents = new ArrayList<>();
        recommendationManager.selectWinesWithIndex(selectedWines, selectedWinePercents, 4);
        assert(selectedWines.size() == 1);
    }
    @Test
    public void testSelectWinesWithIndexMisMatchPreference(){
        recommendationManager.setupWineIndexList();
        recommendationManager.initialiseUserPreferenceModel(wineDrinker);
        List<Wine> selectedWines = new ArrayList<>();
        List<Float> selectedWinePercents = new ArrayList<>();
        recommendationDAO.updateIndividualPreferenceVal(wineDrinker.getUsername(), "Zinfandel", 0);
        recommendationDAO.updateIndividualPreferenceVal(wineDrinker.getUsername(), "FULL", 0);
        recommendationDAO.updateIndividualPreferenceVal(wineDrinker.getUsername(), "Red", 0);
        recommendationDAO.updateIndividualPreferenceVal(wineDrinker.getUsername(), "ABV", 0);
        recommendationManager.selectWinesWithIndex(selectedWines, selectedWinePercents, 121);
        assert(selectedWines.isEmpty());
    }

    @Test
    public void testUpdatePreferenceAfterUserSelectionDislike(){
        recommendationManager.initialiseUserPreferenceModel(wineDrinker);
        recommendationManager.updatePreferenceModelAfterUserSelection(WINE_1, false);
        float newValue = recommendationManager.getUserPreferenceModel(wineDrinker.getUsername()).getPrefValByAttr("White");
        assertEquals(4.8, newValue, 0.02);
    }

    @Test
    public void testUpdatePreferenceAfterUserSelectionLike(){
        recommendationManager.initialiseUserPreferenceModel(wineDrinker);
        recommendationManager.updatePreferenceModelAfterUserSelection(WINE_2, true);
        float newValue = recommendationManager.getCurDrinkerPrefModel().getPrefValByAttr("Red");
        assertEquals(7.2, newValue, 0.02);
    }
}
