package gr.ihu.test.ihu_project_2018;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class JsonParser {

    private static final String OWM_LIST = "list";
    private static final String OWM_DATE = "dt";
    private static final String OWM_WEATHER = "weather";
    private static final String OWM_TEMPERATURE = "temp";
    private static final String OWM_MAX = "max";
    private static final String OWM_MIN = "min";
    private static final String OWM_DESCRIPTION = "main";

    public static String[] getWeatherDataFromJson(String forecastJsonStr,
                      int numDays) throws JSONException {

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
        String[] results = new String[numDays];

        for(int i=0; i < weatherArray.length(); i++){
            //Format: Day - Description - low/high
            // Monday - Rain - 2/15
            String day, description, highLowTemp;

            JSONObject dayForecast = weatherArray.getJSONObject(i);
            long dtLong = dayForecast.getLong(OWM_DATE);
            day = getHumanDate(dtLong);

            JSONObject weather = dayForecast.
                                getJSONArray(OWM_WEATHER).
                                getJSONObject(0);

            description = weather.getString(OWM_DESCRIPTION);

            JSONObject tempObj = dayForecast.getJSONObject(OWM_TEMPERATURE);
            int max = (int)tempObj.getDouble(OWM_MAX);
            int min = (int)tempObj.getDouble(OWM_MIN);

            highLowTemp = min + "/" + max;

            results[i] = day + " - " + description + " - " + highLowTemp;

            Log.i("WEATHER_JSON", results[i]);
        }
        return results;
    }

    private static String getHumanDate(Long unixTimestamp){
        Long timestamp = unixTimestamp * 1000L;
        Date date = new Date(timestamp);
        SimpleDateFormat sdf =
                new SimpleDateFormat("EEE, d MMM");
        sdf.setTimeZone(TimeZone.getDefault());
        return  sdf.format(date);
    }
}
