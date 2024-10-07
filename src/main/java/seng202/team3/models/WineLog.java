package seng202.team3.models;

import java.sql.Time;
import java.sql.Date;

/**
 * object to represent an instance of logging of a wine by a user
 *
 * @author Rafe Dunlop (rdu46)
 */
public class WineLog {

    /**
     * Date of consumption
     */
    private Date date;

    /**
     * time (of day) of consumption
     */
    private Time time;

    /**
     * float of NZ standards which the log corresponds to
     */
    private float standards;

    /**
     * the foreign key to the Wine which the log is about
     */
    private int loggedId;

    /**
     * the note associated with the log
     */
    private String note;

    /**
     * standard constructor for WineLog
     * all parameters are mandatory except for note, which may be null
     * @param loggedId the foreign key to the Wine which the log is about
     * @param note  the note associated with the log
     * @param date date of consumption
     * @param time time (of day) of consumption
     * @param standards float of NZ standards which the log corresponds to
     */
    public WineLog( int loggedId, String note, Date date, Time time, float standards ) {
        this.date = date;
        this.time = time;
        this.standards = standards;
        this.loggedId = loggedId;
        this.note = note;
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



}

