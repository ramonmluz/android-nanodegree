package com.android.nanodegree.udacity.myappportifolio.model.data;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Classe de definição da base de dados
 *
 * Created by ramon on 27/10/17.
 */

public class MovieContract {

    public static final String CONTENT_AUHORITY = "com.android.nanodegree.udacity.myappportifolio";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUHORITY);
    public static final String PATH_MOVIE = "Movie";

    public static final class MovieEntry implements BaseColumns {

        // Monta o camaninho de acesso paara a tabela Movie
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MOVIE).build();

        // Nome da tabela
        public static String TABLE_NAME = "Movie";

        // Nome das Colunas
        public static final String COLUMN_MOVIE_ID = "MOVIE_ID";
        public static final String COLUMN_POSTER_PATH = "POSTER_PATH";
        public static final String COLUMN_ORIGINAL_TITLE = "ORIGINAL_TITLE";
        public static final String COLUMN_OVERVIEW = "OVERVIEW";
        public static final String COLUMN_VOTE_AVERAGE = "VOTE_AVERAGE";
        public static final String COLUMN_RELEASEDATE = "RELEASE_DATE";

    }

}
