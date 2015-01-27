/**
 * Created by Ginto Cherian on 27/01/2015.
 *
 * Client to connect to the RottenTomatoes API
 *
 * The API call documentation is available at http://developer.rottentomatoes.com/docs/read/json/v10/Box_Office_Movies
 */

package com.github.gintocherian.topmoviesatclosesttheatre;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RottenTomatoesClient {
    private final String API_KEY = "fzawfw3pdqywdbhxdyn6m3ks";
    private final String API_BASE_URL = "http://api.rottentomatoes.com/api/public/v1.0/";
    private AsyncHttpClient client;

    public RottenTomatoesClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?apikey=<key>
    public void getBoxOfficeMovies(JsonHttpResponseHandler handler) {
        String url = getApiUrl("lists/movies/box_office.json");
        RequestParams params = new RequestParams("apikey", API_KEY);
        client.get(url, params, handler);
    }
}
