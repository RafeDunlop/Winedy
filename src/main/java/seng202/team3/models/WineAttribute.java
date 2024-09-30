package seng202.team3.models;

/**
 * Maps each attribute of a wine to the string representation of the name of the column in the database
 *
 * @author Yuvraj Fagotra (yfa50)
 */
public enum WineAttribute {
    ID("id"),
    NAME("name"),
    COUNTRY("country"),
    COLOUR("colour"),
    STYLE("style"),
    FULLNESS("fullness"),
    DESCRIPTION("longDescription"),
    PRICE("pricePerBottle"),
    ABV("alcoholByVolume"),
    VOLUME("volumeInML"),
    YEAR("year"),

    //This refers to the column name in the grape table
    VARIETY("name");

    public final String attributeName;
    WineAttribute(String attribute) {
        this.attributeName = attribute;
    }
}
