package gr.ihu.test.ihu_project_2018;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForecastFragment extends Fragment {

    private ArrayAdapter<WeatherInfo> adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_refresh){
            AsyncTask<String, Void, WeatherInfo[]> task = new FetchWeatherTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class FetchWeatherTask extends AsyncTask<String, Void, WeatherInfo[]> {

        @Override
        protected void onPostExecute(WeatherInfo[] weatherInfos) {
            if(weatherInfos != null) {
                adapter.clear();
                for (WeatherInfo forecast : weatherInfos) {
                    adapter.add(forecast);
                }
            }
        }

        @Override
        protected WeatherInfo[] doInBackground(String...params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //MODIFIED FOR CITY OF THESSALONIKI, GREECE
                String cityCode = "734077";
                String mode = "json";
                String units = "metric";
                String daysCount = "7";
                String appid = "8cbf55d68127d9483386b81e1ab1cd8d";

                final String baseUrlForDailyForecast = "http://api.openweathermap.org/data/2.5/forecast/daily?";

                final String queryParam = "id";
                final String formatParam= "mode";
                final String unitsParam= "units";
                final String daysParam = "cnt";
                final String apiKeyParam = "APPID";

                Uri builtUri = Uri.parse(baseUrlForDailyForecast).buildUpon()
                        .appendQueryParameter(queryParam, cityCode) //params[0]
                        .appendQueryParameter(formatParam, mode)
                        .appendQueryParameter(unitsParam, units)
                        .appendQueryParameter(daysParam, daysCount)
                        .appendQueryParameter(apiKeyParam, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
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
                    return null;
                }
                forecastJsonStr = buffer.toString();

                return JsonParser.getWeatherDataFromJson(forecastJsonStr, 7);

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                Log.e("JSON_PARSER", "Json parsing Error");
            }
            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main,
                container,
                false);

        List<WeatherInfo> dataList = new ArrayList<>();

        adapter = new WeatherAdapter(
                        getActivity(),
                      0,
                        dataList
                );

        ListView listView = (ListView) rootView.
                findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view,
                                            int position,
                                            long id) {

                        final WeatherInfo item = adapter.getItem(position);


                        Intent intent = new Intent(getActivity(),
                                DetailActivity.class);

                        intent.putExtra("message", item);

                        startActivity(intent);

                    }
                }
        );






        return rootView;
    }
}