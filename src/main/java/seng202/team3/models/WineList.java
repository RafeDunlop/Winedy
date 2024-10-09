package seng202.team3.models;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a list of wines (stored as Wine objects). The list is stored as an Arraylist which is kept private.
 *
 * @author Yuvraj Singh Fagotra (yfa50)
 */
public abstract class WineList {

    /**
     * The list of wines stored by the wine list model
     */
    private List<Wine> wineList;

    /**
     * Constructs new WineList object and initializes the list.
     */
    public WineList() {
        wineList = new ArrayList<>();
    }

    /**
     * Used to access the list of wines.
     *
     * @return An immutable copy of the wine list to make sure that the list is only modified using the public methods
     * provided by this class.
     */
    public List<Wine> getWineList() {
        return wineList;
    }

    /**
     * Sets the stored list of wines to be the given list
     *
     * @param toSet the list of wines to be set
     */
    public void setWineList(List<Wine> toSet) {
        wineList = toSet;
    }

    /**
     * Adds the given wine to the wine list.
     *
     * @param wine The Wine object to be added to the list. Must not be null.
     * @throws NullPointerException If given Wine is null.
     */
    public void addWineToList(Wine wine) throws NullPointerException {
        if (wine != null) {
            wineList.add(wine);
        } else {
            throw new NullPointerException("Wine must not be null");
        }
    }

    /**
     * Removes the given wine from the wine list.
     *
     * @param wine The Wine object to be removed from the list. Must not be null.
     * @return boolean, true if wine was in the list (and was removed)
     * @throws NullPointerException If given Wine is null.
     */
    public boolean removeWineFromList(Wine wine) throws NullPointerException {
        boolean inList;
        if (wine != null) {
            inList = wineList.remove(wine);
        } else {
            throw new NullPointerException("Wine must not be null");
        }
        return inList;
    }

    /**
     * Returns the name of the wine list. Implemented by child classes of WineList
     *
     * @return the name of the wine list
     */
    public abstract String getWineListName();
}
