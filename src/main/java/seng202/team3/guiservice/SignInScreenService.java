package seng202.team3.guiservice;

import seng202.team3.gui.SignInScreenController;
import seng202.team3.models.IllegalWineDrinkerException;

/**
 * A service class for the sign in screen
 * used by SignInScreenController
 *  handles logic for sign in
 *  Author/s Steven Leishman (sle159)
 */
public class SignInScreenService {

    /**
     * does nothing as long as the inputted passwords are matching
     * @throws IllegalWineDrinkerException thrown if the passwords do not match
     */
    public void validatePasswordsMatch(String password1, String password2) throws IllegalWineDrinkerException {
        if (!(password1.equals(password2))) {
            throw new IllegalWineDrinkerException("Passwords do not match");
        }
    }
}
