package com.github.gintocherian.topmoviesatclosesttheatre;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;


public class MovieShowtimeActivity extends Activity {
    private GraceNoteClient graceNoteClient;
    private BoxOfficeMovie movie;
    private ListView lvTheatreShowtime;
    private TheaterShowtimeAdapter adapterTheatreShowtime;
    private ImageView ivPosterImage;
    private TextView movieTitle;
    private TextView movieDate;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_showtimes);

        lvTheatreShowtime = (ListView) findViewById(R.id.lvMovieShowtimes);
        ArrayList<TheatreShowtime> aMovies = new ArrayList<TheatreShowtime>();
        adapterTheatreShowtime = new TheaterShowtimeAdapter(this, aMovies);
        lvTheatreShowtime.setAdapter(adapterTheatreShowtime);

        ivPosterImage = (ImageView) findViewById(R.id.ivPosterImage);
        movieTitle = (TextView) findViewById(R.id.movieTitle);
        movieDate = (TextView) findViewById(R.id.movieDate);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        // Load movie data
        movie = (BoxOfficeMovie) getIntent().getSerializableExtra(BoxOfficeActivity.MOVIE_DETAIL_KEY);

        movieTitle.setText(movie.getTitle());
        movieDate.setText(" : " + getCurrentDate());
        Picasso.with(this).load(movie.getLargePosterUrl()).
                placeholder(R.drawable.large_movie_poster).
                into(ivPosterImage);
        spinner.setVisibility(View.VISIBLE);

        // Get the showtime
        fetchMovieListings(movie.getTitle());
    }

    // Executes an API call to the local movie listings, parses the results
    // Converts them into an array of movie listing objects and adds them to the adapter
    private void fetchMovieListings(final String requiredTitle) {
        Location myLocation = getLastKnownLocation();

        graceNoteClient = new GraceNoteClient();
        graceNoteClient.getLocalMovies(myLocation.getLatitude(), myLocation.getLongitude(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    ArrayList<MovieShowtime> movieShowtimes = MovieShowtime.fromJson(response, requiredTitle);
                    // Load model objects into the adapter
                    for (TheatreShowtime theatreShowtime : movieShowtimes.get(0).getShowtime()) {
                        adapterTheatreShowtime.add(theatreShowtime);
                    }
                    spinner.setVisibility(View.GONE);
                } catch (Exception e) {
                    Log.e("BoxOfficeActivity", e.toString());
                }
            }
        });
    }

    //Getting the last known location
    private Location getLastKnownLocation()
    {
        Criteria cri= new Criteria();
        LocationManager locationmanager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        String bbb = locationmanager.getBestProvider(cri, true);
        return locationmanager.getLastKnownLocation(bbb);
    }

    /*
    Current Date formatted yyyy-mm-dd.
    */
    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        return String.format("%1$tY-%1$tm-%1$td", c);
    }


}