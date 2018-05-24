package gr.ihu.test.ihu_project_2018;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = this.getIntent();
        if(intent != null){
            WeatherInfo weatherInfo =
                    (WeatherInfo)intent.getSerializableExtra("message");

            TextView textView = (TextView)findViewById(R.id.detail_text);
            textView.setText(weatherInfo.getDescription());
        }
    }


}
