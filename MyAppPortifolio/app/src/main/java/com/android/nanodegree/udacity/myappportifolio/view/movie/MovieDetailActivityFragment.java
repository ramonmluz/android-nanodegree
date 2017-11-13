package com.android.nanodegree.udacity.myappportifolio.view.movie;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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
import com.android.nanodegree.udacity.myappportifolio.util.SimpleDividerItemDecoration;
import com.android.nanodegree.udacity.myappportifolio.util.Util;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements MovieDatailView {


    public static final String SAVED_RECYCLER_VIEW_ID = "SAVED_RECYCLER_VIEW_ID";
    public static final String SAVED_RECYCLER_VIEW_DATASET_ID = "SAVED_RECYCLER_VIEW_DATASET_ID";
    public static final String SAVED_REVIEW_ID = "SAVED_REVIEW_ID";
    public static final String SAVED_REVIEW_DATASET_ID = "SAVED_REVIEW_DATASET_ID";
    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String SORT_TYPE_ID = "SORT_TYPE_ID";

    private Movie movie;
    private MovieTrailerPresenter movieTrailerPresenter;
    private MovieReviewPresenter movieReviewPresenter;
    private List<Trailer> trailers;
    private List<Review> reviews;
    private String sortType;

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
    @BindView(R.id.trailer_tx)
    TextView traillerTx;
    @BindView(R.id.review_tx)
    TextView review_tx;

    @BindView(R.id.favorite_off)
    ImageView favoriteOff;
    @BindView(R.id.favorite_on)
    ImageView favoriteOn;

    @BindView(R.id.separator)
    View viewSeparotor;
    @BindView(R.id.separator_review)
    View viewSeparotorReview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();

        if (savedInstanceState != null) {
            onViewStateRestored(savedInstanceState);
        } else {
            // Verifica se a intent não está preenchida e se tem objeto a partir do parâmetro informado
            if (intent != null && intent.hasExtra(Intent.EXTRA_INITIAL_INTENTS)) {
                // Obtendo a parâmetro informado na Intent
                this.movie = intent.getParcelableExtra(Intent.EXTRA_INITIAL_INTENTS);

                // Preenchendo os dados
                fillMovieData();

                onclickFavoriteMovieImageView();
                sortType = Util.getSharedPreferences(getActivity());

                if (sortType.equals(getString(R.string.favorite_movies_value))) {
                    disableTrailerRecyclerView();
                    disableReviewRecyclerView();
                    fillVisibiltyFavoriteMovie(View.GONE, View.VISIBLE);
                } else {
                    // Só existe os trailers e views se a classificação de filmes escolhida não  for favorita
                    serchTraliersViews(rootView);
                }

            }
        }
        return rootView;
    }

    /**
     * Preenche os dados do Filme
     */
    private void fillMovieData() {
        originalTitle.setText(movie.getOriginalTitle());
        Picasso.with(getContext()).load(movie.getPosterPathUrl()).resize(300, 500).centerCrop().into(movieImage);
        releaseDate.setText(movie.getReleaseYear());
        voteAverage.setText(movie.getVoteAverage());
        overview.setText(movie.getOverview());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(MOVIE_ID, movie);

        outState.putString(SORT_TYPE_ID, sortType);

        if (trailers != null && trailers.size() != 0) {
            saveInstanceStateTrailersReviews(trailerRecyclerView, outState, SAVED_RECYCLER_VIEW_ID,
                    SAVED_RECYCLER_VIEW_DATASET_ID, new ArrayList<Trailer>(trailers));
        }

        if (reviews != null && reviews.size() != 0) {
            saveInstanceStateTrailersReviews(reviewRecyclerView, outState, SAVED_REVIEW_ID,
                    SAVED_REVIEW_DATASET_ID, new ArrayList<Review>(reviews));
        }


        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {

            movie = savedInstanceState.getParcelable(MOVIE_ID);
            sortType = savedInstanceState.getString(SORT_TYPE_ID);

            fillMovieData();

            onclickFavoriteMovieImageView();

            if (sortType != null && sortType.equals(getString(R.string.favorite_movies_value))) {
                disableTrailerRecyclerView();
                disableReviewRecyclerView();
                fillVisibiltyFavoriteMovie(View.GONE, View.VISIBLE);
            } else {
                checkMovieFavorite();
                // Inciando o adapter
                initTrailerAdapter();
                initReviewAdapter();

                getViewStateRestored(trailerRecyclerView, savedInstanceState, SAVED_RECYCLER_VIEW_ID,
                        SAVED_RECYCLER_VIEW_DATASET_ID, true);

                getViewStateRestored(reviewRecyclerView, savedInstanceState, SAVED_REVIEW_ID,
                        SAVED_REVIEW_DATASET_ID, false);
            }

        }
    }

    private void getViewStateRestored(RecyclerView recyclerView, Bundle savedInstanceState, String listStatePositionId, String listId, boolean isTrailer) {

        Parcelable listStatePosition = savedInstanceState.getParcelable(listStatePositionId);
        List list = savedInstanceState.getParcelableArrayList(listId);

        if (list != null && list.size() != 0) {
            // Preenche o RecyclerView de Trailer com a lista obtida do serviço
            if (isTrailer) {
                trailers = list;
                recyclerView.setAdapter(new TrailerRecyclerViewAdapter(trailers));

            } else {
                reviews = list;
                recyclerView.setAdapter(new ReviewRecyclerViewAdapter(reviews));
            }
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
            recyclerView.getLayoutManager().onRestoreInstanceState(listStatePosition);
        } else if (isTrailer) {
            disableTrailerRecyclerView();
        } else {
            disableReviewRecyclerView();
        }
    }

    private void saveInstanceStateTrailersReviews(RecyclerView recyclerView, Bundle outState, String listStatePositionId, String listId, List list) {
        Parcelable listStatePosition = recyclerView.getLayoutManager().onSaveInstanceState();

        // Seta a posição
        outState.putParcelable(listStatePositionId, listStatePosition);

        // Seta os itens
        outState.putParcelableArrayList(listId, (ArrayList<? extends Parcelable>) list);
    }

    private void disableReviewRecyclerView() {
        reviewRecyclerView.setVisibility(View.GONE);
        viewSeparotorReview.setVisibility(View.GONE);
        review_tx.setVisibility(View.GONE);
    }

    private void disableTrailerRecyclerView() {
        trailerRecyclerView.setVisibility(View.GONE);
        viewSeparotor.setVisibility(View.GONE);
        traillerTx.setVisibility(View.GONE);
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

        checkMovieFavorite();

        // Inciando o adapter
        initTrailerAdapter();
        initReviewAdapter();

        // Instancia o presenter do trailer
        movieTrailerPresenter = new MovieTrailerPresenterImpl(this, getActivity(), movie.getId());
        // Instancia o presenter da Review
        movieReviewPresenter = new MovieReviewPresenterImpl(this, getActivity(), movie.getId());

        obtainRequests();
    }

    /**
     * verifica se o filme é favorito
     */
    private void checkMovieFavorite() {
        Integer resultQuery = obtainCountResultQuery();
        if (resultQuery != null && resultQuery > 0) {
            fillVisibiltyFavoriteMovie(View.GONE, View.VISIBLE);
        }
    }

    /**
     * Obtem Request de Trailers e Reviews
     */
    private void obtainRequests() {
        obtainRequestTrailers();
        obtainRequestReviews();
    }

    private void initTrailerAdapter() {
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initReviewAdapter() {
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void obtainRequestTrailers() {
        movieTrailerPresenter.obtainTrailersMoviesVolley();
    }

    public void obtainRequestReviews() {
        movieReviewPresenter.obtainReviewsMoviesVolley();
    }

    @Override
    public void fillTrailersRecyclerView(List<Trailer> trailers) {

        if (trailers != null && trailers.size() != 0) {
            this.trailers = trailers;
            // Preenche o RecyclerView de Trailer com a lista obtida do serviço
            trailerRecyclerView.setAdapter(new TrailerRecyclerViewAdapter(this.trailers));
            trailerRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        } else {
            disableTrailerRecyclerView();
        }
    }

    @Override
    public void fillReviewsRecyclerView(List<Review> reviews) {

        if (reviews != null && reviews.size() != 0) {
            this.reviews = reviews;
            // Preenche o RecyclerView de Review com a lista obtida do serviço
            reviewRecyclerView.setAdapter(new ReviewRecyclerViewAdapter(this.reviews));
            reviewRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        } else {
            disableReviewRecyclerView();
        }
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
            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(movie.getId()).build();

            deleteResult = getContext().getContentResolver().delete(uri, null, null);

            if (deleteResult == 0) {
                Log.d(Constants.TAG_MOVIE_DATAIL, "none record stored ");
                fillVisibiltyFavoriteMovie(View.GONE, View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e(Constants.TAG_MOVIE_DATAIL, e.getMessage());
            fillVisibiltyFavoriteMovie(View.GONE, View.VISIBLE);
        }
        Log.d(Constants.TAG_MOVIE_DATAIL, "Qtd Rows deleted :" + deleteResult);

    }
}
