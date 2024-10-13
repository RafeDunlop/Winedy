package seng202.team3.models;

/**
 * This class represents a Wine Drinker's favourites list. This contains a list of wines which the Wine Drinker has
 * hearted/favourited. This class always has the same name and description i.e. they can not be changed by the
 * Wine Drinker
 *
 * @author Rafe Dunlop (rdu46)
 */
public class FavouritesWineList extends UserWineList {

    /**
     * The name of the favourites list. This is always "Favourites" so it is a final attribute
     */
    private static final String FAVOURITES_NAME = "Favourites";

    /**
     * The description of the favourites list. This is always the same so it is final
     */
    private static final String FAVOURITES_DESCRIPTION = "Here you'll find all the wines you've hearted!";

    /**
     * Constructs a new WineList object and initializes the list to have the favourites list name and description.
     * Sets the sort key to be the minimum value of an integer so that it is always sorted to the top of the list of
     * wine lists.
     */
    public FavouritesWineList() {
        super(FAVOURITES_NAME, FAVOURITES_DESCRIPTION, Integer.MIN_VALUE);
    }

    /**
     * Converts a UserWineList to a FavouritesWineList. Initialises a new FavoriteWinesList with the default constructor.
     * Sets the list of wines to be list of wines from the UserWineList
     *
     * @param toConvert the UserWineList to be converted
     * @return a new FavouritesWineList object with the same list of wines
     */
    public static FavouritesWineList toFavourites (UserWineList toConvert) {
        FavouritesWineList converted = new FavouritesWineList();
        converted.setWineList(toConvert.getWineList());
        return converted;
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
