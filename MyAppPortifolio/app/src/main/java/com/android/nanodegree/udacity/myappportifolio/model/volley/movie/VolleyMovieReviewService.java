package com.android.nanodegree.udacity.myappportifolio.model.volley.movie;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Review;
import com.android.nanodegree.udacity.myappportifolio.model.volley.VolleyRequest;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MovieReviewPresenter;
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

public class VolleyMovieReviewService {

    private Context context;
    private JSONObject movieJsonObject;
    private List<Movie> movies;
    private String moviesId;
    private RequestReviewsListner requestReviewsListner;
    private MovieReviewPresenter.IResult resultListner;


    public VolleyMovieReviewService(Context context, MovieReviewPresenter.IResult resultListner, String moviesId) {
        this.context = context;
        this.resultListner = resultListner;
        this.moviesId = moviesId;
    }

    public void obtainReviewsMoviesVolley() {
        Uri uri = buildReviewsdUri(moviesId);

        requestReviewsListner = new RequestReviewsListner();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                requestReviewsListner, requestReviewsListner) {
        };
        // add it to the RequestQueue
        VolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    private Uri buildReviewsdUri(String movieId) {
        // Construindo uma URI
        return Uri.parse(Constants.POPULAR_MOVIE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(Constants.REVIEWS)
                .appendQueryParameter(Constants.API_KEY, Constants.KEY_MOVIE_DB)
                .appendQueryParameter(Constants.LANGUAGE, Constants.LANGUAGE_VALUE)
                .appendQueryParameter(PAGE, Constants.PAGE_VALUE).build();
    }

    private List<Review> obtainReviewsDataFromJson(JSONObject reviewsMovieJsonObject) throws JSONException {
        if (reviewsMovieJsonObject != null) {
            Gson gson = new Gson();
            JSONArray reviewsArray = reviewsMovieJsonObject.getJSONArray("results");
            Review[] reviews = gson.fromJson(reviewsArray.toString(), Review[].class);
            return Arrays.asList(reviews);
        }
        return null;
    }

    class RequestReviewsListner implements Response.Listener<JSONObject>, Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("Error.Response", error.toString());
            resultListner.notifyError(error);
        }

        @Override
        public void onResponse(JSONObject response) {
            Log.d("Response", response.toString());
            try {
                resultListner.notifySuccess(obtainReviewsDataFromJson(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
