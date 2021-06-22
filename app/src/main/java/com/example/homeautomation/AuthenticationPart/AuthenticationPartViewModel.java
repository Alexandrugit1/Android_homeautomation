package com.example.homeautomation.AuthenticationPart;

import androidx.lifecycle.ViewModel;

public class AuthenticationPartViewModel extends ViewModel
{
    public int getMinimumNumberOfPasswordCharacters()
    {
        return 6;
    }

    public int getNumberOfPINCharacters()
    {
        return 13;
    }

    public int getMinimumNumberOfNameCharacters()
    {
        return 2;
    }
}