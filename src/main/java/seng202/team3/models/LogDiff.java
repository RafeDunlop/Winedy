package seng202.team3.models;

import java.sql.Date;


public class LogDiff {

    private Date date;

    private int hour;

    private Wine wine;

    private float amt;

    private boolean isBottles;

    private String note;

    public LogDiff() {
        note = "";
    }

    public LogDiff(LogDiff other) {
        date = other.getDate();
        hour = other.getHour();
        wine = other.getWine();
        amt = other.getAmt();
        isBottles = other.getIsBottles();
        note = other.getNote();
    }

    public LogDiff setNote(String note) {
        this.note = note;
        return this;
    }

    public LogDiff setDate(Date date) {
        this.date = date;
        return this;
    }

    public LogDiff setHour(int hour) {
        this.hour = hour;
        return this;
    }

    public LogDiff setWine(Wine wine) {
        this.wine = wine;
        return this;
    }

    public LogDiff setAmt(float amt) {
        this.amt = amt;
        return this;
    }

    public LogDiff setIsBottles(boolean isBottles) {
        this.isBottles = isBottles;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public Wine getWine() {
        return wine;
    }

    public float getAmt() {
        return amt;
    }

    public boolean getIsBottles() {
        return isBottles;
    }

    public String getNote() {
        return note;
    }

    public boolean equals(LogDiff other) {
        return this.isBottles == other.getIsBottles() &&
                this.amt == other.getAmt() &&
                this.wine == other.getWine() &&
                this.date.equals(other.getDate()) &&
                this.hour == other.getHour() &&
                this.note.equals(other.getNote());
    }

    public boolean isValid() {
        return wine != null &&
                amt != 0;
    }
}
