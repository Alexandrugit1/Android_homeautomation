package com.example.homeautomation.MainPart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.homeautomation.AuthenticationPart.ActivityLogin;
import com.example.homeautomation.UserDetails;
import com.example.homeautomation.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class ActivityHome extends AppCompatActivity
{
    private SharedPreferences preferences;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView topNavigationView;
    private Intent intent;
    private ActivityHomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setVariables();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setDrawerProfile();
        setFragment(viewModel.getSelectedFragment());
        checkIfTheBottomNavigationBarIsNotSelectable();
    }

    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) // if the drawer is open, then close it
            drawerLayout.closeDrawer(GravityCompat.START);
        else setCountDownTimer(250, 1000, 2); // else go back after 0.25 seconds
    }

    private void setVariables()
    {
        preferences = getSharedPreferences("APP_USER", MODE_PRIVATE);
        viewModel = new ViewModelProvider(this).get(ActivityHomeViewModel.class);
        bottomNavigationView = findViewById(R.id.homeBottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        drawerLayout = findViewById(R.id.homeDrawerLayout);
        toolbar = findViewById(R.id.homeToolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        topNavigationView = findViewById(R.id.homeTopNavigationView);
        topNavigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch(item.getItemId())
            {
                case R.id.nav_reports:
                    viewModel.setSelectedFragment(new FragmentReports());
                    break;
                case R.id.nav_control:
                    viewModel.setSelectedFragment(new FragmentControl());
                    break;
                case R.id.nav_heating:
                    viewModel.setSelectedFragment(new FragmentHeating());
                    break;
            }

            uncheckTopNavigationSelectedItem();
            checkUncheckBottomNavigationBar(true);
            setFragment(viewModel.getSelectedFragment());
            viewModel.setBottomNavigationIsSelectable(true);

            return true;
        }
    };

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch(item.getItemId())
            {
                case R.id.nav_return:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    setCountDownTimer(250, 1000, 0);
                    break;
                case R.id.nav_logout:
                    removeUserFromSharedPreferences();
                    goToActivity(ActivityLogin.class);
                    break;
            }

            checkUncheckBottomNavigationBar(false);
            setFragment(viewModel.getSelectedFragment());
            drawerLayout.closeDrawer(GravityCompat.START);
            viewModel.setBottomNavigationIsSelectable(false);

            return true;
        }
    };

    private void setFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.homeMainFragment, fragment).commit();
    }

    private void uncheckTopNavigationSelectedItem()
    {
        int navMenuSize = topNavigationView.getMenu().size();
        for (int i = 0; i < navMenuSize; i++)
            topNavigationView.getMenu().getItem(i).setChecked(false);
    }

    private void checkUncheckBottomNavigationBar(boolean value) // TRUE: select the selected item from the bottom nav bar; FALSE: deselect the selected item from the bottom nav bar
    {
        bottomNavigationView.getMenu().setGroupCheckable(0, value, true);
    }

    private void checkIfTheBottomNavigationBarIsNotSelectable()
    {
        if(!viewModel.getBottomNavigationIsSelectable())
            checkUncheckBottomNavigationBar(false);
    }

    private void goToActivity(Class<? extends Activity> activity)
    {
        intent = new Intent(ActivityHome.this, activity);
        setCountDownTimer(250, 1000, 1);
    }

    private void setDrawerProfile()
    {
        UserDetails currentUser = retrieveUserFromSharedPreferences();
        View drawerHeader = topNavigationView.getHeaderView(0);
        final TextView drawerName = drawerHeader.findViewById(R.id.navDrawerHeaderName);
        final TextView drawerEmail = drawerHeader.findViewById(R.id.navDrawerHeaderEmail);

        // setting the profile if the user is stored in SharedPreferences
        if(currentUser != null)
        {
            drawerName.setText(currentUser.getUserName());
            drawerEmail.setText(currentUser.getUserEmail());
        }
    }

    private UserDetails retrieveUserFromSharedPreferences()
    {
        Gson gson = new Gson();
        String json = preferences.getString("currentUser", "");

        return gson.fromJson(json, UserDetails.class);
    }

    private void setCountDownTimer(int duration, int interval, final int selectedCase)
    {
        new CountDownTimer(duration, interval)
        {

            @Override
            public void onTick(long millisUntilFinished)
            {

            }

            @Override
            public void onFinish()
            {
                switch(selectedCase)
                {
                    case 0:
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case 1:
                        startActivity(intent);
                        finishAffinity();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                    case 2:
                        ActivityHome.super.onBackPressed();
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }
            }
        }.start();
    }

    private void removeUserFromSharedPreferences()
    {
        preferences.edit().remove("currentUser").apply();
    }
}