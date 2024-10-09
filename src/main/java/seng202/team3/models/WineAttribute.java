package seng202.team3.models;

/**
 * Maps each attribute of a wine to the string representation of the name of the column in the database
 *
 * @author Yuvraj Fagotra (yfa50)
 */
public enum WineAttribute {

    /**
     * The unique id of a wine
     */
    ID("id"),

    /**
     * The name of a wine
     */
    NAME("name"),

    /**
     * The country that a wine is produced in
     */
    COUNTRY("country"),

    /**
     * The colour of a wine
     */
    COLOUR("colour"),

    /**
     * The style of a wine
     */
    STYLE("style"),

    /**
     * This attribute represents the category containing either the fullness or sweetness of a wine
     */
    FULLNESS("fullness"),

    /**
     * The description of a wine, provided by the data source
     */
    DESCRIPTION("longDescription"),

    /**
     * The price per bottle of a wine
     */
    PRICE("pricePerBottle"),

    /**
     * The alcohol by volume percentage of a wine
     */
    ABV("alcoholByVolume"),

    /**
     * The volume of a bottle of the wine, in millilitres
     */
    VOLUME("volumeInML"),

    /**
     * The year that the wine was produced in
     */
    YEAR("year"),

    //This refers to the column name in the grape table
    /**
     * The name of a grape used to produce the wine
     */
    VARIETY("name");

    /**
     * The string representation of the wine attribute
     */
    public final String attributeName;

    /**
     * Constructor for the wine attribute enum. Sets the attribute name to be the given string
     *
     * @param attribute the textual representation of the attribute, given as a string
     */
    WineAttribute(String attribute) {
        this.attributeName = attribute;
    }
}
