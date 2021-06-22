package com.example.homeautomation.MainPart;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

public class ActivityHomeViewModel extends ViewModel
{
    private Fragment selectedFragment = new FragmentReports();
    private Fragment lastFragment = selectedFragment;
    private boolean bottomNavigationIsSelectable = true;

    public Fragment getSelectedFragment()
    {
        return selectedFragment;
    }

    public void setSelectedFragment(Fragment fragment)
    {
        selectedFragment = fragment;
    }

    public Fragment getLastFragment()
    {
        return lastFragment;
    }

    public void setLastFragment(Fragment lastFragment)
    {
        this.lastFragment = lastFragment;
    }

    public boolean getBottomNavigationIsSelectable()
    {
        return bottomNavigationIsSelectable;
    }

    public void setBottomNavigationIsSelectable(boolean value)
    {
        bottomNavigationIsSelectable = value;
    }
}
