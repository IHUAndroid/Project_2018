package gr.ihu.test.ihu_project_2018;

import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ForecastFragment extends Fragment {

    private String[] data = {
            "Mon 2/4 - Sunny - 30 ",
            "Tue 3/4 - Foggy - 20",
            "Wed 4/4 - Snow - 2",
            "Thu 5/4 - Rain - 18",
            "Fir 6/4 - Sunny - 32"
    };

    String forecastJsonStr = null;


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

        if (id == R.id.action_refresh) {
            AsyncTask<Void, Void, Void> task = new FetchWeatherTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class FetchWeatherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Will contain the raw JSON response as a string.


            //MODIFIED FOR CITY OF THESSALONIKI, GREECE
            String cityCode = "734077";
            String mode = "json";
            String units = "metric";
            String daysCount = "7";
            String appid = "8cbf55d68127d9483386b81e1ab1cd8d";

            final String baseUrlForDailyForecast = "http://api.openweathermap.org/data/2.5/forecast/daily?";

            final String queryParam = "id";
            final String formatParam = "mode";
            final String unitsParam = "units";
            final String daysParam = "cnt";
            final String apiKeyParam = "APPID";

            Uri builtUri = Uri.parse(baseUrlForDailyForecast).buildUpon()
                    .appendQueryParameter(queryParam, cityCode) //params[0]
                    .appendQueryParameter(formatParam, mode)
                    .appendQueryParameter(unitsParam, units)
                    .appendQueryParameter(daysParam, daysCount)
                    // .appendQueryParameter(apiKeyParam, appid)
                    .appendQueryParameter(apiKeyParam, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                    .build();

            final URL url;
            try {
                url = new URL(builtUri.toString());
                RequestQueue queue = Volley.newRequestQueue(getContext());

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.i("VOLLEY_Response_is: ", response.substring(0, 500));
                                forecastJsonStr = response;
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY_ERROR", "That didn't work!");
                    }
                });

                queue.add(stringRequest);

            } catch (MalformedURLException e) {
                e.printStackTrace();
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

            List<String> dataList = Arrays.asList(data);

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            getActivity(),
                            R.layout.list_item_forecast,
                            R.id.list_item_forecast_text,
                            dataList
                    );

            ListView listView = (ListView) rootView.
                    findViewById(R.id.listview_forecast);
            listView.setAdapter(adapter);


            return rootView;
        }
    }