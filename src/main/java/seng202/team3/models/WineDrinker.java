package seng202.team3.models;

import seng202.team3.services.WineListManager;

import java.util.*;

/**
 * WineDrinker class holds important information about the user
 * and their wine preferences, holds ArrayList of WineLists
 * Supplies controllers with information to display
 * Calls DBService to retrieve record from database
 *
 * @author Steven Leishman (sle159)
 */
public class WineDrinker {

    /**
     * List of all the wine drinker's lists
     */
    private final ArrayList<UserWineList> drinkersWineLists = new ArrayList<>();

    /**
     * Wine Drinker's username
     */
    private String username;

    /**
     * Wine Drinker's password
     */
    private final String password;

    /**
     * Wine Drinker's country preference
     */
    private final String countryPreference;

    /**
     * Wine Drinker's colour preference
     */
    private String colourPreference;

    /**
     * Wine Drinker's fullness preference
     */
    private String fullnessPreference;

    /**
     * Wine Drinker's grape preference;
     */
    private String grapePreference;

    /**
     * Wine Drinker's alcohol by volume preference
     */
    private double abvLimit;

    /**
     *
     */
    private int totalListChanges;



    /**
     * Initialises WineDrinker object with some given parameters
     * @param username unique username to identify the WineDrinker
     * @param password password for the WineDrinker to get into the account
     * @param countryPreference a WineDrinker's preferred country of wine
     * @param colourPreference a Wine Drinker's preferred colour of wine
     * @param fullnessPreference a WineDrinker's preferred colour of wine
     * @param grapePreference a WineDrinker's preferred grape variety
     */
    public WineDrinker(String username, String password, String countryPreference, String colourPreference, String fullnessPreference, String grapePreference, double abvLimit) {
        this.username = username;
        this.password = password;
        this.countryPreference = countryPreference;
        this.colourPreference = colourPreference;
        this.fullnessPreference = fullnessPreference;
        this.grapePreference = grapePreference;
        this.abvLimit = abvLimit;
    }

    public void setupMinSortKey() {
        totalListChanges = WineListManager.getInstance().getMinSortKey();
    }

    public int getAndIncrementMinKey() {
        return totalListChanges++;

    }


    /**
     * Creates new userWineList and adds it to drinkersWineList
     * TODO Use in wine list functionality in deliverable 3
     */
    void createWineList(String name) {
        UserWineList userWineList = new UserWineList(null, name, null);
        drinkersWineLists.add(userWineList);
    }


    /**
     * Returns the username of current instance of WineDrinker
     * @return this.username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * get users password for adding into DB
     * @return password
     */
    public String getPassword(){return this.password;}

    /**
     * Gets a string containing the WineDrinker's country preference
     * @return preferred country of wine
     */
    public String getCountryPreference() {
        return countryPreference;
    }

    /**
     * Gets a string containing a WineDrinker's wine colour preference
     * @return preferred colour of wine out of red, white and rose
     */
    public String getColourPreference() {
        return colourPreference;
    }

    /**
     * Gets a string with a WineDrinker's wine fullness preference
     * @return preferred fullness of wine
     */
    public String getFullnessPreference() {
        return fullnessPreference;
    }

    /**
     * Gets a string with the WineDrinker's preferred grape variety
     * @return the preferred grape variety
     */
    public String getGrapePreference() {
        return grapePreference;
    }

    /**
     * Gets the Wine Drinker's preferred abv limit
     * @return abv limit preference
     */
    public double getAbvLimit() {
        return abvLimit;
    }


    /**
     * Sets the WineDrinker's unique username
     * @param username unique username for the WineDrinker
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Sets the WineDrinker's preferred wine colour
     * @param colourPreference the preferred wine colour out of red, white and rose
     */
    public void setColourPreference(String colourPreference) {
        this.colourPreference = colourPreference;
    }

    /**
     * Sets the Wine Drinker's preferred wine fullness
     * @param fullnessPreference the preferred fullness
     */
    public void setFullnessPreference(String fullnessPreference) {
        this.fullnessPreference = fullnessPreference;
    }

    /**
     * Sets the WineDrinker's preferred grape for their wine
     * @param grapePreference type of grape the WineDrinker prefers
     */
    public void setGrapePreference(String grapePreference) {
        this.grapePreference = grapePreference;
    }

    /**
     * Sets the Wine Drinker's preferred abv limit
     * @param abvLimit the new abv limit preference
     */
    public void setAbvLimit(double abvLimit) {
        this.abvLimit = abvLimit;
    }


}
