package seng202.team3.services;


import seng202.team3.models.Wine;

import java.util.List;

/**
 * A service class to handle all logic for recommendation screen
 *
 * @author Steven Leishman sle159
 */
public class RecommendationManager {

    /**
     * Calculate the score of a wine against users hidden preferences
     * @param wineToJudge the wine to calculate score with
     * @return integer score calculated from provided wine
     */
    public int calculateWineScore(Wine wineToJudge){return 0;}

    /**
     * Retrieves the users builtin preferences through the database
     */
    public void getUserBuiltInPreferences(){}

    /**
     * Picks 5 wines for the recommendation using score threshold
     * @return List<Wine> A list of 5 wines to run
     */
    // could return a hash map of wine to the calculated score
    public List<Wine> chooseWinesToRecommend(){return null;}

    /**
     * Adjusts the users built in preference by whether they liked the given wine
     * called when user liking or disliking recommended wine
     */
    public void updateUserBuiltInPreferences(Wine pickedWine, Boolean likeStatus){}


}
