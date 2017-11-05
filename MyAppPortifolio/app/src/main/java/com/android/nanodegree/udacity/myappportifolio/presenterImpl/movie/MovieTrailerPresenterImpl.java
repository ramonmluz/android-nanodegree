package com.android.nanodegree.udacity.myappportifolio.presenterImpl.movie;

import android.content.Context;

import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Trailer;
import com.android.nanodegree.udacity.myappportifolio.model.volley.movie.VolleyMovieTrailerService;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MovieTrailerPresenter;
import com.android.nanodegree.udacity.myappportifolio.view.movie.MovieDatailView;
import com.android.volley.VolleyError;

import java.util.List;


public class MovieTrailerPresenterImpl implements MovieTrailerPresenter {

    private MovieDatailView mView;
    private Context context;
    private VolleyMovieTrailerService volleyMovieTrailerService;
    private IResult resultLisnter;

    public class ResulTrailersListner implements IResult {

        @Override
        public void notifySuccess(List<Trailer> trailers) {
            mView.fillTrailersRecyclerView(trailers);
        }

        @Override
        public void notifyError(VolleyError error) {
            mView.notifyError(error);
        }
    }

    public MovieTrailerPresenterImpl(MovieDatailView view, Context context, String movieId) {
        this.mView = view;
        this.context = context;
        this.volleyMovieTrailerService = new VolleyMovieTrailerService(this.context, new ResulTrailersListner(), movieId);
    }

    public void obtainTrailersMoviesVolley() {
        volleyMovieTrailerService.obtainTrailersMoviesVolley();
    }

}
