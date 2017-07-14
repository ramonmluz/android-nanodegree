package com.android.nanodegree.udacity.myappportifolio.presenter.movie;

import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Review;
import com.android.volley.VolleyError;

import java.util.List;


/**
 * Created by ramon on 08/06/17.
 */

public interface MovieReviewPresenter {

    void obtainReviewsMoviesVolley();

    public interface IResult {
        public void notifySuccess(List<Review> reviews);
        public void notifyError(VolleyError error);
    }

}
