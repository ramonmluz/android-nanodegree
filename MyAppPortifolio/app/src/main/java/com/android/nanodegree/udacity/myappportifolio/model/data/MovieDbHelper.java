package com.android.nanodegree.udacity.myappportifolio.model.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLDataException;

/**
 * Created by ramon on 29/10/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static  final String DATABASE_NAME = "movieDb.db";

    public static final int version = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    /**
     * Método iniciado através da criação do databse
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String  CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID    +                " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID  +      " TEXT NOT NULL,   " +
                MovieContract.MovieEntry.COLUMN_POSTER_PATH  +   " TEXT NOT NULL,   " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL,   " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW +       " TEXT NOT NULL,   " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE +   " TEXT NOT NULL,    " +
                MovieContract.MovieEntry.COLUMN_RELEASEDATE +    " TEXT NOT NULL );  ";

        sqLiteDatabase.execSQL(CREATE_TABLE);
     }

//    // Create tasks table (careful to follow SQL formatting rules)
//    final String CREATE_TABLE = "CREATE TABLE "  + TaskEntry.TABLE_NAME + " (" +
//            TaskEntry._ID                + " INTEGER PRIMARY KEY, " +
//            TaskEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
//            TaskEntry.COLUMN_PRIORITY    + " INTEGER NOT NULL);";
//
//        db.execSQL(CREATE_TABLE);


    /**
     * Método recria o database quando a versão é encrementada
     *
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
    }
}
