package com.android.nanodegree.udacity.myappportifolio.presenter.movie;

import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Trailer;
import com.android.volley.VolleyError;

import java.util.List;


/**
 * Created by ramon on 08/06/17.
 */

public interface MovieTrailerPresenter {
    void obtainTrailersMoviesVolley();


    public interface IResult {
        public void notifySuccess(List<Trailer> trailers);
        public void notifyError(VolleyError error);
    }


}
