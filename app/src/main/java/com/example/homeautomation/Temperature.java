package com.example.homeautomation;

public class Temperature
{
    private String TEMP_ID;
    private String DEVICE_ID;
    private String TIME;
    private String VALUE;

    public Temperature(String TEMP_ID, String DEVICE_ID, String TIME, String VALUE)
    {
        this.TEMP_ID = TEMP_ID;
        this.DEVICE_ID = DEVICE_ID;
        this.TIME = TIME;
        this.VALUE = VALUE;
    }

    public String getTEMP_ID()
    {
        return TEMP_ID;
    }

    public void setTEMP_ID(String TEMP_ID)
    {
        this.TEMP_ID = TEMP_ID;
    }

    public String getDEVICE_ID()
    {
        return DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID)
    {
        this.DEVICE_ID = DEVICE_ID;
    }

    public String getTIME()
    {
        return TIME;
    }

    public void setTIME(String TIME)
    {
        this.TIME = TIME;
    }

    public String getVALUE()
    {
        return VALUE;
    }

    public void setVALUE(String VALUE)
    {
        this.VALUE = VALUE;
    }

    @Override
    public String toString()
    {
        return "Temperature{" +
                "TEMP_ID='" + TEMP_ID + '\'' +
                ", DEVICE_ID='" + DEVICE_ID + '\'' +
                ", TIME='" + TIME + '\'' +
                ", VALUE='" + VALUE + '\'' +
                '}';
    }
}
