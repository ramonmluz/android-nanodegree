package com.android.nanodegree.udacity.myappportifolio.fragment;


import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nanodegree.udacity.myappportifolio.R;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.adapter.movie.MovieRecyclerViewAdapter;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MoviePresenter;
import com.android.nanodegree.udacity.myappportifolio.presenterImpl.movie.MoviePresenterImpl;
import com.android.nanodegree.udacity.myappportifolio.task.CursorQueryMovieTask;
import com.android.nanodegree.udacity.myappportifolio.util.Constants;
import com.android.nanodegree.udacity.myappportifolio.util.Util;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements MovieFragmentView {

    private RecyclerView moviesRecyclerView;

    private MoviePresenter moviePresenter;

    private Cursor mData;

    private Parcelable mList;

    private List<Movie> movies;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Carrega o RecycleView
        moviesRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_movie, container, false);

        int orientation;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = 3;
        } else {
            orientation = 2;
        }

        // Instancia o GridLayoutManeger informado que terá duas colunas
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(moviesRecyclerView.getContext(), orientation));
        if (savedInstanceState != null) {
            onViewStateRestored(savedInstanceState);
        } else {
            // Neste primeiro momento, o adapter será  prereenchido com a lista vazia
            moviesRecyclerView.setAdapter(new MovieRecyclerViewAdapter(new ArrayList<Movie>()));
            updateMovies();
        }

        return moviesRecyclerView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

       String sortType = Util.getSharedPreferences(getActivity());

        if (sortType.equals(getString(R.string.favorite_movies_value))) {
            movies = ((MovieRecyclerViewAdapter) moviesRecyclerView.getAdapter()).getMovies();
        }

        Parcelable listStatePosition = moviesRecyclerView.getLayoutManager().onSaveInstanceState();
        // Seta a posição
        outState.putParcelable("SAVED_RECYCLER_VIEW_ID", listStatePosition);

        // Seta os itens
        outState.putParcelableArrayList("SAVED_RECYCLER_VIEW_DATASET_ID", new ArrayList<Movie>(movies));

        super.onSaveInstanceState(outState);

    }

   /* onViewStateRestored of Fragment is the equivalent of
    onRestoreInstanceState of Activity. But it is called after onActivityCreated(Bundle) and before onStart().*/

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        List<Movie> movies;
        if (savedInstanceState != null) {
            Parcelable listStatePosition = savedInstanceState.getParcelable("SAVED_RECYCLER_VIEW_ID");
            movies = savedInstanceState.getParcelableArrayList("SAVED_RECYCLER_VIEW_DATASET_ID");
            moviesRecyclerView.setAdapter(new MovieRecyclerViewAdapter(movies));
            moviesRecyclerView.getLayoutManager().onRestoreInstanceState(listStatePosition);
            this.movies = movies;
        }
        super.onViewStateRestored(savedInstanceState);
    }

    private void updateMovies() {
        String sortType = Util.getSharedPreferences(getActivity());

        if (sortType.equals(getString(R.string.favorite_movies_value))) {
            // Caso a classificação selecionada seja a de filmes favoritos chama-se o CursorQueryMovieTask
            new CursorQueryMovieTask(moviesRecyclerView, getActivity(),movies).execute();

        } else {

            // Instancia o presenter de acordo com a classificação escolhida
            moviePresenter = new MoviePresenterImpl(this, getActivity(), sortType);

            //busca os filmes
            moviePresenter.obtainMoviesVolley();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void fillMovieRecyclerView(List<Movie> movies) {
        this.movies = movies;
        moviesRecyclerView.setAdapter(new MovieRecyclerViewAdapter(movies));

    }

    @Override
    public void notifyError(VolleyError error) {
        Log.e(Constants.TAG_MOVIE_FRAGMENT, error.toString());
        moviesRecyclerView.setAdapter(new MovieRecyclerViewAdapter(new ArrayList<Movie>()));
        Util.sendMessageDilog("Alert", "Is necessary create a key in Site: https://api.themoviedb.org", getActivity());

    }
}
