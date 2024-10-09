package seng202.team3.models;

import seng202.team3.services.WineDrinkerManager;

import java.util.Objects;

/**
 * UserWineList class defines the personal wine lists that Wine Drinkers can create
 * This will be used in deliverable 3
 *
 * @author Krishna Sridhar (nsr36)
 */
public class UserWineList extends WineList {

    /**
     * The name of the user wine list
     */
    private String wineListName;

    /**
     * The description of the user wine list
     */
    private String description;

    /**
     * An integer representing the position that this user wine list was last changed relative to the other user wine
     * lists
     */
    private int lastChanged;

    /**
     * Constructs new WineList object and initializes the list.
     * sets
     *
     * @param wineListName name of the wine list
     * @param description description of the wine list
     * @param sortKey sortKey for the wine list
     */
    public UserWineList(String wineListName, String description, Integer sortKey) {
        this.description = description;
        this.wineListName = wineListName;
        lastChanged = Objects.requireNonNullElseGet(sortKey, () -> WineDrinkerManager.getInstance().getCurrentUser().getAndIncrementMinKey());
    }

    /**
     * adds the specified wine to this UserWineList and updates its sortkey (and the minsortkey)
     *
     * @param wine The Wine object to be added to the list. Must not be null.
     */
    @Override
    public void addWineToList(Wine wine) {
        super.addWineToList(wine);
        lastChanged = WineDrinkerManager.getInstance().getCurrentUser().getAndIncrementMinKey();
    }

    /**
     * Removes the given wine from the user wine list using the super method. Updates the value of lastChanged to make
     * this instance the most recently changed user wine list
     *
     * @param toRemove The Wine object to be removed from the list. Must not be null.
     * @return the truth value of the wine being removed from the list
     * @throws NullPointerException if the given wine is null, this is thrown up to the method that called it
     */
    @Override
    public boolean removeWineFromList(Wine toRemove) throws NullPointerException {
        boolean inList = super.removeWineFromList(toRemove);
        if (inList) {
            lastChanged = WineDrinkerManager.getInstance().getCurrentUser().getAndIncrementMinKey();
        }
        return inList;
    }

    /**
     * Returns the name of the user wine list.
     *
     * @return the name of the user wine list, wineListName
     */
    public String getWineListName() {
        return this.wineListName;
    }

    /**
     * Returns the description of the user wine list
     *
     * @return the description of the user wine list
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the user wine list to be the given String description
     *
     * @param description the description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the name of the user wine list to be the given String name
     *
     * @param wineListName the name to be set
     */
    public void setWineListName(String wineListName) {
        this.wineListName = wineListName;
    }

    /**
     * Returns the sort key of the user wine list. If this user wine list is the Wine Drinker's favourites list, the
     * smallest possible integer is returned, otherwise a negative integer with a magnitude of the value of lastChanged
     * is returned
     *
     * @return the sort key of this user wine list
     */
    public int getSortKey() {
        return (wineListName.equals(FavouritesWineList.getFavouritesName())) ? Integer.MIN_VALUE : -1 * lastChanged;
    }
}
