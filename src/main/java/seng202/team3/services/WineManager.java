package seng202.team3.services;



import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;
import seng202.team3.repository.WineDAO;


import java.util.Arrays;
import java.util.List;



/**
 * Class to handle all actions for Wines
 *
 * @author Sophia Copley (sco207)
 */
public class WineManager {

    /**
     * Wine DAO instance to handle database related actions with a Wine
     */
    private WineDAO wineDAO;
    /**
     * Singleton instance of WineManager
     */
    private static WineManager instance;

    /**
     * Creates a new WineManager object and creates a private WineDAO object it will later use for all database
     * interactions
     */
    private WineManager(String url) {
        wineDAO = new WineDAO(url);
    }

    private WineManager() {
        wineDAO = new WineDAO();
    }

    /**
     * Get the singleton instance of WineManager
     * @return instance of WineManager
     */
    public static WineManager getInstance() {
        if (instance == null) {
            instance = new WineManager();
        }
        return instance;
    }

    /**
     * Get the singleton instance of WineManager
     *
     * @param url the relative or absolute filepath of the database to be set if it is not already set
     * @return instance of WineManager
     */
    public static WineManager getInstance(String url) {
        if (instance == null) {
            instance = new WineManager(url);
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
     * Adds a wine
     * TODO use in deliverable 3 for the tracking consumption feature
     * @param wine wine to add
     * @return -1 if sale added without error
     */
    public int addWine(Wine wine) {
        return wineDAO.add(wine);
    }


    /**
     * Deletes a Wine
     *
     * @param wine wine to delete
     */
    public void deleteWine(Wine wine) {
        wineDAO.delete(wine);
    }

    /**
     * Gets all wines from repository layer
     * @return List of all wines
     */
    public List<Wine> getAllWines() {
        return wineDAO.getAll();
    }

    /**
     * Gets sale from persistence by id
     *
     * @param id id of wine to fetch
     * @return wine specified by id or null if it doesn't exist
     */
    public Wine getWineById(int id) {
        return wineDAO.getWineByID(id);
    }

    /**
     * Gets wine search results based on keywords put into the search bar and filters chosen by the wine drinker
     *
     * @param searchBarInput the user input to the search bar.
     * @param minYear the earliest year a wine can be from, specified by the wine drinker
     * @param maxYear the latest year a wine can be from
     * @param minPrice the minimum price of a wine in the search
     * @param maxPrice the maximum price of a wine in the search
     * @param country the specified country the wine should be from
     * @param colour the specified colour of wine between red, white and rose
     * @param fullness the specified dryness of the wine
     * @param grapeName the colour of grape that the wine is made of
     * @return a SearchWineList object containing the search results of a wine search
     */
    public SearchWineList searchWines(String searchBarInput, Integer minYear, Integer maxYear, Float minPrice, Float maxPrice,
                                      String country, String colour, String fullness, String grapeName) {
        List<String> keywords = getWordsFromSearchBar(searchBarInput);
        return wineDAO.searchWines(keywords, minYear, maxYear, minPrice, maxPrice, country, colour, fullness, grapeName);
    }

    /**
     * Gets all the keywords from an input into the search bar.
     * This is based on the input into the search bar searchBarInput
     *
     * @param searchBarInput String of input from the search bar
     * @return List of words entered into the search bar
     */
    private List<String> getWordsFromSearchBar(String searchBarInput) { //perhaps keywordBank would be global
        if (searchBarInput.isEmpty()) {
            return null;
        }
        List<String> searchWordList = Arrays.asList(searchBarInput.split(" "));
        return searchWordList.stream()
                .map(String::toLowerCase)
                .distinct()
                .toList();
    }

}
