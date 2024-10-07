package seng202.team3.services;


import seng202.team3.models.DrinkerPreferenceModel;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.RecommendationDAO;

import java.util.List;

/**
 * A service class to handle all logic for recommendation screen
 *
 * @author Steven Leishman sle159
 */
public class RecommendationManager {
    private static RecommendationManager instance;

    private RecommendationDAO recommendationDAO;
    private DrinkerPreferenceModel curDrinkerPrefModel;

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
    public static int calculateWineScore(Wine wineToJudge){return 0;}

    /**
     * Retrieves the users builtin preferences through the database
     */
    public static void getUserBuiltInPreferences(){}

    /**
     * Picks 5 wines for the recommendation using score threshold
     * @return List<Wine> A list of 5 wines to run
     */
    // could return a hash map of wine to the calculated score
    public static List<Wine> chooseWinesToRecommend(){return null;}

    /**
     * Adjusts the users built in preference by whether they liked the given wine
     * called when user liking or disliking recommended wine
     */
    public static void updateUserBuiltInPreferences(Wine pickedWine, Boolean likeStatus){}


    /**
     * Calls functions in recommendation DAO to check if user already has
     * a preference model. If not, call function to create a new one
     * called by wineDrinkerManager
     * @param currentUser the WineDrinker Object to retrieve preference of
     */
    public void checkUserPreferenceModelExists (WineDrinker currentUser) {
        curDrinkerPrefModel = recommendationDAO.getPreferenceModelByUsername(currentUser.getUsername());
        if (curDrinkerPrefModel == null){
            recommendationDAO.createNewPreferenceModel(currentUser.getUsername());
        }
    }

    public DrinkerPreferenceModel getCurDrinkerPrefModel () {
        return this.curDrinkerPrefModel;
    }
}
