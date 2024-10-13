package seng202.team3.models;

import java.sql.Date;
import java.sql.Time;

/**
 * interface which specifies a Java.sql using time-based sorting on implementers
 *
 * @author Rafe Dunlop (rdu46)
 */
public interface Timed {

    /**
     * gets the sql Date corresponding to this Timed object
     * @return Java.sql.Date corresponding to this Timed object (at the time of calling)
     */
    Date getDate();

    /**
     * gets the time (nominally time of day) of this Timed object
     * @return Java.sql.Time corresponding to this Timed object (at the time of calling)
     */
    Time getTime();
}
