package gr.ihu.test.ihu_project_2018;

        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_main, new PlaceholderFragment())
                    .commit();
        }

    }


    public static class PlaceholderFragment extends Fragment {

        String[] data = {
                "Mon 2/4 - Sunny - 30 ",
                "Tue 3/4 - Foggy - 20",
                "Wed 4/4 - Snow - 2",
                "Thu 5/4 - Rain - 18",
                "Fir 6/4 - Sunny - 32"
        };

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

            ListView listView = (ListView)rootView.
                                findViewById(R.id.listview_forecast);
            listView.setAdapter(adapter);

            return rootView;

        }
    }

}
