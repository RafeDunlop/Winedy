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
     * the score represents the percentage that the wine matches the preferences
     * @param wineToJudge the wine to calculate score with
     * @return float score calculated from provided wine
     */
    private float calculateWineScore(Wine wineToJudge){
        String colour = wineToJudge.getColour();
        String fullness = wineToJudge.getFullness();
        String grapes = wineToJudge.getGrapes()[0];
        System.out.println("Recommended wine has attributes grape: " + grapes + " full: " + fullness + " colour: " + colour);
        float calculatedScore = curDrinkerPrefModel.getPrefValByAttr(colour) +curDrinkerPrefModel.getPrefValByAttr(fullness)
        + curDrinkerPrefModel.getPrefValByAttr(grapes);
        System.out.println("Wine score = " + calculatedScore);
        return (calculatedScore / findMaxPreferences()) * 100;
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
        if (colPref != null){
            recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), colPref, SELECT_PREFERENCE_DEFAULT_VALUE);
        }
        if (grapePref != null){
            recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), grapePref, SELECT_PREFERENCE_DEFAULT_VALUE);
        }
        if (fullnessPref != null){
            recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), fullnessPref, SELECT_PREFERENCE_DEFAULT_VALUE);
        }


    }

    /**
     * Picks 5 wines for the recommendation using score threshold
     * @return HashMap<Wine, Float> A hash map of the select wines, and it's matching percentage to users preferences
     */
    public void recommendWines(List<Wine> selectedWines, List<Float> selectedWinePercents){
        setupWineIndexList();
        Random rand = new SecureRandom();
        float score_threshold = findMaxPreferences() / 5;
        while (selectedWines.size() < 5) {
            int randomIndex = rand.nextInt(wineIndexes.size());
            Wine wineToCheck = wineDAO.getWineByID(wineIndexes.get(randomIndex));
            float wineScore = calculateWineScore(wineToCheck);
            if (wineScore >= score_threshold) {
                selectedWines.add(wineToCheck);
                System.out.println(wineScore);
                selectedWinePercents.add((float) Math.round(wineScore * 100)/100);
            }
        }
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
    public void updatePreferenceModelAfterUserSelection(Wine pickedWine, Boolean likeStatus){
        String colPref = pickedWine.getColour();
        String fullnessPref = pickedWine.getFullness();
        String grapePref = pickedWine.getGrapes()[0];
        String username = curDrinkerPrefModel.getUsername();
        float valueChange = (float) 0.2;
        if (!likeStatus){
            valueChange*=-1;
        }
        //update database preference model
        recommendationDAO.updateIndividualPreferenceVal(username, colPref, curDrinkerPrefModel.getPrefValByAttr(colPref) + valueChange);
        recommendationDAO.updateIndividualPreferenceVal(username, grapePref, curDrinkerPrefModel.getPrefValByAttr(colPref) + valueChange);
        recommendationDAO.updateIndividualPreferenceVal(username, fullnessPref, curDrinkerPrefModel.getPrefValByAttr(colPref) + valueChange);
        //update the locally stored preference model
        curDrinkerPrefModel = recommendationDAO.getPreferenceModelByUsername(username);
    }


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

    /**
     * Finds the highest possible preferences for use in
     * threshold and percentage calculations
     * @return maxScoreVal the total of the 3 highest preferences
     */
    public float findMaxPreferences(){
        float maxScoreVal;
        curDrinkerPrefModel = recommendationDAO.getPreferenceModelByUsername(curDrinkerPrefModel.getUsername());
        HashMap<String, Float> prefMap = curDrinkerPrefModel.getPreferencesHashMap();
        List<Float> topValues = new ArrayList<>();
        topValues.add((float) 0);
        for (float score : prefMap.values()){
               if (score >= topValues.get(0)){
                   topValues.add(0,score);
               }
        }
        maxScoreVal = topValues.get(0) + topValues.get(1) + topValues.get(2);
        return maxScoreVal;
    }

}
