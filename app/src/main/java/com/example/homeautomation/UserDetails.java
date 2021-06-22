package com.example.homeautomation;

import com.google.gson.annotations.SerializedName;

public class UserDetails
{
    @SerializedName("IMEI")
    private String userIMEI;

    @SerializedName("NUME")
    private String userName;

    @SerializedName("EMAIL")
    private String userEmail;

    @SerializedName("PAROLA")
    private String userPassword;

    @SerializedName("CNP")
    private String userPIN;

    @SerializedName("ADMIN")
    private char userAdmin;

    @SerializedName("PREZENTA")
    private char userPresent;

    public UserDetails(String userName, String userEmail, String userPassword, String userPIN, char userAdmin, char userPresent)
    {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPIN = userPIN;
        this.userAdmin = userAdmin;
        this.userPresent = userPresent;
    }

    public String getUserIMEI()
    {
        return userIMEI;
    }

    public void setUserIMEI(String userIMEI)
    {
        this.userIMEI = userIMEI;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }

    public String getUserPIN()
    {
        return userPIN;
    }

    public void setUserPIN(String userPIN)
    {
        this.userPIN = userPIN;
    }

    public char getUserAdmin()
    {
        return userAdmin;
    }

    public void setUserAdmin(char userAdmin)
    {
        this.userAdmin = userAdmin;
    }

    public char getUserPresent()
    {
        return userPresent;
    }

    public void setUserPresent(char userPresent)
    {
        this.userPresent = userPresent;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "userIMEI='" + userIMEI + '\'' +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPIN='" + userPIN + '\'' +
                ", userAdmin=" + userAdmin +
                ", userPresent=" + userPresent +
                '}';
    }
}