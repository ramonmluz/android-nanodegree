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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    // Intanciando os componentes da tela via BindView da lib butterknife
    @BindView(R.id.original_title) TextView originalTitle;
    @BindView(R.id.movie_image) ImageView movieImage;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.vote_average) TextView voteAverage;
    @BindView(R.id.overview) TextView overview;

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

                ButterKnife.bind(this, rootView);

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
