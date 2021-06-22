package com.example.homeautomation.MainPart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeautomation.GlobalVariables;
import com.example.homeautomation.JsonPlaceHolderAPI;
import com.example.homeautomation.Program;
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

public class FragmentHeating extends Fragment
{
    private Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GlobalVariables.getBaseURL())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private JsonPlaceHolderAPI api = retrofit.create(JsonPlaceHolderAPI.class);
    private ConstraintLayout addNewProgramLayout;
    private Button cancelProgram;
    private Button temperatureButton;
    private Button addNewProgramButton;
    private Button saveProgram;
    private EditText nameField;
    private EditText temperatureField;
    private DatePicker startDateField;
    private TimePicker startTimeField;
    private DatePicker endDateField;
    private TimePicker endTimeField;
    private RecyclerView programsRecyclerView;
    ArrayList<Program> programsList;
    private ProgramsRecyclerViewAdapter adapter;

    public FragmentHeating()
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
        View view = inflater.inflate(R.layout.fragment_heating, container, false);

        setVariables(view);
        setRecyclerView();
        setOnClickListeners();

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        setPrograms();
        setCurrentTemperature();
    }

    private void setVariables(View v)
    {
        temperatureButton = v.findViewById(R.id.heating_temperature);
        saveProgram = v.findViewById(R.id.heating_add_program_save);
        addNewProgramButton = v.findViewById(R.id.heating_add_program_bottom_button);
        addNewProgramLayout = v.findViewById(R.id.heating_add_program_layout);
        cancelProgram = v.findViewById(R.id.heating_add_program_cancel);
        nameField = v.findViewById(R.id.heating_add_program_name);
        temperatureField = v.findViewById(R.id.heating_add_program_temperature);
        startDateField = v.findViewById(R.id.heating_add_program_start_date);
        startTimeField = v.findViewById(R.id.heating_add_program_start_time);
        endDateField = v.findViewById(R.id.heating_add_program_end_date);
        endTimeField = v.findViewById(R.id.heating_add_program_end_time);
        programsRecyclerView = v.findViewById(R.id.heating_programs_recyclerview);
        programsList = new ArrayList<>();
        adapter = new ProgramsRecyclerViewAdapter(programsList, requireContext());

        addNewProgramLayout.setVisibility(View.GONE);
    }

    private void setOnClickListeners()
    {
        addNewProgramButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addNewProgramLayout.setVisibility(View.VISIBLE);
            }
        });

        cancelProgram.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addNewProgramLayout.setVisibility(View.GONE);
                clearHeatingProgramFields();
            }
        });

        saveProgram.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!String.valueOf(nameField.getText()).trim().equals("")
                        && !String.valueOf(temperatureField.getText()).trim().equals(""))
                {
                    final String programName = String.valueOf(nameField.getText()).trim();
                    final String programTemperature = String.valueOf(temperatureField.getText()).trim();

                    String programSelectedStartDateYear = String.valueOf(startDateField.getYear());
                    String programSelectedStartDateMonth = String.valueOf(startDateField.getMonth() + 1);
                    String programSelectedStartDateDay = String.valueOf(startDateField.getDayOfMonth());
                    String programSelectedStartTimeHour = String.valueOf(startTimeField.getHour());
                    String programSelectedStartTimeMinute = String.valueOf(startTimeField.getMinute());
                    String programSelectedStartTimeSecond = "00";

                    String programSelectedEndDateYear = String.valueOf(endDateField.getYear());
                    String programSelectedEndDateMonth = String.valueOf(endDateField.getMonth() + 1);
                    String programSelectedEndDateDay = String.valueOf(endDateField.getDayOfMonth());
                    String programSelectedEndTimeHour = String.valueOf(endTimeField.getHour());
                    String programSelectedEndTimeMinute = String.valueOf(endTimeField.getMinute());
                    String programSelectedEndTimeSecond = "00";

                    // formatting the start date and time

                    if(Integer.parseInt(programSelectedStartDateMonth) < 10)
                        programSelectedStartDateMonth = "0" + programSelectedStartDateMonth;

                    if(Integer.parseInt(programSelectedStartDateDay) < 10)
                        programSelectedStartDateDay = "0" + programSelectedStartDateDay;

                    if(Integer.parseInt(programSelectedStartTimeHour) < 10)
                        programSelectedStartTimeHour = "0" + programSelectedStartTimeHour;

                    if(Integer.parseInt(programSelectedStartTimeMinute) < 10)
                        programSelectedStartTimeMinute = "0" + programSelectedStartTimeMinute;

                    // formatting the end date and time

                    if(Integer.parseInt(programSelectedEndDateMonth) < 10)
                        programSelectedEndDateMonth = "0" + programSelectedEndDateMonth;

                    if(Integer.parseInt(programSelectedEndDateDay) < 10)
                        programSelectedEndDateDay = "0" + programSelectedEndDateDay;

                    if(Integer.parseInt(programSelectedEndTimeHour) < 10)
                        programSelectedEndTimeHour = "0" + programSelectedEndTimeHour;

                    if(Integer.parseInt(programSelectedEndTimeMinute) < 10)
                        programSelectedEndTimeMinute = "0" + programSelectedEndTimeMinute;

                    final String programStartTime = programSelectedStartDateYear + "-" + programSelectedStartDateMonth + "-" + programSelectedStartDateDay + " " + programSelectedStartTimeHour + ":" + programSelectedStartTimeMinute + ":" + programSelectedStartTimeSecond;
                    final String programEndTime = programSelectedEndDateYear + "-" + programSelectedEndDateMonth + "-" + programSelectedEndDateDay + " " + programSelectedEndTimeHour + ":" + programSelectedEndTimeMinute + ":" + programSelectedEndTimeSecond;

                    Call<String> call = api.insertProgram(programName,
                            programStartTime,
                            programEndTime,
                            programTemperature);

                    call.enqueue(new Callback<String>()
                    {
                        @Override
                        public void onResponse(@NonNull Call<String > call, @NonNull Response<String> response)
                        {
                            if(!response.isSuccessful())
                            {
                                Toast.makeText(requireActivity(), "response is not successful, code: " + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String responseString = response.body();

                            if(responseString != null)
                            {
                                programsList.add(new Program(programName, programStartTime, programEndTime, programTemperature));
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String > call, @NonNull Throwable t)
                        {
                            Toast.makeText(requireActivity(), "Fail: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    addNewProgramLayout.setVisibility(View.GONE);
                    clearHeatingProgramFields();
                }
                else Toast.makeText(requireActivity(), "Please complete all the fields", Toast.LENGTH_SHORT).show();
            }
        });
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
                    String text = String.valueOf(temperatureButton.getText());

                    text += currentTemperature.get(0).getVALUE() + " °C";

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
        programsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        programsRecyclerView.setAdapter(adapter);
    }

    private void setPrograms()
    {
        if(!programsList.isEmpty())
            programsList.clear();

        Call<ArrayList<Program>> call = api.getAllPrograms();

        call.enqueue(new Callback<ArrayList<Program>>()
        {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Program>> call, @NonNull Response<ArrayList<Program>> response)
            {
                if(!response.isSuccessful())
                {
                    Toast.makeText(requireActivity(), "response is not successful, code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Program> listOfProgramsFromCloud = response.body();

                if(listOfProgramsFromCloud != null)
                {
                    programsList.addAll(listOfProgramsFromCloud);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Program>> call, @NonNull Throwable t)
            {
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearHeatingProgramFields()
    {
        nameField.setText("");
        temperatureField.setText("");
    }
}