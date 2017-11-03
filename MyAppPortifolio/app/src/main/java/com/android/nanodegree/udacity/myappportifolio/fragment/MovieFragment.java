package com.android.nanodegree.udacity.myappportifolio.fragment;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nanodegree.udacity.myappportifolio.R;
import com.android.nanodegree.udacity.myappportifolio.model.data.MovieContract;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.adapter.movie.MovieRecyclerViewAdapter;
import com.android.nanodegree.udacity.myappportifolio.task.CursorQueryMovieTask;
import com.android.nanodegree.udacity.myappportifolio.task.FecthMovieTask;
import com.android.nanodegree.udacity.myappportifolio.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private RecyclerView moviesRecyclerView;

    private Cursor mData;

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
        // Instancia o GridLayoutManeger informado que terá duas colunas
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(moviesRecyclerView.getContext(), 2));

        // Neste primeiro momento, o adapter será  prereenchido com a lista vazia
        moviesRecyclerView.setAdapter(new MovieRecyclerViewAdapter(new ArrayList<Movie>()));

        return moviesRecyclerView;
    }

    private void updateMovies() {
//        //Obtem a forma de classifiação a partir das preferências do usuário.
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        // Obtem a clissificação selecionada
//        String sortType = prefs.getString(getString(R.string.movie_list_key), getString(R.string.popular_value));

          String sortType = Util.getSharedPreferences(getActivity());

//        if (!sortType.equals(getString(R.string.popular_value))) {
//            sortType = getString(R.string.top_rated_value);
//        }

        if (sortType.equals(getString(R.string.favorite_movies_value))) {
            // Caso a classificação selecionada seja a de filmes favoritos chama-se o CursorQueryMovieTask
           new CursorQueryMovieTask(moviesRecyclerView, getActivity()).execute();

        } else {
            FecthMovieTask movieTask = new FecthMovieTask(moviesRecyclerView, getContext());

            // Invoca a classe assicrona
            movieTask.execute(sortType);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }



}
