package gr.ihu.test.ihu_project_2018;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WeatherAdapter extends ArrayAdapter<WeatherInfo> {

    private List<WeatherInfo> weatherInfos;
    private Context context;

    public WeatherAdapter(@NonNull Context context,
                          int resource,
                          @NonNull List<WeatherInfo> objects) {
        super(context, resource, objects);
        weatherInfos = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View listItem,
                        @NonNull ViewGroup parent) {
        if(listItem == null) {
            listItem = LayoutInflater
                    .from(context)
                    .inflate(R.layout.list_item_forecast,
                            parent,
                            false);
        }

        WeatherInfo info = weatherInfos.get(position);

        TextView dateView = (TextView)listItem.findViewById(R.id.dateText);
        TextView descrView = (TextView)listItem.findViewById(R.id.descriptionText);
        TextView minTempView = (TextView)listItem.findViewById(R.id.minTempText);
        TextView maxTempView = (TextView)listItem.findViewById(R.id.maxTempText);

        dateView.setText(info.getDate());
        descrView.setText(info.getDescription());
        minTempView.setText(Integer.toString(info.getMinTemp()));
        maxTempView.setText(info.getMaxTemp() + "");

        return listItem;
    }
}
