package com.android.nanodegree.udacity.myappportifolio.fragment;

import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.volley.VolleyError;

import java.util.List;

/**
 * Created by ramon on 05/11/17.
 */

public interface MovieFragmentView {

    void fillMovieRecyclerView(List<Movie> movies);
    void notifyError(VolleyError error);
}
