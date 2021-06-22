package com.example.homeautomation.MainPart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeautomation.Device;
import com.example.homeautomation.GlobalVariables;
import com.example.homeautomation.JsonPlaceHolderAPI;
import com.example.homeautomation.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LightsRecyclerViewAdapter extends RecyclerView.Adapter<LightsRecyclerViewAdapter.LightViewHolder>
{
    private ArrayList<Device> programsList;
    private Context context;
    private Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GlobalVariables.getBaseURL())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private JsonPlaceHolderAPI api = retrofit.create(JsonPlaceHolderAPI.class);

    public LightsRecyclerViewAdapter(ArrayList<Device> programsList, Context context)
    {
        this.programsList = programsList;
        this.context = context;
    }

    @NonNull
    @Override
    public LightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.light_program_layout, parent, false);

        return new LightsRecyclerViewAdapter.LightViewHolder(view, api, programsList, context);
    }

    @Override
    public void onBindViewHolder(@NonNull LightViewHolder holder, int position)
    {
        Device lightProgram = programsList.get(position);
        boolean state = lightProgram.getVALOARE_CURENTA().equals("1");

        holder.programName.setText(lightProgram.getNAME());
        holder.programSwitch.setChecked(state);
    }

    @Override
    public int getItemCount()
    {
        return programsList.size();
    }

    public static class LightViewHolder extends RecyclerView.ViewHolder
    {
        private TextView programName;
        private Switch programSwitch;

        public LightViewHolder(@NonNull View itemView, final JsonPlaceHolderAPI api, final ArrayList<Device> devicesList, final Context context)
        {
            super(itemView);

            programName = itemView.findViewById(R.id.light_program_text);
            programSwitch = itemView.findViewById(R.id.light_program_switch);

            programSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    String value = isChecked ? "1" : "0";
                    Device lightProgram = devicesList.get(getAdapterPosition());

                    Call<String> setDeviceValueCall = api.setDeviceValue(lightProgram.getDEVICE_ID(), value);

                    setDeviceValueCall.enqueue(new Callback<String>()
                    {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> setDeviceValueResponse)
                        {
                            if(!setDeviceValueResponse.isSuccessful())
                                Toast.makeText(context, "response is not successful, code: " + setDeviceValueResponse.code(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t)
                        {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
