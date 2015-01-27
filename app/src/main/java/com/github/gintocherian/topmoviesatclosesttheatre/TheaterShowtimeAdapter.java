/**
 * Created by Ginto Cherian on 27/01/2015.
 *
 * The adapter used to load the TheatreShowtime List to the ListView
 */
package com.github.gintocherian.topmoviesatclosesttheatre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TheaterShowtimeAdapter extends ArrayAdapter<TheatreShowtime> {
    public TheaterShowtimeAdapter(Context context, ArrayList<TheatreShowtime> aShowtime) {
        super(context, 0, aShowtime);
    }

    // Translates a particular `TheatreShowtime` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TheatreShowtime showtime = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_theatre_showtime, parent, false);
        }

        // Lookup views within item layout
        TextView theaterName = (TextView) convertView.findViewById(R.id.theaterName);
        TextView screenType = (TextView) convertView.findViewById(R.id.screenType);
        TextView showTimes = (TextView) convertView.findViewById(R.id.showTimes);

        // Populate the data into the template view using the data object
        theaterName.setText(showtime.getTheatreName());
        screenType.setText(showtime.getQuals());
        showTimes.setText(showtime.getSingleStringShowtimes());

        // Return the completed view to render on screen
        return convertView;
    }
}
