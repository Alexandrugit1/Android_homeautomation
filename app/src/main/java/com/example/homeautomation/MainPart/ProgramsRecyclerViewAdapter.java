package com.example.homeautomation.MainPart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeautomation.Program;
import com.example.homeautomation.R;

import java.util.ArrayList;

public class ProgramsRecyclerViewAdapter extends RecyclerView.Adapter<ProgramsRecyclerViewAdapter.ProgramViewHolder>
{
    private ArrayList<Program> programsList;
    private Context context;

    public ProgramsRecyclerViewAdapter(ArrayList<Program> programsList, Context context)
    {
        this.programsList = programsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgramsRecyclerViewAdapter.ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.program_layout, parent, false);

        return new ProgramViewHolder(view, programsList);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramsRecyclerViewAdapter.ProgramViewHolder holder, int position)
    {
        Program program = programsList.get(position);
        String buttonText = program.getACTIVE().equals("0") ? "Start" : "Stop";
        int buttonBackground = program.getACTIVE().equals("0") ? R.drawable.green_button : R.drawable.red_button;

        holder.programName.setText(program.getNAME());
        holder.programButton.setText(buttonText);
        holder.programButton.setBackgroundResource(buttonBackground);
    }

    @Override
    public int getItemCount()
    {
        return programsList.size();
    }

    public static class ProgramViewHolder extends RecyclerView.ViewHolder
    {
        private TextView programName;
        private Button programButton;

        public ProgramViewHolder(@NonNull View itemView, final ArrayList<Program> programsList)
        {
            super(itemView);

            programName = itemView.findViewById(R.id.program_layout_program_name);
            programButton = itemView.findViewById(R.id.program_layout_program_button);

            programButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Program program = programsList.get(getAdapterPosition());
                    String buttonText = "";
                    String buttonActive = "";
                    int background = -1;

                    if(programButton.getText().equals("Start"))
                    {
                        buttonText = "Stop";
                        buttonActive = "1";
                        background = R.drawable.red_button;
                    }
                    else if(programButton.getText().equals("Stop"))
                    {
                        buttonText = "Start";
                        buttonActive = "0";
                        background = R.drawable.green_button;
                    }

                    programButton.setText(buttonText);
                    program.setACTIVE(buttonActive);

                    if(background != -1)
                        programButton.setBackgroundResource(background);

                    programsList.set(getAdapterPosition(), program);

                    //Toast.makeText(context, program.getNAME(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
