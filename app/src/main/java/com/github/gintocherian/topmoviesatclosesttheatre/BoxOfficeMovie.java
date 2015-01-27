/**
 * Created by Ginto Cherian on 27/01/2015.
 *
 * Encapsulation of the response for the API call defined at at http://developer.tmsapi.com/docs/data_v1/movies/Movie_showtimes_by_zip_code
 */

package com.github.gintocherian.topmoviesatclosesttheatre;

import android.text.TextUtils;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BoxOfficeMovie  implements Serializable{
    private static final long serialVersionUID = -8959832007991513854L;
    private String title;
    private int year;
    private String synopsis;
    private String posterUrl;
    private String largePosterUrl;
    private int criticsScore;
    private Date theatreReleaseDate;
    private ArrayList<String> castList;

    public String getTitle(){
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getLargePosterUrl() {
        return largePosterUrl;
    }

    public int getCriticsScore() {
        return criticsScore;
    }

    public Date getTheatreReleaseDate() {
        return theatreReleaseDate;
    }

    public String getCastList() {
        return TextUtils.join(", ", castList);
    }

    // Returns a BoxOfficeMovie given the expected JSON
    // BoxOfficeMovie.fromJson(movieJsonDictionary)
    // Stores the `title`, `year`, `synopsis`, `poster` and `criticsScore`
    public static BoxOfficeMovie fromJson(JSONObject jsonObject) {
        BoxOfficeMovie b = new BoxOfficeMovie();
        try {
            // Deserialize json into object fields
            b.title = jsonObject.getString("title");
            b.year = jsonObject.getInt("year");
            b.synopsis = jsonObject.getString("synopsis");
            b.posterUrl = jsonObject.getJSONObject("posters").getString("thumbnail");
            b.largePosterUrl = jsonObject.getJSONObject("posters").getString("detailed");
            b.criticsScore = jsonObject.getJSONObject("ratings").getInt("critics_score");

            // Construct simple array of cast names
            b.castList = new ArrayList<String>();
            JSONArray abridgedCast = jsonObject.getJSONArray("abridged_cast");
            for (int i = 0; i < abridgedCast.length(); i++) {
                b.castList.add(abridgedCast.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            Log.e("BoxOfficeMovie", e.toString());
            return null;
        }
        // Return new object
        return b;
    }

    // Decodes array of box office movie json results into business model objects
    // BoxOfficeMovie.fromJson(jsonArrayOfMovies)
    public static ArrayList<BoxOfficeMovie> fromJson(JSONArray jsonArray) {
        ArrayList<BoxOfficeMovie> businesses = new ArrayList<BoxOfficeMovie>(jsonArray.length());
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

            BoxOfficeMovie business = BoxOfficeMovie.fromJson(businessJson);
            if (business != null) {
                businesses.add(business);
            }
        }

        return businesses;
    }
}
