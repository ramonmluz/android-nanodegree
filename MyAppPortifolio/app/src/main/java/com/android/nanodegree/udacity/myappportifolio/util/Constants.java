package com.android.nanodegree.udacity.myappportifolio.util;

import com.android.nanodegree.udacity.myappportifolio.adapter.movie.TrailerRecyclerViewAdapter;
import com.android.nanodegree.udacity.myappportifolio.fragment.MovieFragment;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.model.volley.movie.VolleyMovieService;

/**
 * Created by ramon on 05/06/17.
 */

public class Constants {

    // Parâmetros
    public static final String POPULAR_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w185";

    public static final String VIDEOS = "videos";
    public static final String REVIEWS = "reviews";


    public static final String API_KEY = "api_key";
    public static final String LANGUAGE = "language";
    public static final String PAGE = "page";
    public static final String MOVIE = "movie";

    // Valores
    public static String KEY_MOVIE_DB = "";
    public static String LANGUAGE_VALUE = "en-US";
    public static String PAGE_VALUE = "1";

    // TAGS
    public static String TAG_MOVIE_DATAIL = "Movie Datail";
    public static String TAG_MOVIE = Movie.class.getSimpleName();
    public static String TAG_VOLLEY_MOVIE_SERVICE = VolleyMovieService.class.getSimpleName();
    public static String TAG_MOVIE_FRAGMENT = MovieFragment.class.getSimpleName();
    public static String TAG_TRAILER_RECYCLER_VIEW = TrailerRecyclerViewAdapter.class.getSimpleName();



}
