package com.example.homeautomation.MainPart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeautomation.Event;
import com.example.homeautomation.R;

import java.util.ArrayList;

public class ReportsRecyclerViewAdapter extends RecyclerView.Adapter<ReportsRecyclerViewAdapter.EventViewHolder>
{
    private ArrayList<Event> eventsList;
    private Context context;

    public ReportsRecyclerViewAdapter(ArrayList<Event> eventsList, Context context)
    {
        this.eventsList = eventsList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.event_layout, parent, false);

        return new ReportsRecyclerViewAdapter.EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position)
    {
        Event event = eventsList.get(position);

        holder.eventName.setText(event.getName());
        holder.eventValue.setText(event.getValue());
        holder.eventTime.setText(event.getTime());
    }

    @Override
    public int getItemCount()
    {
        return eventsList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        private TextView eventName;
        private TextView eventValue;
        private TextView eventTime;

        public EventViewHolder(@NonNull View itemView)
        {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name_layout_name);
            eventValue = itemView.findViewById(R.id.event_value);
            eventTime = itemView.findViewById(R.id.event_time);
        }
    }
}
