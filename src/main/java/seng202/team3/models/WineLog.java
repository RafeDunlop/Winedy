package seng202.team3.models;

import java.sql.Time;
import java.sql.Date;

public class WineLog {

    private Date date;

    private Time time;

    private float standards;

    private int loggedId;

    private String note;

    public WineLog( int loggedId, String note, Date date, Time time, float standards ) {
        this.date = date;
        this.time = time;
        this.standards = standards;
        this.loggedId = loggedId;
        this.note = note;
    }

    public int getUniqueWineId() {
        return loggedId;
    }

    public String getNote() {
        return note;
    }

    public Time getTime() {
        return time;
    }

    public Date getDate() {
        return date;
    }



}

