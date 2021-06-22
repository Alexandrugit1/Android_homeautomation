package com.example.homeautomation;

import androidx.annotation.NonNull;

public class Event
{
    private int id;
    private String name;
    private String value;
    private String time;

    public Event(int id, String name, String value, String time)
    {
        this.id = id;
        this.name = name;
        this.value = value;
        this.time = time;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
