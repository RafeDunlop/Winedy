package seng202.team3.services;

import seng202.team3.exceptions.IllegalWineDrinkerException;
import seng202.team3.models.WineDrinker;


/**
 * A service class for the sign in screen used by SignInScreenController handles logic for sign in
 *
 *  @author Steven Leishman (sle159)
 */
public final class SignInScreenService {

    /**
     * WineDrinkerManager to handle wine drinker related tasks
     */
    private static WineDrinkerManager wineDrinkerManager = WineDrinkerManager.getInstance();

    /**
     * Regular expression string to define the valid characters for the username and password
     */
    private static final String regex = "^[a-zA-Z0-9]{5,16}$";

    /**
     * does nothing as long as the inputted passwords are matching
     *
     * @throws IllegalWineDrinkerException thrown if the passwords do not match
     */
    public static void validateRegisteringPasswords(String password1, String password2) throws IllegalWineDrinkerException {
        if (!matchRegex(password1)) {
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
    private static boolean matchRegex(String inputString) {
        return inputString.matches(regex);
    }

    /**
     * Checks the inputted username against a regex and if it is not already in use
     *
     * @param username string username to compare
     * @throws IllegalWineDrinkerException thrown if username does not pass checks
     */
    public static void validateRegisteringUsername(String username) throws IllegalWineDrinkerException {
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
    public static void validateLoginDetails(String username, String password) throws IllegalWineDrinkerException {
        if (!matchRegex(username)) {
            throw new IllegalWineDrinkerException("Username must be between 5 and 16 characters and must be alpha-numeric");
        } else if(!matchRegex(password)) {
            throw new IllegalWineDrinkerException("Password must be between 5 and 16 characters and must be alpha-numeric");
        }
    }

    /**
     * Creates a WineDrinker object using the given credential and preference inputs. Sets the current user to this
     * object in the WineDrinkerManager instance. Attempts to register the Wine Drinker in the database
     *
     * @param username the username of the wine drinker to be registered
     * @param password the password of the wine drinker to be registered
     * @param country the country preference of the wine drinker to be registered
     * @param colour the colour preference of the wine drinker to be registered
     * @param fullness the fullness preference of the wine drinker to be registered
     * @param variety the variety preference of the wine drinker to be registered
     * @param ABVLimit the ABV limit of the wine drinker to be registered
     * @throws IllegalWineDrinkerException if an issue occurs registering the wine drinker in the database, this is
     * thrown up to the method that calls it
     */
    public static void registerUser(String username, String password, String country, String colour, String fullness, String variety, double ABVLimit) throws IllegalWineDrinkerException {
        WineDrinker curUser = new WineDrinker(username, password, country, colour, fullness, variety, ABVLimit);
        wineDrinkerManager.setCurrentUser(curUser);
        //register throws exception which needs passing to 1 level up for prompt to user
        wineDrinkerManager.registerWineDrinker();
    }

    /**
     * Single function that calls all required functions to validate and register
     * a new user
     *
     * @param username string representing the username of the WineDrinker being registered
     * @param password string representing the password of the WineDrinker being registered
     * @param secondPassword string representing the re-entered password of the WineDrinker being registered
     * @param country string representing the country preference of the WineDrinker being registered
     * @param colour string representing the colour preference of the WineDrinker being registered
     * @param fullness string representing the fullness preference of the WineDrinker being registered
     * @param variety string representing the variety preference of the WineDrinker being registered
     * @param ABVLimit double representing the ABV limit of the WineDrinker being registered
     * @throws IllegalWineDrinkerException if an issue occurs registering the WineDrinker into the database, this is
     * thrown up to the method that calls it
     */
    public static void validateAndRegisterUser(String username, String password, String secondPassword, String country, String colour, String fullness, String variety, double ABVLimit) throws IllegalWineDrinkerException {
        validateRegisteringUsername(username);
        validateRegisteringPasswords(password, secondPassword);
        registerUser(username, password, null, colour, fullness, variety, ABVLimit);
    }

    /**
     * Single function to log in and validate the user
     * calls helper functions in this class
     *
     * @param username string username of logging in user
     * @param password hashed password of logging in user
     * @throws IllegalWineDrinkerException if there is an issue logging in the user, this is thrown up to the method
     * that calls it
     */
    public static void validateAndLoginUser(String username, String password) throws IllegalWineDrinkerException{
        validateLoginDetails(username, password);
        wineDrinkerManager.loginCurrentUser(username, password);
    }

    /**
     * Sets the wineDrinkerManager
     * used for setting up test database
     *
     * @param toAssign the WineDrinkerManager to be assigned
     */
    public static void setWineDrinkerManager(WineDrinkerManager toAssign) {
        wineDrinkerManager = toAssign;
    }
}
