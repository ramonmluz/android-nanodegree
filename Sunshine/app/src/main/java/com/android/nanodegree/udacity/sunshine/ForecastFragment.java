package com.android.nanodegree.udacity.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private  ArrayAdapter<String> mForecastAdapter;

    public ForecastFragment() {
    }

    /**
     * Inicia a criação de um fragmento
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Informa que existirá um menu de opções manipulado por esse fragmento
          setHasOptionsMenu(true);

    }


    /**
     * Renderiza o menu do fragmento
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Gerencia as opções do item de menu selecionado
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Verifica a existência do item selecionado
        switch (item.getItemId()){
            // Verifica se o item de menu refresh foi adicionado
            case R.id.action_refresh:
                updateWeather();

                return true;
            default:  return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Atualiza a localização através da informação compartilhada
     * e executa a tarefa assincrona
     */
    private void updateWeather() {
        //  Invoca a classe assicrona
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();

         //Obtem a localização a partir das preferências do usuário.
          SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
          String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

          // Executa a classe assincrona
          fetchWeatherTask.execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    /**
     * Executado logo depois do onCreate
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Icializando o Array
        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview);

        ListView listView  = (ListView) rootView.findViewById(R.id.listview_forecast);

        listView.setAdapter(mForecastAdapter);

        // Define o momento em que o a list view for clicada
          listView.setOnItemClickListener(new OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  // Obtem o item (Object) a partir da posição
                  String forecast = mForecastAdapter.getItem(position);

                  //Informa o popup com a o item obitido
                  // Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();

                  // Inicia a intent
                  Intent intent = new Intent(getActivity(), DetailActivity.class);
                  intent.putExtra(Intent.EXTRA_TEXT, forecast);
                  // Chama a Activiity
                  startActivity(intent);
              }
          });

        return rootView;
    }



    /**
     * Classe que será execuntada em segundo plano
     */
    public class FetchWeatherTask extends AsyncTask<String, Void, String[]>{

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();


        /**The date/time conversion code is going to be moved outside the asynctask later,
         * so for convenience we're breaking it out into its own method now.
         */
        private String getReadableDateString(Date time){
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
        private String formatHighLows(double high, double low) {
            //Obtem a unidade de tempratura a partir das preferências do usuário.
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String unitType = sharedPrefs.getString(getString(R.string.temperature_list_key), getString(R.string.pref_unit_metric));

            if(unitType.equals(getString(R.string.pref_unit_imperial))) {
                high = (high * 1.8) + 32;
                low =  (low * 1.8)  + 32;
             } else if(!unitType.equals(getString(R.string.pref_unit_imperial))){
                 Log.d(LOG_TAG, " Unit type not  found: " + unitType);
             }

            // For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "main";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            GregorianCalendar gc = new GregorianCalendar();

            String[] resultStrs = new String[numDays];
            for(int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                //add i dates to current date of calendar
                gc.add(GregorianCalendar.DATE, i);

                //get that date, format it, and "save" it on variable day
                Date time = gc.getTime();

                day = getReadableDateString(time);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }
            return resultStrs;

        }

        @Override
        protected String[] doInBackground(String... params) {

              // Parâmetros
              final  String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
              final  String POSTAL_CODE_PARAM = "q";
              final  String FORMAT_PARAM  = "mode";
              final  String UNITS_PARAM = "units";
              final  String DAYS_PARAM   = "cnt";
              final  String KEY_WEATHERMAP = "appid";

              // Valores
              String format = "json";
              String units = "metric";
              int numDays = 7;
              String keyWeatherMap = "b383d1ec2a43286a38645523531491e4";

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {

                // Usando Uri.Builder para montar a url
                Uri builtUri = Uri.parse(FORECAST_URL).buildUpon()
                .appendQueryParameter(POSTAL_CODE_PARAM, params[0])
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .appendQueryParameter(KEY_WEATHERMAP, keyWeatherMap)
                .build();

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                // http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7"
                URL url = new URL(builtUri.toString());

                /**
                 * Given a string of the form returned by the api call:
                 * http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
                 * retrieve the maximum temperature for the day indicated by dayIndex
                 * (Note: 0-indexed, so 0 would refer to the first day).
                 *
                public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
                throws JSONException {
                    // TODO: add parsing code here



                    return maxTemperatureForDay;
                }*/


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try{
                return getWeatherDataFromJson(forecastJsonStr, numDays);
            }catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            //super.onPostExecute(strings);
            if(strings != null){
                for (String dayForecast: strings ){
                    mForecastAdapter.add(dayForecast);
                }
            }

        }
    }


}
