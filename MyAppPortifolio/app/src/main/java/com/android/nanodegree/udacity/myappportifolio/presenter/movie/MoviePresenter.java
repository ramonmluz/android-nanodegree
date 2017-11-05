package com.android.nanodegree.udacity.myappportifolio.presenter.movie;

import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Trailer;
import com.android.volley.VolleyError;

import java.util.List;

/**
 * Created by ramon on 05/11/17.
 */

public interface MoviePresenter {

    void obtainMoviesVolley();


    public interface IResult {
        public void notifySuccess(List<Movie> movies);
        public void notifyError(VolleyError error);
    }
}
