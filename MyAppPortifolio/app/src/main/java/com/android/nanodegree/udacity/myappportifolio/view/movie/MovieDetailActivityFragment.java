package com.android.nanodegree.udacity.myappportifolio.view.movie;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nanodegree.udacity.myappportifolio.R;
import com.android.nanodegree.udacity.myappportifolio.adapter.movie.ReviewRecyclerViewAdapter;
import com.android.nanodegree.udacity.myappportifolio.adapter.movie.TrailerRecyclerViewAdapter;
import com.android.nanodegree.udacity.myappportifolio.model.data.MovieContract;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Review;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Trailer;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MovieReviewPresenter;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MovieTrailerPresenter;
import com.android.nanodegree.udacity.myappportifolio.presenterImpl.movie.MovieReviewPresenterImpl;
import com.android.nanodegree.udacity.myappportifolio.presenterImpl.movie.MovieTrailerPresenterImpl;
import com.android.nanodegree.udacity.myappportifolio.util.Constants;
import com.android.nanodegree.udacity.myappportifolio.util.Util;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements MovieView {


    private Movie movie;
    private MovieTrailerPresenter movieTrailerPresenter;
    private MovieReviewPresenter movieReviewPresenter;

    // Intanciando os componentes da tela via BindView da lib butterknife
    @BindView(R.id.recycler_movie_trailer_list)
    RecyclerView trailerRecyclerView;
    @BindView(R.id.recycler_movie_review_list)
    RecyclerView reviewRecyclerView;

    @BindView(R.id.original_title)
    TextView originalTitle;
    @BindView(R.id.movie_image)
    ImageView movieImage;
    @BindView(R.id.release_date)
    TextView releaseDate;
    @BindView(R.id.vote_average)
    TextView voteAverage;
    @BindView(R.id.overview)
    TextView overview;

    @BindView(R.id.favorite_off)
    ImageView favoriteOff;
    @BindView(R.id.favorite_on)
    ImageView favoriteOn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();

        // Verifica se a intent não está preenchida e se tem objeto a partir do parâmetro informado
        if (intent != null && intent.hasExtra(Intent.EXTRA_INITIAL_INTENTS)) {

            ButterKnife.bind(this, rootView);

            // Obtendo a parâmetro informado na Intent
            this.movie = intent.getParcelableExtra(Intent.EXTRA_INITIAL_INTENTS);

            // Preenchendo os dados
            originalTitle.setText(movie.getOriginalTitle());
            Picasso.with(getContext()).load(movie.getPosterPath()).resize(300, 500).centerCrop().into(movieImage);
            releaseDate.setText(movie.getReleaseDate());
            voteAverage.setText(movie.getVoteAverage());
            overview.setText(movie.getOverview());

            onclickFavoriteMovieImageView();
            String sortType = Util.getSharedPreferences(getActivity());

            if (sortType.equals(getString(R.string.favorite_movies_value))) {
                trailerRecyclerView.setVisibility(View.GONE);
                reviewRecyclerView.setVisibility(View.VISIBLE);
            } else {
                // Só existe os trailers e views se a classificação de filmes escolhida não  for favorita
                serchTraliersViews(rootView);
            }

        }
        return rootView;
    }

    /**
     * Instancia o click listener do image view
     */
    private void onclickFavoriteMovieImageView() {
        favoriteOff.setOnClickListener(
                view -> {
                    fillVisibiltyFavoriteMovie(View.GONE, View.VISIBLE);
                    insertFavoriteMovie();
                }
        );

        favoriteOn.setOnClickListener(
                view -> {
                    fillVisibiltyFavoriteMovie(View.VISIBLE, View.GONE);
                    deleteFavoriteMovie();
                }
        );
    }

    /**
     * Inicia a pesquisa por trailers and Views
     *
     * @param rootView
     */
    private void serchTraliersViews(View rootView) {

        Integer resultQuery = obtainCountResultQuery();
        if (resultQuery != null && resultQuery > 0) {
            fillVisibiltyFavoriteMovie(View.GONE, View.VISIBLE);
        }

        // Inciando o adapter
        initTrailerAdapter(rootView);
        initReviewAdapter(rootView);

        // Instancia o presenter do trailer
        movieTrailerPresenter = new MovieTrailerPresenterImpl(this, getActivity(), movie.getId());
        // Instancia o presenter da Review
        movieReviewPresenter = new MovieReviewPresenterImpl(this, getActivity(), movie.getId());

        obtainRequests();
    }

    /**
     * Obtem Request de Trailers e Reviews
     */
    private void obtainRequests() {
        obtainRequestTrailers();
        obtainRequestReviews();
    }

    private void initTrailerAdapter(View rootView) {
//        trailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_movie_trailer_list);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(trailerRecyclerView.getContext()));
    }

    private void initReviewAdapter(View rootView) {
//        reviewRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_movie_review_list);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(reviewRecyclerView.getContext()));
    }

    public void obtainRequestTrailers() {
        movieTrailerPresenter.obtainTrailersMoviesVolley();
    }

    public void obtainRequestReviews() {
        movieReviewPresenter.obtainReviewsMoviesVolley();
    }

    @Override
    public void fillTrailersRecyclerView(List<Trailer> trailers) {
        // Preenche o RecyclerView de Trailer com a lista obtida do serviço
        trailerRecyclerView.setAdapter(new TrailerRecyclerViewAdapter(trailers));
    }

    @Override
    public void fillReviewsRecyclerView(List<Review> reviews) {
        // Preenche o RecyclerView de Review com a lista obtida do serviço
        reviewRecyclerView.setAdapter(new ReviewRecyclerViewAdapter(reviews));
    }

    @Override
    public void notifyError(VolleyError error) {
        Log.e(Constants.TAG_MOVIE_DATAIL, error.toString());
        Util.sendMessageDilog("Alert", "Is necessary create a key in Site: https://api.themoviedb.org", getActivity());
    }

    /**
     * Preenche a visibilidade das imagens de estrela
     *
     * @param favoriteMovieOff
     * @param favoriteMovieOn
     */
    private void fillVisibiltyFavoriteMovie(int favoriteMovieOff, int favoriteMovieOn) {
        favoriteOff.setVisibility(favoriteMovieOff);
        favoriteOn.setVisibility(favoriteMovieOn);
    }

    /**
     * Query para saber se o filme já foi armazenado como favorito na base atual
     *
     * @return
     */
    private Integer obtainCountResultQuery() {
        try {
            return getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, new String[]{MovieContract.MovieEntry._ID}
                    , MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{movie.getId()}, null).getCount();

        } catch (Exception e) {
            Log.e(Constants.TAG_MOVIE_DATAIL, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Inserção dos dados principais do filme na base local
     */
    private void insertFavoriteMovie() {
        Uri uri = null;
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASEDATE, movie.getReleaseDate());

        try {
            uri = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

            if (uri != null) {
                Log.d(Constants.TAG_MOVIE_DATAIL, "Qtd Rows inserted : " + uri.toString());
                // Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_LONG).show();
            } else {
                fillVisibiltyFavoriteMovie(View.VISIBLE, View.GONE);
            }
        } catch (Exception e) {
            Log.e(Constants.TAG_MOVIE_DATAIL, e.getMessage());
            fillVisibiltyFavoriteMovie(View.VISIBLE, View.GONE);
        }
    }


    /**
     * deleção dos dados principais do filme na base local
     */
    private void deleteFavoriteMovie() {
        int deleteResult = 0;
        try {
             deleteResult = getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " LIKE  ?",
                    new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID});
             if(deleteResult == 0){
                 fillVisibiltyFavoriteMovie(View.GONE, View.VISIBLE);
             }
        } catch (Exception e) {
            Log.e(Constants.TAG_MOVIE_DATAIL, e.getMessage());
            fillVisibiltyFavoriteMovie(View.GONE, View.VISIBLE);
        }

       // Log.d(Constants.TAG_MOVIE_DATAIL, "Qtd Rows deleted :" + deleteResult);
    }
}
