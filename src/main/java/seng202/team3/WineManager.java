package seng202.team3;


//import seng202.team3.io.Importable;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;
import seng202.team3.services.WineDAO;

//import java.io.File;
import java.util.Arrays;
import java.util.List;



/**
 * Class to handle all actions for sales. This acts as a MVC controller taking requests from the view and completing
 * these using relevant model/repository layer actions
 * @author Morgan English
 */
public class WineManager {
    private final WineDAO wineDAO;

    private static WineManager instance;

    /**
     * Creates a new SalesManager object and creates a private SaleDAO object it will later use for all database
     * interactions
     */
    private WineManager() {
        wineDAO = new WineDAO();
    }

    public static WineManager getInstance() {
        if (instance == null) {
            instance = new WineManager();
        }
        return instance;
    }

//    /**
//     * Saves a file of sales to the repository layer using the specified importer functionality
//     * TODO: handle errors gracefully
//     * @param importer importer object to use
//     * @param file file to be imported
//     */
//    public void addAllWinesFromFile(Importable<Wine> importer, File file) {
//        List<Wine> wines = importer.readFromFile(file);
//        int i = 0;
//        while (i < wines.size()) {
//            if (i + 100 > wines.size()) {
//                wineDAO.addBatch(wines.subList(i, wines.size()));
//            } else {
//                wineDAO.addBatch(wines.subList(i, i + 100));
//            }
//            i += 100;
//        }
//    }

    /**
     * Adds a wine
     *
     * @param wine wine to add
     * @return -1 if sale added without error
     */
    public int addWine(Wine wine) {
        return wineDAO.add(wine);
    }


    /**
     * Deletes a Wine
     * todo work out how to get id for deletion, add id to sale model? add fetch id to saledao?
     *
     * @param wine wine to delete
     * @return true iff deleted, else false (what if it never existed?)
     */
    public boolean deleteWine(Wine wine) {
        wineDAO.delete(wine.getUniqueWineID());
        return false;
    }

    /**
     * Gets all wines from repository layer
     *
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
     * @param searchBarInput the user input to the search bar.
     * @param minYear the earliest year a wine can be from, specified by the wine drinker
     * @param maxYear the latest year a wine can be from
     * @param minPrice the minimum price of a wine in the search
     * @param maxPrice the maximum price of a wine in the search
     * @param country the specified country the wine should be from
     * @param type the specified type of wine between red, white and rose
     * @param shortDescription the specified dryness of the wine
     * @param grapeName the type of grape that the wine is made of
     * @return a SearchWineList object containing the search results of a wine search
     */
    public SearchWineList searchWines(String searchBarInput, Integer minYear, Integer maxYear, Float minPrice, Float maxPrice,
                                      String country, String type, String shortDescription, String grapeName) {
        List<String> keywords = getWordsFromSearchBar(searchBarInput);
        return wineDAO.searchWines(keywords, minYear, maxYear, minPrice, maxPrice, country, type, shortDescription, grapeName);
    }

    /*
     Gets all the keywords from an input into the search bar.
     This is based on the input into the search bar searchBarInput
     */
    private List<String> getWordsFromSearchBar(String searchBarInput) { //perhaps keywordBank would be global
        List<String> searchWordList = Arrays.asList(searchBarInput.split(" "));
        return searchWordList.stream()
                .map(String::toLowerCase)
                .distinct()
                .toList();
    }
}
