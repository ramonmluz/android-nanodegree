package com.android.nanodegree.udacity.myappportifolio.model.volley.movie;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Trailer;
import com.android.nanodegree.udacity.myappportifolio.model.volley.VolleyRequest;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MovieTrailerPresenter;
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
 * Created by ramon on 08/06/17.
 */

public class VolleyMovieTrailerService {

    private Context context;
    private JSONObject movieJsonObject;
    private List<Movie> movies;
    private String moviesId;
    private RequestTrailersListner requestTrailersListner;
    private MovieTrailerPresenter.IResult resultListner;


    public VolleyMovieTrailerService(Context context, MovieTrailerPresenter.IResult resultListner, String moviesId) {
        this.context = context;
        this.resultListner = resultListner;
        this.moviesId = moviesId;
    }


    public void obtainTrailersMoviesVolley() {
        Uri uri = buildTrailerdUri(moviesId);

        requestTrailersListner = new RequestTrailersListner();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                requestTrailersListner, requestTrailersListner) {
        };

        // add it to the RequestQueue
        VolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    //Youtube Trailers - https://api.themoviedb.org/3/movie/321612/videos?api_key=c4852d11798d35ebae996afb362875d4&language=en-US
    private Uri buildTrailerdUri(String movieId) {
        // Construindo uma URI
        return Uri.parse(Constants.POPULAR_MOVIE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(Constants.VIDEOS)
                .appendQueryParameter(Constants.API_KEY, Constants.KEY_MOVIE_DB)
                .appendQueryParameter(Constants.LANGUAGE, Constants.LANGUAGE_VALUE)
                .appendQueryParameter(PAGE, Constants.PAGE_VALUE).build();
    }

    private List <Trailer> obtainTrailersDataFromJson(JSONObject trailerMovieJsonObject) throws JSONException {
        if (trailerMovieJsonObject != null) {
            Gson gson = new Gson();
            JSONArray trailerArray = trailerMovieJsonObject.getJSONArray("results");
            Trailer [] trailers = gson.fromJson(trailerArray.toString(),Trailer[].class);
           return Arrays.asList(trailers);
        }
        return null;
    }

    class RequestTrailersListner implements Response.Listener<JSONObject>, Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("Error.Response", error.toString());
            resultListner.notifyError(error);
        }

        @Override
        public void onResponse(JSONObject response) {
            Log.d("Response", response.toString());
            try {
                resultListner.notifySuccess(obtainTrailersDataFromJson(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
