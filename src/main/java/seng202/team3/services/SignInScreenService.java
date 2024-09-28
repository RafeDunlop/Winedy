package seng202.team3.services;

import seng202.team3.exceptions.IllegalWineDrinkerException;
import seng202.team3.models.WineDrinker;


/**
 * A service class for the sign in screen used by SignInScreenController handles logic for sign in
 *
 *  @author Steven Leishman (sle159)
 */
public class SignInScreenService {
    /**
     * WineDrinkerManager to handle wine drinker related tasks
     */
    private WineDrinkerManager wineDrinkerManager;
    /**
     * Regular expression string to define the valid characters for the username and password
     */
    private final String regex = "^[a-zA-Z0-9]{5,16}$";

    public SignInScreenService() {
        this.wineDrinkerManager = WineDrinkerManager.getInstance();
    }
    /**
     * does nothing as long as the inputted passwords are matching
     *
     * @throws IllegalWineDrinkerException thrown if the passwords do not match
     */
    public void validateRegisteringPasswords(String password1, String password2) throws IllegalWineDrinkerException {
        if (!matchRegex(password1)){
            throw new IllegalWineDrinkerException("Passwords must be between 5 and 16 characters and must be alpha-numeric");
        } else if (!(password1.equals(password2))) {
            throw new IllegalWineDrinkerException("Passwords do not match");
        }
    }

    /**
     * Matches inputted string to predefined regex
     *
     * @param inputString string to be matched
     * @return true if string matches regex, otherwise false
     */
    private boolean matchRegex(String inputString) {
        return inputString.matches(regex);
    }

    /**
     * Checks the inputted username against a regex and if it is not already in use
     *
     * @param username string username to compare
     * @throws IllegalWineDrinkerException thrown if username does not pass checks
     */
    public void validateRegisteringUsername(String username) throws IllegalWineDrinkerException {
        if (!matchRegex(username)) {
            throw new IllegalWineDrinkerException("Username must be between 5 and 16 characters and must be alpha-numeric");
        } else if (wineDrinkerManager.getWineDrinker(username) != null) {
            throw new IllegalWineDrinkerException("Username is already taken");
        }
    }

    /**
     * Validate username and password for login details
     *
     * @param username username to validate
     * @param password password to validate
     * @throws IllegalWineDrinkerException thrown if details don't match regex
     */
    public void validateLoginDetails(String username, String password) throws IllegalWineDrinkerException{
        if(!matchRegex(username)){
            throw new IllegalWineDrinkerException("Username must be between 5 and 16 characters and must be alpha-numeric");
        } else if(!matchRegex(password)) {
            throw new IllegalWineDrinkerException("Password must be between 5 and 16 characters and must be alpha-numeric");
        }
    }

    /**
     *
     * @param username
     * @param password
     * @param country
     * @param colour
     * @param fullness
     * @param variety
     * @param ABVLimit
     * @throws IllegalWineDrinkerException
     */
    public void registerUser(String username, String password, String country, String colour, String fullness, String variety, double ABVLimit) throws IllegalWineDrinkerException{
        try {
            WineDrinker curUser = new WineDrinker(username, password, country, colour, fullness, variety, ABVLimit);
            wineDrinkerManager.setCurrentUser(curUser);
            wineDrinkerManager.registerWineDrinker();
        } catch (IllegalWineDrinkerException e) {
            throw e;
        }
    }

    /**
     * Single function that calls all required functions to validate and register
     * a new user
     * @param username
     * @param password
     * @param secondPassword
     * @param country
     * @param colour
     * @param fullness
     * @param variety
     * @param ABVLimit
     * @throws IllegalWineDrinkerException
     */
    public void validateAndRegisterUser(String username, String password, String secondPassword, String country, String colour, String fullness, String variety, double ABVLimit) throws IllegalWineDrinkerException{
        try {
            validateRegisteringUsername(username);
            validateRegisteringPasswords(password, secondPassword);
            registerUser(username, password, null, colour, fullness, variety, ABVLimit);
        } catch(IllegalWineDrinkerException e) {
            throw e;
        }
    }

    /**
     * Single function to login and validate the user
     * calls helper functions in this class
     * @param username string username of logging in user
     * @param password hashed password of logging in user
     * @throws IllegalWineDrinkerException
     */
    public void validateAndLoginUser(String username, String password) throws IllegalWineDrinkerException{
        try {
            validateLoginDetails(username, password);
            wineDrinkerManager.loginCurrentUser(username, password);
        } catch (IllegalWineDrinkerException e) {
            throw e;
        }
    }

    /*
     * Sets the wineDrinkerManager
     * used for setting up test database
     * @param wineDrinkerManager
     */
    public void setWineDrinkerManager(WineDrinkerManager wineDrinkerManager) {
        this.wineDrinkerManager = wineDrinkerManager;
    }
}
