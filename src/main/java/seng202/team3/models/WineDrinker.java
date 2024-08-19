package seng202.team3.models;

import seng202.team3.services.*;
import java.util.*;

/**
 * WineDrinker class holds important information about the user
 * and their wine preferences, holds ArrayList of WineLists
 *
 * Supplies controllers with information to display
 * Calls DBService to retrieve record from database
 *
 * @author Steven Leishman
 */
public class WineDrinker {
    private ArrayList<WineList> drinkersWineLists = new ArrayList<>();
    private int databaseID;
    private String username;
    private String wineColourPreference;
    private String wineFullnessPreference;
    private String password;
    private boolean isLoggedIn = false;
    DBService dbService;


    /**
     * Initialise Blank WineDrinker object
     */
    public WineDrinker() {
        this.username = null;
        this.wineColourPreference = null;
        this.wineFullnessPreference = null;
        this.dbService = new DBService();

    }

    /**
     * Initialises WineDrinker object and DBService instance and stores
     * data retrieved from database under given username and password
     *
     * @param username username for database search record
     * @param datebaseID id in database
     */
    public WineDrinker(int datebaseID, String username, String password, String wColPreference, String wFullPreference) {
        this.databaseID = databaseID;
        this.username = username;
        this.password = password;
        this.wineColourPreference = wColPreference;
        this.wineFullnessPreference = wFullPreference;

        List<String> retrievedWineDrinkerData = new ArrayList<>();

    }

    /**
     * Reads preferences of user
     * TODO DISCUSS VALIDITY OF THIS, POSSIBLE REDUNDANCY
     */
    void readPreferences() {
        //make a request to db for preferences
    }


    /**
     * Creates new userWineList and adds it to drinkersWineList
     * TODO add input from on screen text box as name
     */
    void createWineList() {
        WineList wineList = new UserWineList();
        drinkersWineLists.add(wineList);
    }

    /**
     * Removes winelist from users stored data
     * TODO add service call to delete record from database
     * @param wineListToRemove
     */
    void removeWineList(WineList wineListToRemove) {
        for (WineList wineList : drinkersWineLists) {
            //need wineList.getName()
            //drinkersWineLists.remove(wineList)
        }
    }

    /**
     * Returns the username of current instance of WineDrinker
     * @return this.username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * returns the database id of current instance
     * @return this.databaseID
     */
    public int getDatabaseID(){return this.databaseID;}

    /**
     * get users password for adding into DB
     * @return password
     */
    public String getPassword(){return this.password;}

    /**
     * Returns the colour preference of current instance of WineDrinker
     * @return this.wineColourPreference
     */
    public String getWineColourPreference(){
        return this.wineColourPreference;
    }

    /**
     * Returns the wineFullness of current instance of WineDrinker
     * @return this.wineFullnessPreference
     */
    public String getWineFullnessPreference(){
        return this.wineFullnessPreference;
    }

    /**
     * sets username to inputted value
     * @param username username to set to
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
