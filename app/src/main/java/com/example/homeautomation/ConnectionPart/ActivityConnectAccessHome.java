package com.example.homeautomation.ConnectionPart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.homeautomation.AuthenticationPart.ActivityLogin;
import com.example.homeautomation.CurrentTime;
import com.example.homeautomation.GlobalVariables;
import com.example.homeautomation.JsonPlaceHolderAPI;
import com.example.homeautomation.MainPart.ActivityHome;
import com.example.homeautomation.R;
import com.example.homeautomation.UserDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityConnectAccessHome extends AppCompatActivity
{
    private SharedPreferences preferences;
    private Retrofit retrofit;
    private JsonPlaceHolderAPI api;
    private Button enter;
    private Button leave;
    private Button connect;
    private FloatingActionButton homeFAB;
    private FloatingActionButton logoutFAB;
    private FloatingActionButton bluetoothOn;
    private FloatingActionButton bluetoothOff;
    private FloatingActionButton menuFAB;
    private TextView permission;
    private TextView connection;
    private TextView currentTimeText;
    private Spinner bluetoothDevicesSpinner;
    private ActivityConnectAccessHomeViewModel viewModel;
    private Intent intent;
    private FrameLayout transparentContainer;
    private ConstraintLayout mainLayout;
    private BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_access_home);
        setVariables();
        setOnClickListeners();
        enableOrDisableButton(connect, btAdapter.isEnabled());

        if(btAdapter.isEnabled())
        {
            viewModel.clearBluetoothDevicesList();
            populateDevicesList();
            populateSpinner(bluetoothDevicesSpinner, viewModel.getBluetoothDevicesList());
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if(viewModel.getFABMenuShown())
            showFABMenu();
        else hideFABMenu();

        initializeDateAndTime();
    }

    @Override
    public void onBackPressed()
    {
        if(viewModel.getFABMenuShown()) // if the FAB menu is open, it'll be hidden
        {
            hideFABMenu();
            viewModel.resetNumberOfClicksOnFABMenu();
        }
        else super.onBackPressed(); // if the FAB menu is closed, it'll perform normally
    }

    private void setVariables()
    {
        preferences = getSharedPreferences("APP_USER", MODE_PRIVATE);
        viewModel = new ViewModelProvider(this).get(ActivityConnectAccessHomeViewModel.class);
        retrofit = new Retrofit.Builder().baseUrl(GlobalVariables.getBaseURL()).addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(JsonPlaceHolderAPI.class);
        connection = findViewById(R.id.connectAccessHomeConnectionText);
        connect = findViewById(R.id.connectAccessHomeConnectButton);
        enter = findViewById(R.id.connectAccessHomeEnterButton);
        leave = findViewById(R.id.connectAccessHomeLeaveButton);
        homeFAB = findViewById(R.id.connectAccessHomeHomeFAB);
        logoutFAB = findViewById(R.id.connectAccessHomeLogoutFAB);
        permission = findViewById(R.id.connectAccessHomePermissionText);
        bluetoothDevicesSpinner = findViewById(R.id.connectAccessHomeBluetoothSpinner);
        bluetoothOn = findViewById(R.id.connectAccessHomeBTOnFAB);
        bluetoothOff = findViewById(R.id.connectAccessHomeBToffFAB);
        menuFAB = findViewById(R.id.connectAccessHomeMenuFAB);
        currentTimeText = findViewById(R.id.connectAccessHomeCurrentTimeText);
        transparentContainer = findViewById(R.id.connectAccessHomeTransparentContainer);
        mainLayout = findViewById(R.id.connectAccessHomeMainLayout);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void setOnClickListeners()
    {
        bluetoothOn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(btAdapter == null)
                    showMessage("Bluetooth is not available");
                else if(!btAdapter.isEnabled())
                {
                    intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, viewModel.getREQUEST_ENABLE_BT());
                }

                hideFABMenu();
                viewModel.incrementNumberOfClicksOnFABMenu();
            }
        });

        bluetoothOff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(btAdapter.isEnabled())
                {
                    btAdapter.disable();
                    showMessage("Bluetooth is now disabled");
                }

                if(!viewModel.getBluetoothDevicesList().isEmpty())
                    clearSpinner(bluetoothDevicesSpinner);

                hideFABMenu();
                viewModel.incrementNumberOfClicksOnFABMenu();
            }
        });

        connect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!viewModel.getBluetoothDevicesList().isEmpty()) // if the devices list isn't empty
                {
                    String text = "Please select a device first";
                    int color = Color.RED;

                    if(bluetoothDevicesSpinner.getSelectedItemPosition() != -1)
                    {
//                        IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
//
//                        /*
//                         * Registering a new BTBroadcast receiver from the Main Activity context
//                         * with pairing request event
//                         */
//                        registerReceiver(new PairingRequest(), filter);

                        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                        registerReceiver(receiver, filter);

                        btAdapter.cancelDiscovery();

                        Set<BluetoothDevice> bluetoothDeviceSet = viewModel.getBluetoothDevicesSet();

                        for(BluetoothDevice device : bluetoothDeviceSet)
                            if(device.getName().equals(bluetoothDevicesSpinner.getSelectedItem()))
                            {
                                String deviceAddress = device.getAddress();
//                                BluetoothSocket tmp = null;
//                                BluetoothSocket mmSocket = null;
//
//                                try {
//                                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//                                    Method m = device.getClass().getMethod("createRfcommSocket", int.class);
//                                    tmp = (BluetoothSocket) m.invoke(device, 1);
//                                } catch (IOException | NoSuchMethodException e) {
//                                    Log.e(TAG, "create() failed", e);
//                                } catch (IllegalAccessException | InvocationTargetException e) {
//                                    e.printStackTrace();
//                                }
//                                mmSocket = tmp;



                                //Toast.makeText(ActivityConnectAccessHome.this, device.getName() + " " + deviceAddress, Toast.LENGTH_SHORT).show();
                                break;
                            }



                        text = "Connected to " + bluetoothDevicesSpinner.getSelectedItem();
                        color = Color.GREEN;
                        viewModel.setBluetoothConnected(true);
                    }
                    connection.setText(text);
                    connection.setTextColor(color);
                    setCountDownTimer(2000, 1000, 0);
                }
            }
        });

        enter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!viewModel.getBluetoothDevicesList().isEmpty()) // if the devices list isn't empty
                {
                    String userIMEI = getDeviceIMEI();

                    if(userIMEI != null)
                    {
                        Call<String> call = api.enterHouse(userIMEI);

                        call.enqueue(new Callback<String>()
                        {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull final Response<String> response)
                            {
                                if(!response.isSuccessful())
                                {
                                    Toast.makeText(ActivityConnectAccessHome.this, "response is not successful, code: " + response.code(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String enterHouseResponse = response.body();

                                if(enterHouseResponse != null)
                                {
                                    String text = "Please connect to a device first";
                                    int color = Color.RED;

                                    if(viewModel.getBluetoothConnected() && !String.valueOf(permission.getText()).trim().equals("Access permission granted") && !String.valueOf(permission.getText()).trim().equals("Exit permission granted"))
                                    {
                                        text = "Access permission granted";
                                        color = Color.GREEN;
                                    }

                                    permission.setText(text);
                                    permission.setTextColor(color);
                                    setCountDownTimer(2000, 1000, 1);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t)
                            {
                                Toast.makeText(ActivityConnectAccessHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        homeFAB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToActivity(ActivityHome.class);
            }
        });

        leave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!viewModel.getBluetoothDevicesList().isEmpty()) // if the devices list isn't empty
                {
                    String userIMEI = getDeviceIMEI();

                    if(userIMEI != null)
                    {
                        Call<String> call = api.leaveHouse(userIMEI);

                        call.enqueue(new Callback<String>()
                        {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull final Response<String> response)
                            {
                                if(!response.isSuccessful())
                                {
                                    Toast.makeText(ActivityConnectAccessHome.this, "response is not successful, code: " + response.code(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String leaveHouseResponse = response.body();

                                if(leaveHouseResponse != null)
                                {
                                    String text = "Please connect to a device first";
                                    int color = Color.RED;

                                    if(viewModel.getBluetoothConnected() && !String.valueOf(permission.getText()).trim().equals("Exit permission granted") && !String.valueOf(permission.getText()).trim().equals("Access permission granted"))
                                    {
                                        text = "Exit permission granted";
                                        color = Color.GREEN;
                                    }

                                    permission.setText(text);
                                    permission.setTextColor(color);
                                    setCountDownTimer(2000, 1000, 2);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t)
                            {
                                Toast.makeText(ActivityConnectAccessHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        logoutFAB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signOutUser();
            }
        });

        mainLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(viewModel.getFABMenuShown())
                {
                    hideFABMenu();
                    viewModel.incrementNumberOfClicksOnFABMenu();
                }
            }
        });

        menuFAB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewModel.incrementNumberOfClicksOnFABMenu();
                if(viewModel.getNumberOfClicksOnFABMenu() % 2 == 1)
                    showFABMenu();
                else hideFABMenu();
            }
        });
    }

    private void goToActivity(Class<? extends Activity> activity)
    {
        intent = new Intent(ActivityConnectAccessHome.this, activity);

        switch(activity.getName())
        {
            case "com.example.homeautomation.AuthenticationPart.ActivityLogin":
                hideFABMenu();
                setCountDownTimer(250, 1000, 4);
                break;
            case "com.example.homeautomation.MainPart.ActivityHome":
                viewModel.setFABMenuShown(false);
                viewModel.resetNumberOfClicksOnFABMenu();
                hideFABMenu();
                setCountDownTimer(250, 1000, 3);
                break;
        }
    }

    private void enableOrDisableFABs(boolean value)
    {
        enableOrDisableButton(logoutFAB, value);
        enableOrDisableButton(homeFAB, value);
        enableOrDisableButton(bluetoothOn, value);
        enableOrDisableButton(bluetoothOff, value);
    }

    private void showFABMenu()
    {
        int phoneOrientation = getResources().getConfiguration().orientation;

        viewModel.setUiDisabled(true);
        freezeUnfreezeMainLayout(viewModel.getUIDisabled());
        viewModel.setFABMenuShown(true);
        menuFAB.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6FC4BD"))); // set FAB background tint
        menuFAB.setColorFilter(Color.WHITE); // set FAB tint
        menuFAB.setRippleColor(Color.parseColor("#6FC4BD"));

        if(phoneOrientation == Configuration.ORIENTATION_PORTRAIT) // if the phone is in portrait mode
        {
            logoutFAB.animate().translationY(-getResources().getDimension(R.dimen.standard_75));
            homeFAB.animate().translationY(-getResources().getDimension(R.dimen.standard_150));
            bluetoothOff.animate().translationY(-getResources().getDimension(R.dimen.standard_225));
            bluetoothOn.animate().translationY(-getResources().getDimension(R.dimen.standard_300));
        }
        else if(phoneOrientation == Configuration.ORIENTATION_LANDSCAPE) // if the phone is in landscape mode
        {
            logoutFAB.animate().translationX(-getResources().getDimension(R.dimen.standard_75));
            homeFAB.animate().translationX(-getResources().getDimension(R.dimen.standard_150));
            bluetoothOff.animate().translationX(-getResources().getDimension(R.dimen.standard_225));
            bluetoothOn.animate().translationX(-getResources().getDimension(R.dimen.standard_300));
        }

        enableOrDisableFABs(true);
        menuFAB.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_remove, getTheme()));
    }

    private void hideFABMenu()
    {
        int phoneOrientation = getResources().getConfiguration().orientation;

        viewModel.setUiDisabled(false);
        freezeUnfreezeMainLayout(viewModel.getUIDisabled());
        viewModel.setFABMenuShown(false);
        menuFAB.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)); // set FAB background tint
        menuFAB.setColorFilter(Color.parseColor("#6FC4BD")); // set FAB tint
        menuFAB.setRippleColor(Color.WHITE);

        if(phoneOrientation == Configuration.ORIENTATION_PORTRAIT) // if the phone is in portrait mode
        {
            logoutFAB.animate().translationY(0);
            homeFAB.animate().translationY(0);
            bluetoothOff.animate().translationY(0);
            bluetoothOn.animate().translationY(0);
        }
        else if(phoneOrientation == Configuration.ORIENTATION_LANDSCAPE) // if the phone is in landscape mode
        {
            logoutFAB.animate().translationX(0);
            homeFAB.animate().translationX(0);
            bluetoothOff.animate().translationX(0);
            bluetoothOn.animate().translationX(0);
        }

        enableOrDisableFABs(false);
        menuFAB.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add, getTheme()));
    }

    private void freezeUnfreezeMainLayout(boolean value)
    {
        int selectedColor = value ? Color.parseColor("#BFFFFFFF") : Color.parseColor("#00000000");
        Animation changeBackgroundAnimation;

        enableOrDisableButton(connect, !value);
        enableOrDisableButton(enter, !value);
        enableOrDisableButton(leave, !value);
        enableOrDisableSpinner(bluetoothDevicesSpinner, !value);

        if(value)
        {
            transparentContainer.setVisibility(View.INVISIBLE);
            transparentContainer.setBackgroundColor(selectedColor);
            changeBackgroundAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            transparentContainer.setAnimation(changeBackgroundAnimation);
            transparentContainer.setVisibility(View.VISIBLE);
        }
        else
        {
            changeBackgroundAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            transparentContainer.startAnimation(changeBackgroundAnimation);
            transparentContainer.setBackgroundColor(selectedColor);
        }
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
                        connection.setText("");
                        break;
                    case 1:
                    case 2:
                        permission.setText("");
                        break;
                    case 3:
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case 4:
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                }
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == viewModel.getREQUEST_ENABLE_BT() && resultCode == RESULT_OK)
        {
            showMessage("Bluetooth is now enabled");
            viewModel.clearBluetoothDevicesList();
            populateDevicesList();
            populateSpinner(bluetoothDevicesSpinner, viewModel.getBluetoothDevicesList());
            enableOrDisableButton(connect, true);
        }
    }

    private void showMessage(String message)
    {
        Toast.makeText(ActivityConnectAccessHome.this, message, Toast.LENGTH_SHORT).show();
    }

    private void populateDevicesList()
    {
        viewModel.setBluetoothDevicesSet(btAdapter.getBondedDevices());

        for(BluetoothDevice device : viewModel.getBluetoothDevicesSet())
            viewModel.insertDeviceNameIntoBluetoothDevicesList(device.getName());

        viewModel.sortDevicesList();
    }

    private void populateSpinner(Spinner spinner, ArrayList<String> list)
    {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, list)
        {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) // setting the selected item
            {
                View v = super.getView(position, convertView, parent);

                ((TextView)v).setGravity(Gravity.CENTER);

                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) // setting the dropdown items
            {
                View v = super.getDropDownView(position, convertView, parent);

                ((TextView)v).setGravity(Gravity.CENTER);

                return v;
            }
        };
        spinner.setAdapter(spinnerAdapter);
    }

    private void clearSpinner(Spinner spinner)
    {
        viewModel.clearBluetoothDevicesList();
        spinner.setAdapter(null);
    }

    private void enableOrDisableButton(Button selectedButton, boolean value)
    {
        selectedButton.setEnabled(value);
    }

    private void enableOrDisableButton(FloatingActionButton selectedButton, boolean value)
    {
        selectedButton.setEnabled(value);
    }

    private void enableOrDisableSpinner(Spinner spinner, boolean value)
    {
        spinner.setEnabled(value);
    }

    private void signOutUser()
    {
        Call<String> call = api.signOutUser();

        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(@NonNull Call<String > call, @NonNull Response<String> response)
            {
                if(!response.isSuccessful())
                {
                    Toast.makeText(ActivityConnectAccessHome.this, "response is not successful, code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String responseString = response.body();

                if(responseString != null && responseString.equals("ok"))
                {
                    removeUserFromSharedPreferences();
                    goToActivity(ActivityLogin.class);
                }
                else Toast.makeText(ActivityConnectAccessHome.this, "Sign out was not successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<String > call, @NonNull Throwable t)
            {
                Toast.makeText(ActivityConnectAccessHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCurrentTime()
    {
        Call<CurrentTime> call = api.getCurrentTime();

        call.enqueue(new Callback<CurrentTime>()
        {
            @Override
            public void onResponse(@NonNull Call<CurrentTime> call, @NonNull final Response<CurrentTime> response)
            {
                if(!response.isSuccessful())
                {
                    Toast.makeText(ActivityConnectAccessHome.this, "response is not successful, code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                CurrentTime responseCurrentTime = response.body();
                String responseCurrentTimeParsed = "";

                if(responseCurrentTime != null)
                    responseCurrentTimeParsed = getParsedTime(responseCurrentTime);

                currentTimeText.setText(responseCurrentTimeParsed);
            }

            @Override
            public void onFailure(@NonNull Call<CurrentTime> call, @NonNull Throwable t)
            {
                Toast.makeText(ActivityConnectAccessHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getParsedTime(CurrentTime time)
    {
        String parsedTime = "";
        String weekDay = time.getWeekday();
        String monthDay = String.valueOf(time.getMday());
        String monthName = time.getMonth();
        String year = String.valueOf(time.getYear());
        String hour = String.valueOf(time.getHours() + 3);
        String minute = String.valueOf(time.getMinutes());
        String second = String.valueOf(time.getSeconds());

        if(Integer.parseInt(monthDay) < 10)
            monthDay = "0" + monthDay;

        if(Integer.parseInt(hour) < 10)
            hour = "0" + hour;

        if(Integer.parseInt(minute) < 10)
            minute = "0" + minute;

        if(Integer.parseInt(second) < 10)
            second = "0" + second;

        parsedTime += weekDay;
        parsedTime += ", ";
        parsedTime += monthDay;
        parsedTime += " ";
        parsedTime += monthName;
        parsedTime += " ";
        parsedTime += year;
        parsedTime += " ";
        parsedTime += hour;
        parsedTime += ":";
        parsedTime += minute;
        parsedTime += ":";
        parsedTime += second;

        return parsedTime;
    }

    @SuppressLint("HardwareIds")
    private String getDeviceIMEI()
    {
        return Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void removeUserFromSharedPreferences()
    {
        preferences.edit().remove("currentUser").apply();
    }

    private UserDetails retrieveUserFromSharedPreferences()
    {
        Gson gson = new Gson();
        String json = preferences.getString("currentUser", "");

        return gson.fromJson(json, UserDetails.class);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(device != null)
                {
                    if(device.getBondState() == BluetoothDevice.BOND_BONDED)
                    {
                        Log.d("onReceive", "Broadcast Receiver: BOND_BONDED");
                    }

                    if(device.getBondState() == BluetoothDevice.BOND_BONDING)
                    {
                        Log.d("onReceive", "Broadcast Receiver: BOND_BONDING");
                    }

                    if(device.getBondState() == BluetoothDevice.BOND_NONE)
                    {
                        Log.d("onReceive", "Broadcast Receiver: BOND_NONE");
                    }
                }
            }
        }
    };

    private void initializeDateAndTime()
    {
        ActivityConnectAccessHome.DateSetter dateSetter = new ActivityConnectAccessHome.DateSetter();
        dateSetter.start();
    }

    public class DateSetter extends Thread
    {
        public void run()
        {
            while(true)
            {
                try
                {
                    sleep(1000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }

                setCurrentTime();
            }
        }
    }
}