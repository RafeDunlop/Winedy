package seng202.team3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.exceptions.UnauthorisedWineDrinkerException;
import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.models.IllegalWineDrinkerException;
import seng202.team3.models.WineDrinker;
import seng202.team3.services.WineDrinkerDAO;

/** Singleton manager for interactions involving wineDrinker
 * WineDrinkerManager class to handle all actions for wine drinkers. This acts as an MVC controller taking requests
 * from the view and completing these using relevant model/repository layer actions
 * Author/s Sophia Copley (sco207), Steven Leishman (sle159)
 */
public class WineDrinkerManager {
    private static final Logger log = LogManager.getLogger(WineDrinkerManager.class);
    private final WineDrinkerDAO wineDrinkerDAO;
    private WineDrinker currentUser = null;

    private static WineDrinkerManager instance;

    /**
     * Creates a new WineDrinkerManager object and creates a private WineDrinkerDAO object that it will later
     * use for all database interactions
     */
    private WineDrinkerManager() {
        wineDrinkerDAO = new WineDrinkerDAO();
    }

    /**
     * Get the singleton instance of WineDrinkerManager
     * @return instance -
     */
    public static WineDrinkerManager getInstance() {
        if (instance == null) {
            instance = new WineDrinkerManager();
        }
        return instance;
    }

    /**
     * Set the currentUser WineDrinker object
     * @param currentUser the WineDrinker to be stored
     */
    public void setCurrentUser (WineDrinker currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Returns currentUser WineDrinker object that holds the
     * current info of the user
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
     * @return WineDrinker object corresponding to the provided username and password is the authentication succeeds
     */
   public void loginCurrentUser(String username, String password) throws IllegalWineDrinkerException{

        WineDrinker wineDrinker = wineDrinkerDAO.getWineDrinkerFromUsername(username);
        if (wineDrinker != null) {
            if (password.equals(wineDrinker.getPassword())) {
                setCurrentUser(wineDrinker);
            } else {
                throw new IllegalWineDrinkerException("Password Incorrect");
            }
        } else {
            throw new IllegalWineDrinkerException("User does not exist.");
        }

   }


   public WineDrinker getWineDrinker(String username){
       WineDrinker drinker;
       drinker = wineDrinkerDAO.getWineDrinkerFromUsername(username);
       return drinker;
   }
}
