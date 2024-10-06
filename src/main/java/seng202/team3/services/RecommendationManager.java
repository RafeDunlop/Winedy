package seng202.team3.services;


import seng202.team3.models.DrinkerPreferenceModel;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.RecommendationDAO;
import seng202.team3.repository.WineDAO;

import java.security.SecureRandom;
import java.util.*;

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
    public float calculateWineScore(Wine wineToJudge){
        String colour = wineToJudge.getColour();
        String fullness = wineToJudge.getFullness();
        String grapes = wineToJudge.getGrapes()[0];
        System.out.println("Recommended wine has attributes style: " + grapes + " full: " + fullness + " colour: " + colour
        + "wine description is = " + wineToJudge.getLongDescription());
        float calculatedScore = curDrinkerPrefModel.getPrefValByAttr(colour) +curDrinkerPrefModel.getPrefValByAttr(fullness)
        + curDrinkerPrefModel.getPrefValByAttr(grapes);
        System.out.println("Wine score = " + calculatedScore);
        return calculatedScore;
    }

    /**
     * Retrieves the users builtin preferences through the database
     */
    public  DrinkerPreferenceModel getUserPreferenceModel(){
        String curUsername = wineDrinkerManager.getCurrentUser().getUsername();
        curDrinkerPrefModel = recommendationDAO.getPreferenceModelByUsername(curUsername);
        return curDrinkerPrefModel;
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
     * @return HashMap<Wine, Float> A hash map of the select wines and it's score
     */
    public HashMap<Wine, Float> recommendWines(){
        System.out.println("Got to recommend");
        if (wineIndexes == null){
            setupWineIndexList();
        }

        Random rand = new SecureRandom();
        HashMap<Wine, Float> selectedWines = new HashMap<>();
        int THRESHOLD = 0; //TODO HAVE A GOOD THRESHOLD
        while (selectedWines.size() < 5) {
            int randomIndex = rand.nextInt(wineIndexes.size());
            System.out.println(randomIndex);
            Wine wineToCheck = wineDAO.getWineByID(wineIndexes.get(randomIndex));
            float wineScore = calculateWineScore(wineToCheck); //TODO IMPLEMENT SCORE
            if (wineScore >= THRESHOLD) {
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
        wineIndexes = new ArrayList<>();
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
        curDrinkerPrefModel = getUserPreferenceModel();
        if (curDrinkerPrefModel == null){
            recommendationDAO.createNewPreferenceModel(currentUser.getUsername());
            updatePreferenceModelWithUserSelectedPreferences();
            curDrinkerPrefModel = getUserPreferenceModel();
        }

    }

    /**
     * returns the current instance of this object
     * @return DrinkerPreferenceModel object storing Strings and floats
     */
    public DrinkerPreferenceModel getCurDrinkerPrefModel () {
        return curDrinkerPrefModel;
    }
}
