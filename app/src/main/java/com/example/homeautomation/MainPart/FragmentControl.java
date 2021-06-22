package com.example.homeautomation.MainPart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeautomation.Device;
import com.example.homeautomation.GlobalVariables;
import com.example.homeautomation.JsonPlaceHolderAPI;
import com.example.homeautomation.R;
import com.example.homeautomation.Temperature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentControl extends Fragment
{
    private Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GlobalVariables.getBaseURL())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private JsonPlaceHolderAPI api = retrofit.create(JsonPlaceHolderAPI.class);
    private RecyclerView lightProgramsRecyclerView;
    private ArrayList<Device> devicesList;
    private LightsRecyclerViewAdapter adapter;
    private Button temperatureButton;

    public FragmentControl()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_control, container, false);

        setVariables(view);
        setRecyclerView();
        setLightPrograms();
        setCurrentTemperature();

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    private void setVariables(View v)
    {
        temperatureButton = v.findViewById(R.id.control_temperature);
        lightProgramsRecyclerView = v.findViewById(R.id.control_lights_recyclerview);
        devicesList = new ArrayList<>();
        adapter = new LightsRecyclerViewAdapter(devicesList, requireContext());
    }

    private void setCurrentTemperature()
    {
        Call<ArrayList<Temperature>> call = api.getTemperature("1");

        call.enqueue(new Callback<ArrayList<Temperature>>()
        {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Temperature>> call, @NonNull Response<ArrayList<Temperature>> response)
            {
                if(!response.isSuccessful())
                {
                    Toast.makeText(requireActivity(), "response is not successful, code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Temperature> currentTemperature = response.body();

                if(currentTemperature != null)
                {
                    String text = "Current temperature: " + currentTemperature.get(0).getVALUE() + " °C";

                    temperatureButton.setText(text);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Temperature>> call, @NonNull Throwable t)
            {
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView()
    {
        lightProgramsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        lightProgramsRecyclerView.setAdapter(adapter);
    }

    private void setLightPrograms()
    {
        Call<ArrayList<Device>> call = api.getAllDevices();

        call.enqueue(new Callback<ArrayList<Device>>()
        {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Device>> call, @NonNull final Response<ArrayList<Device>> response)
            {
                if(!response.isSuccessful())
                {
                    Toast.makeText(requireActivity(), "response is not successful, code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Device> currentDevices = response.body();

                if(currentDevices != null)
                {
                    if(!devicesList.isEmpty())
                        devicesList.clear();

                    for(final Device device : currentDevices)
                        if(device.getTIP().equals("LIGHT"))
                            devicesList.add(device);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Device>> call, @NonNull Throwable t)
            {
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}