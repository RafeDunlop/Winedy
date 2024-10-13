package seng202.team3.models;

import seng202.team3.services.WineManager;

import java.sql.Time;
import java.sql.Date;
import java.util.Objects;

/**
 * object to represent an instance of logging of a wine by a user
 *
 * @author Rafe Dunlop (rdu46)
 */
public class WineLog implements Timed {

    /**
     * Date of consumption
     */
    private final Date date;

    /**
     * time (of day) of consumption
     */
    private final Time time;

    /**
     * float of NZ standards which the log corresponds to
     */
    private final float standards;

    /**
     * the foreign key to the Wine which the log is about
     */
    private final int loggedId;

    /**
     * the note associated with the log
     */
    private final String note;

    /**
     * true if this log was made with bottles, false if it was made with glasses
     */
    private final boolean loggedByBottles;

    /**
     * standard constructor for WineLog
     * all parameters are mandatory except for note, which may be null
     * @param loggedId the foreign key to the Wine which the log is about
     * @param note  the note associated with the log
     * @param date date of consumption
     * @param time time (of day) of consumption
     * @param standards float of NZ standards which the log corresponds to
     * @param loggedByBottles boolean, whether the log was in bottles or not (in glasses)
     */
    public WineLog( int loggedId, String note, Date date, Time time, float standards, boolean loggedByBottles) {
        this.date = date;
        this.time = time;
        this.standards = standards;
        this.loggedId = loggedId;
        this.note = note;
        this.loggedByBottles = loggedByBottles;
    }

    /**
     * gets the foreign key to the Wine which the log is about
     * @return integer which references the logged wine
     */
    public int getUniqueWineId() {
        return loggedId;
    }

    /**
     * gets the note corresponding to the log, may be null (if there is no note)
     * @return the log's note (String)
     */
    public String getNote() {
        return note;
    }

    /**
     * gets the time (of day) of consumption
     * @return Java.sql.Time object corresponding to the time of consumption
     */
    public Time getTime() {
        return time;
    }

    /**
     * gets the date of consumption
     * @return Java.sql.Date object corresponding to the date of consumption
     */
    public Date getDate() {
        return date;
    }

    /**
     * gets the number of NZ standard drinks which the log corresponds to
     * @return float, number of standards drank
     */
    public float getStandards() {
        return standards;
    }

    /**
     * gets whether the log was created by logging bottles or not
     * this needs to be stored in case the user chooses to edit their log (because they made an error etc.)
     * and changes, this allows the application to reverse calculate amounts (bottles/glasses) from standard drinks
     * @return boolean, whether the log was made with bottles (or glasses)
     */
    public boolean getIsBottles() {
        return loggedByBottles;
    }

    /**
     * gets the readable string representation of this Wine Log
     * @return String, multiline representation which shows the name of the logged wine and below the number of standards
     * logged
     */
    public String toString() {
        return WineManager.getInstance().getWineById(loggedId).getName() +
                "\nStandard drinks: " +
                String.format("%.1f",standards);
    }

    /**
     *
     * @param other
     * @return
     */
    public boolean equals(WineLog other) {
        boolean same = other.getIsBottles() == this.loggedByBottles;
        same = other.getStandards() == this.standards && same;
        same = other.getDate().equals(this.date) && same;
        same = other.getTime().equals(this.time) && same;
        same = Objects.equals(other.getNote(), this.note) && same;
        same = other.getUniqueWineId() == loggedId && same;
        return same;
    }

}

