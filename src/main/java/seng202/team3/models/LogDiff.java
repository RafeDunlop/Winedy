package seng202.team3.models;

import seng202.team3.services.LogManager;

import java.sql.Date;

/**
 * class which acts as a psuedo-builder for a WineLog, is mutable and stores data in a more raw, less processed format
 * wraps controller args, data validation, equality and printing (for controller). Not a builder because it is missing a build method
 * (which is in logManager). However, this is essentially the builder pattern
 *
 * @author Rafe Dunlop (rdu46)
 */
public class LogDiff {

    /**
     * the current specified date of logging
     */
    private Date date;

    /**
     * the hour of day of log, converted to a Java.sql.Time object on WineLog creation
     */
    private int hour;

    /**
     * the Wine selected for the log. reduced to an iD on WineLog creation
     */
    private Wine wine;

    /**
     * the raw amount entered, either bottles or glasses. converted to standards upon WineLog creation
     */
    private float amt;

    /**
     * whether amt refers to bottles or glasses
     */
    private boolean isBottles;

    /**
     * the note to go with the log
     */
    private String note;

    /**
     * empty constructor, defaults note to the empty string (reversed to null before database insertion if still empty)
     * avoids null pointer exceptions upon comparison
     */
    public LogDiff() {
        note = "";
    }

    /**
     * constructor which copies an existing LogDiff
     * @param other LogDiff to be copied
     */
    public LogDiff(LogDiff other) {
        date = other.getDate();
        hour = other.getHour();
        wine = other.getWine();
        amt = other.getAmt();
        isBottles = other.getIsBottles();
        note = other.getNote();
    }

    /**
     * sets a new note
     * @param note the new note
     * @return this LogDiff object (to chain sets)
     */
    public LogDiff setNote(String note) {
        this.note = note;
        return this;
    }

    /**
     * sets a new sql Date for the date of logging
     * @param date the new date
     * @return this LogDiff object (to chain sets)
     */
    public LogDiff setDate(Date date) {
        this.date = date;
        return this;
    }

    /**
     * sets the new hour of logging
     * @param hour an int representing an hour of day i.e. 0, 15 etc.
     * @return this LogDiff object (to chain sets)
     */
    public LogDiff setHour(int hour) {
        this.hour = hour;
        return this;
    }

    /**
     * sets a new wine
     * @param wine the new wine
     * @return this LogDiff object (to chain sets)
     */
    public LogDiff setWine(Wine wine) {
        this.wine = wine;
        return this;
    }

    /**
     * sets a new amount
     * @param amt the new amount to be set
     * @return this LogDiff object (to chain sets)
     */
    public LogDiff setAmt(float amt) {
        this.amt = amt;
        return this;
    }

    /**
     * sets whether the amount should be interpreted as bottles or glasses
     * @param isBottles the new interpretation
     * @return this LogDiff object (to chain sets)
     */
    public LogDiff setIsBottles(boolean isBottles) {
        this.isBottles = isBottles;
        return this;
    }

    /**
     * gets the date currently set for this log
     * @return date of logging
     */
    public Date getDate() {
        return date;
    }

    /**
     * gets the currently specified hour of logging
     * @return integer representing the hour of logging
     */
    public int getHour() {
        return hour;
    }

    /**
     * gets the wine currently set. may be null
     * @return the currently set wine. May be null
     */
    public Wine getWine() {
        return wine;
    }

    /**
     * gets the currently set amount
     * @return float the parsing of what is entered by the user. May be null
     */
    public float getAmt() {
        return amt;
    }

    /**
     * gets whether this LogDiff is configured to log by bottles (or glasses)
     * @return boolean, true if logging by bottle
     */
    public boolean getIsBottles() {
        return isBottles;
    }

    /**
     * gets the currently set note
     * @return null-safe note string
     */
    public String getNote() {
        return note;
    }

    /**
     * compares this LogDiff to another by trivially comparing all fields
     * @param other the LogDiff to be compared to
     * @return true if they contain the same fields, else false
     */
    public boolean equals(LogDiff other) {
        return this.isBottles == other.getIsBottles() &&
                this.amt == other.getAmt() &&
                this.wine == other.getWine() &&
                this.date == other.getDate() &&
                this.hour == other.getHour() &&
                this.note.equals(other.getNote());
    }

    /**
     * gets whether the log embedded in this pseudo-builder is valid (and buildable)
     * @return true if both non-default mandatory values are set, selected wine and log amount, else false
     */
    public boolean isValid() {
        return wine != null &&
                amt != 0;
    }

    /**
     * prints the valid log as a gerund, corresponding to the set fields
     * @return String to be displayed
     */
    public String toString() {
        return String.format("You are logging %.1f %s of %s at %s on %s.",
                amt,
                (isBottles) ? "bottles" : "glasses",
                wine.getName(),
                LogManager.getInstance().getHourConverter().toString(hour),
                LogManager.getInstance().getDateString(date));
    }
}
