package com.example.gps_g12.goalTracker.model.data;

public class Date {

    private int day;
    private int month;
    private int year;

    private boolean wasZeroTyped = false;

    public Date(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    //Constructor with no parameters
    public Date(){
        this(0, 0, 0);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean getWasZeroTyped() {
        return wasZeroTyped;
    }

    public void setWasZeroTyped(boolean wasZeroTyped) {
        this.wasZeroTyped = wasZeroTyped;
    }

    @Override
    public String toString(){
        return day + "/" + month + "/" + year;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Date other = (Date) obj;
        return day == other.day && month == other.month && year == other.year;
    }

}
