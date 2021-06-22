package com.example.homeautomation;

import androidx.annotation.NonNull;

public class Entry
{
    private String ID;
    private String CNP;
    private String IMEI;
    private String TIMP;

    public Entry(String ID, String CNP, String IMEI, String TIMP)
    {
        this.ID = ID;
        this.CNP = CNP;
        this.IMEI = IMEI;
        this.TIMP = TIMP;
    }

    public String getID()
    {
        return ID;
    }

    public void setID(String ID)
    {
        this.ID = ID;
    }

    public String getCNP()
    {
        return CNP;
    }

    public void setCNP(String CNP)
    {
        this.CNP = CNP;
    }

    public String getIMEI()
    {
        return IMEI;
    }

    public void setIMEI(String IMEI)
    {
        this.IMEI = IMEI;
    }

    public String getTIMP()
    {
        return TIMP;
    }

    public void setTIMP(String TIMP)
    {
        this.TIMP = TIMP;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Entry{" +
                "ID='" + ID + '\'' +
                ", CNP='" + CNP + '\'' +
                ", IMEI='" + IMEI + '\'' +
                ", TIMP='" + TIMP + '\'' +
                '}';
    }
}