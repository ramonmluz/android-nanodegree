package com.android.nanodegree.udacity.myappportifolio.fragment;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.nanodegree.udacity.myappportifolio.R;
import com.android.nanodegree.udacity.myappportifolio.VO.Movie;
import com.android.nanodegree.udacity.myappportifolio.adapter.MovieArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private MovieArrayAdapter movieArrayAdapter;
    private GridView gridView;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Movie[] movieArray = {};
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        movieArrayAdapter = new MovieArrayAdapter(getActivity(), 0, Arrays.asList(movieArray));

        gridView = (GridView) rootView.findViewById(R.id.movies_grid);

        gridView.setAdapter(movieArrayAdapter);

        return rootView;
    }

    private void updateMovies() {

        //Obtem a forma de classifiação a partir das preferências do usuário.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // Obtem a clissificação selecionada
        String sortType = prefs.getString(getString(R.string.movie_list_key), getString(R.string.popular_value));

        if(!sortType.equals(getString(R.string.popular_value))){
                sortType = getString(R.string.top_rated_value);
        }
         FecthMovieTask movieTask = new FecthMovieTask();

        // Invoca a classe assicrona
        movieTask.execute(sortType);
    }

    // Classe será executrada em segundo plano
    public class FecthMovieTask extends AsyncTask<String, Void, Movie[]>{

        private final String LOG_TAG = FecthMovieTask.class.getSimpleName();
        // String que receberá os dados em Json
        private String movieJsonStr;

        @Override
        protected void onPostExecute(Movie[] moviesParam) {
            movieArrayAdapter = new MovieArrayAdapter(getActivity(), 0, Arrays.asList(moviesParam));
            gridView.setAdapter(movieArrayAdapter);
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            //https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1
            //https://api.themoviedb.org/3/movie/top_rated?api_key=<<api_key>>&language=en-US&page=1

            // Parâmetros
            final String POPULAR_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
            final String API_KEY = "api_key";
            final String LANGUAGE = "language";
            final String PAGE = "page";

            // Valores
            String keyMovieDb = "c4852d11798d35ebae996afb362875d4";
            String language = "en-US";
            String page = "1";

            // Fará a conexão
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            Uri builtUri = buildUri(POPULAR_MOVIE_URL,params, API_KEY, LANGUAGE, PAGE, keyMovieDb, language, page);
            try {
                urlConnection = buildHttpURLConnection(builtUri);
                fillMovieJson(urlConnection);

            }  catch (IOException e) {
                Log.e(LOG_TAG,"Error",e);
                movieJsonStr = null;
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson();
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return null;
        }

        private Movie[] getMovieDataFromJson() throws JSONException {
            Movie[] movies = null;
            if(movieJsonStr != null){
                JSONObject movieJson = new JSONObject(movieJsonStr);
                JSONArray movieArray = movieJson.getJSONArray(Movie.ParametersEnum.RESULTS.getValue());

                movies = new Movie[movieArray.length()];

                for(int i=0; i< movieArray.length();i++){
                    JSONObject dataMovie = movieArray.getJSONObject(i);

                    // Preenche através do construtor
                    movies[i] = new Movie(dataMovie.getString(Movie.ParametersEnum.ID.getValue()),
                            dataMovie.getString(Movie.ParametersEnum.POSTER_PATH.getValue()),
                            dataMovie.getString(Movie.ParametersEnum.ORIGINAL_TITLE.getValue()),
                            dataMovie.getString(Movie.ParametersEnum.OVERVIEW.getValue()),
                            dataMovie.getString(Movie.ParametersEnum.VOTE_AVERAGE.getValue()),
                            dataMovie.getString(Movie.ParametersEnum.RELEASE_DATE.getValue()) );
                }

            }
            return movies;
        }

        private void fillMovieJson(HttpURLConnection urlConnection) throws IOException {
            BufferedReader reader;// Obtendo o input Stream através  do UrlConnection
            InputStream inputStream =  urlConnection.getInputStream();
            StringBuffer  buffer = new StringBuffer();

            if(inputStream == null){
                movieJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // Armazena a String no Buffer linha a linha
            while((line=reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0){
                movieJsonStr = null;
            }
            // Seta os dados obitidos em JSON informados no buffer
            movieJsonStr = buffer.toString();
        }

        @NonNull
        private HttpURLConnection buildHttpURLConnection(Uri builtUri) throws IOException {
            HttpURLConnection urlConnection;// Controi a URL para o pi.themoviedb.org
            URL url = new URL(builtUri.toString());

            // Cria uma URL de conexão a partir da url montada
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            return urlConnection;
        }

        private Uri buildUri(String POPULAR_MOVIE_URL, String[] params, String API_KEY, String LANGUAGE, String PAGE,
                             String keyMovieDb, String language, String page) {
            // Construindo uma URI
            return Uri.parse(POPULAR_MOVIE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(API_KEY, keyMovieDb)
                    .appendQueryParameter(LANGUAGE, language)
                    .appendQueryParameter(PAGE, page).build();
        }
    }


}
