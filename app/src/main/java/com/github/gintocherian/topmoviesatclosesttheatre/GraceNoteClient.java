/**
 * Created by Ginto Cherian on 27/01/2015.

 * Client to connect to the GraceNote API
 *
 * The API call documentation is available at http://developer.tmsapi.com/docs/data_v1/movies/Movie_showtimes_by_zip_code
 */
package com.github.gintocherian.topmoviesatclosesttheatre;

import android.location.Location;

import 	java.util.Calendar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class GraceNoteClient {
    private final String API_KEY = "74zktfhu6mfgrwpwvb4v3ryr";
    private final String API_BASE_URL = "http://data.tmsapi.com/v1/";
    private AsyncHttpClient client;

    public GraceNoteClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    //http://data.tmsapi.com/v1/movies/showings?startDate=<date>&lat=<lat>&lng=<lng>&api_key=<key>
    public void getLocalMovies(double latitude, double longitude, JsonHttpResponseHandler handler) {
        String url = getApiUrl("movies/showings");
        RequestParams params = new RequestParams();
        params.put("api_key", API_KEY);
        params.put("startDate", getStartDate());
        params.put("lat", Double.toString(latitude));
        params.put("lng", Double.toString(longitude));
        client.get(url, params, handler);
    }

    /*
    startDate	Yes	Start date, formatted yyyy-mm-dd.
    Current date or greater.
     */
    private String getStartDate(){
        Calendar c = Calendar.getInstance();
        return String.format("%1$tY-%1$tm-%1$td", c);
    }
}