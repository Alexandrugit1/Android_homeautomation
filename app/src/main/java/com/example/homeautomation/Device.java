package com.example.homeautomation;

public class Device
{
    private String DEVICE_ID;
    private String NAME;
    private String TIP;
    private String VALOARE_CURENTA;

    public Device(String DEVICE_ID, String NAME, String TIP, String VALOARE_CURENTA)
    {
        this.DEVICE_ID = DEVICE_ID;
        this.NAME = NAME;
        this.TIP = TIP;
        this.VALOARE_CURENTA = VALOARE_CURENTA;
    }

    public String getDEVICE_ID()
    {
        return DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID)
    {
        this.DEVICE_ID = DEVICE_ID;
    }

    public String getNAME()
    {
        return NAME;
    }

    public void setNAME(String NAME)
    {
        this.NAME = NAME;
    }

    public String getTIP()
    {
        return TIP;
    }

    public void setTIP(String TIP)
    {
        this.TIP = TIP;
    }

    public String getVALOARE_CURENTA()
    {
        return VALOARE_CURENTA;
    }

    public void setVALOARE_CURENTA(String VALOARE_CURENTA)
    {
        this.VALOARE_CURENTA = VALOARE_CURENTA;
    }

    @Override
    public String toString()
    {
        return "Device{" +
                "DEVICE_ID='" + DEVICE_ID + '\'' +
                ", NAME='" + NAME + '\'' +
                ", TIP='" + TIP + '\'' +
                ", VALOARE_CURENTA='" + VALOARE_CURENTA + '\'' +
                '}';
    }
}
