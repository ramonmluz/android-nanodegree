package com.android.nanodegree.udacity.myappportifolio.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.nanodegree.udacity.myappportifolio.VO.Movie;
import com.android.nanodegree.udacity.myappportifolio.adapter.MovieRecyclerViewAdapter;

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
 *
 *  Classe será executrada em segundo plano
 */
public class FecthMovieTask extends AsyncTask<String, Void, Movie[]> {

    private final String LOG_TAG = FecthMovieTask.class.getSimpleName();
    // String que receberá os dados em Json
    private String movieJsonStr;
    private RecyclerView moviesRecyclerView;
    private Context context;

    public FecthMovieTask() {}

    public FecthMovieTask(RecyclerView moviesRecyclerView, Context context) {
        this.moviesRecyclerView = moviesRecyclerView;
        this.context = context;
    }

    @Override
        protected void onPostExecute(Movie[] moviesParam) {
            if(moviesParam == null){
                Toast.makeText(context,"Is necessary create a key in Site: https://api.themoviedb.org",Toast.LENGTH_LONG).show();
            }else{
                //Preenche o RecycleView isntanciado no Movie Fragment
                moviesRecyclerView.setAdapter(new MovieRecyclerViewAdapter(Arrays.asList(moviesParam)));
            }

        }

        @Override
        protected Movie[] doInBackground(String... params) {

            // Parâmetros
            final String POPULAR_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
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

            Uri builtUri = buildUri(POPULAR_MOVIE_URL,params, API_KEY, LANGUAGE, PAGE, keyMovieDb, language, page);
            try {
                urlConnection = buildHttpURLConnection(builtUri);
                fillMovieJson(urlConnection);

            }  catch (IOException e) {
                Log.e(LOG_TAG,"Error",e);
                movieJsonStr = null;
            } finally {
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