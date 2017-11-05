package com.android.nanodegree.udacity.myappportifolio.presenterImpl.movie;

import android.content.Context;

import com.android.nanodegree.udacity.myappportifolio.fragment.MovieFragmentView;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.model.volley.movie.VolleyMovieService;
import com.android.nanodegree.udacity.myappportifolio.model.volley.movie.VolleyMovieTrailerService;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MoviePresenter;
import com.android.volley.VolleyError;

import java.util.List;

/**
 * Created by ramon on 05/11/17.
 */
public class MoviePresenterImpl implements MoviePresenter {

    private MovieFragmentView mView;
    private Context context;
    private VolleyMovieService volleyMovieTrailerService;
    private IResult resultLisnter;

    public class MovieResulListner implements IResult {

        @Override
        public void notifySuccess(List<Movie> movies) {
            mView.fillMovieRecyclerView(movies);
        }

        @Override
        public void notifyError(VolleyError error) {
            mView.notifyError(error);
        }
    }

    public MoviePresenterImpl(MovieFragmentView view, Context context, String sortMovie) {
        this.mView = view;
        this.context = context;
        this.volleyMovieTrailerService = new VolleyMovieService(this.context, new MovieResulListner(), sortMovie);
    }

    @Override
    public void obtainMoviesVolley() {
        volleyMovieTrailerService.obtainMoviesVolley();
    }

}
