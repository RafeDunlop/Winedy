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
    private String wineListName;

    private String description;

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
     * @param wine The Wine object to be added to the list. Must not be null.
     */
    @Override
    public void addWineToList(Wine wine) {
        super.addWineToList(wine);
        lastChanged = WineDrinkerManager.getInstance().getCurrentUser().getAndIncrementMinKey();
    }

    @Override
    public boolean removeWineFromList(Wine toRemove) throws NullPointerException {
        boolean inList = super.removeWineFromList(toRemove);
        if (inList) {
            lastChanged = WineDrinkerManager.getInstance().getCurrentUser().getAndIncrementMinKey();
        }
        return inList;
    }

    public String getWineListName() {
        return this.wineListName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWineListName(String wineListName) {
        this.wineListName = wineListName;
    }

    public int getSortKey() {
        return (wineListName.equals(FavouritesWineList.getFavouritesName())) ? Integer.MIN_VALUE : -1 * lastChanged;
    }
}
