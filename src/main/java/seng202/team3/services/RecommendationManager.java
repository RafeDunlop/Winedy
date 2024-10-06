package seng202.team3.services;


import com.password4j.Hash;
import seng202.team3.models.DrinkerPreferenceModel;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.RecommendationDAO;
import seng202.team3.repository.WineDAO;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.List;

/**
 * A singleton manager class to handle all logic for recommendation screen
 *
 * @author Steven Leishman sle159
 */
public class RecommendationManager {
    private static RecommendationManager instance;
    private final float SELECT_PREFERENCE_DEFAULT_VALUE = 7;
    private  RecommendationDAO recommendationDAO;
    private WineDAO wineDAO;
    private List<Integer> wineIndexes;
    private  WineDrinkerManager wineDrinkerManager;
    private  DrinkerPreferenceModel curDrinkerPrefModel;

    public static RecommendationManager getInstance(){
        if (instance == null) {
            instance = new RecommendationManager();
        }
        return instance;
    }

    /**
     * Initialising function, initiliases the recommendationDAO
     */
    private RecommendationManager (){
        recommendationDAO = new RecommendationDAO();
        wineDAO = new WineDAO();
    }

    /**
     * Calculate the score of a wine against users hidden preferences
     * @param wineToJudge the wine to calculate score with
     * @return integer score calculated from provided wine
     */
    public int calculateWineScore(Wine wineToJudge){return 0;}

    /**
     * Retrieves the users builtin preferences through the database
     */
    public  void getUserPreferenceModel(){
        String curUsername = wineDrinkerManager.getCurrentUser().getUsername();
        curDrinkerPrefModel = recommendationDAO.getPreferenceModelByUsername(curUsername);
    }

    public void updatePreferenceModelWithUserSelectedPreferences(){
        WineDrinker curUser = wineDrinkerManager.getCurrentUser();
        String colPref = curUser.getColourPreference();
        String grapePref = curUser.getGrapePreference();
        String fullnessPref = curUser.getFullnessPreference();
        recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), colPref, SELECT_PREFERENCE_DEFAULT_VALUE);
        recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), grapePref, SELECT_PREFERENCE_DEFAULT_VALUE);
        recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), fullnessPref, SELECT_PREFERENCE_DEFAULT_VALUE);

    }

    /**
     * Picks 5 wines for the recommendation using score threshold
     * @return HashMap<Wine, Integer> A hash map of the select wines and it's score
     */
    public HashMap<Wine, Integer> recommendWines(){
        if (wineIndexes == null){
            setupWineIndexList();
        }
        Random rand = new SecureRandom();
        HashMap<Wine, Integer> selectedWines = new HashMap<>();
        int THRESHOLD = 5;
        while (selectedWines.size() < 5) {
            int randomIndex = rand.nextInt(wineIndexes.size());
            Wine wineToCheck = wineDAO.getWineByID(wineIndexes.get(randomIndex));
            int wineScore = calculateWineScore(wineToCheck);
            if (wineScore > THRESHOLD) {
                selectedWines.put(wineToCheck, wineScore);
            }
        }

        return selectedWines;
    }

    /**
     * Gets all wines from Database and inputs their id's
     * into a list for random selection
     */
    public void setupWineIndexList(){
        List<Wine> wines = wineDAO.getAll();
        for (Wine wine : wines) {
            wineIndexes.add(wine.getUniqueWineID());
        }
    }

    /**
     * Adjusts the users built in preference by whether they liked the given wine
     * called when user liking or disliking recommended wine
     */
    public  void updateUserBuiltInPreferences(Wine pickedWine, Boolean likeStatus){}


    /**
     * Calls functions in recommendation DAO to check if user already has
     * a preference model. If not, call function to create a new one
     * called by wineDrinkerManager
     * @param currentUser the WineDrinker Object to retrieve preference of
     */
    public void InitialiseUserPreferenceModel(WineDrinker currentUser) {
        wineDrinkerManager = WineDrinkerManager.getInstance();
        curDrinkerPrefModel = recommendationDAO.getPreferenceModelByUsername(currentUser.getUsername());
        if (curDrinkerPrefModel == null){
            recommendationDAO.createNewPreferenceModel(currentUser.getUsername());
            updatePreferenceModelWithUserSelectedPreferences();
        }
        curDrinkerPrefModel = recommendationDAO.getPreferenceModelByUsername(currentUser.getUsername());
    }

    /**
     * returns the current instance of this object
     * @return DrinkerPreferenceModel object storing Strings and floats
     */
    public DrinkerPreferenceModel getCurDrinkerPrefModel () {
        return curDrinkerPrefModel;
    }
}
