package com.android.nanodegree.udacity.myappportifolio.model.volley.movie;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Trailer;
import com.android.nanodegree.udacity.myappportifolio.model.volley.VolleyRequest;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MoviePresenter;
import com.android.nanodegree.udacity.myappportifolio.util.Constants;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.android.nanodegree.udacity.myappportifolio.util.Constants.PAGE;


/**
 * Created by ramon on 05/11/17.
 */

public class VolleyMovieService {

    private Context context;
    private JSONObject movieJsonObject;
    private List<Movie> movies;
    private String sortMovie;
    private RequestMoviesListner requestMoviesListner;
    private MoviePresenter.IResult resultListner;


    public VolleyMovieService(Context context, MoviePresenter.IResult resultListner, String sortMovie) {
        this.context = context;
        this.resultListner = resultListner;
        this.sortMovie = sortMovie;
    }


    public void obtainMoviesVolley() {
        Uri uri = buildMoviesdUri(sortMovie);

        requestMoviesListner = new RequestMoviesListner();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                requestMoviesListner, requestMoviesListner) {
        };

        // add it to the RequestQueue
        VolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    private Uri buildMoviesdUri(String sortMovies) {
        // Construindo uma URI
        return Uri.parse(Constants.POPULAR_MOVIE_URL).buildUpon()
                .appendPath(sortMovies)
                .appendQueryParameter(Constants.API_KEY, Constants.KEY_MOVIE_DB)
                .appendQueryParameter(Constants.LANGUAGE, Constants.LANGUAGE_VALUE)
                .appendQueryParameter(PAGE, Constants.PAGE_VALUE).build();
    }


    private List <Movie> obtainMoviesDataFromJson(JSONObject movieJsonObject) throws JSONException {
        if (movieJsonObject != null) {
            Gson gson = new Gson();
            JSONArray movieArray = movieJsonObject.getJSONArray("results");
            Movie [] movies = gson.fromJson(movieArray.toString(),Movie[].class);
           return Arrays.asList(movies);
        }
        return null;
    }

    class RequestMoviesListner implements Response.Listener<JSONObject>, Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("Error.Response", error.toString());
            resultListner.notifyError(error);
        }

        @Override
        public void onResponse(JSONObject response) {
            Log.d("Response", response.toString());
            try {
                resultListner.notifySuccess(obtainMoviesDataFromJson(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
