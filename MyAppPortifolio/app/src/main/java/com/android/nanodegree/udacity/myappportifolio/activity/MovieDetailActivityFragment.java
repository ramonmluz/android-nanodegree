package com.android.nanodegree.udacity.myappportifolio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nanodegree.udacity.myappportifolio.R;
import com.android.nanodegree.udacity.myappportifolio.VO.Movie;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        // Verifica se a intent não está preenchida e se tem objeto a partir do parâmetro informado
        if(intent!= null && intent.hasExtra(Intent.EXTRA_INITIAL_INTENTS)){
                // Obtendo a parâmetro informado na Intent
                Movie movie = intent.getParcelableExtra(Intent.EXTRA_INITIAL_INTENTS);

                // Intanciando os componentes da tela
                TextView originalTitle = (TextView) rootView.findViewById(R.id.original_title);
                ImageView movieImage = (ImageView) rootView.findViewById(R.id.movie_image) ;
                TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
                TextView voteAverage = (TextView) rootView.findViewById(R.id.vote_average);
                TextView overview = (TextView) rootView.findViewById(R.id.overview);

                // Preenchendo os dados
                originalTitle.setText(movie.getOriginalTitle());
                Picasso.with(getContext()).load(movie.getPosterPath()).resize(300,500).centerCrop().into(movieImage);
                releaseDate.setText(movie.getReleaseDate());
                voteAverage.setText(movie.getVoteAverage());
                overview.setText(movie.getOverview());

        }

        return rootView;
    }
}
