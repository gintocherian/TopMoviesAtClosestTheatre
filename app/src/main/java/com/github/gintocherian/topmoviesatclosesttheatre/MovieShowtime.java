package com.github.gintocherian.topmoviesatclosesttheatre;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gcherian on 2015/01/26.
 * Note: that IMAX or other listings like 3D will be shown as additional
 * theatre listing.
 */
public class MovieShowtime {
    private String tmsId;
    private int rootId;
    private String title;
    private Date theatreReleaseDate;
    private ArrayList<TheatreShowtime> showtime;

    public String getTmsId() {
        return tmsId;
    }

    public int getRootId() {
        return rootId;
    }

    public String getTitle() {
        return title;
    }

    public Date getTheatreReleaseDate() {
        return theatreReleaseDate;
    }

    public ArrayList<TheatreShowtime> getShowtime() {
        return showtime;
    }

    //In cases different showings like IMAX are listed as separate movies
    public void addDuplicate(MovieShowtime anotherMovie) {
        showtime.addAll(anotherMovie.getShowtime());
    }

    // Returns a BoxOfficeMovie given the expected JSON
    // BoxOfficeMovie.fromJson(movieJsonDictionary)
    // Stores the `title`, `year`, `synopsis`, `poster` and `criticsScore`
    public static MovieShowtime fromJson(JSONObject jsonObject, String requiredTitle) {
        MovieShowtime b = new MovieShowtime();
        try {
            // Deserialize json into object fields
            b.tmsId = jsonObject.getString("tmsId");
            b.rootId = jsonObject.getInt("rootId");
            b.title = jsonObject.getString("title");

            if(!b.title.contains(requiredTitle))
                return null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            b.theatreReleaseDate = sdf.parse(jsonObject.getString("releaseDate"));
            b.showtime = TheatreShowtime.fromJson(jsonObject.getJSONArray("showtimes"));
        } catch (JSONException e) {
            Log.e("BoxOfficeActivity", e.toString());
            return null;
        }
        catch (ParseException e) {
            Log.e("BoxOfficeActivity", e.toString());
            return null;
        }
        // Return new object
        return b;
    }

    // Decodes array of local movie listings json results into business model objects
    // MovieShowtime.fromJson(jsonArrayOfMovieListings)
    // Merges together all different movie formats for same movie
    public static ArrayList<MovieShowtime> fromJson(JSONArray jsonArray, String requiredTitle) {
        ArrayList<MovieShowtime> businesses = new ArrayList<MovieShowtime>();
        ArrayList<Integer> rootIds = new ArrayList<Integer>();
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject businessJson = null;
            try {
                businessJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            MovieShowtime business = MovieShowtime.fromJson(businessJson, requiredTitle);
            if (business != null) {
                if(!rootIds.contains(business.getRootId())){
                    //If new movie add
                    businesses.add(business);
                    rootIds.add(business.getRootId());
                }
                else
                {
                    //If existing movie merge showtimes
                    int index = rootIds.indexOf(business.getRootId());
                    MovieShowtime atIndex = businesses.get(index);
                    atIndex.addDuplicate(business);
                    businesses.set(index, atIndex);
                }
            }
        }

        return businesses;
    }

}
