package com.example.homeautomation;

import androidx.annotation.NonNull;

public class Program
{
    private String ID;
    private String NAME;
    private String START_TIME;
    private String FINISH_TIME;
    private String TEMPERATURE;
    private String ACTIVE;

    public Program(String ID, String NAME, String START_TIME, String FINISH_TIME, String TEMPERATURE, String ACTIVE)
    {
        this.ID = ID;
        this.NAME = NAME;
        this.START_TIME = START_TIME;
        this.FINISH_TIME = FINISH_TIME;
        this.TEMPERATURE = TEMPERATURE;
        this.ACTIVE = ACTIVE;
    }

    public Program(String NAME, String START_TIME, String FINISH_TIME, String TEMPERATURE)
    {
        this.ID = null;
        this.NAME = NAME;
        this.START_TIME = START_TIME;
        this.FINISH_TIME = FINISH_TIME;
        this.TEMPERATURE = TEMPERATURE;
        this.ACTIVE = "1";
    }

    public String getID()
    {
        return ID;
    }

    public void setID(String ID)
    {
        this.ID = ID;
    }

    public String getNAME()
    {
        return NAME;
    }

    public void setNAME(String NAME)
    {
        this.NAME = NAME;
    }

    public String getSTART_TIME()
    {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME)
    {
        this.START_TIME = START_TIME;
    }

    public String getFINISH_TIME()
    {
        return FINISH_TIME;
    }

    public void setFINISH_TIME(String FINISH_TIME)
    {
        this.FINISH_TIME = FINISH_TIME;
    }

    public String getTEMPERATURE()
    {
        return TEMPERATURE;
    }

    public void setTEMPERATURE(String TEMPERATURE)
    {
        this.TEMPERATURE = TEMPERATURE;
    }

    public String getACTIVE()
    {
        return ACTIVE;
    }

    public void setACTIVE(String ACTIVE)
    {
        this.ACTIVE = ACTIVE;
    }

    @NonNull
    @Override
    public String toString() {
        return "Program{" +
                "ID='" + ID + '\'' +
                ", NAME='" + NAME + '\'' +
                ", START_TIME='" + START_TIME + '\'' +
                ", FINISH_TIME='" + FINISH_TIME + '\'' +
                ", TEMPERATURE='" + TEMPERATURE + '\'' +
                ", ACTIVE='" + ACTIVE + '\'' +
                '}';
    }
}
