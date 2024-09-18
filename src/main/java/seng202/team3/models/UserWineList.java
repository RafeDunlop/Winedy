package seng202.team3.models;

/**
 * UserWineList class defines the personal wine lists that Wine Drinkers can create
 * This will be used in deliverable 3
 *
 * @author Krishna Sridhar (nsr36)
 */
public class UserWineList extends WineList {
    String wineListName;
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
}
