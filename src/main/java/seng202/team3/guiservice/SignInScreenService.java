package seng202.team3.guiservice;

import seng202.team3.WineDrinkerManager;
import seng202.team3.gui.SignInScreenController;
import seng202.team3.models.IllegalWineDrinkerException;


/**
 * A service class for the sign in screen
 * used by SignInScreenController
 *  handles logic for sign in
 *  Author/s Steven Leishman (sle159)
 */
public class SignInScreenService {
    private WineDrinkerManager wineDrinkerManager;

    public SignInScreenService() {
        this.wineDrinkerManager = WineDrinkerManager.getInstance();
    }
    /**
     * does nothing as long as the inputted passwords are matching
     * @throws IllegalWineDrinkerException thrown if the passwords do not match
     */
    public void validatePasswordsMatch(String password1, String password2) throws IllegalWineDrinkerException {
        if (!password1.matches("^[a-zA-Z0-9]{5,16}$")){
            throw new IllegalWineDrinkerException("Passwords must be between 5 and 16 characters");
        } else if (!(password1.equals(password2))) {
            throw new IllegalWineDrinkerException("Passwords do not match");
        }
    }


    /**
     * Checks the inputted username against a regex and if it is not already in use
     * @param username string username to compare
     * @throws IllegalWineDrinkerException thrown if username does not pass checks
     */
    public void validateRegisteringUsername(String username) throws IllegalWineDrinkerException {
        if (!username.matches("^[a-zA-Z0-9]{3,16}$")) {
            throw new IllegalWineDrinkerException("Username must be between 3 and 16 characters and cannot contain any spaces");
        } else if (wineDrinkerManager.getWineDrinker(username) != null) {
            throw new IllegalWineDrinkerException("Username is already taken");
        }
    }
}
