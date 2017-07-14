package com.android.nanodegree.udacity.myappportifolio.presenterImpl.movie;

import android.content.Context;

import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Review;
import com.android.nanodegree.udacity.myappportifolio.model.volley.movie.VolleyMovieReviewService;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MovieReviewPresenter;
import com.android.nanodegree.udacity.myappportifolio.view.movie.MovieView;
import com.android.volley.VolleyError;

import java.util.List;


public class MovieReviewPresenterImpl implements MovieReviewPresenter {

    private MovieView mView;
    private Context context;
    private VolleyMovieReviewService volleyMovieReviewService;
    private IResult resultLisnter;


    public class ResultReviewsListner implements IResult {

        @Override
        public void notifySuccess(List<Review> reviews) {
            mView.fillReviewsRecyclerView(reviews);
        }

        @Override
        public void notifyError(VolleyError error) {
            mView.notifyError(error);
        }
    }

    public MovieReviewPresenterImpl(MovieView view, Context context, String movieId) {
        this.mView = view;
        this.context = context;
        this.volleyMovieReviewService = new VolleyMovieReviewService(this.context, new ResultReviewsListner(), movieId);
    }


    @Override
    public void obtainReviewsMoviesVolley() {
        volleyMovieReviewService.obtainReviewsMoviesVolley();
    }

}
