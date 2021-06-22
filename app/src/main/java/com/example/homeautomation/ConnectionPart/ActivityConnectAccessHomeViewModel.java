package com.example.homeautomation.ConnectionPart;

import android.bluetooth.BluetoothDevice;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class ActivityConnectAccessHomeViewModel extends ViewModel
{
    private Set<BluetoothDevice> bluetoothDevicesSet;
    private ArrayList<String> bluetoothDevicesList = new ArrayList<>();
    private boolean fabMenuShown = false;
    private int numberOfClicksOnMenuFAB = 0;
    private boolean bluetoothConnected = false;
    private boolean uiDisabled = false;

    public int getREQUEST_ENABLE_BT()
    {
        return 0;
    }

    public void setBluetoothDevicesSet(Set<BluetoothDevice> bluetoothDevices)
    {
        this.bluetoothDevicesSet = bluetoothDevices;
    }

    public Set<BluetoothDevice> getBluetoothDevicesSet()
    {
        return bluetoothDevicesSet;
    }

    public ArrayList<String> getBluetoothDevicesList()
    {
        return bluetoothDevicesList;
    }

    public void clearBluetoothDevicesList()
    {
        if(!bluetoothDevicesList.isEmpty())
            bluetoothDevicesList.clear();
    }

    public void insertDeviceNameIntoBluetoothDevicesList(String deviceName)
    {
        bluetoothDevicesList.add(deviceName);
    }

    public void sortDevicesList()
    {
        Collections.sort(bluetoothDevicesList);
    }

    public void incrementNumberOfClicksOnFABMenu()
    {
        numberOfClicksOnMenuFAB++;
    }

    public void resetNumberOfClicksOnFABMenu()
    {
        numberOfClicksOnMenuFAB = 0;
    }

    public int getNumberOfClicksOnFABMenu()
    {
        return numberOfClicksOnMenuFAB;
    }

    public void setFABMenuShown(boolean value)
    {
        fabMenuShown = value;
    }

    public boolean getFABMenuShown()
    {
        return fabMenuShown;
    }

    public void setBluetoothConnected(boolean value)
    {
        bluetoothConnected = value;
    }

    public boolean getBluetoothConnected()
    {
        return bluetoothConnected;
    }

    public void setUiDisabled(boolean value)
    {
        uiDisabled = value;
    }

    public boolean getUIDisabled()
    {
        return uiDisabled;
    }
}
