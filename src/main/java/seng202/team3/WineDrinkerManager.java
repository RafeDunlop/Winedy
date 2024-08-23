package seng202.team3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.exceptions.UnauthorisedWineDrinkerException;
import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.exceptions.WineDrinkerDoesNotExistException;
import seng202.team3.models.WineDrinker;
import seng202.team3.services.WineDrinkerDAO;

/** WineDrinkerManager class to handle all actions for wine drinkers. This acts as an MVC controller taking requests
 * from the view and completing these using relevant model/repository layer actions
 */
public class WineDrinkerManager {
    private static final Logger log = LogManager.getLogger(WineDrinkerManager.class);
    private final WineDrinkerDAO wineDrinkerDAO;

    /**
     * Creates a new WineDrinkerManager object and creates a private WineDrinkerDAO object that it will later
     * use for all database interactions
     */
    public WineDrinkerManager() {
        wineDrinkerDAO = new WineDrinkerDAO();
    }

    /**
     * Create a new WineDrinker to be added to the database
     * @param username username to be created
     * @param password password to be created
     * @param reEnteredPassword password that has been reentered
     * @return the newly created WineDrinker object created from the username, password and preferences
     */
    public WineDrinker registerWineDrinker(String username, String password, String reEnteredPassword, String countryPreference, String colourPreference, String fullnessPreference, String grapePreference) {
        try {
            if (username.equals("") || password.equals("") || reEnteredPassword.equals("") || !password.equals(reEnteredPassword)) {
                return null;
            }
            WineDrinker wineDrinker = new WineDrinker(username, password, countryPreference, colourPreference, fullnessPreference, grapePreference);
            wineDrinkerDAO.add(wineDrinker);
            return wineDrinker;
        } catch (WineDrinkerAlreadyExistsException e) {
            log.error(e);
            return null;
        }
    }

    /**
     * Authorises and fetches a wine drinker by checking that the username and password match the relevant WineDrinker
     * @param username username entered by the WineDrinker
     * @param password password to check for WineDrinker
     * @return WineDrinker object corresponding to the provided username and password is the authentication succeeds
     */
   public WineDrinker validateWineDrinker(String username, String password) {
        try {
            WineDrinker wineDrinker = wineDrinkerDAO.getWineDrinkerFromUsername(username);
            if (password.equals(wineDrinker.getPassword())) {
                return wineDrinker;
            }
            throw new UnauthorisedWineDrinkerException("Password Incorrect");
        } catch (WineDrinkerDoesNotExistException e) {
            log.error(e);
            return null;
        } catch (UnauthorisedWineDrinkerException e) {
            log.error(e);
            return null;
        }
    }
}
