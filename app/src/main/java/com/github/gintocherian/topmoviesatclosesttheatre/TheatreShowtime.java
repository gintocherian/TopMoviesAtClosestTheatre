package com.github.gintocherian.topmoviesatclosesttheatre;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gcherian on 2015/01/26.
 */
public class TheatreShowtime {
    private int theatreId;
    private String country;
    private String theatreName;
    private ArrayList<Date> showtimes;
    private String quals;

    public int getTheatreId() {
        return theatreId;
    }

    public String getCountry() {
        return country;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public ArrayList<Date> getShowtimes() {
        return showtimes;
    }

    public String getSingleStringShowtimes(){
        StringBuilder builder = new StringBuilder();
        for(Date showtime: showtimes){
            DateFormat df = new SimpleDateFormat("HH:mm >> ");
            builder.append(df.format(showtime));
        }
        return builder.toString();
    }

    public void addShowtime(Date showtime){
        showtimes.add(showtime);
    }

    public String getQuals() {
        return quals;
    }

    // Returns a BoxOfficeMovie given the expected JSON
    // BoxOfficeMovie.fromJson(movieJsonDictionary)
    // Stores the `title`, `year`, `synopsis`, `poster` and `criticsScore`
    public static TheatreShowtime fromJson(JSONObject jsonObject) {
        TheatreShowtime b = new TheatreShowtime();
        try {
            // Deserialize json into object fields
            b.theatreId = jsonObject.getJSONObject("theatre").getInt("id");
            b.country = jsonObject.getJSONObject("theatre").getJSONObject("address").getString("country");
            b.theatreName = jsonObject.getJSONObject("theatre").getString("name");
            b.showtimes = new ArrayList<Date>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            b.showtimes.add(sdf.parse(jsonObject.getString("dateTime")));

            //For IMAX movies Quals is not populated
            try {
            b.quals = jsonObject.getString("quals");
            } catch (JSONException e) {
                b.quals = "IMAX";
            }

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

    // Decodes array of box office movie json results into business model objects
    // BoxOfficeMovie.fromJson(jsonArrayOfMovies)
    public static ArrayList<TheatreShowtime> fromJson(JSONArray jsonArray) {
        ArrayList<TheatreShowtime> businesses = new ArrayList<TheatreShowtime>();
        ArrayList<Integer> theatreIds = new ArrayList<Integer>();
        // Process each result in json array, decode, check if already present and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject businessJson = null;
            try {
                businessJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            TheatreShowtime business = TheatreShowtime.fromJson(businessJson);
            if (business != null) {
                if(!theatreIds.contains(business.getTheatreId())){
                    //If new theatre add
                    businesses.add(business);
                    theatreIds.add(business.getTheatreId());
                }
                else
                {
                    //If existing theatre add to showtime list
                    int index = theatreIds.indexOf(business.getTheatreId());
                    TheatreShowtime atIndex = businesses.get(index);
                    atIndex.addShowtime(business.getShowtimes().get(0));
                    businesses.set(index, atIndex);
                }
            }
        }

        return businesses;
    }

}
