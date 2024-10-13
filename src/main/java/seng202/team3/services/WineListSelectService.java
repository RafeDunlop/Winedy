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
        List<UserWineList> wineList = wineListManager.getAllUserWineLists();
        return wineListManager.getAllUserWineLists();
    }

    /**
     * Adds given wine to given wine list
     * @param wine the wine to be added to the list
     * @param wineList the list to which the wine needs to be added
     */
    public void updateWineList(Wine wine, UserWineList wineList) {

        if (wineList.getWineList().contains(wine)) {
            wineList.removeWineFromList(wine);
        } else {
            wineList.addWineToList(wine);
        }

        wineListManager.update(wineList);
    }

}
