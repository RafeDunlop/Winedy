package seng202.team3.models;

/**
 * UserWineList class defines the personal wine lists that Wine Drinkers can create
 * This will be used in deliverable 3
 *
 * @author Krishna Sridhar (nsr36)
 */
public class UserWineList extends WineList {
    private String wineListName;

    private String description;

    /**
     * Constructs new WineList object and initializes the list.
     *
     * @param description
     */
    public UserWineList(String wineListName, String description) {
        this.description = description;
        this.wineListName = wineListName;
    }

    @Override
    public void addWineToList(Wine wine) {
        super.addWineToList(wine);
    }

    public void editWineListName(String newName) {
        // To implement
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
}
