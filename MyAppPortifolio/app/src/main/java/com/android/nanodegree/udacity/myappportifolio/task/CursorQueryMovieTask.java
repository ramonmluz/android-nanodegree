package com.android.nanodegree.udacity.myappportifolio.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import com.android.nanodegree.udacity.myappportifolio.adapter.movie.MovieRecyclerViewAdapter;
import com.android.nanodegree.udacity.myappportifolio.model.data.MovieContract;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;
import com.android.nanodegree.udacity.myappportifolio.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe Task que faz a query e retorna o cursor
 */
public class CursorQueryMovieTask extends AsyncTask<Void, Void, Cursor> {

    private final String LOG_TAG = CursorQueryMovieTask.class.getSimpleName();

    private RecyclerView moviesRecyclerView;
    private Context mContext;

    public CursorQueryMovieTask() {
    }

    public CursorQueryMovieTask(RecyclerView moviesRecyclerView, Context context) {
        this.moviesRecyclerView = moviesRecyclerView;
        this.mContext = context;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        // Cursor com os dados preenchidos
        return mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);

        List<Movie> movies = new ArrayList<Movie>();

        if (cursor != null) {
            while (cursor.moveToNext()) {

                Movie movie = new Movie();
                movie.setId(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASEDATE)));
                movies.add(movie);

            }
            // Preenche o RecycleView com os movies armazenados
            moviesRecyclerView.setAdapter(new MovieRecyclerViewAdapter(movies));

        } else {
            // Não existe filmes armazenados como favoritos
            Util.sendMessageDilog("Alert", " There isn't stored movies as favorite ", mContext);
            ((FragmentActivity) mContext).finish();
        }

    }
}


