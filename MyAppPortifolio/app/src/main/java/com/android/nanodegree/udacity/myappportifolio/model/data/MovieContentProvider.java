package com.android.nanodegree.udacity.myappportifolio.model.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Movie;

/**
 * Classe Provedor para acesso a dados
 * <p>
 * Created by ramon on 29/10/17.
 */

public class MovieContentProvider extends ContentProvider {

    /**
     * Definem qual diretório será invocado
     * Varios filmes (Movies) = 100
     * Um filme (101)
     */
    private static final int MOVIES = 100;
    private static final int MOVIE = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Caso queira retornar /acessar a lista de filmes acessa o diretório
        uriMatcher.addURI(MovieContract.CONTENT_AUHORITY, MovieContract.PATH_MOVIE, MOVIES);
        // Caso queira retornar / acessar um registro de filmes acessa esse diretório aqui
        uriMatcher.addURI(MovieContract.CONTENT_AUHORITY, MovieContract.PATH_MOVIE + "/#", MOVIE);

        return uriMatcher;
    }

    private MovieDbHelper movieDbHelper;


    @Override
    public boolean onCreate() {

        // Instancia a classe que criará a tebela, que por sua vez é crida usando a classe de contrato
        movieDbHelper = new MovieDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case MOVIES:
                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection, // Listas de colunas, se não informado retorna todas
                        selection,  // where
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknow uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        // Obtendo uma instancia pra escrever um dado
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri = null;

        // Verifca o diretório informado
        switch (match) {
            case MOVIES:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);

                if (id > 0) {
                    ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row  into" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException(" Unknow uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
