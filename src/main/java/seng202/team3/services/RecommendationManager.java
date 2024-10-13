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
    private  DrinkerPreferenceModel curDrinkerPrefModel;
    private WineDrinker curUser;

    /**
     * Get the singleton instance of RecommendationManager
     *
     * @return instance of RecommendationManager
     */
    public static RecommendationManager getInstance() {
        if (instance == null) {
            instance = new RecommendationManager();
        }
        return instance;
    }

    /**
     * Get the singleton instance of RecommendationManager
     * used for Junit testing
     *
     * @param url the relative url that the test database is located at
     * @return instance of RecommendationManager
     */
    public static RecommendationManager getInstance(String url) {
        if (instance == null) {
            instance = new RecommendationManager(url);
        }
        return instance;
    }

    /**
     *  WARNING Sets the current singleton instance to null
     */
    public static void REMOVE_INSTANCE() {
        instance = null;
    }

    /**
     * Default constructor for the RecommendationManager. sets the DAO's to use the standard database.
     */
    private RecommendationManager() {
        recommendationDAO = new RecommendationDAO();
        wineDAO = new WineDAO();
    }

    /**
     * Constructor for the RecommendationManager. sets the DAO's to use the database at the given url.
     * Used for testing using the test database rather than the standard database
     *
     * @param url the url that th test database is located at
     */
    private RecommendationManager(String url) {
        recommendationDAO = new RecommendationDAO(url);
        wineDAO = new WineDAO(url);
    }

    /**
     * Calculate the score of a wine against users hidden preferences
     * the score represents the percentage that the wine matches the preferences
     *
     * @param wineToJudge the wine to calculate score with
     * @return float score calculated from provided wine
     */
    public float calculateWineScore(Wine wineToJudge){
        String colour;
        String fullness;
        String grapes;
        try {
            colour = wineToJudge.getColour();
            fullness = wineToJudge.getFullness();
            grapes = wineToJudge.getGrapes()[0];
        } catch (RuntimeException e){
            // if wine contains attributes not stored
            return 0;
        }

        float calculatedScore = curDrinkerPrefModel.getPrefValByAttr(colour) +curDrinkerPrefModel.getPrefValByAttr(fullness)
        + curDrinkerPrefModel.getPrefValByAttr(grapes);
        double wineABV = wineToJudge.getAlcoholByVolume();
        double userABV = curUser.getAbvLimit();

        if (wineABV >= (userABV - 2) && wineABV <= (userABV + 2)) {
            calculatedScore += curDrinkerPrefModel.getABV();
        }
        calculatedScore = (calculatedScore / findMaxPreferences()) * 100;
        if (calculatedScore > 100){
            calculatedScore = 100;
        }
        return calculatedScore;
    }

    /**
     * Retrieves Wine Drinkers preferences as a DrinkerPreferenceModel object from the database
     *
     * @param username the username of the Wine Drinker whose preference model to retrieve
     * @return the users builtin preferences through the database
     */
    public DrinkerPreferenceModel getUserPreferenceModel(String username) {
        curDrinkerPrefModel = recommendationDAO.getPreferenceModelByUsername(username);
        return curDrinkerPrefModel;
    }

    /**
     * Updates the preference model of the current Wine Drinker preferences (colour, grape, and fullness) to have the
     * default preference value
     *
     * @param curUser the currently logged in WineDrinker
     */
    public void updatePreferenceModelWithUserSelectedPreferences(WineDrinker curUser) {

        String colPref = curUser.getColourPreference();
        String grapePref = curUser.getGrapePreference();
        String fullnessPref = curUser.getFullnessPreference();
        if (colPref != null) {
            recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), colPref, SELECT_PREFERENCE_DEFAULT_VALUE);
        }
        if (grapePref != null) {
            recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), grapePref, SELECT_PREFERENCE_DEFAULT_VALUE);
        }
        if (fullnessPref != null) {
            recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), fullnessPref, SELECT_PREFERENCE_DEFAULT_VALUE);
        }
    }

    /**
     * Picks 5 wines for the recommendation using score threshold
     *
     * @param selectedWines list of selected wines
     * @param selectedWinePercents the matching percentages of all selected wines based on the users preferences
     */
    public void recommendWines(List<Wine> selectedWines, List<Float> selectedWinePercents) {
        Random rand = new SecureRandom();
        setupWineIndexList();
        while (selectedWines.size() < 4) {
            int randomIndex = rand.nextInt(wineIndexes.size());
            selectWinesWithIndex(selectedWines, selectedWinePercents, randomIndex);
        }

    }

    /**
     * Retrieves the wine from database with given index and adds it to the selectedWines list if it meets the score
     * threshold
     *
     * @param selectedWines List of wines to input passing wines into
     * @param selectedWinePercents list of scores corresponding to selectedWines
     * @param indexToSearch the index of the wine to search
     */
    public void selectWinesWithIndex(List<Wine> selectedWines, List<Float> selectedWinePercents, int indexToSearch) {
        float score_threshold = findMaxPreferences() / 5;
        Wine wineToCheck = wineDAO.getWineByID(wineIndexes.get(indexToSearch));
        float wineScore = calculateWineScore(wineToCheck);
        if (wineScore >= score_threshold) {
            selectedWines.add(wineToCheck);
            selectedWinePercents.add((float) Math.round(wineScore * 100)/100);
        }
    }

    /**
     * Gets all wines from Database and inputs their id's
     * into a list for random selection
     */
    public void setupWineIndexList() {
        List<Wine> wines = wineDAO.getAll();
        wineIndexes = new ArrayList<>();
        for (Wine wine : wines) {
            wineIndexes.add(wine.getUniqueWineID());
        }
    }

    /**
     * Adjusts the users built in preference by whether they liked the given wine
     * called when user liking or disliking recommended wine
     *
     * @param pickedWine the wine that was picked whose preference value is to be updated
     * @param likeStatus the truth value of whether the Wine Drinker liked the wine or not
     */
    public void updatePreferenceModelAfterUserSelection(Wine pickedWine, Boolean likeStatus) {
        String colPref = pickedWine.getColour();
        String fullnessPref = pickedWine.getFullness();
        String grapePref = pickedWine.getGrapes()[0];
        String username = curDrinkerPrefModel.getUsername();
        float valueChange = (float) 0.2;
        if (!likeStatus) {
            valueChange*=-1;
        }
        double wineABV = pickedWine.getAlcoholByVolume();
        double userABV = curUser.getAbvLimit();
        //ABV treated differently as stored as one value
        //If user likes a wine close to their preference, the abv score increases
        //Score only decreases if they like a wine out of range
        if ((wineABV >= (userABV - 2) && wineABV <= (userABV + 2))) {
            recommendationDAO.updateIndividualPreferenceVal(username, "abv", curDrinkerPrefModel.getABV()+valueChange);
        } else if(valueChange > 0) {
            recommendationDAO.updateIndividualPreferenceVal(username, "abv", curDrinkerPrefModel.getABV()-valueChange);
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
     *
     * @param currentUser the WineDrinker Object to retrieve preference of
     */
    public void initialiseUserPreferenceModel(WineDrinker currentUser) {
        curUser = currentUser;
        String username = currentUser.getUsername();
        curDrinkerPrefModel = getUserPreferenceModel(username);
        if (curDrinkerPrefModel == null) {
            recommendationDAO.createNewPreferenceModel(username);
            updatePreferenceModelWithUserSelectedPreferences(currentUser);
            curDrinkerPrefModel = getUserPreferenceModel(username);
        }
    }

    /**
     * returns the current instance of this object
     *
     * @return DrinkerPreferenceModel object storing Strings and floats
     */
    public DrinkerPreferenceModel getCurDrinkerPrefModel () {
        return curDrinkerPrefModel;
    }

    /**
     * Finds the highest possible preferences for use in
     * threshold and percentage calculations
     *
     * @return maxScoreVal the total of the 3 highest preferences and the abv preference
     */
    public float findMaxPreferences() {
        float maxScoreVal;
        curDrinkerPrefModel = recommendationDAO.getPreferenceModelByUsername(curDrinkerPrefModel.getUsername());
        HashMap<String, Float> prefMap = curDrinkerPrefModel.getPreferencesHashMap();
        List<Float> topValues = new ArrayList<>();
        topValues.add((float) 0);
        for (String key : prefMap.keySet()) {
            //Abv has lower priority on preference and is treated differently
            if (key != "abv") {
                float score = prefMap.get(key);
                if (score >= topValues.get(0)) {
                    topValues.add(0,score);
                }
            }

        }
        maxScoreVal = topValues.get(0) + topValues.get(1) + topValues.get(2) + curDrinkerPrefModel.getABV();
        return maxScoreVal;
    }

    /**
     * Returns list of database wine unique ids
     *
     * @return this.wineIndexes
     */
    public List<Integer> getWineIndexes(){
        return this.wineIndexes;
    }
}
