package com.example.homeautomation.MainPart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeautomation.Event;
import com.example.homeautomation.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class FragmentReports extends Fragment
{
    private RecyclerView reportsRecyclerView;
    private ArrayList<Event> eventsList;
    private ReportsRecyclerViewAdapter adapter;
    private GraphView graphView;

    public FragmentReports()
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
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        setVariables(view);
        setTemperatureGraph();
        setRecyclerView();

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        setEvents();
    }

    private void setVariables(View v)
    {
        graphView = v.findViewById(R.id.reports_graph);
        reportsRecyclerView = v.findViewById(R.id.reports_recyclerview);
        eventsList = new ArrayList<>();
        adapter = new ReportsRecyclerViewAdapter(eventsList, requireContext());
    }

    private void setRecyclerView()
    {
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        reportsRecyclerView.setAdapter(adapter);
    }

    private void setEvents()
    {
        if(!eventsList.isEmpty())
            eventsList.clear();

        eventsList.add(new Event(1, "House entry", "Vlad", "12:43"));
        eventsList.add(new Event(2, "Temperature measurement", "24.3°C", "12:45"));
        eventsList.add(new Event(3, "Temperature measurement", "24.7°C", "13:05"));
        eventsList.add(new Event(4, "House entry", "Alex", "13:20"));
        eventsList.add(new Event(5, "House leave", "Vlad", "14:05"));

        adapter.notifyDataSetChanged();
    }

    private void setTemperatureGraph()
    {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(12.00, 23.7),
                new DataPoint(12.25, 23.8),
                new DataPoint(12.5, 24.1),
                new DataPoint(12.75, 24.3),
                new DataPoint(13.00, 24.6)
        });

        graphView.addSeries(series);
    }
}