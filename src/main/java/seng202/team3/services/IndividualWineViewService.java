package seng202.team3.services;

import seng202.team3.models.FavouritesWineList;
import seng202.team3.models.Wine;

/**
 * Handles logic for Individual wine view
 *
 * @author Yuvraj Fagotra (yfa50)
 */
public class IndividualWineViewService {

    private final WineListManager wineListManager;

    /**
     * Constructs a new object. Sets up the favourites list if it is not already set up.
     */
    public IndividualWineViewService() {
        wineListManager = WineListManager.getInstance();

        if (wineListManager.getFavourites() == null) {
            wineListManager.setupFavourites();
        }
    }

    /**
     * Checks membership status of a wine in the favourites list
     * @param wine The wine to be checked
     * @return true if the wine is contained in the favourites list, false otherwise
     */
    public boolean inFavourites(Wine wine) {
        FavouritesWineList favouritesWineList = wineListManager.getFavourites();
        return favouritesWineList.getWineList().contains(wine);
    }

    /**
     * Adds given wine to the favourites list
     * @param wine to be added to favourites
     */
    public void updateFavourites(Wine wine) {

        FavouritesWineList favouritesWineList = wineListManager.getFavourites();

        if (favouritesWineList.getWineList().contains(wine)) {
            favouritesWineList.removeWineFromList(wine);
        } else {
            favouritesWineList.addWineToList(wine);
        }

        wineListManager.update(favouritesWineList);
    }
}
