package com.android.nanodegree.udacity.myappportifolio;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {


    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    // Classe será executrada em segundo plano
    public class fecthMovieTask extends AsyncTask<Void, Void, String[]>{

        @Override
        protected String[] doInBackground(Void... params) {

            //https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1

            // Parâmetros
            final String POPULAR_MOVIE_URL = "https://api.themoviedb.org/3/movie/popular?&?&?";
            final String API_KEY = "api_key";
            final String LANGUAGE = "language";
            final String PAGE = "page";

            // Valores
            String keyMovieDb = "";
            String language = "en-US";
            String page = "1";

            // Fará a conexão
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJson = null;

            // Construindo uma URI
            Uri builtUri =  Uri.parse(POPULAR_MOVIE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, keyMovieDb)
                    .appendQueryParameter(LANGUAGE, language)
                    .appendQueryParameter(PAGE, page).build();

            try {
                // Controi a URL para o pi.themoviedb.org
                URL url = new URL(builtUri.toString());

                // Cria uma URL de conexão a partir da url montada
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new String[0];
        }
    }


}
