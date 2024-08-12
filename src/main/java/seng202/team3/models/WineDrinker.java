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
    ArrayList<WineList> drinkersWineLists = new ArrayList<>();
    String username;
    String wineColourPreference;
    String wineFullnessPreference;
    boolean isLoggedIn = false;
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
     * @param password password to authenticate user
     */
    public WineDrinker(String username, String password) {
        this.username = username;
        this.dbService = new DBService();
        List<String> retrievedWineDrinkerData = new ArrayList<>();
        try {
            retrievedWineDrinkerData = dbService.readDrinkerDetails(username, password);
        } catch (WineDrinkerDoesNotExistException e){
            e.printStackTrace();
            dbService.writeNewDrinkerRecord(username, password);
        }
        for (int i = 0; i < retrievedWineDrinkerData.size(); i++){
            //do stuff with the data
        }
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
