package seng202.team3.services;

import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;

import java.util.List;

public class WineListSelectService {

    private final WineListManager wineListManager;

    /**
     * Constructs a new service object.
     */
    public WineListSelectService() {
        wineListManager = WineListManager.getInstance();
    }

    /**
     * returns all the current user's wine lists except the favourites list
     * @return a list of the current WineDrinker's UserWineLists
     */
    public List<UserWineList> getWineLists() {
        return wineListManager.getAllUserWineLists();
    }

    public void addWineToList(Wine wine, UserWineList wineList) {

    }

}
