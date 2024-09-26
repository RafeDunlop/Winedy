package seng202.team3.services;

import seng202.team3.models.FavouritesWineList;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.UserWineList;
import seng202.team3.repository.UserWineListDAO;

import java.util.Comparator;
import java.util.List;

/**
 * singleton class for processing, creating and adjusting WineLists
 *
 * @author Rafe Dunlop (rdu46)
 */
public class WineListManager {

    /**
     * instance used by this class so that the correct database is used (test vs actual)
     */
    private final UserWineListDAO userWineListDAO;

    /**
     * Singleton instance of WineListManager
     */
    private static WineListManager instance;

    /**
     * favourites list associated with the logged-in user
     */
    private FavouritesWineList favourites;

    /**
     * the previous stored search results
     */
    private SearchWineList lastSearched;

    /**
     * private constructor with specified database path
     * @param url database path
     */
    private WineListManager(String url) {
        userWineListDAO = new UserWineListDAO(url);
    }

    /**
     * private constructor with default database path
     */
    private WineListManager() {
        userWineListDAO = new UserWineListDAO();
    }

    /**
     * gets the singleton instance of WineListManager
     * if none is set, it is set up with the default database path
     * @return unique WineListManager
     */
    public static WineListManager getInstance() {
        if (instance == null) {
            instance = new WineListManager();
        }
        return instance;
    }

    /**
     * gets the singleton instance of WineListManager
     * if none is set, it is set up with the specified database path
     *
     * @param url database path with which to set up the instance (if the instance has not been instantiated)
     * @return unique WineListManager
     */
    public static WineListManager getInstance(String url) {
        if (instance == null) {
            instance = new WineListManager(url);
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
     * sets up the currently logged-in user's favourites list
     * called whenever a new user is logged in
     * TODO: call this somewhere
     */
    public void setupFavourites() {
        List<UserWineList> all = getAllUserWineLists();
        if (!all.isEmpty()) {
            favourites = (FavouritesWineList) all.getFirst();
        } else {
            favourites = new FavouritesWineList();
            userWineListDAO.add(favourites);
        }
    }

    /**
     * gets every UserWineList, including favourites etc.
     * lists are preset with their contents
     * favourites list is always first
     *
     * @return sorted list of WineLists
     */
    public List<UserWineList> getAllUserWineLists() {
        return userWineListDAO.getAll()
                .stream()
                .sorted(Comparator.comparingInt(UserWineList::getSortKey))
                .toList();
    }

    /**
     * creates a new list and stores it in the database with the specified name and description and associated with the logged-in user
     * @param listName String to be the name of the new UserWineList
     * @param description String to be the description of the new UserWineList
     * @return the instantiated UserWineList
     */
    public UserWineList newList(String listName, String description) {
        UserWineList userWineList = new UserWineList(listName, description);
        userWineListDAO.add(userWineList);
        return userWineList;
    }

    /**
     * updates the specified UserWineList in the database
     * at the end of this call the database correctly reflects the passed UserWineList, so to add a Wine to a list in the database:
     * call userWineList.addWineToList(toAdd) and then,
     * call wineListManager.update(userWineListManager)
     * Therefore, after changes are made to a UserWineList, including adding or removing wiens from it,
     * this method should always be called
     *
     * @param userWineList the list to be updated
     */
    public void update(UserWineList userWineList) {
        userWineListDAO.update(userWineList);
    }

    /**
     * renames the list passed to the specified new name
     * the passed UserWineList must retain the old name
     * @param toRename UserWineList to be renamed (still has old name)
     * @param newName String to set the new name to
     */
    public void rename(UserWineList toRename, String newName) {
        userWineListDAO.rename(toRename, newName);
    }

    /**
     * sets the new stored search
     * @param lastSearch most recently acquired SearchWineList
     */
    public void setLastSearched(SearchWineList lastSearch) {
        lastSearched = lastSearch;
    }

    /**
     * gets the most recently searched WineList
     * @return SearchWineList corresponding to the last search results
     */
    public SearchWineList getLastSearched() {
        return lastSearched;
    }

    /**
     * gets the lowest sort key (other than that of the favourites list)
     * this corresponds to the most recently altered list (other than favourites)
     * a UserWineList is set to the lowest sort key when it is updated (as it is now the most recently altered list)
     *
     * @return int, the sort key corresponding to the most recently altered list belonging to the logged-in user other than their favourites list
     */
    public int getMinSortKey() {
        return userWineListDAO.getMinSortKey();
    }

    /**
     * gets the favourites list for the like button, i.e:
     * WineListManager.getInstance().getFavourites().addWineToList(toLike)
     * WineListManager.getInstance().update(WineListManager.getInstance().getFavourites())
     * @return the FavouritesWineList associated with the logged-in user
     */
    public FavouritesWineList getFavourites() {
        return favourites;
    }
}
