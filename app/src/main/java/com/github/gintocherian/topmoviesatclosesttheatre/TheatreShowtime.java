/**
 * Created by Ginto Cherian on 27/01/2015.
 *
 * Partial encapsulation of the response for the API call defined at http://developer.tmsapi.com/docs/data_v1/movies/Movie_showtimes_by_zip_code
 * This only takes care of the theatre listings
 */
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

    // Returns a TheatreShowtime given the expected JSON
    // TheatreShowtime.fromJson(theatreShowtimeJsonDictionary)
    // Stores the `theatreId`, `country`, `theatreName`, `showtimes` and `quals`
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
            Log.e("TheatreShowtime", e.toString());
            return null;
        }
        catch (ParseException e) {
            Log.e("TheatreShowtime", e.toString());
            return null;
        }
        // Return new object
        return b;
    }

    // Decodes array of TheatreShowtime json results into business model objects
    // TheatreShowtime.fromJson(jsonArrayOfShowtimes)
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