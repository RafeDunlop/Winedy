package seng202.team3.models;

import seng202.team3.services.*;
import java.util.ArrayList;

public class WineDrinker {
    ArrayList<WineList> drinkersWineLists = new ArrayList<>();
    String username;
    String wineColourPreference;
    String wineFullnessPreference;
    boolean isLoggedIn = false;
    DBService dbService;

    public WineDrinker() {
        this.username = null;
        this.wineColourPreference = null;
        this.wineFullnessPreference = null;
        this.dbService = new DBService();

    }

    public WineDrinker(String username, String password) {
        this.username = username;
        this.dbService = new DBService();
        try {
            dbService.readDrinkerDetails(username, password);
        } catch (WineDrinkerDoesNotExistException e){
            e.printStackTrace();
            dbService.writeNewDrinkerRecord(username, password);
        }
    }

    void readPreferences() {

    }

    void createWineList() {

    }

    void removeWineList(WineList list) {
        for (WineList wineList : drinkersWineLists) {

        }
    }

    public String getUsername(){
        return this.username;
    }
    public String getWineColourPreference(){
        return this.wineColourPreference;
    }
    public String getWineFullnessPreference(){
        return this.wineFullnessPreference;
    }
}
