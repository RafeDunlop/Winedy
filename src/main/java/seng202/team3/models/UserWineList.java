package seng202.team3.models;

import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;

import java.util.List;

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
     * @param description
     */
    public UserWineList(String wineListName, String description) {
        this.description = description;
        this.wineListName = wineListName;
        lastChanged = WineDrinkerManager.getInstance().getCurrentUser().getAndIncrementMinKey();
    }

    /**
     *
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
        return -1 * lastChanged;
    }
}
