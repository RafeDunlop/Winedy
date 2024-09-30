package seng202.team3.repository;

/**
 * Maps each attribute of a wine to the string representation of the name of the column in the database
 *
 * @author Yuvraj Fagotra (yfa50)
 */
public enum Tables {
    AWARD("award"),
    CONTAINS("Contains"),
    GRAPE("grape"),
    LOGS("logs"),
    PERSONALWINE("personalWine"),
    WINE("wine"),
    WINESUPER("wineSuper"),
    WINEDRINKER("wineDrinker"),
    WINELIST("wineList"),
    NOTES("writesNoteAbout"),
    MASTER("sqlite_master");

    public final String name;

    Tables(String name) {
        this.name = name;
    }
}
