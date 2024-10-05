package seng202.team3.services;


import seng202.team3.models.DrinkerPreferenceModel;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.RecommendationDAO;

import java.util.List;

/**
 * A singleton manager class to handle all logic for recommendation screen
 *
 * @author Steven Leishman sle159
 */
public class RecommendationManager {
    private static RecommendationManager instance;
    private float  SELECT_PREFERENCE_DEFAULT = 7;

    private  RecommendationDAO recommendationDAO;
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
        recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), colPref, SELECT_PREFERENCE_DEFAULT);
        recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), grapePref, SELECT_PREFERENCE_DEFAULT);
        recommendationDAO.updateIndividualPreferenceVal(curUser.getUsername(), fullnessPref, SELECT_PREFERENCE_DEFAULT);

    }

    /**
     * Picks 5 wines for the recommendation using score threshold
     * @return List<Wine> A list of 5 wines to run
     */
    // could return a hash map of wine to the calculated score
    public  List<Wine> chooseWinesToRecommend(){return null;}

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
        }
        updatePreferenceModelWithUserSelectedPreferences();
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
