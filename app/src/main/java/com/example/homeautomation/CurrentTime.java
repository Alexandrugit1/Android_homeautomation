package com.example.homeautomation;

import androidx.annotation.NonNull;

public class CurrentTime
{
    private int seconds;
    private int minutes;
    private int hours;
    private int mday;
    private int wday;
    private int mon;
    private int year;
    private int yday;
    private String weekday;
    private String month;
    private int zero;

    public CurrentTime(int seconds, int minutes, int hours, int mday, int wday, int mon, int year, int yday, String weekday, String month, int zero)
    {
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        this.mday = mday;
        this.wday = wday;
        this.mon = mon;
        this.year = year;
        this.yday = yday;
        this.weekday = weekday;
        this.month = month;
        this.zero = zero;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public void setSeconds(int seconds)
    {
        this.seconds = seconds;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public void setMinutes(int minutes)
    {
        this.minutes = minutes;
    }

    public int getHours()
    {
        return hours;
    }

    public void setHours(int hours)
    {
        this.hours = hours;
    }

    public int getMday()
    {
        return mday;
    }

    public void setMday(int mday)
    {
        this.mday = mday;
    }

    public int getWday()
    {
        return wday;
    }

    public void setWday(int wday)
    {
        this.wday = wday;
    }

    public int getMon()
    {
        return mon;
    }

    public void setMon(int mon)
    {
        this.mon = mon;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getYday()
    {
        return yday;
    }

    public void setYday(int yday)
    {
        this.yday = yday;
    }

    public String getWeekday()
    {
        return weekday;
    }

    public void setWeekday(String weekday)
    {
        this.weekday = weekday;
    }

    public String getMonth()
    {
        return month;
    }

    public void setMonth(String month)
    {
        this.month = month;
    }

    public int getZero()
    {
        return zero;
    }

    public void setZero(int zero)
    {
        this.zero = zero;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "CurrentTime{" +
                "seconds=" + seconds +
                ", minutes=" + minutes +
                ", hours=" + hours +
                ", mday=" + mday +
                ", wday=" + wday +
                ", mon=" + mon +
                ", year=" + year +
                ", yday=" + yday +
                ", weekday='" + weekday + '\'' +
                ", month='" + month + '\'' +
                ", zero=" + zero +
                '}';
    }
}
