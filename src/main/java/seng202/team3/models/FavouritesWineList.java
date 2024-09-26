package seng202.team3.models;

public class FavouritesWineList extends UserWineList {


    private static final String FAVOURITES_NAME = "Favourites";

    private static final String FAVOURITES_DESCRIPTION = "Here you'll find all the wines you've hearted";

    /**
     * Constructs new WineList object and initializes the list.
     * sets
     *
     */
    public FavouritesWineList() {
        super(FAVOURITES_NAME, FAVOURITES_DESCRIPTION);
    }

    /**
     * key used when sorting UserWineLists
     * ensures favourites is always the first list
     *
     * @return smallest value an integer can take on, equivalent to negative infinity
     */
    @Override
    public int getSortKey() {
        return Integer.MIN_VALUE;
    }

    /**
     * gets the name of the favourites list name for all users
     * needed to avoid a feedback loop on database startup
     *
     * @return the name field of FavouritesWineList objects
     */
    public static String getFavouritesName() {
        return FAVOURITES_NAME;
    }
}
