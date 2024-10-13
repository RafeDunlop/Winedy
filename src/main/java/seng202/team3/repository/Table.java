package seng202.team3.repository;

/**
 * Maps each attribute of a wine to the string representation of the name of the column in the database
 *
 * @author Yuvraj Fagotra (yfa50)
 */
public enum Table {

    /**
     * Represents the award table in the database which maps wines to the awards they have been awarded
     */
    AWARD("award"),

    /**
     * Represents the contains table in the database which maps wines to the wine lists that contain them
     */
    CONTAINS("Contains"),

    /**
     * Represents the grape table in the database which maps wines to the grapes that were made with
     */
    GRAPE("grape"),

    /**
     * Represents the logs table in the database which maps Wine Drinkers to the wine they have logged. Also contains
     * when they drank, how much they drank, and the comment they want to leave on the wine for that experience.
     */
    LOGS("logs"),

    /**
     * Represents the personalwine table in the database which maps the personal wine ids from the wine super table to
     * the wine drinker that created them
     */
    PERSONALWINE("personalWine"),

    /**
     * Represents the wine table in the database which contains all wine ids from the wine super table that are not
     * personal wines
     */
    WINE("wine"),

    /**
     * Represents the winesuper table in the database which contains all the wines in the database and their attributes
     */
    WINESUPER("wineSuper"),

    /**
     * Represents the winedrinker table from the database which contains all the registered Wine Drinkers in the
     * database as well as their preferences
     */
    WINEDRINKER("wineDrinker"),

    /**
     * Represents the winelist table in the database which stores the Wine Drinker who owns the wine list as well as the
     * name, description, and sort key of the wine list
     */
    WINELIST("wineList"),

    /**
     * Represents the writesnotesabout table in the database which stores the wine drinker and the wine they wrote about
     * aswell as the note they have written
     */
    NOTES("writesNoteAbout"),

    /**
     * Represents the master table in the database which is a table that stores the information about the tables created
     */
    MASTER("sqlite_master");

    /**
     * The string representation of name of the Table value in the database
     */
    public final String tableName;

    /**
     * Constructor for the Table values, sets the tableName to be the given string
     *
     * @param name the string representation of the name of the table
     */
    Table(String name) {
        this.tableName = name;
    }
}
