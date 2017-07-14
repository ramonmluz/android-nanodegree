package com.android.nanodegree.udacity.myappportifolio.view.movie;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Review;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Trailer;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MovieReviewPresenter;
import com.android.nanodegree.udacity.myappportifolio.presenter.movie.MovieTrailerPresenter;
import com.android.nanodegree.udacity.myappportifolio.presenterImpl.movie.MovieReviewPresenterImpl;
import com.android.nanodegree.udacity.myappportifolio.presenterImpl.movie.MovieTrailerPresenterImpl;
import com.android.nanodegree.udacity.myappportifolio.util.Constants;
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

   private JSONObject jsonObject;
   private MovieTrailerPresenter movieTrailerPresenter;
   private MovieReviewPresenter movieReviewPresenter;
   private RecyclerView trailerRecyclerView;
   private RecyclerView reviewRecyclerView;

    // Intanciando os componentes da tela via BindView da lib butterknife
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

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        // Verifica se a intent não está preenchida e se tem objeto a partir do parâmetro informado
        if (intent != null && intent.hasExtra(Intent.EXTRA_INITIAL_INTENTS)) {
            // Obtendo a parâmetro informado na Intent
            Movie movie = intent.getParcelableExtra(Intent.EXTRA_INITIAL_INTENTS);
            ButterKnife.bind(this, rootView);

            // Preenchendo os dados
            originalTitle.setText(movie.getOriginalTitle());
            Picasso.with(getContext()).load(movie.getPosterPath()).resize(300, 500).centerCrop().into(movieImage);
            releaseDate.setText(movie.getReleaseDate());
            voteAverage.setText(movie.getVoteAverage());
            overview.setText(movie.getOverview());

            // Inciando o adapter
            initTrailerAdapter(rootView);
            initReviewAdapter(rootView);

            // Instancia o presenter do trailer
            movieTrailerPresenter = new MovieTrailerPresenterImpl(this, getActivity(), movie.getId());
            // Instancia o presenter da Review
            movieReviewPresenter = new MovieReviewPresenterImpl(this, getActivity(), movie.getId());

            obtainRequests();
        }
        return rootView;
    }

    /**
     * Obtem Request de Trailers e Reviews
     */
    private void obtainRequests() {
        obtainRequestTrailers();
        obtainRequestReviews();
    }

    private void initTrailerAdapter(View rootView){
        trailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_movie_trailer_list);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(trailerRecyclerView.getContext()));
//        trailerRecyclerView.setAdapter(new TrailerRecyclerViewAdapter(new ArrayList<Trailer>()));
    }
    private void initReviewAdapter(View rootView){
        reviewRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_movie_review_list);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(reviewRecyclerView.getContext()));
//        reviewRecyclerView.setAdapter(new ReviewRecyclerViewAdapter(new ArrayList<Review>()));
    }

    public void obtainRequestTrailers(){
           movieTrailerPresenter.obtainTrailersMoviesVolley();
    }
    public void obtainRequestReviews(){
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

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Is necessary create a key in Site: https://api.themoviedb.org");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }
}
