package seng202.team3.models;

import java.util.ArrayList;

public abstract class WineList {
    ArrayList<Wine> wineListArray = new ArrayList<>();

    public abstract void addWineToList(Wine wine);

    public abstract void removeWineFromList(Wine wine);

}
