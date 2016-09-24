package com.android.nanodegree.udacity.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        String[] data = {      "Today - Sunny - 83/63 ",
                               "Tomorrow - Foggy -70/46",
                               "Weds - Cloudy - 72/46 ",
                               "Thurs - Rainy - 64/5'",
                                "Fri - Foggy - 70/46",
                                "Sat - Sunny - 76/46"};

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);

        ListView listView  = (ListView) rootView.findViewById(R.id.listview_forecast);

        listView.setAdapter(listAdapter);

        return rootView;
    }
}
