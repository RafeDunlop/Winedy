package seng202.team3.services;

import seng202.team3.models.FavouritesWineList;
import seng202.team3.models.UserWineList;

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
        List<UserWineList> wineLists = wineListManager.getAllUserWineLists();
        //wineLists.removeIf(wineList -> wineList.getWineListName().equals(FavouritesWineList.getFavouritesName()));
        return wineLists;
    }
}
