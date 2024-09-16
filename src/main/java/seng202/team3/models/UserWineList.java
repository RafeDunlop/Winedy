package seng202.team3.models;

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
