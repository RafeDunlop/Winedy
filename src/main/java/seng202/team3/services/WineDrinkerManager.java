package seng202.team3.services;

import com.password4j.Password;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.exceptions.IllegalWineDrinkerException;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.WineDrinkerDAO;

/** Singleton manager for interactions involving wineDrinker
 * WineDrinkerManager class to handle all actions for wine drinkers.
 *
 * @author Sophia Copley (sco207), Steven Leishman (sle159)
 */
public class WineDrinkerManager {

    /**
     * Logger for robust error handling
     */
    private static final Logger log = LogManager.getLogger(WineDrinkerManager.class);

    /**
     * Wine Drinker DAO instance to handle database related actions with a Wine Drinker
     */
    private WineDrinkerDAO wineDrinkerDAO;

    /**
     * Currently logged in Wine Drinker
     */
    private WineDrinker currentUser = null;

    private static WineDrinkerManager instance;
    private RecommendationManager recommendationManager;

    /**
     * Creates a new WineDrinkerManager object and creates a private WineDrinkerDAO object that it will later
     * use for all database interactions
     */
    private WineDrinkerManager(String url) {
        wineDrinkerDAO = new WineDrinkerDAO(url);
        recommendationManager = RecommendationManager.getInstance();
    }

    /**
     * Get the singleton instance of WineDrinkerManager
     * @return instance of WineDrinkerManager
     */
    public static WineDrinkerManager getInstance() {
        if (instance == null) {
            instance = new WineDrinkerManager(null);
        }
        return instance;
    }

    /**
     * Get the singleton instance of WineDrinkerManager
     * @return instance of WineDrinkerManager
     */
    public static WineDrinkerManager getInstance(String url) {
        if (instance == null) {
            instance = new WineDrinkerManager(url);
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
     * Set the currentUser WineDrinker object
     *
     * @param currentUser the WineDrinker to be stored
     */
    public void setCurrentUser (WineDrinker currentUser) {
        this.currentUser = currentUser;
        if (currentUser != null) {
            currentUser.setupMinSortKey();
        }
    }

    /**
     * Returns currentUser WineDrinker object that holds the
     * current info of the user
     *
     * @return currentUser - WineDrinker object or null object
     */
    public WineDrinker getCurrentUser() {
        return currentUser;
    }

    /**
     * Create a new WineDrinker to be added to the database
     * run when user is registering for the first time
     * currentUser is a wineDrinker object validated in SignInScreenService
     */
    public void registerWineDrinker() {
        try {
            if (currentUser != null) {
                wineDrinkerDAO.add(currentUser);
                recommendationManager.InitialiseUserPreferenceModel(currentUser);
            }
        } catch (WineDrinkerAlreadyExistsException e) {
            log.error(e);
        }
    }

    /**
     * Authorises and fetches a wine drinker by checking that the username and password match the relevant WineDrinker
     * Uses the data to populate the currentUser object
     * @param username username entered by the WineDrinker
     * @param password password to check for WineDrinker
     */
   public void loginCurrentUser(String username, String password) throws IllegalWineDrinkerException{

        WineDrinker wineDrinker = wineDrinkerDAO.getWineDrinkerFromUsername(username);
        if (wineDrinker != null) {
            String[] saltPass = wineDrinker.getPassword().split(":");
            if (Password.check(password, saltPass[1]).addSalt(saltPass[0]).withBcrypt()) {
                setCurrentUser(wineDrinker);
                recommendationManager.InitialiseUserPreferenceModel(currentUser);
                WineListManager.getInstance().setupFavourites();
            } else {
                throw new IllegalWineDrinkerException("Password Incorrect");
            }
        } else {
            throw new IllegalWineDrinkerException("User does not exist.");
        }
   }

    /**
     * Updates the wine drinker
     *
     * @param wineDrinker Wine Drinker to update
     */
   public void updateWineDrinker(WineDrinker wineDrinker) {
       wineDrinkerDAO.update(wineDrinker);
   }

    /**
     * Gets Wine Drinker from database
     *
     * @param username Username of the Wine Drinker to get from the database
     * @return a Wine Drinker object associated with the username
     */
   public WineDrinker getWineDrinker(String username){
       WineDrinker drinker;
       drinker = wineDrinkerDAO.getWineDrinkerFromUsername(username);
       return drinker;
   }

    /**
     * Calls the delete method of the DAO
     * @param toDelete the wine drinker to delete
     */
   public void deleteWineDrinker(WineDrinker toDelete){
       wineDrinkerDAO.delete(toDelete);
   }

    /**
     * Sets wineDrinkerDao used for setting up test database
     * @param wineDrinkerDAO
     */
    public void setWineDrinkerDAO(WineDrinkerDAO wineDrinkerDAO) {
       this.wineDrinkerDAO = wineDrinkerDAO;
    }
}
